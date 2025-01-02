package com.guilherme.neoasteroid;

import com.badlogic.gdx.math.Vector2;
import com.guilherme.neoasteroid.spaceship.SpaceShip;

public class SpaceShipDTO {
  private Player player;
  private Vector2 position;
  private Vector2 linearVelocity;
  private float angle;

  public SpaceShipDTO() {
  }

  public SpaceShipDTO(SpaceShip spaceShip) {
    this.player = spaceShip.getPlayer();
    this.position = spaceShip.getPosition();
    this.linearVelocity = spaceShip.body.getLinearVelocity();
    this.angle = spaceShip.body.getAngle();
  }

  public Player getPlayer() {
    return player;
  }

  public Vector2 getPosition() {
    return position;
  }

  public Vector2 getLinearVelocity() {
    return linearVelocity;
  }

  public float getAngle() {
    return angle;
  }
}
