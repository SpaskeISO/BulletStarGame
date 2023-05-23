package com.shooterdemo.game.ships;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.StringBuilder;
import com.shooterdemo.game.PRNG;
import com.shooterdemo.game.screens.GameScreen;

import java.util.concurrent.ThreadLocalRandom;

public class EnemyShipPool extends Pool<EnemyShip> {

    public static TextureAtlas textureAtlas;

    public static float xCenter, yCenter;
    public static float width, height;
    public static float movementSpeed;
    public static int shield;
    public static float laserWidth, laserHeight;
    public static float laserMovementSpeed, timeBetweenShots;
    public static TextureRegion shipTexture, shieldTexture, laserTextureRegion;
    public static int texture;

    public EnemyShipPool(int init, int max, TextureAtlas textureAtlas){
        super(init, max);
        this.textureAtlas = textureAtlas;
        randomizeEnemyShipAttributes();
    }

    EnemyShipPool(){
        super();
    }

    public static void randomizeEnemyShipAttributes(){
        /*laser ratio
        width ratio = 0.04
        height ratio = 0.33*/
        xCenter = ThreadLocalRandom.current().nextFloat() * (GameScreen.WORLD_WIDTH - 10) + 5;
        yCenter = GameScreen.WORLD_HEIGHT - 5;
        width = height = ThreadLocalRandom.current().nextFloat(60, 90 + 1);
        movementSpeed = ThreadLocalRandom.current().nextFloat(384, 576 + 1);
        shield = ThreadLocalRandom.current().nextInt(2, 6);
        //za lasere mora da se skalira sa width i height
        laserWidth = width * 0.04f;
        laserHeight = height * 0.33f;
        shield = ThreadLocalRandom.current().nextInt(3, 6 + 1);
        laserMovementSpeed = ThreadLocalRandom.current().nextFloat(400, 600);
        timeBetweenShots = ThreadLocalRandom.current().nextFloat(1.8f, 3.6f) - (EnemyShip.initCounter / 400);
        int color = ThreadLocalRandom.current().nextInt(1, 4 + 1);
        String stringColor = "";
        texture = ThreadLocalRandom.current().nextInt(1, 5 + 1);
        switch (color){
            case 1:
                stringColor = new StringBuilder("Black").toString();
                break;
            case 2:
                stringColor = new StringBuilder("Blue").toString();
                break;
            case 3:
                stringColor = new StringBuilder("Green").toString();
                break;
            case 4:
                stringColor = new StringBuilder("Red").toString();
                break;
        }
        if(texture == 1){
            shipTexture = textureAtlas.findRegion("enemy" + stringColor +"1");
        }
        else if(texture == 2){
            shipTexture = textureAtlas.findRegion("enemy" + stringColor +"2");
        }
        else if(texture == 3){
            shipTexture = textureAtlas.findRegion("enemy" + stringColor +"3");
        }
        else if(texture == 4){
            shipTexture = textureAtlas.findRegion("enemy" + stringColor +"4");
        }
        else if(texture == 5){
            shipTexture = textureAtlas.findRegion("enemy" + stringColor +"5");
        }
        shieldTexture = textureAtlas.findRegion("shield1");
        //shieldTexture.flip(false, true);
        laserTextureRegion = textureAtlas.findRegion("laserRed03");


    }



    @Override
    protected EnemyShip newObject() {
        return new EnemyShip();
    }
}
