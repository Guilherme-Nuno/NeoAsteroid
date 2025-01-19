package com.guilherme.neoasteroid.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.guilherme.neoasteroid.Main;
import com.guilherme.neoasteroid.network.Message;
import com.guilherme.neoasteroid.network.SpaceShipDTO;
import com.guilherme.neoasteroid.spaceship.SpaceShip;

public class InputHandlerClient implements InputProcessor {
  private final Main game;
  private final SpaceShip playerShip;
  private final Message<SpaceShipDTO> spaceShipDTOMessage = new Message<>();

  public InputHandlerClient(Main game, SpaceShip playerShip) {
    this.game = game;
    this.playerShip = playerShip;
  }

  @Override
  public boolean keyDown(int keycode) {
    switch (keycode) {
      case Input.Keys.UP:
        spaceShipDTOMessage.setMessage("inputStartUp", new SpaceShipDTO(playerShip));
        game.client.sendTCP(spaceShipDTOMessage);
        return true;
      case Input.Keys.LEFT:
        spaceShipDTOMessage.setMessage("inputStartLeft", new SpaceShipDTO(playerShip));
        game.client.sendTCP(spaceShipDTOMessage);
        return true;
      case Input.Keys.RIGHT:
        spaceShipDTOMessage.setMessage("inputStartRight", new SpaceShipDTO(playerShip));
        game.client.sendTCP(spaceShipDTOMessage);
        return  true;
    }
    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
    switch (keycode) {
      case Input.Keys.UP:
        spaceShipDTOMessage.setMessage("inputEndUp", new SpaceShipDTO(playerShip));
        game.client.sendTCP(spaceShipDTOMessage);
        return true;
      case Input.Keys.LEFT:
        spaceShipDTOMessage.setMessage("inputEndLeft", new SpaceShipDTO(playerShip));
        game.client.sendTCP(spaceShipDTOMessage);
        return true;
      case Input.Keys.RIGHT:
        spaceShipDTOMessage.setMessage("inputEndRight", new SpaceShipDTO(playerShip));
        game.client.sendTCP(spaceShipDTOMessage);
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
      spaceShipDTOMessage.setMessage("inputStartMouseLeft", new SpaceShipDTO(playerShip));
      game.client.sendTCP(spaceShipDTOMessage);
      return true;
    }
    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    if (button == Input.Buttons.LEFT) {
      spaceShipDTOMessage.setMessage("inputEndMouseLeft", new SpaceShipDTO(playerShip));
      game.client.sendTCP(spaceShipDTOMessage);
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
