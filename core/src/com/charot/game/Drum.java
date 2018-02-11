package com.charot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;
import java.util.Random;


/**
 * Created by Sasakin M on 11.02.2018.
 */

public class Drum {

    private Texture top;
    private Texture botom;

    private Texture apple;
    private Texture banana;
    private Texture arbus;
    private Texture qiwi;
    private Texture ezhevika;
    private Texture klubnika;
    private Texture vishnya;

    private ArrayMap<Rectangle, Pair<Integer, Texture>> drumCells;
    private Array<Pair<Integer, Texture>> imgArray;

    private int positionX;

    private long roundTime;

    private long startTime;

    private boolean isRunnung = true;   // если не запущен барабан или если в процесси вращения - то значение true
                                        // если остановился после вращения - значение false

    public Drum(int positionX) {
        isRunnung = true;
        this.positionX = positionX + 50;
        top = new Texture("top.jpg");
        botom = new Texture("botom.jpg");

        apple = new Texture("apple.jpeg");
        banana = new Texture("banana.jpg");
        arbus = new Texture("arbus.jpg");
        qiwi = new Texture("qiwi.jpg");
        ezhevika = new Texture("ezhevika.jpg");
        klubnika = new Texture("klubnika.jpg");
        vishnya = new Texture("vishnya.jpg");


        drumCells = new ArrayMap<Rectangle, Pair<Integer, Texture>>();
        imgArray = new Array<Pair<Integer, Texture>>();

        imgArray.add(new Pair<Integer, Texture>(0,apple));
        imgArray.add(new Pair<Integer, Texture>(1,banana));
        imgArray.add(new Pair<Integer, Texture>(2,arbus));
        imgArray.add(new Pair<Integer, Texture>(3,qiwi));
        imgArray.add(new Pair<Integer, Texture>(4,ezhevika));
        imgArray.add(new Pair<Integer, Texture>(5,klubnika));
        imgArray.add(new Pair<Integer, Texture>(6,vishnya));
        startTime = TimeUtils.nanoTime();
    }

    private Pair<Integer, Texture> getRundomImg() {
        int item = new Random().nextInt(imgArray.size);
        return imgArray.get(item);
    }

    private void spawnRaindrop(){
        Rectangle raindrop = new Rectangle();
        raindrop.x = positionX;
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        drumCells.put(raindrop, getRundomImg());
        roundTime = TimeUtils.nanoTime();
    }

    void render (SpriteBatch batch) {
        batch.begin();
        for (ObjectMap.Entry<Rectangle, Pair<Integer, Texture>> entry: drumCells.entries()){
            batch.draw(entry.value.second(), entry.key.x, entry.key.y);
            batch.draw(top, 0, 450);
            batch.draw(botom, 0, 0);
        }
        batch.end();

        if(TimeUtils.nanoTime() - startTime<4200000000L ) {
            if (TimeUtils.nanoTime() - roundTime > 180000000) spawnRaindrop();

            Iterator<ObjectMap.Entry<Rectangle, Pair<Integer, Texture>>> iter = drumCells.iterator();
            while (iter.hasNext()) {
                ObjectMap.Entry<Rectangle, Pair<Integer, Texture>> object = iter.next();
                object.key.y -= 700 * Gdx.graphics.getDeltaTime();
                if (object.key.y + 64 < 0) iter.remove();
            }
        } else {

            Iterator<ObjectMap.Entry<Rectangle, Pair<Integer, Texture>>> iter = drumCells.iterator();
            while (iter.hasNext()) {
                ObjectMap.Entry<Rectangle, Pair<Integer, Texture>> object = iter.next();
                if (object.key.y >= 330) {
                    object.key.y = 330;
                } else if(object.key.y >= 210) {
                    object.key.y = 210;
                } else if(object.key.y >=90){
                    object.key.y = 90;
                } else {
                    iter.remove();
                }
            }
            isRunnung = false;
        }
    }

    public ArrayMap<Rectangle, Pair<Integer, Texture>> getDrumCells() {
        return drumCells;
    }

    public boolean isRunnung() {
        return isRunnung;
    }

    public void setRunnung(boolean runnung) {
        isRunnung = runnung;
    }
}
