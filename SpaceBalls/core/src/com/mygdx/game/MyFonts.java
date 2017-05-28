package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by Tiago Neves on 28/05/2017.
 */

public class MyFonts {
    private static final MyFonts ourInstance = new MyFonts();

    private  BitmapFont font;

    public static MyFonts getInstance() {
        return ourInstance;
    }

    private MyFonts() {
        this.font = loadFonts();
    }

    private BitmapFont loadFonts(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/DAGGERSQUARE.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 200;
        parameter.color = Color.RED;
        parameter.borderWidth = 0;

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    public BitmapFont getFont() {
        return font;
    }
}
