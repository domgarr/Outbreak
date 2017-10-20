package com.garreffd.outbreak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import static com.garreffd.outbreak.Constants.*;
/*Note the static import prevents me from having to explicitly type Constants every time I want to
 access a constant.
 The '*' is a wildcard meaning 0 or anything.
*/

/**
 * Created by Domenic on 2017-10-20.
 */

public class OutbreakScreen extends InputAdapter implements Screen {

    OutbreakGame game; //Will be used later to switch between screen.
    ExtendViewport outbreakViewport; //A viewport is the solution the different sized screens.

    public OutbreakScreen(OutbreakGame game){
        this.game = game;
    }

    //Called when Screen gains focus.
    @Override
    public void show() {
        /*
        This following takes two parameters, minWorldWidth and minWorldHeight. This creates
        a new viewport using a new Orthographic camera with no maximum world size. Orthographic
        means that the rays emitting from this camera are parallel and the rays are orthogonal or
        perpendicular the image plane.
         */
        outbreakViewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
    }
    /*
    Called by game loop everytime rendering needs to be done. Game logic should be performed here.
     */
    @Override
    public void render(float delta) {
        /*
        Calling apply with no parameters will apply the view port without centering the camera.
        In this case we set the parameter to true, centering the camera in the world. Futhermore,
        this call applies the viewport to the camera and sets the glViewport.
        glViewport is in screen pixel units. Its responsible for telling which part of the window
        will be used for rendering.
         */
        outbreakViewport.apply(true);

        /*
        Clear the screen.
        The steps are set the desired clear color followed by clearing the buffers.
         The reasoning for clearing the screen is to tell openGL (Used to communicate with GPU)
         that the previous frame's image does not need to be reused and by staring with a clean slate
         we prevent any oddities.
         */

        //Sets background to black.
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
        //Note that this is a 2D game, clearing the DEPTH_BUFFER_BIT isn't necessary.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    /*
    This is called after create() and every time the screen size changes.
     */
    @Override
    public void resize(int width, int height) {
        //Whenever a resize occurs the viewport must be modified.
        outbreakViewport.update(width, height, true);
    }
    /*
    On android, called when Home is pressed or interruption occurs such as a phoen call.
    Good place to save game state.
     */
    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    //Called when Screen loses focus.
    @Override
    public void hide() {

    }
    //Called when application is destroyed. Note that pause() is called before dispose()
    @Override
    public void dispose() {

    }
}
