package com.shooterdemo.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.shooterdemo.game.*;
import com.shooterdemo.game.powerups.PowerUp;
import com.shooterdemo.game.ships.EnemyShip;
import com.shooterdemo.game.ships.EnemyShipPool;
import com.shooterdemo.game.ships.PlayerShip;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class GameScreen extends BaseScreen{

    private TextureRegion playerShipTextureRegion, playerShieldTextureRegion, playerLaserTextureRegion,
            enemyShipTextureRegion, enemyShieldTextureRegion, enemyLaserTextureRegion, explosionTextureRegion;



    //timing
    private State state;
    private float timeBetweenEnemySpawns = 4f;
    private float enemySpawnTimer = 0;
    //audio
    public Sound explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));
    private Sound laserHitSound = Gdx.audio.newSound(Gdx.files.internal("laser_hit.wav"));

    //Controls
    public enum controlEnum {Mouse, Keyboard}
    public static controlEnum controls;

    //world parameters
    private final float TOUCH_MOVEMENT_THRESHOLD = 5f;

    //game objects;
    private PlayerShip playerShip;
    private LinkedList<EnemyShip> enemyShipList;
    private EnemyShipPool enemyShipPool;
    private LinkedList<Laser> playerLaserList;
    private LinkedList<Laser> enemyLaserList;
    private LinkedList<Explosion> explosionList;
    private LinkedList<PowerUp> powerUpList;
    private LinkedList<PowerUp> lostPowerUpList;

    private int score = 0;
    private int createdEnemies = 0;
    private int destoryedEnemies = 0;
    private int wave = 1;
    private float blinkTime = 0;
    private boolean drawIt = true;

    //HUD
    private BitmapFont font;
    private float hudVerticalMargin, hudLeftX, hudRightX, hudCenterX, hudRow1Y, hudRow2Y, hudSectionWidth;
    private float waveY;
    private float fontOpacity = 1;

    //Control vectors
    private Vector2 touchpoint;
    private Vector2 playerShipCenter;

    //Scene2D.ui pause meni
    private Skin skin;
    private Group pauseGroup;
    private Group gameOverGroup;



    public GameScreen(ShooterDemo parent){
        super(parent);
        state = State.RUN;


        //initialize texture regions;
        playerShipTextureRegion = textureAtlas.findRegion("playerShip2_blue");
        enemyShipTextureRegion = textureAtlas.findRegion("enemyRed3");
        playerShieldTextureRegion = textureAtlas.findRegion("shield2");
        enemyShieldTextureRegion = textureAtlas.findRegion("shield1");
        enemyShieldTextureRegion.flip(false, true);

        playerLaserTextureRegion = textureAtlas.findRegion("laserBlue03");
        enemyLaserTextureRegion = textureAtlas.findRegion("laserRed03");

        explosionTextureRegion = textureAtlas.findRegion("exp2");
        //System.out.println(explosionTextureRegion.getRegionWidth() / 16);

        //Control vectors
        touchpoint = new Vector2();
        playerShipCenter = new Vector2();


        //setup game objects
        playerShip = new PlayerShip(WORLD_WIDTH / 2f, WORLD_HEIGHT / 4f,
                60, 60,
                384, 10,
                3.2f, 32,
                360, 0.5f,
                playerShipTextureRegion, playerShieldTextureRegion,
                playerLaserTextureRegion);
        enemyShipList = new LinkedList<EnemyShip>();

        enemyShipPool = new EnemyShipPool(1, 10, textureAtlas);
        playerLaserList = new LinkedList<Laser>();
        enemyLaserList = new LinkedList<Laser>();
        explosionList = new LinkedList<Explosion>();
        powerUpList = new LinkedList<PowerUp>();
        lostPowerUpList = new LinkedList<PowerUp>();


        batch = new SpriteBatch();

        prepareHUD();

        //Scene2D.ui
        skin = new Skin(Gdx.files.internal("star-soldier/skin/star-soldier-ui.json"));
        stage = new Stage(this.viewport);
        createPauseUI();
        createGameOverUI();


    }
    private void createPauseUI(){
        pauseGroup = new Group();
        pauseGroup.setVisible(false);
        final TextButton backButton = new TextButton("Back", skin, "default");
        backButton.setBounds((WORLD_WIDTH / 2) - (WORLD_WIDTH / 2 / 2), WORLD_HEIGHT/10 * 4, WORLD_WIDTH / 2, WORLD_HEIGHT / 8);
        backButton.setName("back");
        backButton.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resume();
            }
        });

        final TextButton mainMenuButton = new TextButton("Main Menu", skin, "default");
        mainMenuButton.setBounds((WORLD_WIDTH / 2) - (WORLD_WIDTH / 2 / 2), WORLD_HEIGHT/10 * 5, WORLD_WIDTH / 2, WORLD_HEIGHT / 8);
        mainMenuButton.setName("mainMenu");
        mainMenuButton.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenFadeOut(ShooterDemo.screenEnum.MainMenu);
            }
        });

        pauseGroup.addActor(backButton);
        pauseGroup.addActor(mainMenuButton);
        stage.addActor(pauseGroup);
    }

    private void createGameOverUI(){
        gameOverGroup = new Group();
        gameOverGroup.setVisible(false);
        final TextButton tryAgainButton = new TextButton("Try Again", skin, "default");
        tryAgainButton.setBounds((WORLD_WIDTH / 2) - (WORLD_WIDTH / 2 / 2), WORLD_HEIGHT/10 * 4, WORLD_WIDTH / 2, WORLD_HEIGHT / 8);
        tryAgainButton.setName("back");
        tryAgainButton.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                show();
            }
        });

        final TextButton gameOverMainMenuButton = new TextButton("Main Menu", skin, "default");
        gameOverMainMenuButton.setBounds((WORLD_WIDTH / 2) - (WORLD_WIDTH / 2 / 2), WORLD_HEIGHT/10 * 5, WORLD_WIDTH / 2, WORLD_HEIGHT / 8);
        gameOverMainMenuButton.setName("mainMenu");
        gameOverMainMenuButton.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenFadeOut(ShooterDemo.screenEnum.MainMenu);
            }
        });

        gameOverGroup.addActor(tryAgainButton);
        gameOverGroup.addActor(gameOverMainMenuButton);
        stage.addActor(gameOverGroup);
    }

    private void prepareHUD() {
        //Create a BitmapFont from the file
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("EdgeOfTheGalaxyRegular-OVEa6.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 144;
        fontParameter.borderWidth = 3.6f;
        fontParameter.padRight = 3;
        fontParameter.padLeft = 3;
        fontParameter.padTop = 3;
        fontParameter.padBottom = 3;
        fontParameter.color = new Color(1,1,1, 0.3f);
        fontParameter.borderColor = new Color(0,0,0, 0.3f);

        font = fontGenerator.generateFont(fontParameter);

        //scale the font to fit the world
        font.getData().setScale(0.32f);


        hudVerticalMargin = font.getCapHeight() / 2;
        hudLeftX = hudVerticalMargin;
        hudRightX = WORLD_WIDTH * 2 / 3 - hudLeftX;
        hudCenterX = WORLD_WIDTH / 3;
        hudRow1Y = WORLD_HEIGHT - hudVerticalMargin;
        hudRow2Y = hudRow1Y - hudVerticalMargin - font.getCapHeight();
        waveY = hudRow2Y - hudVerticalMargin * 11 - font.getCapHeight();
        hudSectionWidth = WORLD_WIDTH / 3;
    }

    private void setControls(){
        if(SettingsScreen.controlType == 0){
            controls = controlEnum.Mouse;
        }
        else if(SettingsScreen.controlType == 1){
            controls = controlEnum.Keyboard;
        }
    }

    @Override
    public void render(float deltaTime) {
        //System.out.println(Gdx.graphics.getFramesPerSecond());
        if((state == State.PAUSE) || state == State.GAMEOVER){
            deltaTime = 0f;
        }
        //deltaTime = Math.min(deltaTime, 0.0158f);
        ScreenUtils.clear(Color.BLACK);


        spawnEnemyShips(deltaTime);

        //detect collisions between ships and lasers
        detectLaserCollsions();
        //detect collision between playerShip and PowerUps
        detectPowerUpColision();

        detectInput(deltaTime);
        playerShip.update(deltaTime);


        batch.begin();


        //scrolling background
        renderBackground(deltaTime);

        //rendering lasers;
        spawnPlayerLasers();
        renderPlayerLasers(deltaTime);
        renderEnemyLasers(deltaTime);


        for (EnemyShip enemyShip : enemyShipList) {
            moveEnemy(enemyShip, deltaTime);
            enemyShip.update(deltaTime);
            //lasers
            spawnEnemyLasers(enemyShip);
            //enemy ships
            enemyShip.draw(batch);
        }




        //player ship
        playerShip.draw(batch);


        //explosions
        updateAndRenderExplosions(deltaTime);

        //PowerUps
        updateAndRenderPowerUps(deltaTime);

        //HUD rendering
        updateAndRenderHUD();
        renderAcquiredPowerUps();
        batch.end();


        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();
    }

    private void updateAndRenderHUD() {
        //render first row
        Color color = font.getColor();
        font.setColor(color.r, color.g, color.b, 1);
        font.draw(batch, "Score", hudLeftX, hudRow1Y, hudSectionWidth, Align.left, false);
        font.draw(batch, "Shield", hudCenterX, hudRow1Y, hudSectionWidth, Align.center, false);
        font.draw(batch, "Lives", hudRightX, hudRow1Y, hudSectionWidth, Align.right, false);
        //render second row
        font.draw(batch, String.format(Locale.getDefault(), "%06d", score), hudLeftX, hudRow2Y, hudSectionWidth, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%02d", playerShip.currentShield), hudCenterX, hudRow2Y, hudSectionWidth, Align.center, false);
        font.draw(batch, String.format(Locale.getDefault(), "%02d", playerShip.lives), hudRightX, hudRow2Y, hudSectionWidth, Align.right, false);
        if(createdEnemies % 10 == 0 && timeBetweenEnemySpawns > 0 && destoryedEnemies % 10 == 0){
            fontOpacity=1;
            font.setColor(color.r, color.g, color.b, fontOpacity);
            font.draw(batch, "Wave: " + wave, hudCenterX, waveY, hudSectionWidth, Align.center, false);
        }
        else{
            color = font.getColor();
            font.setColor(color.r, color.g, color.b, fontOpacity);
            font.draw(batch, "Wave: " + wave, hudCenterX, waveY, hudSectionWidth, Align.center, false);
            fontOpacity-=0.1;
        }
    }

    private void spawnEnemyShips(float deltaTime){
        //System.out.println("method spawnEnemyShips called");
        if(createdEnemies % 10 != 0 || destoryedEnemies == 10 || createdEnemies == 0){
            enemySpawnTimer += deltaTime;
            if(enemySpawnTimer > timeBetweenEnemySpawns){
                enemySpawnTimer = 0;
                //System.out.println("Enough time passed");
                createdEnemies++;
                if(createdEnemies == 10){
                    wave++;
                }
                EnemyShip newEnemyShip = enemyShipPool.obtain();
                newEnemyShip.init();
                enemyShipList.add(newEnemyShip);
                if(destoryedEnemies == 10){
                    destoryedEnemies = 0;
                }
            }
        }


    }

    private void detectInput(float deltaTime) {


        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            if(state == State.RUN){
                pause();
            }
            else{
                resume();
            }
        }

        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -playerShip.boundingBox.x;
        downLimit = -playerShip.boundingBox.y;
        rightLimit = WORLD_WIDTH - playerShip.boundingBox.x - playerShip.boundingBox.width;
        upLimit = (float)(WORLD_HEIGHT / 2) - playerShip.boundingBox.y - playerShip.boundingBox.height;
        if(controls == controlEnum.Keyboard){
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && rightLimit > 0){
                playerShip.translate(Math.min(playerShip.currentMovementSpeed * deltaTime,rightLimit), 0);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && leftLimit < 0){
                playerShip.translate(Math.max(-playerShip.currentMovementSpeed * deltaTime,leftLimit), 0);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.UP) && upLimit > 0){
                playerShip.translate(0, Math.min(playerShip.currentMovementSpeed * deltaTime,upLimit));
            }
            if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && downLimit < 0){
                playerShip.translate(0, Math.max(-playerShip.currentMovementSpeed * deltaTime,downLimit));
            }
        }
        if(controls == controlEnum.Mouse){
            //touch input (is also mouse)
            if(Gdx.input.isTouched()){
                //get the screen position of the touch
                float xTouchPixels = Gdx.input.getX();
                float yTouchPixels = Gdx.input.getY();

                //convert to the world position
                touchpoint.set(xTouchPixels, yTouchPixels);
                touchpoint = viewport.unproject(touchpoint);


                //calculate the x and y differences
                playerShipCenter.set(
                        playerShip.boundingBox.x + playerShip.boundingBox.width / 2,
                        playerShip.boundingBox.y + playerShip.boundingBox.height / 2);

                float touchDistance = touchpoint.dst(playerShipCenter);
                if(touchDistance > TOUCH_MOVEMENT_THRESHOLD){
                    float xTouchDifference = touchpoint.x - playerShipCenter.x;
                    float yTouchDifference = touchpoint.y - playerShipCenter.y;
                    //scale to maximum speed of the ship
                    float xMove = (xTouchDifference / touchDistance) * playerShip.currentMovementSpeed * deltaTime;
                    float yMove = (yTouchDifference / touchDistance) * playerShip.currentMovementSpeed * deltaTime;

                    if(xMove > 0){ xMove = Math.min(xMove, rightLimit); }
                    else{ xMove = Math.max(xMove, leftLimit); }
                    if(yMove > 0){ yMove = Math.min(yMove, upLimit); }
                    else{ yMove = Math.max(yMove, downLimit); }

                    playerShip.translate(xMove, yMove);
                }
            }
        }
    }

    private void moveEnemy(EnemyShip enemyShip, float deltaTime) {
        //System.out.println("method moveEnemyCalled");
        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -enemyShip.boundingBox.x;
        downLimit = (float)(WORLD_HEIGHT/2)-enemyShip.boundingBox.y;
        rightLimit = WORLD_WIDTH - enemyShip.boundingBox.x - enemyShip.boundingBox.width;
        upLimit = WORLD_HEIGHT - enemyShip.boundingBox.y - enemyShip.boundingBox.height;

        float xMove = enemyShip.getDirectionVector().x * enemyShip.movementSpeed * deltaTime;
        float yMove = enemyShip.getDirectionVector().y * enemyShip.movementSpeed * deltaTime;

        if(xMove > 0){ xMove = Math.min(xMove, rightLimit); }
        else{ xMove = Math.max(xMove, leftLimit); }
        if(yMove > 0){ yMove = Math.min(yMove, upLimit); }
        else{ yMove = Math.max(yMove, downLimit); }

        enemyShip.translate(xMove, yMove);


    }

    private void detectLaserCollsions() {
        ListIterator<Laser> laserListIterator = playerLaserList.listIterator();
        while(laserListIterator.hasNext()) {
            Laser laser = laserListIterator.next();
            ListIterator<EnemyShip> enemyShipListIterator = enemyShipList.listIterator();
            while(enemyShipListIterator.hasNext()){
                EnemyShip enemyShip = enemyShipListIterator.next();
                if(enemyShip.intersects(laser.boundingBox)){
                    laserHitSound.play();
                    if(enemyShip.hitAndCheckDestroyed()){
                        enemyShipListIterator.remove();
                        spawnPowerUp(enemyShip);
                        explosionSound.play();
                        score += 10;
                        destoryedEnemies++;
                        explosionList.add(new Explosion(explosionTextureRegion, new Rectangle(enemyShip.boundingBox), 0.7f));
                        enemyShipPool.free(enemyShip);
                    }
                    laserListIterator.remove();
                    break;
                }
            }

        }
        laserListIterator = enemyLaserList.listIterator();
        while(laserListIterator.hasNext()) {
            Laser laser = laserListIterator.next();
            if(playerShip.intersects(laser.boundingBox)){
                laserHitSound.play();
                if(playerShip.hitAndCheckDestroyed()){
                    gameOver();
                    explosionList.add(new Explosion(explosionTextureRegion, new Rectangle(playerShip.boundingBox), 1.6f));
                    explosionSound.play();
                    if(playerShip.isShieldUpgraded){
                        playerShip.currentShield = playerShip.upgradedShield;
                    }
                    else{
                        playerShip.currentShield = playerShip.shield;
                    }
                    playerShip.lives--;
                    dropPowerUp();

                }
                laserListIterator.remove();
            }
        }
    }

    private void gameOver(){
        if(playerShip.lives <= 0){
            state = State.GAMEOVER;
            gameOverGroup.setVisible(true);
        }
    }

    private void updateAndRenderExplosions(float deltaTime) {
        ListIterator<Explosion> explosionListIterator = explosionList.listIterator();
        while(explosionListIterator.hasNext()){
            Explosion explosion = explosionListIterator.next();
            explosion.update(deltaTime);
            if(explosion.isFinished()){
                explosionListIterator.remove();
            }
            else{explosion.draw(batch);}
        }

    }

    private void updateAndRenderPowerUps(float deltaTime){
        //draw PowerUps
        //remove old PowerUps
        ListIterator<PowerUp> iterator = powerUpList.listIterator();
        while(iterator.hasNext()){
            PowerUp powerUp = iterator.next();
            powerUp.draw(batch);
            powerUp.boundingBox.y -= powerUp.movementSpeed * deltaTime;
            if(powerUp.boundingBox.y + powerUp.boundingBox.height < 0){
                iterator.remove();
            }
        }
        //draw dropped PowerUps
        blinkTime+=deltaTime;
        if(blinkTime >= 0.25){
            drawIt = !drawIt;
            blinkTime = 0;
        }
        if(drawIt) {
            iterator = lostPowerUpList.listIterator();
            while (iterator.hasNext()) {
                PowerUp powerUp = iterator.next();
                powerUp.draw(batch);
            }

        }
        //remove old dropped PowerUps
        iterator = lostPowerUpList.listIterator();
        while(iterator.hasNext()){
            PowerUp powerUp = iterator.next();
            powerUp.boundingBox.y -= powerUp.movementSpeed * deltaTime;
            if(powerUp.boundingBox.y + powerUp.boundingBox.height < 0){
                iterator.remove();
            }
        }



    }

    private void spawnPowerUp(EnemyShip enemyShip){
        if(ThreadLocalRandom.current().nextFloat(0, 100) < 10){
            powerUpList.add(new PowerUp(enemyShip.boundingBox.x, enemyShip.boundingBox.y, textureAtlas));
        }
    }

    private void dropPowerUp(){
        if(playerShip.powerUpList.size() > 0){
            System.out.println();
            PowerUp powerUp = playerShip.powerUpList.get(ThreadLocalRandom.current().nextInt(0, playerShip.powerUpList.size()));
            playerShip.powerUpList.remove(powerUp);
            lostPowerUpList.add(powerUp);
        }

    }

    private void detectPowerUpColision(){
        ListIterator<PowerUp> powerUpListIterator1 = powerUpList.listIterator();
        while(powerUpListIterator1.hasNext()){
            PowerUp powerUp1 = powerUpListIterator1.next();
            if(powerUp1.boundingBox.overlaps(playerShip.boundingBox)){
                ListIterator<PowerUp> powerUpListIterator2 = playerShip.powerUpList.listIterator();
                int different = 0;
                while(powerUpListIterator2.hasNext()){
                    PowerUp powerUp2 = powerUpListIterator2.next();
                    if(powerUp2.type !=  powerUp1.type){
                        different++;
                    }


                }
                if(different == playerShip.powerUpList.size()){
                    powerUpListIterator1.remove();
                    powerUpListIterator2.add(powerUp1);
                }
                if(playerShip.powerUpList.size() == 0){
                    powerUpListIterator1.remove();
                    powerUpListIterator2.add(powerUp1);
                }
                playerShip.checkPowerUps();
            }
        }
    }

    private void renderAcquiredPowerUps(){
        float x = 10;
        float y = 10;
        for(PowerUp powerUp : playerShip.powerUpList){
            powerUp.uiDraw(batch, x, y);
            x += powerUp.boundingBox.width + 10;
        }
    }

    private void spawnPlayerLasers(){
        if(playerShip.canFireLaser()){
            Laser[] lasers = playerShip.fireLasers();
            playerLaserList.addAll(Arrays.asList(lasers));
        }
    }

    private void renderPlayerLasers(float deltaTime){

        ListIterator<Laser> iterator = playerLaserList.listIterator();
        while(iterator.hasNext()){
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y += laser.movementSpeed * deltaTime;
            if(laser.boundingBox.y > WORLD_HEIGHT){
                iterator.remove();
            }
        }
    }

    private void spawnEnemyLasers(EnemyShip enemyShip){
        if(enemyShip.canFireLaser()){
            Laser[] lasers = enemyShip.fireLasers();
            enemyLaserList.addAll(Arrays.asList(lasers));
        }
    }

    private void renderEnemyLasers(float deltaTime){
        //draw lasers
        //remove old lasers
        ListIterator<Laser> iterator = enemyLaserList.listIterator();
        while(iterator.hasNext()){
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y -= laser.movementSpeed * deltaTime;
            if(laser.boundingBox.y + laser.boundingBox.height < 0){
                iterator.remove();
            }
        }
    }




    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        state = State.PAUSE;
        pauseGroup.setVisible(true);
    }

    @Override
    public void resume() {
        state = State.RUN;
        pauseGroup.setVisible(false);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
    }

    @Override
    public void show() {
        screenFadeIn();
        Gdx.input.setInputProcessor(stage);
        resume();
        setControls();
        enemyShipList.clear();
        enemyLaserList.clear();
        playerLaserList.clear();
        explosionList.clear();
        powerUpList.clear();
        lostPowerUpList.clear();
        playerShip.lives = 3;
        if(playerShip.isShieldUpgraded){
            playerShip.currentShield = playerShip.upgradedShield;
        }
        else{
            playerShip.currentShield = playerShip.shield;
        }
        playerShip.timeSinceLastShot = 0;
        playerShip.boundingBox.setPosition((WORLD_WIDTH / 2f) - (playerShip.boundingBox.width / 2), (WORLD_HEIGHT / 4f) - (playerShip.boundingBox.height / 2));
        gameOverGroup.setVisible(false);

        //TEST
        //playerShip.currentShield = 4;

    }

}
