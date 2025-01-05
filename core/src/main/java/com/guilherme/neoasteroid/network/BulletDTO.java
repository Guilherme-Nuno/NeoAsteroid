package com.guilherme.neoasteroid.network;

import com.badlogic.gdx.math.Vector2;

public class BulletDTO {
  private Vector2 direction;
  private Vector2 origin;

  public BulletDTO() {}

  public BulletDTO(Vector2 origin, Vector2 direction) {
    this.direction = direction;
    this.origin = origin;
  }

  public Vector2 getDirection() {
    return direction;
  }

  public Vector2 getOrigin() {
    return origin;
  }
}
