package com.shooterdemo.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.shooterdemo.game.ShooterDemo;

public class LoadingScreen extends BaseScreen{

    private Skin skin;
    private Group group;
    private Label loadingLabel;
    private ProgressBar progressBar;
    public LoadingScreen(ShooterDemo parent) {
        super(parent);
        stage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal("star-soldier/skin/star-soldier-ui.json"));
        Gdx.input.setInputProcessor(stage);
        CreateUI();

    }

    private void CreateUI(){
        loadingLabel = new Label("LOADING", skin);
        loadingLabel.setBounds((WORLD_WIDTH / 2) - (WORLD_WIDTH / 2 / 2), WORLD_HEIGHT/ 10 * 5.5f, WORLD_WIDTH / 2, WORLD_HEIGHT / 8);
        loadingLabel.setName("loadingLabel");

        progressBar = new ProgressBar(0, 100, 1, false, skin);
        progressBar.setBounds((WORLD_WIDTH / 2) - (WORLD_WIDTH / 2 / 2), WORLD_HEIGHT/ 10 * 5, WORLD_WIDTH / 2, WORLD_HEIGHT / 8);
        progressBar.setName("progressBar");
        progressBar.setAnimateDuration(0.7f);

        group = new Group();
        group.addActor(loadingLabel);
        group.addActor(progressBar);

        stage.addActor(group);
    }

    @Override
    public void show() {
        screenFadeIn();
    }

    @Override
    public void render(float deltaTime) {
        if((progressBar.getValue() == progressBar.getMaxValue()) && !progressBar.isAnimating()){
            screenFadeOut(ShooterDemo.screenEnum.MainMenu);
        }
        proggres();
        ScreenUtils.clear(Color.BLACK);
        batch.begin();

        renderBackground(deltaTime);

        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();

    }

    private void proggres(){
        progressBar.setValue(progressBar.getValue()+1);

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
