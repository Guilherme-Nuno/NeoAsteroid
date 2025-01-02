package com.guilherme.neoasteroid.weapons;

import com.badlogic.gdx.graphics.Texture;

public class Weapon {
  private final int damage;
  private final float rateOfFire;
  private boolean isFiring;
  private final Texture weaponTexture;
  private final Texture bulletTexture;
  private final float rateOfRotation;
  private float rateOfFireTimer;
  private final float energyPerShot;

  public Weapon (int damage, float rateOfFire, Texture weaponTexture, Texture bulletTexture, float rateOfRotation, float energyPerShot) {
    this.damage = damage;
    this.rateOfFire = rateOfFire;
    isFiring = false;
    this.weaponTexture = weaponTexture;
    this.rateOfRotation = rateOfRotation;
    this.bulletTexture = bulletTexture;
    this.energyPerShot = energyPerShot;
    rateOfFireTimer = (float) Math.random() * rateOfFire;
  }

  public void setFiring(boolean firing) {
    isFiring = firing;
  }

  public boolean isFiring() {
    return isFiring;
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
}
