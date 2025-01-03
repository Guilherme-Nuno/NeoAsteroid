package com.guilherme.neoasteroid.spaceship;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.guilherme.neoasteroid.weapons.Weapon;

public class HardPoint {
  private final Weapon weapon;
  private final Vector2 positionOnShip;

  public HardPoint(Weapon weapon, Vector2 positionOnShip) {
    this.weapon = weapon;
    this.positionOnShip = positionOnShip;
  }

  public Vector2 getPositionOnShip() {
    return positionOnShip;
  }

  public Weapon getWeapon() {
    return weapon;
  }
}
