package com.divinity.hlspells.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RuneParticle extends TextureSheetParticle {


    protected RuneParticle(ClientLevel level, double xCoord, double yCoord, double zCoord,
                           SpriteSet spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.friction = 0.8F;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.quadSize*= 1F;
        this.lifetime = 3;
        this.pickSprite(spriteSet);
        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
    }

    @Override
    public void tick() {
        super.tick();
        fadeOut();
        this.alpha = (-(1/(float)lifetime) * age + 2);

    }

    private void fadeOut(){
        this.alpha = (-(1/(float)lifetime) * age + 1);
    }

    @Override
    public ParticleRenderType getRenderType(){
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }
    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new RuneParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}
