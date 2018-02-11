package com.charot.game;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by Sasakin M on 11.02.2018.
 */

/**
 * Данный клас необходим для запуска барабанов.
 */
public class DrumTask extends Timer.Task {

    private Array<Drum> drums;

    public DrumTask(Array<Drum> drums) {
        this.drums = drums;
    }

    @Override
    public void run() {
                drums.add(new Drum(30*(drums.size+1)+(drums.size)*100));
    }
}
