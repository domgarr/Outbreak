package com.garreffd.outbreak;


import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;



/**
 * Created by Domenic on 2017-10-25.
 */

public class TitleScene implements Screen {
    private Stage stage;
    private Skin skin;
    private OutbreakGame game;
    private Table table;

    public TitleScene(OutbreakGame game){
        this.game = game;
    }

    public void render(float delta){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void dispose(){
        stage.dispose();
    }

    public void resize(int width, int height){
        stage.getViewport().update(width, height, true);
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
    public void show() {
        skin = new Skin(Gdx.files.internal("skin/clean-crispy-ui.json"));
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);


        Viewport viewport = stage.getViewport();

        float buttonWidth = viewport.getScreenWidth() / 4;
        float buttonHeight = viewport.getScreenHeight() / 10;

        table = new Table();
        table.setWidth(stage.getWidth());
        table.align(Align.center | Align.top);
        table.setPosition(0, Gdx.graphics.getHeight());

        final TextButton playButton = new TextButton("Play", skin);
        //TODO: Look into FreeFontGenerator for rendering font during runtime.
        //https://gamedev.stackexchange.com/questions/24638/resolution-independence-in-libgdx
        playButton.getLabel().setFontScale(2f);
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.showOutbreakScreen();
            }
        });



        final TextButton quitButton = new TextButton("Quit", skin);
        quitButton.getLabel().setFontScale(2f);
        quitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        FileHandle fileArial72 = Gdx.files.internal("fonts/arial-black-72.fnt");
        BitmapFont fontArial72 = new BitmapFont(fileArial72);

        FileHandle fileArial21 = Gdx.files.internal("fonts/arial-black-21.fnt");
        BitmapFont fontArial21 = new BitmapFont(fileArial21);
        skin.add("arial-21", fontArial21, BitmapFont.class);

        Label.LabelStyle label = new Label.LabelStyle(fontArial72, new Color(1f,1f,1f,1f));

        final Label titleOfGame = new Label("Outbreak RPG", skin);
        titleOfGame.setFontScale(1f);
        titleOfGame.setStyle(label);

        table.padTop(30);
        table.add(titleOfGame).padBottom(30);
        table.row();
        table.padTop(30);
        table.add(playButton).padBottom(30).width(buttonWidth).height(buttonHeight);
        table.row();
        table.add(quitButton).width(buttonWidth).height(buttonHeight);



        stage.addActor(table);


    }


}
