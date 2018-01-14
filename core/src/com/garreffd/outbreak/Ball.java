package com.garreffd.outbreak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.garreffd.outbreak.Constants.*;

/**
 * Created by Domenic on 2017-10-20.
 */

public class Ball {
    private final int FORWARD_MOVEMENT = 1;
    private final int REVERSE_MOVEMENT = -1;

    private final int NEGATE_MOVEMENT = -1;

    private OutbreakGame game;
    private Paddle paddle;

    private OutbreakScreen outBreakScreen;
    private Viewport viewport;




    //Ball
    private Vector2 position;
    private Vector2 velocity;
    private float currentSpeedModifier;

    private int verticalBallDirection = 1;
    private int horizontalBallDirection = -1;

    //Circle is used for checking for collisions with the paddle.
    private Circle circle;
    //Collision variables. This prevents multiple collisions after consecutive frames.
    private boolean collision;
    private final static float COLLISION_RESET_TIMER = 0.01f;

    public Ball(OutbreakGame game, Viewport viewport, Paddle paddle){
        this.game = game;
        this.viewport = viewport;
        this.paddle = paddle;

        currentSpeedModifier = BALL_INITIAL_SPEED;
    }

    //Sets the Ball to be placed in the middle of the screen. The velocity is set to project upwards on start.
    //Todo: Resizing screen causes ball to continue moving.So while resizing the ball needs to stop its motion.

    public void init(){
        if(position == null) {
            position = new Vector2(viewport.getWorldWidth() / 2, PLAYER_POSITION_Y + BALL_POSITION_Y_OFFSET);
        }
        if(velocity == null) {
            //float initialXSpeed = MathUtils.random(-BALL_INITIAL_VELOCITY_X, BALL_INITIAL_VELOCITY_X);
            velocity = new Vector2(horizontalBallDirection, BALL_INITIAL_VELOCITY_Y);
        }
        circle = new Circle(position.x, position.y, BALL_RADIUS);
        collision = false;
    }

    //Draws the ball and sets the colour. Called in the Screen render function.
    public void render(ShapeRenderer renderer){
        renderer.setColor(BALL_COLOR);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.circle(position.x, position.y, BALL_RADIUS, BALL_SEGMENTS);
        renderer.set(ShapeRenderer.ShapeType.Point);
        renderer.point(position.x, position.y , 0);

        renderer.end();
    }

