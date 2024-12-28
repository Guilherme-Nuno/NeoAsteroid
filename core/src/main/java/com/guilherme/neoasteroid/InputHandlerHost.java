package com.guilherme.neoasteroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.minlog.Log;

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
