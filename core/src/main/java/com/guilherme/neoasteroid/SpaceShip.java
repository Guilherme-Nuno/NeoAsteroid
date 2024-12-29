package com.guilherme.neoasteroid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
// import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class SpaceShip {
  private final Player player;
  private Vector2 position;
  private final float angularForce = 2500.0f;
  private final float acceleration;
  private final Texture spaceShipImg = new Texture("space_ship_medium.png");
  public Body body;
  private final Sprite sprite;
  private final float spaceShipWidth = 2.0f * 2;
  private final float spaceShipHeight = 2.6f * 2;
  private final float rateOfFire;
  private float rateOfFireTimer;
  private boolean isAccelerating = false;
  private boolean isTurningRight = false;
  private boolean isTurningLeft = false;
  private boolean isFiring = false;
  private int energy = 100;
  private float energyTimer = 0;
  private float energyRechargeRate = 0.2f;
  private int shield = 100;
  private int hull = 100;
  private boolean isAlive = true;

  public SpaceShip(World world, SpaceShipDTO spaceShipDTO) {
    this.player = spaceShipDTO.getPlayer();
    this.position = spaceShipDTO.getPosition();

    acceleration = 33.3f;
    rateOfFire = 0.25f;
    rateOfFireTimer = rateOfFire;

    sprite = new Sprite(spaceShipImg);
    sprite.setSize(spaceShipWidth, spaceShipHeight);
    sprite.setPosition(this.position.x, this.position.y);

    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DynamicBody;
    bodyDef.position.set(position);
    body = world.createBody(bodyDef);
    body.setAngularDamping(25.0f);

    // CircleShape shape = new CircleShape();
    // shape.setRadius(spaceShipRadius);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(this.spaceShipWidth / 2, this.spaceShipHeight / 2);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 1.0f;
    fixtureDef.filter.categoryBits = Constants.CATEGORY_SHIP;

    Fixture fixture = body.createFixture(fixtureDef);
    fixture.setUserData(this);

    shape.dispose();

    body.setLinearVelocity(spaceShipDTO.getLinearVelocity());
  }

  public SpaceShip(World world, Player player) {
    this.player = player;

    position = new Vector2(setCoordinates(80, 120));
    acceleration = 33.3f;
    rateOfFire = 0.25f;
    rateOfFireTimer = rateOfFire;

    sprite = new Sprite(spaceShipImg);
    sprite.setSize(spaceShipWidth, spaceShipHeight);
    sprite.setPosition(position.x, position.y);

    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DynamicBody;
    bodyDef.position.set(position);
    body = world.createBody(bodyDef);
    body.setAngularDamping(25.0f);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(this.spaceShipWidth / 2, this.spaceShipHeight / 2);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 1.0f;
    fixtureDef.filter.categoryBits = Constants.CATEGORY_SHIP;

    Fixture fixture = body.createFixture(fixtureDef);
    fixture.setUserData(this);

    shape.dispose();
  }

  public void render(SpriteBatch spriteBatch) {
    position = body.getPosition();
    sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    sprite.setRotation(MathUtils.radiansToDegrees * -body.getAngle());
    sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
    sprite.draw(spriteBatch);
  }

  public void dispose() {
  }

  public void accelerate() {
    float angle = body.getAngle();
    Vector2 direction = new Vector2((float) MathUtils.sin(angle), (float) MathUtils.cos(angle));
    body.applyForceToCenter(direction.scl(acceleration), true);
  }

  public void rotateRight() {
    body.applyTorque(angularForce, true);
  }

  public void rotateLeft() {
    body.applyTorque(-angularForce, true);
  }

  public Vector2 getPosition() {
    return body.getPosition();
  }

  public void setBodyPosition(Vector2 position, float angle, Vector2 linearVelocity) {
    body.setTransform(position, angle);
    body.setLinearVelocity(linearVelocity);
  }

  public Player getPlayer() {
    return player;
  }

  public boolean isAccelerating() {
    return isAccelerating;
  }

  public boolean isFiring() {
    return isFiring;
  }

  public boolean isTurningLeft() {
    return isTurningLeft;
  }

  public boolean isTurningRight() {
    return isTurningRight;
  }

  public void setAccelerating(boolean value) {
    isAccelerating = value;
  }

  public void setTurningLeft(boolean turningLeft) {
    isTurningLeft = turningLeft;
  }

  public void setTurningRight(boolean turningRight) {
    isTurningRight = turningRight;
  }

  public void setFiring(boolean firing) {
    isFiring = firing;
  }

  private Vector2 setCoordinates(float minDistance, float maxDistance) {
    float distance = (float) Math.random() * (maxDistance - minDistance) + minDistance;
    float angle = (float) (Math.random() * 2 * Math.PI);

    float x = distance * (float) Math.cos(angle);
    float y = distance * (float) Math.sin(angle);

    return new Vector2(x, y);
  }

  public float getRateOfFireTimer() {
    return rateOfFireTimer;
  }

  public void setRateOfFireTimer(float rateOfFireTimer) {
    this.rateOfFireTimer = rateOfFireTimer;
  }

  public float getRateOfFire() {
    return rateOfFire;
  }

  public void setEnergyTimer(float energyTimer) {
    this.energyTimer = energyTimer;
  }

  public int getEnergy() {
    return energy;
  }

  public int getHull() {
    return hull;
  }

  public int getShield() {
    return shield;
  }

  public float getEnergyTimer() {
    return energyTimer;
  }

  public float getEnergyRechargeRate() {
    return energyRechargeRate;
  }

  public void setEnergy(int energy) {
    this.energy = energy;
  }

  public void setHull(int hull) {
    this.hull = hull;
  }

  public void setShield(int shield) {
    this.shield = shield;
  }

  public void setAlive(boolean alive) {
    isAlive = alive;
  }

  public boolean isAlive() {
    return isAlive;
  }
}

