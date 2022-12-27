package com.divinity.hlspells.world.blocks.blockentities.screen;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.network.NetworkManager;
import com.divinity.hlspells.network.packets.serverbound.TransferSpellsPacket;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.world.blocks.blockentities.inventory.AltarOfAttunementMenu;
import com.divinity.hlspells.util.SpellUtils;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AltarOfAttunementScreen extends AbstractContainerScreen<AltarOfAttunementMenu> {

    private static final ResourceLocation GUI = new ResourceLocation(HLSpells.MODID, "textures/gui/container/altar_gui.png");
    private final ItemStackHandler handler = this.menu.blockEntity.itemHandler;

    public AltarOfAttunementScreen(AltarOfAttunementMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.leftPos = 0;
        this.topPos = 0;
        this.inventoryLabelX = 12;
        this.inventoryLabelY = 90;
        this.imageWidth = 175;
        this.imageHeight = 186;
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        if (this.minecraft != null && this.minecraft.player != null) {
            pPartialTick = this.minecraft.getFrameTime();
            this.renderBackground(pPoseStack);
            super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
            this.renderTooltip(pPoseStack, pMouseX, pMouseY);
            boolean isCreative = this.minecraft.player.getAbilities().instabuild;
            int i = this.menu.getMaterialCount();
            for (int j = 0; j < 3; ++j) {
                int k = this.menu.costs[j];
                String clue = this.menu.spellClues[j];
                int spellSlotNumber = j + 1;
                if (this.isHovering(42, 22 + 19 * j, 87, 19, pMouseX, pMouseY) && k > 0) {
                    List<Component> list = Lists.newArrayList();
                    list.add((new TranslatableComponent("container.spell.clue", clue).withStyle(ChatFormatting.WHITE))); // Change this to diff color for rarity
                     if (!isCreative) {
                        list.add(TextComponent.EMPTY);
                        if (this.minecraft.player.experienceLevel < k) {
                            list.add((new TranslatableComponent("container.spell.level.requirement", this.menu.costs[j])).withStyle(ChatFormatting.RED));
                        }
                        else {
                            Item materialItem = this.handler.getStackInSlot(2).getItem();
                            String isAmethystOrLapis = materialItem == Items.LAPIS_LAZULI ? "Lapis Lazuli" : "Amethyst Shard";
                            MutableComponent conditionalComponent = spellSlotNumber == 1
                                    ? new TranslatableComponent("container.spell.material.one", isAmethystOrLapis)
                                    : new TranslatableComponent("container.spell.material.many", spellSlotNumber, isAmethystOrLapis);
                            list.add(conditionalComponent.withStyle(i >= spellSlotNumber ? ChatFormatting.GRAY : ChatFormatting.RED));
                            list.add(conditionalComponent.withStyle(ChatFormatting.GRAY));
                        }
                    }
                    this.renderComponentTooltip(pPoseStack, list, pMouseX, pMouseY);
                    break;
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        for(int k = 0; k < 3; ++k) {
            double d0 = pMouseX - (double)(i + 42);
            double d1 = pMouseY - (double)(j + 22 + 19 * k);
            if (this.minecraft != null && this.minecraft.player != null && this.minecraft.gameMode != null) {
                if (d0 >= 0.0D && d1 >= 0.0D && d0 < 87.0D && d1 < 19.0D && this.menu.clickMenuButton(this.minecraft.player, k)) {
                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, k);
                    return true;
                }
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new ImageButton(this.leftPos + 19, this.topPos + 49, 11, 7, 93, 227, 7, GUI, onPress -> NetworkManager.INSTANCE.sendToServer(new TransferSpellsPacket())) {
            @Override
            public void renderButton(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
                super.renderButton(pPoseStack, pMouseX, pMouseY, pPartialTick);
                if (this.isHoveredOrFocused()) {
                    if (!AltarOfAttunementScreen.this.handler.getStackInSlot(0).isEmpty()) {
                        List<Component> list = new ArrayList<>();
                        ItemStack stack = AltarOfAttunementScreen.this.handler.getStackInSlot(0);
                        list.add(new TranslatableComponent("container.spell.button.requirement", SpellUtils.getSpell(stack).rarityAsInt()).withStyle(ChatFormatting.RED));
                        AltarOfAttunementScreen.this.renderComponentTooltip(pPoseStack, list, pMouseX, pMouseY);
                    }
                }
            }
        });
    }

    @Override
    protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, GUI);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        EnchantmentNames.getInstance().initSeed(this.menu.getEnchantmentSeed());
        int materialCount = this.menu.getMaterialCount();
        if (this.minecraft != null && this.minecraft.player != null) {
            for (int k = 0; k < 3; k++) {
                int distanceToEnchantSlot = i + 42;
                int offsetDistance = distanceToEnchantSlot + 2;
                this.setBlitOffset(0);
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, GUI);
                int cost = this.menu.costs[k];
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                if (cost == 0)
                    this.blit(pPoseStack, distanceToEnchantSlot, j + 22 + 19 * k, 90, 202, 89, 19);
                else {
                    String costWord = String.valueOf(cost);
                    int fontWidth = 86 - this.font.width(costWord);
                    FormattedText formattedtext = EnchantmentNames.getInstance().getRandomName(this.font, fontWidth);
                    int j2 = 6839882;
                    if (((materialCount < k + 1 || this.minecraft.player.experienceLevel < cost) && !this.minecraft.player.getAbilities().instabuild)) {
                        this.blit(pPoseStack, distanceToEnchantSlot, j + 22 + 19 * k, 90, 202, 89, 19);
                        this.font.drawWordWrap(formattedtext, offsetDistance, j + 24 + 19 * k, fontWidth, (j2 & 16711422) >> 1);
                        j2 = 4226832;
                    }
                    else {
                        int k2 = pMouseX - (i + 42);
                        int l2 = pMouseY - (j + 22 + 19 * k);
                        if (k2 >= 0 && l2 >= 0 && k2 < 87 && l2 < 19) {
                            this.blit(pPoseStack, distanceToEnchantSlot, j + 22 + 19 * k, 0, 221, 89, 19);
                            j2 = 16777088;
                        }
                        else this.blit(pPoseStack, distanceToEnchantSlot, j + 22 + 19 * k, 0, 202, 89, 19);
                        this.font.drawWordWrap(formattedtext, offsetDistance, j + 24 + 19 * k, fontWidth, j2);
                        j2 = 8453920;
                    }
                    this.font.drawShadow(pPoseStack, costWord, (float) (offsetDistance + 86 - this.font.width(costWord)), (float) (j + 24 + 19 * k + 7), j2);
                }
            }
        }
    }
}
