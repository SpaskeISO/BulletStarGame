package com.shooterdemo.game.ships;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.shooterdemo.game.Laser;
import com.shooterdemo.game.PRNG;
import com.shooterdemo.game.screens.BaseScreen;
import com.shooterdemo.game.screens.GameScreen;

import java.util.concurrent.ThreadLocalRandom;

public class EnemyShip extends Ship implements Pool.Poolable {

    private Vector2 directionVector ;
    private float timeSinceLastChange = 0;
    private float directionChangeFrequency = 0.75f;
    public boolean alive;
    public static int initCounter = 0;

    public EnemyShip(){
        this.boundingBox = new Rectangle();
        this.directionVector = new Vector2();
    }

    public EnemyShip(float xCenter, float yCenter,
                      float width, float height,
                      float movementSpeed, int shield,
                      float laserWidth, float laserHeight,
                      float laserMovementSpeed, float timeBetweenShots,
                      TextureRegion shipTexture, TextureRegion shieldTexture, TextureRegion laserTextureRegion) {
        super(xCenter, yCenter,
                width, height,
                movementSpeed, shield,
                laserWidth, laserHeight,
                laserMovementSpeed, timeBetweenShots,
                shipTexture, shieldTexture, laserTextureRegion);

        directionVector = new Vector2(0, -1);
        alive = false;
    }

    public Vector2 getDirectionVector() {
        return directionVector;
    }

    private void randomizeDirectionVector(){
        double bearing = ThreadLocalRandom.current().nextDouble() * 6.283185; // 0 to 2*PI

        directionVector.x = (float) Math.sin(bearing);
        directionVector.y = (float) Math.cos(bearing);
        if(boundingBox.x == 0 && directionVector.x < 0){
            directionVector.x *= -1f;
        }
        if(boundingBox.x == (GameScreen.WORLD_WIDTH - boundingBox.width) && directionVector.x > 0){
            directionVector.x *= -1f;
        }
        if(boundingBox.y == (GameScreen.WORLD_HEIGHT/2) && directionVector.y < 0){
            directionVector.y *= -1f;
        }
        if(boundingBox.y == (GameScreen.WORLD_HEIGHT - boundingBox.height) && directionVector.y > 0){
            directionVector.y *= -1f;
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        timeSinceLastChange += deltaTime;
        if(timeSinceLastChange > directionChangeFrequency){
            randomizeDirectionVector();
            timeSinceLastChange -= directionChangeFrequency;
        }
    }

    @Override
    public Laser[] fireLasers() {
        if(EnemyShipPool.texture == 1){
            Laser[] laser = new Laser[2];
            laser[0] = new Laser(boundingBox.x + (boundingBox.width * 0.691f), boundingBox.y - laserHeight,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion);
            laser[1] = new Laser(boundingBox.x + (boundingBox.width * 0.309f), boundingBox.y - laserHeight,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion);
            timeSinceLastShot = 0;
            return laser;
        }
        else if(EnemyShipPool.texture == 2){
            Laser[] laser = new Laser[2];
            laser[0] = new Laser(boundingBox.x + (boundingBox.width * 0.6539f), boundingBox.y - laserHeight,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion);
            laser[1] = new Laser(boundingBox.x + (boundingBox.width * 0.3461f), boundingBox.y - laserHeight,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion);
            timeSinceLastShot = 0;
            return laser;
        }
        else if(EnemyShipPool.texture == 3){
            Laser[] laser = new Laser[2];
            laser[0] = new Laser(boundingBox.x + (boundingBox.width * 0.82f), boundingBox.y - laserHeight,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion);
            laser[1] = new Laser(boundingBox.x + (boundingBox.width * 0.18f), boundingBox.y - laserHeight,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion);
            timeSinceLastShot = 0;
            return laser;
        }
        else if(EnemyShipPool.texture == 4){
            Laser[] laser = new Laser[1];
            laser[0] = new Laser(boundingBox.x + (boundingBox.width * 0.5f), boundingBox.y - laserHeight,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion);
            /*laser[1] = new Laser(boundingBox.x + (boundingBox.width * 0.18f), boundingBox.y - laserHeight,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion);*/
            timeSinceLastShot = 0;
            return laser;
        }
        else if(EnemyShipPool.texture == 5){
            Laser[] laser = new Laser[1];
            laser[0] = new Laser(boundingBox.x + (boundingBox.width * 0.5f), boundingBox.y - laserHeight,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion);
            /*laser[1] = new Laser(boundingBox.x + (boundingBox.width * 0.18f), boundingBox.y - laserHeight,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion);*/
            timeSinceLastShot = 0;
            return laser;
        }


        return new Laser[0];
    }

    @Override
    public void draw(Batch batch){
        batch.draw(shipTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        if(shield > 0){
            batch.draw(shieldTextureRegion, boundingBox.x + (boundingBox.width / 2) - ((boundingBox.width * 1.1f) / 2),
                    boundingBox.y - ((boundingBox.height * 1.1f) * 0.2f),
                    boundingBox.width * 1.1f, boundingBox.height * 1.1f);
        }
    }

    @Override
    public void reset() {
        directionVector.set(0, 0);
        boundingBox.setPosition(ThreadLocalRandom.current().nextFloat() * (BaseScreen.WORLD_WIDTH - 10) + 5,BaseScreen.WORLD_HEIGHT + 100 );
        alive = false;
    }

    public void init(){
        //System.out.println(initCounter);
        if(initCounter % 10 == 0){
            EnemyShipPool.randomizeEnemyShipAttributes();
        }
        initCounter++;
        this.boundingBox.set(EnemyShipPool.xCenter - (EnemyShipPool.width/2),
                EnemyShipPool.yCenter - (EnemyShipPool.height/2),
                EnemyShipPool.width, EnemyShipPool.height);
        this.shipTextureRegion = EnemyShipPool.shipTexture;
        this.shieldTextureRegion = EnemyShipPool.shieldTexture;
        this.laserTextureRegion = EnemyShipPool.laserTextureRegion;
        this.laserWidth = EnemyShipPool.laserWidth;
        this.laserHeight = EnemyShipPool.laserHeight;
        this.laserMovementSpeed = EnemyShipPool.laserMovementSpeed;
        this.timeBetweenShots = EnemyShipPool.timeBetweenShots;
        this.shield = EnemyShipPool.shield;
        this.movementSpeed = EnemyShipPool.movementSpeed;
        directionVector.set(0, -1);
        alive = true;
    }
}