    //Called every time the screen is rendered. Called in the Screen's render method.
    public void update(float delta){
        circle.setPosition(position);
        //Check if Ball has hit any bounds (north,west,east) or if the south bound is hit the game is over.
        checkBounds();
        //Checks if ball collided with paddle.
        checkPaddleCollision(paddle.getPlayerCollider());
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
            changeVerticalDirection(NEGATE_MOVEMENT);
        }
        // If the ball hits any walls, reverse velocity.x
        else if( position.x - BALL_RADIUS < 0 || position.x + BALL_RADIUS > viewport.getWorldWidth() && !collision) {
            changeHorizontalDirection(NEGATE_MOVEMENT);
        //If the ball moves below the paddle, the game is over.
        }else if(position.y < 0){
            //For now reset ball when out of Y bounds.
            game.showTitleScreen();
        }
    }
    /*
    Checks if the ball collides with paddle or bricks.
    Note to self: This method does too much, some lines should be extracted
    to make for better understanding.
    Returns true if collision occurs, false otherise.
    */
    public boolean checkPaddleCollision(Rectangle paddle){
        /* If paddle collides with ball, reverse the movement.
        This method also handles if a brick is hit. If the side of the brick is hit,
        vertical movement doesn't change, and the horizontal movement does change.
         */
        if(Intersector.overlaps(circle,paddle) && !collision){
            changeVerticalDirection(FORWARD_MOVEMENT);
            //Sets the x and y ratio variables proportionally to the distance from the center of the paddle.
            float yMovementSpeedRatio = this.paddle.getCollidingTrajectory(position.x);
            float xMovementSpeedRatio = 1 - yMovementSpeedRatio;

            /*
            Gdx.app.log("BALL", "xRatio " + xMovementSpeedRatio);
            Gdx.app.log("BALL", "yRatio " + yMovementSpeedRatio);
            Gdx.app.log("BALL", "velocity.x " + velocity.x);
            Gdx.app.log("BALL", "velocity.y " + velocity.y);
            */

            /*
            Added velocity.Y value to be proportional to where the paddle hits the ball.
            If the ball was moving upwards with a positive velocity, make it negative.
            If it was negative make it positive.
             */

            velocity.y = yMovementSpeedRatio * currentSpeedModifier ;
            velocity.x = xMovementSpeedRatio * currentSpeedModifier * horizontalBallDirection;


            position.y = paddle.y + paddle.height + BALL_RADIUS;

                /* If the ball hits the left side of the paddle, the balls trajectory
                should be reversed( move to the left ) else if the right side of the paddle
                is hit, the ball should move forward (move to the right).
                The first If statement takes the center of the ball and compares
                 */
                if(position.x < this.paddle.getPosition().x + PLAYER_WIDTH / 2){
                    horizontalBallDirection = REVERSE_MOVEMENT;
                    velocity.x = currentSpeedModifier * horizontalBallDirection;
                } else {
                    horizontalBallDirection = FORWARD_MOVEMENT;
                    velocity.x = currentSpeedModifier * horizontalBallDirection;
                }
        return true;
        }
        return false;
    }

    public boolean checkBrickCollision(Rectangle brick) {
        if (Intersector.overlaps(circle, brick) && !collision) {
            collision = true;
            resetCollision(COLLISION_RESET_TIMER);

            //If we hit a brick, a few things differ.
            //1: If we hit any side, the ball should continue its trajectory. ie no change in velocity.y
            //2: If we hit the bottom/top, the velocity.x should not change in direction, but the velocity.y should reverse.

            //Check if the side was hit.
            if (isHittingSideOfBrick(brick)) {
                //Right side collision of brick
                if (isHittingRightSide(brick)) {
                    position.x = brick.x + brick.width + BALL_RADIUS;
                    changeHorizontalDirection(FORWARD_MOVEMENT);
                    //velocity.x = Math.abs(velocity.x);
                    Gdx.app.log("BALL", "HITTING RIGHT SIDE");
                }
                //Left side collision of brick
                else if (isHittingLeftSide(brick)) {
                    Gdx.app.log("BALL", "HITTING LEFT SIDE");
                    position.x = brick.x - BALL_RADIUS;
                    changeHorizontalDirection(REVERSE_MOVEMENT);
                    //velocity.x = velocity.x * horizontalBallDirection;
                }
            } else {
                //Top of brick hit.
                if(isHittingTopSide(brick)){
                    position.y = brick.y + brick.height + BALL_RADIUS;
                    changeVerticalDirection(FORWARD_MOVEMENT);
                    Gdx.app.log("BALL", "HITTING TOP SIDE");
                }
                //Bottom of brick hit.
                else if (isHittingBottomSide(brick)) {
                    position.y = brick.y - BALL_RADIUS;
                    changeVerticalDirection(REVERSE_MOVEMENT);
                }
            }
            //Return true, collision occured.
            return true;
        }
        return false;
    }



    //Adds velocity to the ball every time the update loop is called.
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

    /*
    Checks if the ball has hit the side.
    For the side of a brick to be hit, the ball has to be in taller than the bottom edge
    of the brick, and shorter than the top of the brick. Also the ball must be less than or
    greater than the length of the brick to actually hit the side.
    Returns true if the side of the brick is hit
    Returns false if the side was not hit.
     */
    //TODO There ball performs unexpectedly when hit the corner right corner( maybe left) of the brick.
    boolean isHittingSideOfBrick(Rectangle brick){
        //Check if between height of brick
        if( isBelowBrick(brick) && isAboveBrick(brick) ){
                Gdx.app.log("BALL", "SIDE OF BRICK HIT");
                return true;
        }
        return false;
    }


    private void changeVerticalDirection(int direction){
        collision = true;
        verticalBallDirection = direction;

        currentSpeedModifier += BALL_SPEED_MODIFIER;
        velocity.y = verticalBallDirection * currentSpeedModifier;

        if(position.y > viewport.getWorldHeight() + BALL_RADIUS) {
            position.y = viewport.getWorldHeight() - BALL_RADIUS;
        }
        resetCollision(COLLISION_RESET_TIMER);
    }

    private void changeHorizontalDirection(int direction){
        collision = true;
        horizontalBallDirection = direction;

        if(position.x - BALL_RADIUS < 0){
            position.x = BALL_RADIUS;
            horizontalBallDirection = FORWARD_MOVEMENT;
        }else if(position.x + BALL_RADIUS > viewport.getWorldWidth()){
            position.x = viewport.getWorldWidth() - BALL_RADIUS;
            horizontalBallDirection = REVERSE_MOVEMENT;
        }

        currentSpeedModifier += BALL_SPEED_MODIFIER;
        velocity.x = horizontalBallDirection * currentSpeedModifier;

        resetCollision(COLLISION_RESET_TIMER);
    }

    //private void checkSideBoundsAnd

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    private boolean isAboveBrick(Rectangle brick) {

        return position.y  >= brick.y;
    }

    private boolean isBelowBrick(Rectangle brick){
        return position.y  <= brick.y + brick.getHeight();
    }

    private boolean isHittingLeftSide(Rectangle brick){
        return position.x < brick.x;
    }

    private boolean isHittingRightSide(Rectangle brick){
        return position.x > brick.x + brick.width;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    private boolean isHittingTopSide(Rectangle brick){
        return position.y > brick.y + brick.height;
    }

    private boolean isHittingBottomSide(Rectangle brick){
        return position.y < brick.y;
    }
}
