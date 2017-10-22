package com.garreffd.outbreak;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.garreffd.outbreak.Constants.*;

/**
 * Created by Domenic on 2017-10-21.
 */

public class Bricks {

    /*
    DelayedRemovalArray.
   Objects in the array are not removed immediately which is fine for our game.
   Allows code out of your control (render, update ??)to remove items without affecting
   iteration.
     */
    private DelayedRemovalArray<Brick> bricks;
    private int numRows;
    private int cols;

    private Viewport viewport;

    public Bricks(Viewport viewport, int numRows, int numCols){
        this.viewport = viewport;
        this.numRows = numRows;
        this.cols = numCols;



    }

    public void init(){
        final float BRICK_WIDTH = viewport.getWorldWidth() / cols;

        //If bricks is not null, an instance of bricks are present and resize was called.
        if(bricks != null) {
            for(Brick b : bricks){
                b.setWidth(BRICK_WIDTH);
                b.setX( b.getColIndex() * BRICK_WIDTH );
                b.updateCollider();
            }
            return;
        }

        bricks = new DelayedRemovalArray<Brick>(false, numRows * cols);

        for(int row = 0; row < numRows; row++){
            for(int col = 0; col < cols ; col++){
                bricks.add(new Brick(col, col * BRICK_WIDTH , viewport.getWorldHeight() - (row * BRICK_HEIGHT),BRICK_WIDTH, BRICK_HEIGHT ,getRandomColor()) );
            }
        }
    }

    /*
    Draw remaining bricks onto screen.
     */
    public void render(ShapeRenderer renderer){
        for(Brick b : bricks){
            b.render(renderer);
        }
    }

    /*
    Check every Brick for collisions.
     */
    public void update(Ball ball){
        //delayedRemovalArray will start queuing removals after begin().
        bricks.begin();
        for(int i = 0; i < bricks.size ; i++){
            /*
                We check for collision, by passing the brick collider and true. We pass true to
                tell the method we are a brick, not the player. A brick has different mechanics
                when hit, and is dealt with properly by checkPlayerCollision.
             */
            if(ball.checkPlayerCollision(bricks.get(i).getBrickCollider(), true)){
                //If checkPlayerCollision returns true, remove brick.
                bricks.removeIndex(i);
            }
        }
        //The items aren't actually removed until end() is called.
        bricks.end();
    }
    /*
    Returns a random Color.
     */
    public Color getRandomColor(){
        switch(MathUtils.random(Constants.DIFFERENT_BRICK_COLORS)){
            case 1: return BRICK_COLOR_RED;
            case 2: return BRICK_COLOR_GREEN;
            case 3: return BRICK_COLOR_BLUE;
        }
        return Color.WHITE;
    }

}