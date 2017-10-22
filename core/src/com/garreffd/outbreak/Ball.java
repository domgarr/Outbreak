package com.garreffd.outbreak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.javafx.geom.Shape;

import static com.garreffd.outbreak.Constants.*;

/**
 * Created by Domenic on 2017-10-20.
 */

public class Ball {
    public static final float REVERSE_MOVEMENT = -1;

    private Player player;

    private OutbreakScreen outBreakScreen;
    private Viewport viewport;

    private Vector2 position;
    private Vector2 velocity;
    //Circle is used for checking for collisions with the player.
    private Circle circle;
    //Collision variables. This prevents multiple collisions after consecutive frames.
    private boolean collision;
    private final static float COLLISION_RESET_TIMER = 0.05f;



    public Ball(Viewport viewport, Player player){
        this.viewport = viewport;
        this.player = player;
    }

    //Sets the Ball to be placed in the middle of the screen. The velocity is set to project upwards on start.
    public void init(){
        position = new Vector2(viewport.getWorldWidth() / 2, PLAYER_POSITION_Y + BALL_POSITION_Y_OFFSET);
        velocity = new Vector2(0f, BALL_INITIAL_VELOCITY_Y);
        circle = new Circle(position.x, position.y, BALL_RADIUS);
        collision = false;

    }

    //Draws the ball and sets the colour. Called in the Screen render function.
    public void render(ShapeRenderer renderer){
        renderer.setColor(BALL_COLOR);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.circle(position.x, position.y, BALL_RADIUS, BALL_SEGMENTS);
        renderer.end();
    }

    //Called every time the screen is rendered. Called in the Screen's render method.
    public void update(float delta){
        circle.setPosition(position);
        //Check if Ball has hit any bounds (north,west,east) or if the south bound is hit the game is over.
        checkBounds();
        //Checks if ball collided with player.
        checkPlayerCollision(player.getPlayerCollider(), false);

        //Adds velocity to the position vector.
        addMovement(delta);
    }

    /*Checks to see if the Ball hits the boundaries(The boundaries for Outbreak are north, east, and west
    - south is game over)
    If the upper Y boundary is hit, the velocity.y is reversed.
    If the left or right X boundary is hit, the velocity.x is reversed.
    If the lower Y boundary is surpassed, the game is over.
    */

    private void checkBounds(){
        //If the ball is greater than the height of the map, reverse the velocity.y
        if(position.y + BALL_RADIUS > viewport.getWorldHeight() && !collision){
            collision = true;
            velocity.y *= REVERSE_MOVEMENT;
            resetCollision(COLLISION_RESET_TIMER);
        }
        // If the ball hits any walls, reverse velocity.x
        else if( (position.x - BALL_RADIUS < 0 || position.x + BALL_RADIUS > viewport.getWorldWidth()) && !collision) {
            collision = true;
            velocity.x *= REVERSE_MOVEMENT;
            resetCollision(COLLISION_RESET_TIMER);
        //If the ball moves below the player, the game is over.
        }else if(position.y < 0){
            //For now reset ball when out of Y bounds.
            init();
        }
    }

    public boolean checkPlayerCollision(Rectangle rectangle, boolean isBrick){
        //If the player collides with ball, reverse the movement.
        if(Intersector.overlaps(circle,rectangle) && !collision){
            //Set collision to true to prevent multiple collisions.
            collision = true;
            //By default if the player is hit or the bottom/top of a brick is hit, reverse movement.
            velocity.y *= REVERSE_MOVEMENT;
            //If we hit a brick, a few things differ.
            //1: If we hit any side, the ball should continue its trajectory. ie no change in velocity.y
            //2: If we hit the bottom/top, the velocity.x should not change in direction.
            if(isBrick){
                //Check if the side was hit.
                if(position.y > rectangle.y){
                    velocity.y *= REVERSE_MOVEMENT;
                    velocity.x *= REVERSE_MOVEMENT;
                }
                //Reset Collisions allows for collisions to occur again after a interval.
                resetCollision(COLLISION_RESET_TIMER);
                //Since we are returning here, we must call resetCollision again.
                return true;
            }

            //If the ball hits the west area of the player, move the ball west.
            if(position.x < player.getPosition().x + PLAYER_WIDTH / 2){
                velocity.x = -BALL_INITIAL_VELOCITY_X;
            }
            //Else the east side was hit, move the ball east.
            else {
                velocity.x = BALL_INITIAL_VELOCITY_X;
            }
            //Reset Collisions allows for collisions to occur again after a interval.
            resetCollision(COLLISION_RESET_TIMER);
        return true;
        }
        return false;
    }

    private void addMovement(float delta){
        position.y += velocity.y * delta;
        position.x += velocity.x * delta;
    }

    /* Resets 'collision' after a interval to allow for collisions to occur again
    after a interval.
    1st and only param:
    */
    private void resetCollision(float intervalSeconds){
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                collision = false;
            }
        }, intervalSeconds);
    }
}
