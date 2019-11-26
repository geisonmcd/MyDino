package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

class Actor {
    NinjaRunner game;
    Sprite sprite;
    boolean dead = false;
    private boolean dying = false;
    private int explosion = 4;
    private int explosionCount = 0;

    Actor(float x, float y, NinjaRunner game) {
        this.game = game;
    }

    void execute() {
    }

    void run() {
        if (dead) return;
        if (exploding()) return;
        execute();
        if (sprite.getX() + sprite.getWidth() > game.w) {
            sprite.setX(game.w - sprite.getWidth());
        }
    }

    void draw() {
        if (dead) return;
        sprite.draw(game.batch);
    }

    private boolean exploding() {
        if (!dying) return false;
        explosionCount++;
        if (explosionCount > 10) {
            explosionCount = 0;
            explosion--;
            if (explosion <= 0) dead = true;
        }
        return true;
    }

    void explode() {
        game.sndExplosion.play();
        dying = true;
    }

    boolean collide(Actor b) {
        return spriteCollide(this.sprite, b.sprite)
                || spriteCollide(b.sprite, this.sprite);
    }

    private boolean spriteCollide(Sprite a, Sprite b) {
        float x = a.getX();
        float y = a.getY();
        float w = a.getWidth();
        float h = a.getHeight();
        return pointCollide(x, y, b) ||
                pointCollide(x + w, y, b) ||
                pointCollide(x, y + h, b) ||
                pointCollide(x + w, y + h, b);
    }

    private boolean pointCollide(float px, float py, Sprite s) {
        float x = s.getX();
        float y = s.getY();
        float w = s.getWidth();
        float h = s.getHeight();
        return (px > x && px < x + w && py > y && py < y + h);
    }
}