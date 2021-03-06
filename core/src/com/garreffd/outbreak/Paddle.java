package com.garreffd.outbreak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.garreffd.outbreak.Constants.*;

/**
 * Created by Domenic on 2017-10-20.
 */

public class Paddle {
    public static float PLAYER_WIDTH_HALF = PLAYER_WIDTH / 2;

    private Vector2 position;
    private Viewport viewport;
    private OutbreakScreen outbreakScreen;

    private int money;
    private int score;
    private int highScore;


    public Paddle(OutbreakScreen outbreakScreen, Viewport viewport){
        //Viewport contains information regarding world size.
        this.viewport = viewport;
        this.outbreakScreen = outbreakScreen;
        init();

        money = 0;
        score = 0;
        highScore = 0;
    }

    public void init(){

        //Sets initial position of paddle.
        position = new Vector2(viewport.getWorldWidth() / 2f - PLAYER_WIDTH_HALF, PLAYER_POSITION_Y);
    }

    public void update(float delta){
        //If left arrow key is pressed, move paddle left.
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && !atWestBoundary()){
            position.x -= delta * PLAYER_MOVEMENT_SPEED;
        //If right arrow key is pressed, move paddle right.
        }else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !atEastBoundary()){
            position.x += delta * PLAYER_MOVEMENT_SPEED;
        }
    }

    //Called in a Screen render's function.
    public void render(ShapeRenderer renderer){
        //Set the color, begin the drawing, draw desired object, and remember to end the renderer.
        renderer.setColor(PLAYER_COLOR.r, PLAYER_COLOR.g, PLAYER_COLOR.b, PLAYER_COLOR.a);
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        renderer.rect(position.x, position.y, PLAYER_WIDTH, PLAYER_HEIGHT);
        renderer.end();
    }

    public Rectangle getPlayerCollider(){
        return new Rectangle(position.x, position.y, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    public Vector2 getPosition(){
        return position;
    }

    /*
    Check if the paddle doesn't leave the boundary to the east.
    Returns true if paddle is at the west bound.
    Returns false if paddle is anywhere but the west bound.
    */
    public boolean atWestBoundary(){
        if(position.x  < 0  ){
            return true;
        }
        return false;
    }

    /*
     Ensure paddle doesn't leave the boundary through the east.
     Returns true if the paddle is at the east bound.
     Returns false if the paddle is anywhere but the east bound.
     */
    public boolean atEastBoundary(){
        if(position.x > viewport.getWorldWidth() - PLAYER_WIDTH   ){
            return true;
        }
        return false;
    }

    /*
    When the ball hits the paddle's paddle the trajectory of the ball depends on the distance between
    the ball and paddle. The angle is clamped between two values returning a float
    between BALL_TRAJECTORY_MIN_ANGLE and BALL_TRAJECTORY_MAX_ANGLE Constants. The MAX_ANGLE constant is reached the ball hits exactly
    the center.
    1st parameter: Balls X position.
    Returns: A value between BALL_TRAJECTORY_MIN_ANGLE and BALL_TRAJECTORY_MAX_ANGLE
     */
    public float getCollidingTrajectory(float ballPositionX){
        /* Gets distance between Paddle and Ball. Set the Y positions to 0, because
            I don't want the Y-axis effecting the horizontal distance.
        */
        float distanceBtwPlayerAndBall = new Vector2(position.x + PLAYER_WIDTH_HALF,0).dst(new Vector2(ballPositionX, 0));

        /* Gets the progress - the ratio between max and min distance the ball can be from the paddle's center
        for example, If the distance is 2 from a paddle with WIDTH_HALF of 2, the ratio is 1.
        */
        float progress = distanceBtwPlayerAndBall / PLAYER_WIDTH_HALF;

        /*
        We use lerp here to linearly interpolate between two angles we want the ball to project after
        hitting the paddle.
        The first parameter is the fromValue. We set it to the MAX_BALL_ANGLE so that if the ball hits
         the center of the paddle, it has a high angle.
        The second parameter is the toValue, for reasons opposite of the first value, we set it to MIN_BALL_ANGLE
        The third parameter is progress. We use Math.Min() in case the ball hits the side of the paddle
            whic would result in progress grater than 1 and since progress is a ratio that can't happen.
         */
        return MathUtils.lerp(BALL_TRAJECTORY_MAX_ANGLE, BALL_TRAJECTORY_MIN_ANGLE, Math.min(1, progress));
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money += money;
    }

    public void setScore(int score) {
        this.score += score;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public void updateGui(){
        if(outbreakScreen != null) {
            outbreakScreen.updateGui(score, money);
        }
    }

}
