package com.shooterdemo.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.shooterdemo.game.ShooterDemo;

public class MainMenuScreen extends BaseScreen{

    //Scene 2d UI
    private Skin skin;
    private Group group;


    public MainMenuScreen(ShooterDemo parent) {
        super(parent);

        skin = new Skin(Gdx.files.internal("star-soldier/skin/star-soldier-ui.json"));
        stage = new Stage(viewport);

        createUI();



    }

    private void createUI() {
        group = new Group();
        final TextButton exitButton = new TextButton("Exit", skin, "default");
        exitButton.setBounds((WORLD_WIDTH / 2) - (WORLD_WIDTH / 2 / 2), WORLD_HEIGHT/10 * 4, WORLD_WIDTH / 2, WORLD_HEIGHT / 8);
        exitButton.setName("exit");
        exitButton.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        final TextButton settingsButton = new TextButton("Settings", skin, "default");
        settingsButton.setBounds((WORLD_WIDTH / 2) - (WORLD_WIDTH / 2 / 2), WORLD_HEIGHT/10 * 5, WORLD_WIDTH / 2, WORLD_HEIGHT / 8);
        settingsButton.setName("settings");
        settingsButton.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenFadeOut(ShooterDemo.screenEnum.Settings);
            }
        });

        final TextButton gameButton = new TextButton("Play Endless", skin, "default");
        gameButton.setBounds((WORLD_WIDTH / 2) - (WORLD_WIDTH / 2 / 2), WORLD_HEIGHT/10 * 6, WORLD_WIDTH / 2, WORLD_HEIGHT / 8);
        gameButton.setName("endless");
        gameButton.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenFadeOut(ShooterDemo.screenEnum.Game);
            }
        });

        group.addActor(gameButton);
        group.addActor(settingsButton);
        group.addActor(exitButton);
        stage.addActor(group);
    }



    @Override
    public void show() {
        screenFadeIn();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float deltaTime) {
        ScreenUtils.clear(Color.BLACK);


        batch.begin();

        renderBackground(deltaTime);


        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
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
        super.dispose();
        stage.dispose();
        skin.dispose();

    }
}
