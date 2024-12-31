package com.guilherme.neoasteroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {
  private final Main game;
  private final Stage stage;

  public MainMenuScreen(Main game) {
    this.game = game;
    this.stage = new Stage(new ScreenViewport());
    Gdx.input.setInputProcessor(stage);

    TextButton startButton = new TextButton("Start Game", game.uiSkin);
    startButton.setSize(200, 50);
    startButton.setPosition(Gdx.graphics.getWidth() / 2f - startButton.getWidth() / 2,
        Gdx.graphics.getHeight() / 2f + startButton.getHeight() + 35);
    startButton.addListener(new ClickListener() {
      @Override
      public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        game.player.setHost(true);
        game.setScreen(new LobbyScreen(game));
      }
    });

    TextButton joinGameButton = new TextButton("Join Game", game.uiSkin);
    joinGameButton.setSize(200, 50);
    joinGameButton.setPosition(Gdx.graphics.getWidth() / 2f - joinGameButton.getWidth() / 2,
        Gdx.graphics.getHeight() / 2f + joinGameButton.getHeight() / 2);

    joinGameButton.addListener(new ClickListener() {
      @Override
      public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        game.setScreen(new JoinScreen(game));
      }
    });

    TextButton testConnectionButton = new TextButton("Test Connection", game.uiSkin);
    testConnectionButton.setSize(200, 50);
    testConnectionButton.setPosition(Gdx.graphics.getWidth() / 2f - testConnectionButton.getWidth() / 2,
        Gdx.graphics.getHeight() / 2f - 35);

    Label testConnectionResult = new Label("", game.uiSkin);
    testConnectionResult.setPosition(Gdx.graphics.getWidth() / 2f + testConnectionButton.getWidth() / 2 + 10,
        testConnectionButton.getY() + testConnectionButton.getHeight() / 2 - testConnectionResult.getHeight() / 2);

    testConnectionButton.addListener(new ClickListener() {
      @Override
      public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        boolean connectionSuccessful = game.testServerConnection(game.client, "127.0.0.1");
        if (connectionSuccessful) {
          testConnectionResult.setText("Connection successful!");
        } else {
          testConnectionResult.setText("Connection failed.");
        }
      }
    });

    TextButton exitGameButton = new TextButton("Exit Game", game.uiSkin);
    exitGameButton.setSize(200, 50);
    exitGameButton.setPosition(Gdx.graphics.getWidth() / 2f - exitGameButton.getWidth() / 2f,
        Gdx.graphics.getHeight() / 4f + exitGameButton.getHeight() / 2f);
    exitGameButton.addListener(new ClickListener() {
      @Override
      public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        Gdx.app.exit();
      }
    });

    stage.addActor(startButton);
    stage.addActor(joinGameButton);
    stage.addActor(testConnectionButton);
    stage.addActor(testConnectionResult);
    stage.addActor(exitGameButton);
  }

  @Override
  public void show() {
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    stage.act(delta);
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void pause() {
  }

  @Override
  public void resume() {
  }

  @Override
  public void hide() {
  }

  @Override
  public void dispose() {
    stage.dispose();
    game.uiSkin.dispose();
  }
}
