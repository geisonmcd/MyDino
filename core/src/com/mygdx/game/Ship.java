package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

/**class Ship extends NinjaActor {
    Ship(float x, float y, Texture texture, NinjaRunner game) {
        super(x, y, texture, game);
    }

    @Override
    void execute() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            sprite.setX(sprite.getX() - 10);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            sprite.setX(sprite.getX() + 10);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.justTouched()) {
            game.shoot(
                    sprite.getX() + (sprite.getWidth() / 2),
                    sprite.getY() + sprite.getHeight() + 1);
        }
        if (game.accel) {
            sprite.setX(sprite.getX() + Gdx.input.getAccelerometerY() * 5);
        }
        for (NinjaActor m : game.actors) {
            if (m != this && collide(m)) {
                game.end = true;
                m.dead = true;
                explode();
            }
        }
    }
}**/