package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

class NinjaActor extends Actor {
    private static final int w = 85;
    private static final int h = 100;
    private final int WALK = 0;
    private final int JUMP = 3;
    private final int RIGHT = 1;
    private final float G = 0.99f;
    private final int stepMax = 8;
    private int step;
    private int action;
    private boolean jumping = false;
    NinjaRunner game;
    private TextureRegion texture;
    private float vectorG = 0;
    private int frames = 0;

    NinjaActor(TextureRegion reg, int x, int y, NinjaRunner game) {
        super(x, y, game);
        this.game = game;
        texture = reg;
        sprite = new Sprite(new TextureRegion(texture,
                w * step, (action * h * 2) + (RIGHT * h), w, h));
        sprite.setPosition(x, y);
    }

    @Override
    void execute() {
        for (Actor m : game.enemies) {
            if (m != this && collide(m)) {
                game.end = true;
//                m.dead = false;
                explode();
            }
        }
    }

    void draw() {
        frames++;
        if (frames < 3 ) {
            sprite.draw(game.batch);
            return;
        }

        frames = 0;
        sprite.setRegion(texture, w * step, (action * h * 2) + (RIGHT * h), w, h);
        sprite.draw(game.batch);
    }

    @Override
    void run() {
        gravity();
        if (step >= stepMax - 1) step = 0;
        move(RIGHT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            jump();
        }
        if (jumping) step = 3;
        super.run();
    }

    private void move(int dir) {
        if (!jumping) {
            step++;
            action = WALK;
        }
    }

    private void jump() {
        if (jumping) return;
        game.sndJump.play();
        jumping = true;
        action = JUMP;
        sprite.translateY(10);
        vectorG = -15;

    }

    private void gravity() {
        boolean falling = false;
        int middle = w / 2;
        if (game.isFree(sprite.getX() + middle, sprite.getY() - 1)) {
            vectorG += G;
            sprite.translateY(-vectorG + 2);
            falling = true;
        }
        if (!falling && jumping) jumping = false;
    }
}