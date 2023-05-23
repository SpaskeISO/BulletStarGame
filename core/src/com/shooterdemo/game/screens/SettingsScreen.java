package com.shooterdemo.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.shooterdemo.game.ShooterDemo;

public class SettingsScreen extends BaseScreen{

    //Scene 2d UI
    private Skin skin;
    private Group group;
    //Controls
    public static int controlType = 0; // 0 - Mouse ; 1 - Keyboard


    public SettingsScreen(ShooterDemo parent){
        super(parent);
        skin = new Skin(Gdx.files.internal("star-soldier/skin/star-soldier-ui.json"));
        stage = new Stage(viewport);
        createUI();
    }

    private void createUI(){
        group = new Group();

        final TextButton backgroundButton = new TextButton("", skin);
        backgroundButton.setBounds((WORLD_WIDTH / 2) - (WORLD_WIDTH / 1.5f / 2), WORLD_HEIGHT/10 * 4, WORLD_WIDTH / 1.5f, WORLD_HEIGHT / 3);
        backgroundButton.setName("backgroundButton");
        backgroundButton.setTouchable(Touchable.disabled);

        final Label volumeLabel = new Label("VOLUME", skin);
        volumeLabel.setBounds((WORLD_WIDTH / 2) - (WORLD_WIDTH / 6 / 2), (WORLD_HEIGHT / 10 * 6 ), WORLD_WIDTH / 6, WORLD_HEIGHT / 10);
        volumeLabel.setName("volumeLabel");

        final Slider volumeSlider = new Slider(0, 100, 5, false, skin);
        volumeSlider.setBounds((WORLD_WIDTH / 2) - (WORLD_WIDTH / 2 / 2), (WORLD_HEIGHT / 10 * 5.8f ), WORLD_WIDTH / 2, WORLD_HEIGHT / 25);
        volumeSlider.setName("volumeSlider");
        volumeSlider.setValue(100);
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.soundtrack.setVolume(volumeSlider.getValue()/100f);
            }
        });

        final Label controlsLabel = new Label("Controls", skin);
        controlsLabel.setBounds((WORLD_WIDTH / 2) - (WORLD_WIDTH / 4 / 2), (WORLD_HEIGHT / 10 * 4.8f ), WORLD_WIDTH / 4, WORLD_HEIGHT / 10);
        controlsLabel.setName("controlsLabel");

        final ButtonGroup<CheckBox> buttonGroup = new ButtonGroup<CheckBox>();

        final CheckBox mouseCheckBox = new CheckBox("Mouse", skin);
        mouseCheckBox.setBounds((WORLD_WIDTH / 2) - (WORLD_WIDTH / 2 / 2), (WORLD_HEIGHT / 10 * 4.2f ), WORLD_WIDTH / 2, WORLD_HEIGHT / 10);
        mouseCheckBox.setName("mouseCheckBox");
        mouseCheckBox.setChecked(true);
        mouseCheckBox.align(Align.topLeft);
        mouseCheckBox.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controlType = 0;
                System.out.println("Mouse");
            }
        });

        final CheckBox arrowKeysCheckBox = new CheckBox("Arrow Keys", skin);
        arrowKeysCheckBox.setBounds((WORLD_WIDTH / 2) - (WORLD_WIDTH / 2 / 2), (WORLD_HEIGHT / 10 * 3.8f ), WORLD_WIDTH / 2, WORLD_HEIGHT / 10);
        arrowKeysCheckBox.setName("arrowKeysCheckBox");
        arrowKeysCheckBox.align(Align.topLeft);
        arrowKeysCheckBox.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controlType = 1;
                System.out.println("Keyboard");
            }
        });

        final TextButton backButton = new TextButton("BACK", skin);
        backButton.setBounds((WORLD_WIDTH / 2) - (WORLD_WIDTH / 2 / 2), WORLD_HEIGHT/10 * 2, WORLD_WIDTH / 2, WORLD_HEIGHT / 8);
        backButton.setName("back");
        backButton.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenFadeOut(ShooterDemo.screenEnum.MainMenu);
            }
        });

        buttonGroup.add(mouseCheckBox);
        buttonGroup.add(arrowKeysCheckBox);

        group.addActor(backgroundButton);
        group.addActor(volumeLabel);
        group.addActor(volumeSlider);
        group.addActor(controlsLabel);
        group.addActor(mouseCheckBox);
        group.addActor(arrowKeysCheckBox);
        group.addActor(backButton);
        stage.addActor(group);
    }

    @Override
    public void show() {
        screenFadeIn();
        Gdx.input.setInputProcessor(this.stage);
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
    }
}
