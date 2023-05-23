package com.shooterdemo.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Explosion {

    private Animation<TextureRegion> explosionAnimation;
    private float explosionTimer;
    private Rectangle boundingBox;

    public Explosion(TextureRegion textureRegion, Rectangle boundingBox, float totalAnimationTime) {
        this.boundingBox = boundingBox;

        //splitting the texture;
        TextureRegion[][] textureRegions2D = textureRegion.split( textureRegion.getRegionWidth() / 4, textureRegion.getRegionHeight() / 4);

        TextureRegion[] textureRegion1D = new TextureRegion[textureRegion.getRegionWidth() / 16];
        int index = 0;
        for(int i = 0; i < (textureRegion.getRegionWidth() / 64); i++){
            for(int j = 0; j < (textureRegion.getRegionHeight() / 64); j++){
                textureRegion1D[index] = textureRegions2D[i][j];
                index++;
            }
        }

        explosionAnimation = new Animation<TextureRegion>(totalAnimationTime / 16, textureRegion1D);
        explosionTimer = 0;
    }

    public void update(float deltaTime){
        explosionTimer += deltaTime;
    }

    public void draw(SpriteBatch batch){
        batch.draw(explosionAnimation.getKeyFrame(explosionTimer),
                boundingBox.x, boundingBox.y,
                boundingBox.width, boundingBox.height);
    }

    public boolean isFinished(){
        return explosionAnimation.isAnimationFinished(explosionTimer);
    }
}
