package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class Background {
	private Texture texture;
	private int ay, by;

	Background(Texture texture) {
		this.texture = texture;
		ay = 0;
		by = texture.getWidth();
	}

	void run() {
		int speed = 2;
		ay -= speed;
		by -= speed;
		if (ay <= -texture.getWidth()) {
			ay = by + texture.getWidth();
		}
		if (by <= -texture.getWidth()) {
			by = ay + texture.getWidth();
		}
	}

	void draw(SpriteBatch batch) {
		batch.draw(texture, ay, 0);
		batch.draw(texture, by, 0);
	}

	void dispose() {
		texture.dispose();
	}
}