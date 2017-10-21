package com.garreffd.outbreak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.garreffd.outbreak.Constants.*;

/**
 * Created by Domenic on 2017-10-20.
 */

public class Player {
    Vector2 position;
    Viewport viewport;

    public Player(Viewport viewport){
        //Viewport contains information regarding world size.
        this.viewport = viewport;
        init();
    }

    public void init(){

        //Sets initial position of player.
        position = new Vector2(viewport.getWorldWidth() / 2f - PLAYER_WIDTH / 2, PLAYER_POSITION_Y);
    }

    public void update(float delta){
        //If left arrow key is pressed, move player left.
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            position.x -= delta * PLAYER_MOVEMENT_SPEED;
        //If right arrow key is pressed, move player right.
        }else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
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

    public Rectangle getRectangleCollider(){
        return new Rectangle(position.x, position.y, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    public Vector2 getPosition(){
        return position;
    }


}