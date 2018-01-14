package com.garreffd.outbreak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.sun.corba.se.impl.orbutil.closure.Constant;

import java.awt.Color;

import static com.garreffd.outbreak.Constants.BACKGROUND_COLOR;
import static com.garreffd.outbreak.Constants.PLAYER_MOVEMENT_SPEED;

/**
 * Created by Domenic on 2018-01-11.
 */

public class TestingScreen implements Screen {
    ExtendViewport outbreakViewport;
    ShapeRenderer renderer;

    OutbreakGame game;
    Paddle paddle;
    Ball ball;
    Brick brick;

    public TestingScreen(OutbreakGame game){
        this.game = game;
    }


    @Override
    public void show() {



        outbreakViewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);

        paddle = new Paddle(null, outbreakViewport);
        ball = new Ball(game, outbreakViewport, paddle);
        brick = new Brick(6,4,5,3);

        init();
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) ){
            init();
            //If right arrow key is pressed, move paddle right.
        }

        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        renderer.setProjectionMatrix(outbreakViewport.getCamera().combined);

        paddle.update(delta);
        ball.update(delta);
        brick.update(ball);

        brick.render(renderer);
        paddle.render(renderer);
        ball.render(renderer);

    }

    @Override
    public void resize(int width, int height) {
        //Whenever a resize occurs the viewport must be modified.
        outbreakViewport.update(width, height, true);

        paddle.init();
        ball.init();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private void init(){

        ball.setPosition(new Vector2(15,0));
        ball.setVelocity(new Vector2(-2, 3));
        brick.setX(0);
        brick.updateCollider();
    }
}
