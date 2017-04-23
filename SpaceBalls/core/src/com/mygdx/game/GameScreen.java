package com.mygdx.game;

/**
 * Created by Tiago Neves on 21/04/2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * The only game screen.
 */
public class GameScreen extends ScreenAdapter {
    private final Stage gameStage;

    /**
     *
     * @param game the game
     */
    GameScreen(Ball game) {
        this.gameStage = new GameStage(game);

        // Sets the stage as its input processor
        Gdx.input.setInputProcessor(gameStage);
    }

    /**
     * Renders the screen
     *
     * @param delta time since last rendered in seconds
     */
    @Override
    public void render(float delta) {
        super.render(delta);

        Gdx.gl.glClearColor( 103/255f, 69/255f, 117/255f, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );


        // Steps the stage
        gameStage.act(delta);

        // Draws the stage
        gameStage.draw();
    }

    /**
     * Resize the stage viewport when the screen is resized
     *
     * @param width the new screen width
     * @param height the new screen height
     */
    @Override
    public void resize(int width, int height) {
        gameStage.getViewport().update(width, height, true);
    }
}