package com.garreffd.outbreak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static com.garreffd.outbreak.Constants.*;
/*Note the static import prevents me from having to explicitly type Constants every time I want to
 access a constant.
 The '*' is a wildcard meaning 0 or anything.
*/

/**
 * Created by Domenic on 2017-10-20.
 */

public class OutbreakScreen extends InputAdapter implements Screen {

    ExtendViewport outbreakViewport; //A viewport is the solution the different sized screens.
    ShapeRenderer renderer; // Will draw objects onto the screen.

    OutbreakGame game;
    Paddle paddle;
    Ball ball;
    Bricks bricks;

    Stage stage;
    Table table;
    Skin skin;

    Label score;
    Label money;

    boolean paused = false;

    public OutbreakScreen(OutbreakGame game ){
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
        Initialize new paddle passing in the viewport. Passing in the viewport allows for positioning
        of the paddle in the middle of the screen, since viewport contains the viewportWidth.
         */
        paddle = new Paddle(this, outbreakViewport);
        ball = new Ball(game, outbreakViewport, paddle);
        bricks = new Bricks(paddle, outbreakViewport, BRICK_ROWS, BRICK_COLS);

        /*
        InputProcessor recieves input event from the keyboard and touch screen.
        Instead of polling - such as using an if-statement to check every update - InputProcessor can handle
        input events.
         */
        Gdx.input.setInputProcessor(this);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("skin/clean-crispy-ui.json"));

        table = new Table();
        table.setWidth(stage.getWidth());
        table.setHeight(1f);
        table.align(Align.center | Align.top);
        table.setPosition(0, Gdx.graphics.getHeight());

        final TextButton pauseButton = new TextButton("||", skin);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                paused = !paused;
            }
        });

        FileHandle fileArial21 = Gdx.files.internal("fonts/arial-black-21.fnt");
        BitmapFont fontArial21 = new BitmapFont(fileArial21);
        skin.add("arial-21", fontArial21, BitmapFont.class);

        Label.LabelStyle label = new Label.LabelStyle(fontArial21, new Color(1f,1f,1f,1f));

        score = new Label("Score: ", skin);
        score.setStyle(label);

        money = new Label("Money: ", skin);
        money.setStyle(label);

        table.add(money).padRight(50f);
        table.add(pauseButton).width(35f).height(Gdx.graphics.getHeight() / 20 );
        table.add(score).padLeft(50f);

        updateGui(0,0);

        stage.addActor(table);

        outbreakViewport.apply(true);
    }
    /*
    Called by game loop everytime rendering needs to be done. Game logic should be performed here.
     */
    @Override
    public void render(float delta) {
        if(paused){
            Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
            //Note that this is a 2D game, clearing the DEPTH_BUFFER_BIT isn't necessary.
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            stage.draw();

        }else {
            paddle.update(delta);
            ball.update(delta);
            bricks.update(ball);

        /*
        Calling apply with no parameters will apply the view port without centering the camera.
        In this case we set the parameter to true, centering the camera in the world. Futhermore,
        this call applies the viewport to the camera and sets the glViewport.
        glViewport is in screen pixel units. Its responsible for telling which part of the window
        will be used for rendering.
         */

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
            //Draws paddle on screen.
            paddle.render(renderer);
            ball.render(renderer);
            bricks.render(renderer);

            stage.draw();
        }
    }

    /*
    This is called after create() and every time the screen size changes.
     */
    @Override
    public void resize(int width, int height) {
        //Whenever a resize occurs the viewport must be modified.
        outbreakViewport.update(width, height, true);
        stage.getViewport().update(width, height, true);

        paddle.init();
        ball.init();
        bricks.init();
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
        stage.dispose();
    }

    public void updateGui(int points, int money){
            this.score.setText("Score: " + points);
            this.money.setText("Money: " +  money);
            }

}
