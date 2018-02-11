package com.charot.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class PlayButton extends Button {
    public static final float WIDTH = 160;
    public static final float HEIGHT = 60;

    public static final Drawable drawableDown = new Image(new Texture("start-1.png")).getDrawable();
    public static final Drawable drawableUp = new Image(new Texture("start-2.png")).getDrawable();

    public PlayButton(final MainApp screen) {
        super(drawableUp, drawableDown);
        setSize(WIDTH, HEIGHT);
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.start();
            }
        });
    }
}
