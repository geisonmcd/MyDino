package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

/**class Bomb extends NinjaActor {
    Bomb(float x, float y, Texture texture, NinjaRunner game) {
        super(x, y, texture, game);
        game.sndBomb.play();
    }

    @Override
    void execute() {
        sprite.setY(sprite.getY() + 5);
        dead = sprite.getY() > game.h;
        for (NinjaActor m : game.actors) {
            if (m != this && collide(m)) {
                dead = true;
                m.explode();
            }
        }
    }
}**/