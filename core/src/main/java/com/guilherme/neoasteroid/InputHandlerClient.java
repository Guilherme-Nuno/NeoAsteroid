package com.guilherme.neoasteroid;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class InputHandlerClient implements InputProcessor {
  private final Main game;
  private final SpaceShip playerShip;
  private Message<SpaceShipDTO> spaceShipDTOMessage = new Message<>();

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
      case Input.Buttons.LEFT:
        spaceShipDTOMessage.setMessage("inputStartMouseLeft", new SpaceShipDTO(playerShip));
        // TODO Need to send mouse position too
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
      case Input.Buttons.LEFT:
        spaceShipDTOMessage.setMessage("inputEndMouseLeft", new SpaceShipDTO(playerShip));
        // TODO Need to send mouse position too
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
    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
/*

private void applyInput() {

  if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
    playerShip.accelerate();
  }

  if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
    playerShip.rotateRight();
  } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
    playerShip.rotateLeft();
  }

  if (Gdx.input.isKeyPressed(Input.Keys.E)) {
    camera.zoom += 0.05f;
  } else if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
    camera.zoom -= 0.05f;
  }

  if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
    float mouseX = Gdx.input.getX();
    float mouseY = Gdx.input.getY();

    Vector3 worldCoordinates = new Vector3(mouseX, mouseY, 0);

    camera.unproject(worldCoordinates);

    Vector2 mouseCoordinates = new Vector2(worldCoordinates.x, worldCoordinates.y);
    Vector2 direction = new Vector2(mouseCoordinates).sub(playerShip.getPosition()).nor();

    if (rateOfFireTimer > 0.025f) {
      if (bulletCount >= 5) {
        Bullet bullet = new Bullet(world, direction, playerShip.getPosition(), tracerBulletTexture, 30.0f * 2, 10,
          0.5f * 2,
          0.075f * 2);
        bullets.add(bullet);
        rateOfFireTimer = 0;
        bulletCount = 0;
      } else {
        Bullet bullet = new Bullet(world, direction, playerShip.getPosition(), bulletTexture, 30.0f * 2, 10,
          0.25f * 2,
          0.075f * 2);
        bullets.add(bullet);
        rateOfFireTimer = 0;
        bulletCount++;
      }
    }
  }
}
*/
