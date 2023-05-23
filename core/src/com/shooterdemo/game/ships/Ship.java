package com.shooterdemo.game.ships;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shooterdemo.game.Laser;

public abstract class Ship {

    //ship characteristics
    //base characteristics
    public float movementSpeed; // world units  per seconds;
    public int shield;
    public float timeBetweenShots;


    //position and dimension
    public Rectangle boundingBox;

    //laser information;
    public float laserWidth, laserHeight;
    public float laserMovementSpeed;
    public float timeSinceLastShot = 0;



    //graphics
    public TextureRegion shipTextureRegion, shieldTextureRegion, laserTextureRegion;

    public Ship(){
        boundingBox = new Rectangle();
    }

    public Ship(float xCenter, float yCenter,
                float width, float height,
                float movementSpeed, int shield,
                float laserWidth, float laserHeight,
                float laserMovementSpeed, float timeBetweenShots,
                TextureRegion shipTexture, TextureRegion shieldTexture,
                TextureRegion laserTextureRegion) {
        this.movementSpeed = movementSpeed;
        this.shield = shield;
        this.boundingBox = new Rectangle(xCenter - (width/2), yCenter - (height/2), width, height);

        this.shipTextureRegion = shipTexture;
        this.shieldTextureRegion = shieldTexture;
        this.laserTextureRegion = laserTextureRegion;
        this.laserWidth = laserWidth;
        this.laserHeight = laserHeight;
        this.laserMovementSpeed = laserMovementSpeed;
        this.timeBetweenShots = timeBetweenShots;
    }

    public void update(float deltaTime){

        timeSinceLastShot += deltaTime;

    }

    public boolean canFireLaser(){
        return (timeSinceLastShot - timeBetweenShots >= 0);
    }

    public abstract Laser[] fireLasers();

    public void translate(float xChange, float yChagne){
        boundingBox.setPosition(boundingBox.x + xChange, boundingBox.y + yChagne);
    }

    public void draw(Batch batch){
        batch.draw(shipTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        if(shield > 0){
            batch.draw(shieldTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        }
    }

    public boolean hitAndCheckDestroyed(){
        if(shield > 0){
            shield--;
            return false;
        }
        return true;

    }

    public boolean intersects(Rectangle otherRectangle){
        return boundingBox.overlaps(otherRectangle);

    }

}
