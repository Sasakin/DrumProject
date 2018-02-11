package com.charot.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

public class MainApp extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Stage stage;
	private PlayButton button;
	private SelectLinesButton selectLinesButton;

	private Array<Drum> drums;

	private long timeFps1 = 0;
	private long timeFps2 = 0;

	private int fpsCount = 0;
	private int FPS_GENERAL = 0;

	private BitmapFont fontSmall;
	
	@Override
	public void create () {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,480);

		drums = new Array<Drum>();

		batch = new SpriteBatch();
		button = new PlayButton(this);

		button.setPosition(410,0);

		selectLinesButton = new SelectLinesButton(drums);

		selectLinesButton.setPosition( 400 - selectLinesButton.getWidth()- 10,0);
		stage.addActor(button);

		stage.addActor(selectLinesButton);

		fontSmall = new BitmapFont();
		fontSmall .setColor(1f, 0f, 0f, 1f);
	}

	public void start() {
		selectLinesButton.setChecked(false);
		drums.clear();
		new Timer().scheduleTask(new DrumTask(drums),0);
		new Timer().scheduleTask(new DrumTask(drums),0.2f);
		new Timer().scheduleTask(new DrumTask(drums),0.4f);
		new Timer().scheduleTask(new DrumTask(drums),0.6f);
		new Timer().scheduleTask(new DrumTask(drums),0.8f);
		timeFps1 = System.currentTimeMillis(); // замеряем время сначала и приравниваем к time1
	}

	@Override
	public void render () {
		timeFps2 = System.currentTimeMillis(); // замеряем время и приравниваем к time2
		fpsCount++;
		if (timeFps2 - timeFps1 >= 1000) { // если разница между time2 и time1 >= 1000, то значит прошла 1 секунда
			FPS_GENERAL = fpsCount;
			fpsCount = 0;
			timeFps1 = System.currentTimeMillis();
		}
		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		batch.setProjectionMatrix(camera.combined);

		for(Drum drum : drums) {
			if (drum != null) {
				drum.render(batch);
			}
		}

		if(selectLinesButton.isChecked()) {
			selectLinesButton.render();
		}

		stage.draw();

		batch.begin();
		fontSmall.draw(batch,"fps: " + String.valueOf(FPS_GENERAL),30,30);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
