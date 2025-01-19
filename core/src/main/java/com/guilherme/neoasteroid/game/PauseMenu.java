package com.guilherme.neoasteroid.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.guilherme.neoasteroid.Main;

public class PauseMenu {
  private Stage pauseMenu;
  private boolean isPaused;

  public PauseMenu(Main game, GameScreen gameScreen) {
    isPaused = false;

    pauseMenu = new Stage(new FitViewport(gameScreen.camera.viewportWidth, gameScreen.camera.viewportHeight));
    Table pauseTable = new Table();

    TextButton resumeTextButton = new TextButton("Resume", game.uiSkin);
    resumeTextButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        // TODO Dispose of the pause Menu and return to game
      }
    });

    TextButton exitTextButton = new TextButton("Exit Game", game.uiSkin);
    exitTextButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        // TODO Exit to main menu screen
      }
    });

    pauseTable.add(resumeTextButton).width(200).height(50).center();
    pauseTable.row();
    pauseTable.add(exitTextButton).width(200).height(50).center();

    pauseTable.setFillParent(true);
    pauseMenu.addActor(pauseTable);
  }

  public void setPaused(boolean paused) {
    isPaused = paused;
  }

  public boolean isPaused() {
    return isPaused;
  }

  public void render() {
    pauseMenu.act();
  }
}
