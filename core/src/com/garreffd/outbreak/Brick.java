package com.garreffd.outbreak;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.w3c.dom.css.Rect;

import static com.garreffd.outbreak.Constants.*;

/**
 * Created by Domenic on 2017-10-21.
 */

public class Brick {
    private Vector2 position;
    /*
    rowNumber is useful for when resizing occurs. We want to remember the row index so we can make
    changes to position.x according to the screen new width.
    */
    private int rowNumber;
    private float width,height;
    private Color color;
    private Rectangle rectangle;

    public Brick(int rowNumber, float x, float y, float width, float height, Color color){
        position = new Vector2(x, y);
        this.rowNumber = rowNumber;
        this.width = width;
        this.height = height;
        this.color = color;

        rectangle = new Rectangle(x,y,width,height);
    }

    public void render(ShapeRenderer renderer){
        renderer.setColor(color);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.rect(position.x, position.y, width, height);
        renderer.end();
    }
    //Bricks.java will call this to check if the ball collided with this brick.
    public Rectangle getBrickCollider(){
        return rectangle;
    }
    //Setters needed for when resizing occurs.
    public void setWidth(float width){
        this.width = width;
    }

    public void setX(float x){
        position.x = x;
    }

    public void updateCollider(){
        rectangle = new Rectangle(position.x, position.y, width, height);
    }
    /*
    Needed for proper position on the x-axis. The width of the rectangle will change meaning
    each rectangle will have to be shifted a new distance. RowNumber is multiplied by the
    new width to get the new position.
     */
    public int getColIndex(){
        return rowNumber;
    }



}
