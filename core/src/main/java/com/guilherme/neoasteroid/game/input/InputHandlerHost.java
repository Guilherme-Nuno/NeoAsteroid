package com.guilherme.neoasteroid.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.guilherme.neoasteroid.game.GameScreen;
import com.guilherme.neoasteroid.Main;
import com.guilherme.neoasteroid.network.Message;
import com.guilherme.neoasteroid.network.SpaceShipDTO;
import com.guilherme.neoasteroid.spaceship.SpaceShip;

public class InputHandlerHost implements InputProcessor {
  private final Main game;
  private final GameScreen gameScreen;
  private final SpaceShip playerShip;
  private Message<SpaceShipDTO> spaceShipDTOMessage = new Message<>();

  public InputHandlerHost(Main game, GameScreen gameScreen, SpaceShip playerShip) {
    this.game = game;
    this.playerShip = playerShip;
    this.gameScreen = gameScreen;
  }

  @Override
  public boolean keyDown(int keycode) {
    switch (keycode) {
      case Input.Keys.UP:
        playerShip.setAccelerating(true);
        return true;
      case Input.Keys.LEFT:
        playerShip.setTurningLeft(true);
        return true;
      case Input.Keys.RIGHT:
        playerShip.setTurningRight(true);
        return  true;
      case Input.Buttons.LEFT:

        return  true;
    }
    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
    switch (keycode) {
      case Input.Keys.UP:
        playerShip.setAccelerating(false);
        return true;
      case Input.Keys.LEFT:
        playerShip.setTurningLeft(false);
        return true;
      case Input.Keys.RIGHT:
        playerShip.setTurningRight(false);
        return  true;
    }
    return false;
  }

  @Override
  public boolean keyTyped(char character) {
    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    if (button == Input.Buttons.LEFT) {
      playerShip.setFiring(true);
      return true;
    }
    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    if (button == Input.Buttons.LEFT) {
      playerShip.setFiring(false);
      return true;
    }
    return false;
  }

  @Override
  public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    return false;
  }

  @Override
  public boolean scrolled(float amountX, float amountY) {
    return false;
  }
}
