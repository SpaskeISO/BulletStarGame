package com.shooterdemo.game.ships;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.shooterdemo.game.Laser;
import com.shooterdemo.game.powerups.PowerUp;

import java.util.LinkedList;

public class PlayerShip extends Ship{

    public int lives;
    public LinkedList<PowerUp> powerUpList;

    public int currentShield;
    public float currentMovementSpeed;
    public float currentTimeBetweenShots;

    public int upgradedShield;
    public float upgradedTimeBetweenShots;
    public float upgradedMovementSpeed;
    public boolean isShieldUpgraded, isLaserSpeedUpgraded, isSpeedUpgraded = false;

    public PlayerShip(float xCenter, float yCenter,
                      float width, float height,
                      float movementSpeed, int shield,
                      float laserWidth, float laserHeight,
                      float laserMovementSpeed, float timeBetweenShots,
                      TextureRegion shipTexture, TextureRegion shieldTexture, TextureRegion laserTextureRegion) {
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

        currentShield = shield;
        currentMovementSpeed = movementSpeed;
        currentTimeBetweenShots = timeBetweenShots;

        upgradedShield = (int)(shield * 1.25f);
        upgradedTimeBetweenShots = timeBetweenShots * 0.75f;
        upgradedMovementSpeed = movementSpeed * 1.5f;

        lives = 3;
        powerUpList = new LinkedList<PowerUp>();
    }

    @Override
    public boolean canFireLaser() {
        return (timeSinceLastShot - currentTimeBetweenShots >= 0);
    }

    @Override
    public boolean hitAndCheckDestroyed() {
        if(currentShield > 0){
            currentShield--;
            return false;
        }
        return true;
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(shipTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        if(currentShield > 0){
            batch.draw(shieldTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        }
    }

    @Override
    public Laser[] fireLasers() {
        Laser[] laser = new Laser[2];
        laser[0] = new Laser(boundingBox.x + (boundingBox.width * 0.07f), boundingBox.y + (boundingBox.height * 0.45f),
                laserWidth, laserHeight,
                laserMovementSpeed, laserTextureRegion);
        laser[1] = new Laser(boundingBox.x + (boundingBox.width * 0.93f), boundingBox.y + (boundingBox.height * 0.45f),
                laserWidth, laserHeight,
                laserMovementSpeed, laserTextureRegion);
        timeSinceLastShot = 0;

        return laser;
    }

    public void checkPowerUps(){
        for(PowerUp powerUp : powerUpList){
            if(powerUp.type == PowerUp.powerUpType.ShieldUpgrade){
                isShieldUpgraded = true;
                currentShield = 20;
            }
            else if(powerUp.type == PowerUp.powerUpType.LaserSpeedUpgrade){
                isLaserSpeedUpgraded = true;
                currentTimeBetweenShots = upgradedTimeBetweenShots;
            }
            else if(powerUp.type == PowerUp.powerUpType.SpeedUpgrade){
                isSpeedUpgraded = true;
                currentMovementSpeed = upgradedMovementSpeed;
            }
        }
    }

}
