package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NinjaRunner extends ApplicationAdapter {
    int w, h;
    boolean end = false;
    Sound sndJump;
    Sound sndExplosion;
    SpriteBatch batch;
    private Music music;

    private Texture enemy;
    private Texture gameover;
    private Texture gamepaused;
    private TextureRegion ninja;
    private NinjaActor ninjaActor;
    private Background background;

    List<Actor> enemies = new ArrayList<Actor>();
    private List<Actor> news = new ArrayList<Actor>();

    private int enemyCounter = 0;
    private int enemyMaxCounter = 50;
    private boolean paused = false;
    private boolean playMusic = true;
    private Random rand = new Random();

    @Override
    public void create() {
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        enemy = new Texture("enemy.png");
        gameover = new Texture("gameover.png");
        gamepaused = new Texture("paused.png");
        ninja = new TextureRegion(new Texture("midori.png"), 0, 24, 680, 1000);

        ninjaActor = new NinjaActor(ninja, 200, 15, this);
        background = new Background(new Texture("background.png"));

        sndJump = Gdx.audio.newSound(Gdx.files.internal("jump.wav"));
        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.ogg"));
        music.setLooping(true);
        music.play();
    }

    @Override
    public void render() {
        if (end) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isTouched()) {
                end = false;
                ninjaActor = new NinjaActor(ninja, 200, 15, this);
            }
        } else {
            if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                paused = !paused;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
                playMusic = !playMusic;
            }
        }
        if (playMusic) {
            if (paused || end) {
                if (music.isPlaying()) music.stop();
            } else {
                if (!music.isPlaying()) music.play();
            }
        } else {
            if (music.isPlaying()) music.stop();
        }
        if (!paused) {
            ninjaActor.run();
            background.run();
            for (Actor m : enemies) m.run();

            enemies();
            refresh();
        }

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        background.draw(batch);
        ninjaActor.draw();
        for (Actor m : enemies) m.draw();

        if (end) {
            enemies.clear();
            batch.draw(gameover, (w / 2) - (gameover.getWidth() / 2), h / 2);
        }
        if (paused) {
            batch.draw(gamepaused, (w / 2) - (gamepaused.getWidth() / 2), h / 2);
        }

        batch.end();
    }

    boolean isFree(float px, float py) {
        if (px < 0 || px > w || py < 0 || py > h) return false;
        return true;
    }

    private void enemies() {
        enemyCounter++;
        if (enemyCounter > enemyMaxCounter) {
            enemyMaxCounter = rand.nextInt(200) + 50;
            enemyCounter = 0;
            news.add(new Enemy(w, 0, enemy, this));
        }
    }

    private void refresh() {
        List<Actor> aux = enemies;
        enemies = new ArrayList<Actor>();
        for (Actor m : aux) if (!m.dead) enemies.add(m);
        enemies.addAll(news);
        news.clear();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        sndJump.dispose();
    }
}