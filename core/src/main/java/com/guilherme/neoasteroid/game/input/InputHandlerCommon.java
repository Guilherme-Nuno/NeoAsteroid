package com.guilherme.neoasteroid.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.esotericsoftware.minlog.Log;
import com.guilherme.neoasteroid.game.GameScreen;

public class InputHandlerCommon implements InputProcessor {
  private GameScreen gameScreen;

  public InputHandlerCommon(GameScreen gameScreen) {
    this.gameScreen = gameScreen;
  }

  @Override
  public boolean keyDown(int keycode) {
      switch (keycode){
        case Input.Keys.ESCAPE:
          gameScreen.pauseMenu.setPaused(!gameScreen.pauseMenu.isPaused());
          return true;
      }
    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
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
