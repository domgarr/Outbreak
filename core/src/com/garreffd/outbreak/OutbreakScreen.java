package com.garreffd.outbreak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    ShapeRenderer renderer; // Will draw objects onto the screen.

    Player player;
    Ball ball;

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

        /*
        ShapeRenderer renders/draws points,lines,shapes - outlined or filled.

         */
        renderer = new ShapeRenderer();
        /*
        This is set to false by default, although when set to true whenever a shape can not be drawn
        within the batch - used to draw 2D rectangles that references a region - given a ShapeType, it
        automatically attempt to redraw using a different ShapeType.
         */
        renderer.setAutoShapeType(true);

        /*
        Initialize new player passing in the viewport. Passing in the viewport allows for positioning
        of the player in the middle of the screen, since viewport contains the viewportWidth.
         */
        player = new Player(outbreakViewport);
        ball = new Ball(outbreakViewport, player);

        /*
        InputProcessor recieves input event from the keyboard and touch screen.
        Instead of polling - such as using an if-statement to check every update - InputProcessor can handle
        input events.
         */
        Gdx.input.setInputProcessor(this);

    }
    /*
    Called by game loop everytime rendering needs to be done. Game logic should be performed here.
     */
    @Override
    public void render(float delta) {
        player.update(delta);
        ball.update(delta);

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

        //Combines View and Projection matrices into one.
        renderer.setProjectionMatrix(outbreakViewport.getCamera().combined);
        //Draws player on screen.
        player.render(renderer);
        ball.render(renderer);

    }

    /*
    This is called after create() and every time the screen size changes.
     */
    @Override
    public void resize(int width, int height) {
        //Whenever a resize occurs the viewport must be modified.
        outbreakViewport.update(width, height, true);

        player.init();
        ball.init();
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
