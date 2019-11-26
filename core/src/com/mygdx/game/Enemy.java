package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

class Enemy extends Actor {
    private static final int SPEED = 8;

    Enemy(int x, int y, Texture texture, NinjaRunner game) {
        super(x, y, game);
        sprite = new Sprite(texture);
        sprite.setPosition(x, y);
    }

    @Override
    void execute() {
        sprite.setX(sprite.getX() - SPEED);
        dead = sprite.getX() + sprite.getWidth() < 0;
    }
}