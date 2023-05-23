package com.shooterdemo.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.shooterdemo.game.screens.GameScreen;
import com.shooterdemo.game.screens.LoadingScreen;
import com.shooterdemo.game.screens.MainMenuScreen;
import com.shooterdemo.game.screens.SettingsScreen;

public class ShooterDemo extends Game {


	//Screens
	private GameScreen gameScreen;
	private MainMenuScreen mainMenuScreen;
	private SettingsScreen settingsScreen;
	private LoadingScreen loadingScreen;

	//Music
	public Music soundtrack;


	//Screen enums
	public enum screenEnum {MainMenu, Settings, Game, Loading }


	@Override
	public void create() {
		soundtrack = Gdx.audio.newMusic(Gdx.files.internal("Abstraction - Patreon Goal Reward Loops/Patreon Goal Reward Loops - Track 08.wav"));
		soundtrack.play();
		soundtrack.setLooping(true);
		screenEnum screen = screenEnum.MainMenu;
		changeScreen(screen);

	}

	public void changeScreen(screenEnum screen){
		if(screen == screenEnum.MainMenu){
			if(mainMenuScreen == null) { mainMenuScreen = new MainMenuScreen(this); }
			this.setScreen(mainMenuScreen);
		}
		else if(screen == screenEnum.Settings){
			if(settingsScreen == null) {settingsScreen = new SettingsScreen(this); }
			this.setScreen(settingsScreen);
		}
		else if(screen == screenEnum.Game){
			if(gameScreen == null) { gameScreen = new GameScreen(this);
				System.out.println("New game screen made"); }
			this.setScreen(gameScreen);
		}
		else if(screen == screenEnum.Loading){
			if(loadingScreen == null) { loadingScreen = new LoadingScreen(this); }
			this.setScreen(loadingScreen);
		}
	}

	@Override
	public void dispose() {
		if(gameScreen != null){
			gameScreen.dispose();
		}
		if(mainMenuScreen != null){
			mainMenuScreen.dispose();
		}
		if(settingsScreen != null){
			settingsScreen.dispose();
		}
	}

	@Override
	public void render() {
		super.render();

	}

	@Override
	public void resize(int width, int height) {
		getScreen().resize(width, height);
	}
}
