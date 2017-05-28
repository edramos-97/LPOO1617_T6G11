package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.SpaceBallsGame;
import com.mygdx.game.controller.LevelController;
import com.mygdx.game.controller.OptionsController;
import com.mygdx.game.controller.SandBoxController;
import com.mygdx.game.model.entities.EntityModel;
import com.mygdx.game.view.entities.EntityView;
import com.mygdx.game.view.entities.ViewFactory;

/**
 * Created by Tiago Neves on 28/05/2017.
 */

public abstract class GameView extends ScreenAdapter{

    /**
     * Used to debug the position of the physics fixtures
     */
    protected static final boolean DEBUG_PHYSICS = true;

    /**
     * How much meters does a pixel represent.
     */
    public static final float PIXEL_TO_METER = 0.20f / 200;

    /**
     * The width of the viewport in meters. The height is
     * automatically calculated using the screen ratio.
     */
    public static final float VIEWPORT_WIDTH = 4;

    /**
     * The screen ratio.
     */
    public static final float RATIO = ((float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth());

    /**
     * A renderer used to debug the physical fixtures.
     */
    protected Box2DDebugRenderer debugRenderer;

    /**
     * The transformation matrix used to transform meters into
     * pixels in order to show fixtures in their correct places.
     */
    protected Matrix4 debugCamera;

    /**
     * The camera used to show the viewport.
     */
    protected OrthographicCamera camera;

    protected final Stage stage;

    SpaceBallsGame game;
    float offsetY;
    float offsetX;

    float sensitivity;

    boolean joystick;

    public GameView(SpaceBallsGame game) {
        super();
        stage = new Stage();
        camera = createCamera();
        this.game = game;
    }

    public void render(float delta) {

        handleInputs(delta);

        camera.update();

        game.getBatch().setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        game.getBatch().begin();
        drawEntities();
        game.getBatch().end();

        stage.act();
        stage.draw();
    }

    abstract void drawEntities();

    abstract void handleInputs(float delta);

    protected void backToMenu(){
        LevelController.getInstance().delete();
        game.setScreen(new MenuView(game));
    }

    protected void drawView(EntityModel model){
        EntityView view = ViewFactory.makeView(game, model);
        view.update(model);
        view.draw(game.getBatch());
    }

    protected void debugPhysics(World world){
        if (DEBUG_PHYSICS) {
            debugCamera = camera.combined.cpy();
            debugCamera.scl(1 / PIXEL_TO_METER);
            debugRenderer.render(world, debugCamera);
        }
    }

    protected OrthographicCamera createCamera() {
        OrthographicCamera camera = new OrthographicCamera(VIEWPORT_WIDTH / PIXEL_TO_METER, VIEWPORT_WIDTH / PIXEL_TO_METER * RATIO);

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        if (DEBUG_PHYSICS) {
            debugRenderer = new Box2DDebugRenderer();
            debugCamera = camera.combined.cpy();
            debugCamera.scl(1 / PIXEL_TO_METER);
        }
        return camera;
    }



    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height,true);
    }

}
