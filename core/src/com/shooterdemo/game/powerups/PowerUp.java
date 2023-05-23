package com.shooterdemo.game.powerups;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.concurrent.ThreadLocalRandom;

public class PowerUp {

    public enum powerUpType {LaserSpeedUpgrade, SpeedUpgrade, ShieldUpgrade}
    //Position and dimension
    public Rectangle boundingBox;
    //PowerUp physical characteristics
    public float movementSpeed = 307.2f; // world units per second
    public powerUpType type;

    //graphics
    private TextureRegion textureRegion;
    public PowerUp(float x, float y, TextureAtlas textureAtlas){
        boundingBox = new Rectangle(x, y, 45, 45);
        type = powerUpType.values()[ThreadLocalRandom.current().nextInt(powerUpType.values().length)];
        if(type == powerUpType.LaserSpeedUpgrade){
            textureRegion = textureAtlas.findRegion("powerupBlue_laser");
        }
        else if(type == powerUpType.SpeedUpgrade){
            textureRegion = textureAtlas.findRegion("powerupBlue_bolt");
        }
        else if(type == powerUpType.ShieldUpgrade){
            textureRegion = textureAtlas.findRegion("powerupBlue_shield");
        }
    }

    public void draw(Batch batch){
        batch.draw(textureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

    public void uiDraw(Batch batch,float x,float y){
        batch.draw(textureRegion, x, y, boundingBox.width, boundingBox.height);
    }

    public void translate(float xChange, float yChagne){
        boundingBox.setPosition(boundingBox.x + xChange, boundingBox.y + yChagne);
    }

}
