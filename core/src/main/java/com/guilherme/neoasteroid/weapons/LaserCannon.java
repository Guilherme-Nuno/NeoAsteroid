package com.guilherme.neoasteroid.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class LaserCannon extends Weapon {

  public LaserCannon() {
    super(10,
      0.25f,
      new Texture("laserTurret.png"),
      new Texture("laser.png"),
      4f,
      0.85f,
      2.0f);

    Sprite sprite = new Sprite(this.getWeaponTexture());
    float weaponWidth = 1.5f;
    float weaponHeight = 3;
    sprite.setSize(weaponWidth, weaponHeight);
    this.setWeaponSprite(sprite);
  }
}
