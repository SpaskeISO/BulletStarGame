package com.shooterdemo.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shooterdemo.game.ShooterDemo;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public abstract class BaseScreen implements Screen {

    //Parent
    protected ShooterDemo parent;

    //States
    protected enum State { PAUSE, RUN, GAMEOVER }

    //world parameters
    public static final float WORLD_WIDTH = 576;
    public static final float WORLD_HEIGHT = 1024;

    //screen
    protected final Camera camera;
    protected final Viewport viewport;

    //Graphics
    protected SpriteBatch batch;

    public static TextureAtlas textureAtlas;
    protected TextureRegion[] backgrounds;
    protected float backgroundHeight;
    protected Stage stage;


    //timing
    protected float[] backgroundOffsets = {0, 0, 0, 0};
    protected float backgroundMaxScrollingSpeed;

    protected BaseScreen(ShooterDemo parent){
        this.parent = parent;
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        batch = new SpriteBatch();

        textureAtlas = new TextureAtlas(Gdx.files.internal("images.atlas"));

        backgrounds = new TextureRegion[4];
        backgrounds[0] = textureAtlas.findRegion("Starscape00");
        backgrounds[1] = textureAtlas.findRegion("Starscape01");
        backgrounds[2] = textureAtlas.findRegion("Starscape02");
        backgrounds[3] = textureAtlas.findRegion("Starscape03");

        backgroundHeight = WORLD_HEIGHT;
        backgroundMaxScrollingSpeed = WORLD_HEIGHT / 4;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    protected void renderBackground(float deltaTime) {
        backgroundOffsets[0] += deltaTime * backgroundMaxScrollingSpeed / 8;
        backgroundOffsets[1] += deltaTime * backgroundMaxScrollingSpeed / 4;
        backgroundOffsets[2] += deltaTime * backgroundMaxScrollingSpeed / 2;
        backgroundOffsets[3] += deltaTime * backgroundMaxScrollingSpeed;

        for(int i = 0; i < backgroundOffsets.length; i++){
            if(backgroundOffsets[i] > WORLD_HEIGHT){
                backgroundOffsets[i] = 0;
            }
            batch.draw(backgrounds[i], 0, -backgroundOffsets[i], WORLD_WIDTH, backgroundHeight);
            batch.draw(backgrounds[i], 0, -backgroundOffsets[i] + WORLD_HEIGHT, WORLD_WIDTH, backgroundHeight);
        }
    }

    protected void screenFadeIn(){
        stage.getRoot().getColor().a = 0;
        stage.getRoot().addAction(fadeIn(0.5f));
    }

    protected void screenFadeOut(final ShooterDemo.screenEnum screen){
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(fadeOut(0.5f));
        sequenceAction.addAction(run(new Runnable() {
            @Override
            public void run() {
                parent.changeScreen(screen);
            }
        }));
        stage.getRoot().addAction(sequenceAction);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void     resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        textureAtlas.dispose();

    }
}
