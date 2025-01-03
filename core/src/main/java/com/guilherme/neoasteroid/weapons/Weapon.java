package com.guilherme.neoasteroid.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Weapon {
  private final int damage;
  private final float rateOfFire;
  private boolean isRotating;
  private float rotationDeg;
  private final Texture weaponTexture;
  private final Texture bulletTexture;
  private final float rateOfRotation;
  private float rateOfFireTimer;
  private final float energyPerShot;
  private Sprite weaponSprite;
  private final float bulletSpread;


  public Weapon (
    int damage,
    float rateOfFire,
    Texture weaponTexture,
    Texture bulletTexture,
    float rateOfRotation,
    float energyPerShot,
    float bulletSpread)
  {
    this.damage = damage;
    this.rateOfFire = rateOfFire;
    isRotating = false;
    this.weaponTexture = weaponTexture;
    this.rateOfRotation = rateOfRotation;
    this.bulletTexture = bulletTexture;
    this.energyPerShot = energyPerShot;
    rateOfFireTimer = (float) Math.random() * rateOfFire;
    this.bulletSpread = bulletSpread;
    this.rotationDeg = 0;
  }

  public void setRotating(boolean rotating) {
    isRotating = rotating;
  }

  public boolean isRotating() {
    return isRotating;
  }

  public float getRotationDeg() {
    return rotationDeg;
  }

  public void setRotationDeg(float rotationDeg) {
    this.rotationDeg = rotationDeg;
  }

  public float getRateOfFire() {
    return rateOfFire;
  }

  public int getDamage() {
    return damage;
  }

  public void setRateOfFireTimer(float rateOfFireTimer) {
    this.rateOfFireTimer = rateOfFireTimer;
  }

  public void incrementRateOfFireTimer(float value) {
    rateOfFireTimer += value;
  }

  public float getRateOfFireTimer() {
    return rateOfFireTimer;
  }

  public float getRateOfRotation() {
    return rateOfRotation;
  }

  public Texture getBulletTexture() {
    return bulletTexture;
  }

  public Texture getWeaponTexture() {
    return weaponTexture;
  }

  public float getEnergyPerShot() {
    return energyPerShot;
  }

  public void setWeaponSprite(Sprite weaponSprite) {
    this.weaponSprite = weaponSprite;
  }

  public Sprite getWeaponSprite() {
    return weaponSprite;
  }

  public float getBulletSpread() {
    return bulletSpread;
  }
}
