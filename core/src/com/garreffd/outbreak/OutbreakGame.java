package com.garreffd.outbreak;

import com.badlogic.gdx.Game;

public class OutbreakGame extends Game {
	//Gets call once the application is created.
	@Override
	public void create() {
		showTitleScreen();
		//showTestScreen();
	}

	//Creates a new instance of OutBreakScreen and sets it to the current screen.
	public void showOutbreakScreen(){
		setScreen(new OutbreakScreen(this));
	}

	public void showTitleScreen(){
		setScreen(new TitleScene(this));
	}

	public void showTestScreen(){
		setScreen(new TestingScreen(this));
	}
}
