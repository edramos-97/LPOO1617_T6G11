package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.controller.entities.BallBody;
import com.mygdx.game.controller.entities.WallsBody;
import com.mygdx.game.model.SandBoxModel;
import com.mygdx.game.model.entities.EntityModel;
import com.mygdx.game.view.SandBoxView;

import java.util.Random;
import java.util.Vector;

import static com.mygdx.game.view.MenuView.PIXEL_TO_METER;
import static com.mygdx.game.view.MenuView.RATIO;
import static com.mygdx.game.view.MenuView.VIEWPORT_WIDTH;

/**
 * Created by Tiago Neves on 22/05/2017.
 */

public class SandBoxController {
    private float accumulator;

    private float seconds;

    private static SandBoxController instance;

    private World world;
    private Random forceRand = new Random();

    private final WallsBody wallsBody;

    private Vector<BallBody> rballBodys = new Vector<BallBody>();

    BallBody playerBody;

    private boolean Colliding;

    private Object userData;

    boolean joystick = true;




    SandBoxController() {

            world = new World(new Vector2(0, 0), false);

            playerBody = new BallBody(world,SandBoxModel.getInstance().getPlayerModel());
            playerBody.setDrag(0.2f);
            userData = playerBody.getUserData();

            for(int i=0; i < SandBoxModel.getInstance().getnBalls();i++){
                rballBodys.addElement(new BallBody(world, SandBoxModel.getInstance().getEnemyModel(i)));
                rballBodys.elementAt(i).setType(BodyDef.BodyType.DynamicBody);
                playerBody.setDrag(0.6f);
                System.out.print(rballBodys.elementAt(i).getUserData());
            }


            wallsBody = new WallsBody(world,SandBoxModel.getInstance().getWallsModel(), 0.5f);

            world.setContactListener(new ContactListener() {

                @Override
                public void beginContact(Contact contact) {
                    for (int i = 0; i < SandBoxModel.getInstance().getnBalls(); i++) {
                        if(contact.getFixtureA().getBody().getUserData() == userData && contact.getFixtureB().getBody().getUserData() != userData){
                            Colliding = true;
                            System.out.println("Contact detected");
                        }
                    }


                }

                @Override
                public void endContact(Contact contact) {

                }

                @Override
                public void preSolve(Contact contact, Manifold oldManifold) {

                }

                @Override
                public void postSolve(Contact contact, ContactImpulse impulse) {

                }

            });

}

    public static SandBoxController getInstance() {
        if (instance == null)
            instance = new SandBoxController();
        return instance;
    }

    public void update(float delta) {
        float frameTime = Math.min(delta, 0.25f);
        accumulator += frameTime;
        seconds += frameTime;
        while (accumulator >= 1/60f) {
            world.step(1/60f, 6, 2);
            accumulator -= 1/60f;

        }



        if(seconds >= 5f){
            SandBoxModel.getInstance().nextLevel();
            seconds = 0;
        }



        float accelX = Gdx.input.getAccelerometerX();
        float accelY = Gdx.input.getAccelerometerY();
        Vector2 vector = new Vector2(accelY /30, -accelX / 30);

        if(!joystick)
            playerBody.applyForceToCenter(vector.x,vector.y, true);



        for(int i=0; i < rballBodys.size();i++){
            Vector2 follow = new Vector2((playerBody.getX()-rballBodys.elementAt(i).getX())/50,(playerBody.getY()-rballBodys.elementAt(i).getY())/50);
            follow.limit(0.01f);
            rballBodys.elementAt(i).applyForceToCenter(follow.x,follow.y,true);
        }



        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);

        for (Body body : bodies) {
            ((EntityModel) body.getUserData()).setPosition(body.getPosition().x, body.getPosition().y);
        }


        playerBody.limitSpeed();

    }

    /**
     * Applies force to the player sandbox ball
     * @param x
     * @param y
     */
    public void accelerate(float x,float y){
        playerBody.applyForceToCenter(x,y, true);
    }



    public World getWorld() {
        return world;
    }

    public void nextLevel(int nBall,float radius){

        Random r = new Random();
        for(int i = 0; i < nBall-1;i++) {
            float x = (r.nextFloat()*(VIEWPORT_WIDTH-radius)) + radius;// + radius;
            float y =(r.nextFloat()*(VIEWPORT_WIDTH*RATIO - radius)) + radius;// + radius ;
            this.rballBodys.elementAt(i).setTransform(x,y,0);
            this.rballBodys.elementAt(i).setLinearVelocity(0);
        }
//
        rballBodys.addElement(new BallBody(world, SandBoxModel.getInstance().getEnemyModel(nBall-1)));
        rballBodys.elementAt(nBall-1).setType(BodyDef.BodyType.DynamicBody);
        playerBody.setDrag(0.6f);
    }

    public boolean isColliding() {
        return Colliding;
    }

    public void setColliding(boolean colliding) {
        Colliding = colliding;
    }

    public void delete(){
        this.instance = null;
        SandBoxModel.getInstance().delete();
    }

    public float getSeconds() {
        return seconds;
    }

}

