package com.guilherme.neoasteroid;

import com.badlogic.gdx.math.Vector2;

public class Player {
  private String name;
  private boolean host;
  private boolean loadingComplete;
  private boolean inLobby;
  private boolean inGameScreen;
  private boolean spaceShipsLoaded;
  private boolean asteroidsLoaded;
  private Vector2 mousePosition;

  public Player() {
    float randomNumber = (float) Math.random() * 10000000;

    name = "Player" + (int) randomNumber;
    host = false;
    loadingComplete = false;
    inLobby = false;
    inGameScreen = false;
    spaceShipsLoaded = false;
    asteroidsLoaded = false;
    mousePosition = new Vector2(0,0);
  }

  public String getName() {
    return name;
  }

  public boolean isHost() {
    return host;
  }

  public void setInLobby(boolean value) {
    inLobby = value;
  }

  public boolean isInLobby() {
    return inLobby;
  }

  public void setInGameScreen(boolean value) {
    inGameScreen = value;
  }

  public boolean isInGameScreen() {
    return inGameScreen;
  }

  public void setAsteroidsLoaded(boolean value) {
    asteroidsLoaded = value;
  }

  public boolean areAsteroidsLoaded() {
    return asteroidsLoaded;
  }

  public void setSpaceShipsLoaded(boolean value) {
    spaceShipsLoaded = value;
  }

  public boolean areSpaceShipsLoaded() {
    return spaceShipsLoaded;
  }

  public boolean isLoadingComplete() {
    return loadingComplete;
  }

  public void setName(String newName) {
    name = newName;
  }

  public void setHost(boolean value) {
    host = value;
  }

  public void setLoadingComplete(boolean value) {
    loadingComplete = value;
  }

  public void setMousePosition(Vector2 mousePosition) {
    this.mousePosition = mousePosition;
  }

  public Vector2 getMousePosition() {
    return mousePosition;
  }
}
