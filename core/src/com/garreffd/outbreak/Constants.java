package com.garreffd.outbreak;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Domenic on 2017-10-20.
 */

public class Constants {
    final static float WORLD_HEIGHT = 10f;
    final static float WORLD_WIDTH = 10f;
    final static Color BACKGROUND_COLOR = new Color(0f,0f,0f,1f);

    //Player
    final static float PLAYER_POSITION_X = 0f;
    final static float PLAYER_POSITION_Y = 1f;
    final static float PLAYER_MOVEMENT_SPEED = 5f;

    final static Color PLAYER_COLOR = new Color(1f,1f,1f,1f);
    final static float PLAYER_WIDTH = 2f;
    final static float PLAYER_HEIGHT = 0.5f;

    //Ball
    final static float BALL_POSITION_X = 0f;
    final static float BALL_POSITION_Y_OFFSET = 2f;

    final static float BALL_INITIAL_VELOCITY_Y = 5f;
    final static float BALL_INITIAL_VELOCITY_X = 3f;


    final static float BALL_RADIUS = 0.25f;
    final static int BALL_SEGMENTS = 20;
    final static Color BALL_COLOR = new Color(1f,1f,1f,1f);
}
