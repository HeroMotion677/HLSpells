package com.divinity.hlspells.items.capabilities;

import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.items.WandItem;
import com.divinity.hlspells.network.NetworkManager;
import com.divinity.hlspells.network.packets.WandInputPacket;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.util.Util;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import static com.divinity.hlspells.HLSpells.*;
import static com.divinity.hlspells.items.capabilities.WandItemStorage.CURRENT_SPELL_VALUE;

// Lemon before you witness this disgusting code I would like to apologize
@Mod.EventBusSubscriber(modid = MODID)
public class WandCapHandler
{
    private static final float DISPLAY_TIME = 30F;
    private static final float FADE_TIME = 20F;
    private static final float TOTAL_TIME = DISPLAY_TIME + FADE_TIME;
    private static ItemStack playerMainItem = ItemStack.EMPTY;
    private static ItemStack playerOffItem = ItemStack.EMPTY;

    private static boolean isActive1;
    private static boolean isActive2;
    private static boolean isActive3;

    public static int hudTime;

    @SubscribeEvent
    public static void onScreenRender (RenderGameOverlayEvent.Post event)
    {
        if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) return;
        if (hudTime == 0)
        {
            isActive1 = false;
            isActive2 = false;
            isActive3 = false;
            return;
        }

        ClientPlayerEntity player = Minecraft.getInstance().player;

        if (player != null)
        {
            boolean playerMainItemCheck = player.getMainHandItem().getItem() instanceof WandItem;
            boolean playerOffItemCheck = player.getOffhandItem().getItem() instanceof WandItem;
            ItemStack main = player.getMainHandItem();
            ItemStack off = player.getOffhandItem();

            if (playerMainItemCheck && playerOffItemCheck)
            {
                if (playerMainItem == main)
                {
                    if (!isActive2 && !isActive3) isActive1 = true;
                    if (isActive1 && !isActive2 && !isActive3) doRenderFunc(player, player.getMainHandItem(), event); //Problem
                }
            }

            else if (playerMainItemCheck)
            {
                if (playerMainItem == main)
                {
                    if (!isActive1 && !isActive3) isActive2 = true;
                    if (isActive2 && !isActive1 && !isActive3) doRenderFunc(player, player.getMainHandItem(), event); //Problem (maybe)

                }
            }

            else if (playerOffItemCheck)
            {
                if (playerOffItem == off)
                {
                    if (!isActive1 && !isActive2) isActive3 = true;
                    if (isActive3 && !isActive1 && !isActive2) doRenderFunc(player, player.getOffhandItem(), event); //Problem
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event)
    {
        if (event.getObject().getItem() instanceof WandItem)
        {
            event.addCapability(new ResourceLocation(MODID, "wandcap"), new WandItemProvider());
        }
    }

    static boolean buttonPressedFlag;

    @SubscribeEvent
    public static void onClientTick (TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END && hudTime > 0)
        {
            hudTime--;
        }

        if (event.phase == TickEvent.Phase.END)
        {
            ClientPlayerEntity player = Minecraft.getInstance().player;

            if (WAND_BINDING.isDown() && !buttonPressedFlag)
            {
                hudTime = (int) TOTAL_TIME;
                if (player != null)
                {
                    if (!player.isUsingItem())
                    {
                        NetworkManager.INSTANCE.sendToServer(new WandInputPacket(WAND_BINDING.getKey().getValue()));
                        if (player.getMainHandItem().getItem() instanceof WandItem) playerMainItem = player.getMainHandItem();
                        if (player.getOffhandItem().getItem() instanceof WandItem) playerOffItem = player.getOffhandItem();
                    }
                }
                buttonPressedFlag = true;
            }

            if (!WAND_BINDING.isDown() && buttonPressedFlag)
            {
                buttonPressedFlag = false;
            }
        }
    }

    private static void doRenderFunc (PlayerEntity player, ItemStack stack, RenderGameOverlayEvent.Post event)
    {
        if (player.isUsingItem())
        {
            hudTime = 0;
            return;
        }

        stack.getCapability(WandItemProvider.WAND_CAP, null).ifPresent(cap ->
        {
            int windowWidth = event.getWindow().getGuiScaledWidth();
            int windowHeight = event.getWindow().getGuiScaledHeight();
            float alpha = (hudTime + event.getPartialTicks()) / FADE_TIME;
            cap.setCurrentSpellCycle(CURRENT_SPELL_VALUE); // Ensures Sync (Temp solution for now, will probably need server -> client packet)

            if (cap.getSpells().size() > 0)
            {
                for (RegistryObject<Spell> spell : SpellInit.SPELLS_DEFERRED_REGISTER.getEntries())
                {
                    ResourceLocation spellLocation = spell.get().getRegistryName();
                    if (spellLocation != null && spellLocation.toString().equals(cap.getSpells().get(cap.getCurrentSpellCycle())))
                    {
                        RenderSystem.pushMatrix();
                        RenderSystem.enableBlend();
                        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        RenderSystem.blendColor(1F, 1F, 1F, alpha);
                        Minecraft.getInstance().font.draw(event.getMatrixStack(), "Spell: " + spell.get().getTrueDisplayName(), (windowWidth / 2F) - 23, (windowHeight / 2F) + 55F, Util.selectNextColor());
                        RenderSystem.popMatrix();
                        break;
                    }
                }
            }
        });
    }
}
