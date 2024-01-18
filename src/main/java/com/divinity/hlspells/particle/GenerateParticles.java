package com.divinity.hlspells.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GenerateParticles{

    public static void generateLargeParticleRune(ResourceLocation location, LivingEntity livingEntity, SimpleParticleType type) throws IOException {
       InputStream stream = Minecraft.getInstance().getResourceManager().getResource(location).getInputStream();
       BufferedReader input = new BufferedReader(new InputStreamReader(stream));
        String textLine;
        while (input.readLine() != null) {
            textLine = input.readLine();
            String[] split = textLine.split(" ");
            livingEntity.getLevel().addParticle(ModParticles.GREEN_PARTICLE.get(), livingEntity.getX() + Double.parseDouble(split[1]),
                    livingEntity.getY() + Double.parseDouble(split[2]),
                    livingEntity.getZ() + Double.parseDouble(split[3]),
                    0, 0, 0);
        }
    }
    public static void generateSmallParticleRune(ResourceLocation location, LivingEntity livingEntity, SimpleParticleType type) throws IOException {
        InputStream stream = Minecraft.getInstance().getResourceManager().getResource(location).getInputStream();
        BufferedReader input = new BufferedReader(new InputStreamReader(stream));
        String textLine;
        while (input.readLine() != null){
            textLine = input.readLine();
            String[] split = textLine.split(" ");

            float pitch = livingEntity.getXRot();
            float yaw = livingEntity.getYRot();

            double pitchRad = Math.toRadians(-pitch);
            double yawRad = Math.toRadians(-yaw);

            // Calculate offset based on pitch and yaw
            double offsetX = Math.sin(yawRad) * Math.cos(pitchRad);
            double offsetY = Math.sin(pitchRad);
            double offsetZ = Math.cos(yawRad) * Math.cos(pitchRad);

            livingEntity.getLevel().addParticle(ModParticles.GREEN_PARTICLE.get(),
                    livingEntity.getX()  + offsetX + Double.parseDouble(split[1]),
                    livingEntity.getY() + offsetY + Double.parseDouble(split[2]),
                    livingEntity.getZ() + offsetZ + Double.parseDouble(split[3]),
                    0, 0, 0);
        }
    }
}
