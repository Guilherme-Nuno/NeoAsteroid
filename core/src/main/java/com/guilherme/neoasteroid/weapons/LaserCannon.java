package com.guilherme.neoasteroid.weapons;

import com.badlogic.gdx.graphics.Texture;

public class LaserCannon extends Weapon {
  public LaserCannon() {
    super(10,
      0.25f,
      new Texture("laserTurret.png"),
      new Texture("laser.png"),
      0.4f,
      2);
  }
}
