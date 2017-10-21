package com.garreffd.outbreak;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class OutbreakGame extends Game {
	//Gets call once the application is created.
	@Override
	public void create() {
		showOutbreakScreen();
	}

	//Creates a new instance of OutBreakScreen and sets it to the current screen.
	public void showOutbreakScreen(){
		setScreen(new OutbreakScreen(this));
	}
}
