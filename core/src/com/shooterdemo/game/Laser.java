package com.shooterdemo.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Laser {

    //position and dimension
    public Rectangle boundingBox;

    //laser physical characteristics
    public float movementSpeed; //world units per second

    //graphics
    public TextureRegion textureRegion;

    public Laser(float xCenter, float yBottom,
                 float width, float height,
                 float movementSpeed, TextureRegion textureRegion) {
        this.boundingBox = new Rectangle(xCenter - (width/2), yBottom, width, height);

        this.movementSpeed = movementSpeed;
        this.textureRegion = textureRegion;
    }

    public void draw(Batch batch){
        batch.draw(textureRegion, boundingBox.x - (boundingBox.width / 2), boundingBox.y, boundingBox.width, boundingBox.height);
    }

    public Rectangle getBoundingBox(){
        return boundingBox;
    }
}
