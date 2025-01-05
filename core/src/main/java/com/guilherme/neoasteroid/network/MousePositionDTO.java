package com.guilherme.neoasteroid.network;

import com.badlogic.gdx.math.Vector2;

public class MousePositionDTO {
  Vector2 mousePosition;
  String playerName;

  public MousePositionDTO() {
  }

  public MousePositionDTO(String playerName, Vector2 mousePosition) {
    this.mousePosition = mousePosition;
    this.playerName = playerName;
  }

  public Vector2 getMousePosition() {
    return mousePosition;
  }

  public String getPlayerName() {
    return playerName;
  }
}
