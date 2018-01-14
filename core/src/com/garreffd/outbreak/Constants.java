//TODO: Add accelermoter functionality or arrows for moving paddle on android.
//TODO: ADD GUI to top of screen for score and money.

package com.garreffd.outbreak;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Domenic on 2017-10-20.
 */

public class Constants {
    public final static float WORLD_HEIGHT = 10f;
    public final static float WORLD_WIDTH = 10f;
    final static Color BACKGROUND_COLOR = new Color(0f,0f,0f,1f);

    //Paddle
    public final static float PLAYER_POSITION_X = 0f;
    public final static float PLAYER_POSITION_Y = 1f;
    public final static float PLAYER_MOVEMENT_SPEED = 5f;

    public final static Color PLAYER_COLOR = new Color(1f,1f,1f,1f);
    public final static float PLAYER_WIDTH = 2f;
    public final static float PLAYER_HEIGHT = 0.5f;

    public final static float BALL_TRAJECTORY_MIN_ANGLE = 0.25f;
    public final static float BALL_TRAJECTORY_MAX_ANGLE = 0.90f;


    //Ball
    public final static float BALL_POSITION_X = 0f;
    public final static float BALL_POSITION_Y_OFFSET = 2f;

    public final static float BALL_INITIAL_SPEED = 5f;
    public final static float BALL_SPEED_MODIFIER = 0f;

    public final static float BALL_INITIAL_VELOCITY_Y = 5f;
    public final static float BALL_INITIAL_VELOCITY_X = 5f;




    public final static float BALL_RADIUS = 0.25f;
    public final static int BALL_SEGMENTS = 20;
    public final static Color BALL_COLOR = new Color(1f,1f,1f,1f);

    //Brick
    public final static float BRICK_HEIGHT = 0.5f;
    public final static int BRICK_ROWS = 7;
    public final static int BRICK_COLS = 10;
    public final static float BRICK_OFFSET_FROM_TOP = 1f;

    public final static Color BRICK_COLOR_RED = new Color(1f,0f,0f,1f);
    public final static Color BRICK_COLOR_ORANGE = new Color(1f,0.5f,0f,1f);
    public final static Color BRICK_COLOR_YELLOW = new Color(1f,1f,0f,1f);
    public final static Color BRICK_COLOR_GREEN= new Color(0f,1f,0f,1f);
    public final static Color BRICK_COLOR_BLUE = new Color(0f,0f,1f,1f);
    public final static Color BRICK_COLOR_INDIGO = new Color(0.29f,0f,0.52f,1f);
    public final static Color BRICK_COLOR_VIOLET = new Color(0.58f,0f,0.82f,1f);

    //TitleScreen.java

    public final static float BUTTON_WIDTH = 3f;
    public final static float BUTTON_HEIGHT = 1f;

    public final static float PLAY_BUTTON_Y_OFFSET = 4f;
    public final static String PLAY_LABEL = "Play";

    public final static float TITLE_BUTTON_Y_OFFSET = 6f;
    public final static String TITLE_LABEL = "Break-Out";
}
