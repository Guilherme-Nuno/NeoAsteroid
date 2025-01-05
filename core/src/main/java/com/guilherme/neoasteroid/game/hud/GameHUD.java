package com.guilherme.neoasteroid.game.hud;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.guilherme.neoasteroid.game.GameScreen;

public class GameHUD {
  private final GameScreen gameScreen;
  private final Skin skin;
  private final Stage hudStage;
  private ProgressBar hullBar;
  private ProgressBar energyBar;
  private ProgressBar shieldBar;
  private ProgressBar earthHealthBar;

  public GameHUD (GameScreen gameScreen, Skin gameSkin, Viewport viewport) {
    hudStage = new Stage(new FitViewport(gameScreen.camera.viewportWidth, gameScreen.camera.viewportHeight));
    skin = gameSkin;
    this.gameScreen = gameScreen;

    hudStage.setDebugAll(true);

    setAllBars();
    setBarTable();
  }

  private void setAllBars() {
    setHullBar();
    setEnergyBar();
    setShieldBar();
  }

  private void setBarTable() {
    Table barTable = new Table();
    barTable.setFillParent(true);
    barTable.left();
    barTable.top();
    barTable.add(hullBar).pad(8).width(90).height(8);
    barTable.row();
    barTable.add(shieldBar).pad(8).width(90).height(8);
    barTable.row();
    barTable.add(energyBar).pad(8).width(90).height(8);

    hudStage.addActor(barTable);
  }

  private void setHullBar() {
    hullBar = new ProgressBar(0, gameScreen.playerShip.getHull(), 1,false, skin);
    hullBar.setValue(gameScreen.playerShip.getHull());
    hullBar.setSize(40, 8);
  }

  private void setEnergyBar() {
    energyBar = new ProgressBar(0, gameScreen.playerShip.getHull(), 1,false, skin);
    energyBar.setValue(gameScreen.playerShip.getEnergy());
    energyBar.setSize(40, 8);
  }

  private void setShieldBar() {
    shieldBar = new ProgressBar(0, gameScreen.playerShip.getHull(), 1,false, skin);
    shieldBar.setValue(gameScreen.playerShip.getShield());
    shieldBar.setSize(40, 8);
  }

  public void updateBars() {
    hullBar.setValue(gameScreen.playerShip.getHull());
    energyBar.setValue(gameScreen.playerShip.getEnergy());
    shieldBar.setValue(gameScreen.playerShip.getShield());
  }

  public void render(float delta) {
    hudStage.act(delta);
    hudStage.draw();
  }
}
