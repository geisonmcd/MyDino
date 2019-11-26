package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private Texture enemy2;
    private Texture gameover;
    private Texture gamepaused;
    private TextureRegion ninja;
    private NinjaActor ninjaActor;
    private Background background;

    List<Actor> enemies = new ArrayList<Actor>();
    private List<Actor> news = new ArrayList<Actor>();

    private int enemyCounter = 0;
    private int enemiesDead = 0;
    private int enemyMaxCounter = 50;
    private boolean paused = false;
    private boolean playMusic = true;
    private Random rand = new Random();
    private boolean initialScreen = true;

    private BitmapFont fnt;
    private BitmapFont defaultFnt;
    private Color colorRed;
    private Color colorWhite;
    private Color colorBlue;

    @Override
    public void create() {
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        enemy = new Texture("box.png");
        enemy2 = new Texture("enemy.png");
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

        this.fnt = new BitmapFont(Gdx.files.internal("fonts/myFont.fnt"));
        this.defaultFnt = new BitmapFont();
        this.colorRed = new Color(1, 0, 0, 1);
        this.colorWhite = new Color(1, 1, 1, 1);
        this.colorBlue = new Color(0, 0, 1, 1);

    }

    @Override
    public void render() {
        if (initialScreen) {
            makeInitialScreen();
            return;
        }
        if (end) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isTouched()) {
                end = false;
                enemiesDead = 0;
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
        fnt.draw(batch, Integer.toString(enemiesDead), 40, 530);
        batch.end();
    }

    private void makeInitialScreen() {
        Gdx.gl.glClearColor(0, 0, 0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        fnt.setColor(colorBlue);
        fnt.draw(batch, "Ninja,", 100, 350);
        fnt.setColor(colorWhite);
        fnt.draw(batch, "Runner,", 200, 260);
        fnt.setColor(colorRed);
        defaultFnt.draw(batch, "Toque na tela ou pressione a tecla enter!", 400, 130);
        batch.end();

        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            initialScreen = false;
        }
    }

    private void makeGameOverScreen() {
        Gdx.gl.glClearColor(0, 0, 0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        fnt.setColor(colorBlue);
        fnt.draw(batch, "Ninja,", 100, 350);
        fnt.setColor(colorWhite);
        fnt.draw(batch, "Runner,", 200, 260);
        fnt.setColor(colorRed);
        defaultFnt.draw(batch, "Toque na tela ou pressione a tecla enter!", 400, 130);
        batch.end();

        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            initialScreen = false;
        }
    }

    boolean isFree(float px, float py) {
        if (px < 0 || px > w || py < 0 || py > h) return false;
        return true;
    }

    private void enemies() {
        Texture random = (rand.nextInt(2) == 2) ? enemy2 : enemy;
        enemyCounter++;
        if (enemyCounter > enemyMaxCounter) {
            enemyMaxCounter = rand.nextInt(100) + 30;
            enemyCounter = 0;
            news.add(new Enemy(w, 0, random, this));
        }
    }

    private void refresh()
    {
        List<Actor> aux = enemies;
        enemies = new ArrayList<Actor>();
        for (Actor m : aux) {
            if (!m.dead)
            {
                enemies.add(m);
            }else {
                enemiesDead++;
            }
        }
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