package com.guilherme.neoasteroid;

import com.badlogic.gdx.math.Vector2;

public class SatelliteDTO {
  private int id;
  private float x, y;
  private float velocityX, velocityY;
  private int healthPoints;
  private float radius;

  public SatelliteDTO() {
  }

  public SatelliteDTO(Satellite satellite) {
    this.id = satellite.getId();
    this.x = satellite.body.getPosition().x;
    this.y = satellite.body.getPosition().y;
    this.velocityX = satellite.body.getLinearVelocity().x;
    this.velocityY = satellite.body.getLinearVelocity().y;
    this.healthPoints = satellite.getHealthPoints();
    this.radius = satellite.getRadius();
  }

  public int getId() {
    return this.id;
  }

  public int getHealthPoints() {
    return this.healthPoints;
  }

  public float getRadius() {
    return this.radius;
  }

  public Vector2 getPosition() {
    Vector2 position = new Vector2(this.x, this.y);
    return position;
  }

  public Vector2 getLinearVelocity() {
    Vector2 velocity = new Vector2(this.velocityX, this.velocityY);
    return velocity;
  }
}
