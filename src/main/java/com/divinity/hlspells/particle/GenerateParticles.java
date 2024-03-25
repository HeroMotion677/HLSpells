package com.divinity.hlspells.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.RegistryObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GenerateParticles{

    public static void generateParticleRune(ResourceLocation location, LivingEntity livingEntity, RegistryObject<SimpleParticleType> type) throws IOException {
       InputStream stream = Minecraft.getInstance().getResourceManager().getResource(location).getInputStream();
       BufferedReader input = new BufferedReader(new InputStreamReader(stream));
        String textLine = input.readLine();
        while (input.readLine() != null) {
            String[] split = textLine.split(" ");
            livingEntity.getLevel().addParticle(type.get(), livingEntity.getX() + Double.parseDouble(split[0]),
                    livingEntity.getY() + Double.parseDouble(split[1]) + 0.1,
                    livingEntity.getZ() + Double.parseDouble(split[2]),
                    0, 0, 0);
            textLine = input.readLine();
        }
    }
}
