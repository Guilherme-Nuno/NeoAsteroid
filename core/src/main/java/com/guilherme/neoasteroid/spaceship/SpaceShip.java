package com.guilherme.neoasteroid.spaceship;

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
import com.guilherme.neoasteroid.Constants;
import com.guilherme.neoasteroid.game.GameScreen;
import com.guilherme.neoasteroid.Player;
import com.guilherme.neoasteroid.network.SpaceShipDTO;
import com.guilherme.neoasteroid.weapons.LaserCannon;
import com.guilherme.neoasteroid.weapons.Weapon;

import java.util.ArrayList;

public class SpaceShip {
  private final Player player;
  private Vector2 position;
  private final float angularForce = 2500.0f;
  private final float acceleration;
  private final Texture spaceShipImg = new Texture("space_ship_medium.png");
  public Body body;
  private final Sprite sprite;
  private final float spaceShipWidth = 2.0f * 4;
  private final float spaceShipHeight = 2.6f * 4;
  private boolean isAccelerating = false;
  private boolean isTurningRight = false;
  private boolean isTurningLeft = false;
  private boolean isFiring = false;
  private float energy = 100;
  private float maxEnergy = 100;
  private float shotEnergy = 2;
  private float energyTimer = 0;
  private float energyRechargeRate = 5;
  private float shield = 10;
  private float maxShield = 100;
  private float hull = 100;
  private boolean isAlive = true;
  private final ArrayList<HardPoint> hardpointList;
  private int totalPrimaryHardpoint;

  public SpaceShip(World world, SpaceShipDTO spaceShipDTO) {
    this.player = spaceShipDTO.getPlayer();
    this.position = spaceShipDTO.getPosition();

    acceleration = 33.3f;

    sprite = new Sprite(spaceShipImg);
    sprite.setSize(spaceShipWidth, spaceShipHeight);
    sprite.setPosition(this.position.x, this.position.y);

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

    hardpointList = new ArrayList<>();
    totalPrimaryHardpoint = 2;

    hardpointList.add(new HardPoint(new LaserCannon(), new Vector2(2,0)));
    hardpointList.add(new HardPoint(new LaserCannon(), new Vector2(-2,0)));

    body.setLinearVelocity(spaceShipDTO.getLinearVelocity());
  }

  public SpaceShip(World world, Player player) {
    this.player = player;

    position = new Vector2(setCoordinates(80, 120));
    acceleration = 33.3f;

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

    hardpointList = new ArrayList<>();
    hardpointList.add(new HardPoint(new LaserCannon(), new Vector2(2,0)));
    hardpointList.add(new HardPoint(new LaserCannon(), new Vector2(-2,0)));
    hardpointList.add(new HardPoint(new LaserCannon(), new Vector2(3,-2)));
    hardpointList.add(new HardPoint(new LaserCannon(), new Vector2(-3,-2)));
    totalPrimaryHardpoint = 2;

    shape.dispose();
  }

  public void render(SpriteBatch spriteBatch) {
    position = body.getPosition();
    sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    sprite.setRotation(MathUtils.radiansToDegrees * -body.getAngle());
    sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
    sprite.draw(spriteBatch);

    Player player = getPlayer();

    for (HardPoint hardPoint : hardpointList) {
      hardPoint.getWeapon().getWeaponSprite().setOrigin(
        hardPoint.getWeapon().getWeaponSprite().getWidth() / 2,
        hardPoint.getWeapon().getWeaponSprite().getHeight() / 2);

      hardPoint.getWeapon().getWeaponSprite().setPosition(
        body.getPosition().x + hardPoint.getPositionOnShip().x - hardPoint.getWeapon().getWeaponSprite().getOriginX(),
        body.getPosition().y + hardPoint.getPositionOnShip().y - hardPoint.getWeapon().getWeaponSprite().getOriginY());

      hardPoint.getWeapon().getWeaponSprite().setRotation(hardPoint.getWeapon().getRotationDeg() - 90);

      hardPoint.getWeapon().getWeaponSprite().draw(spriteBatch);
    }

  }

  public void dispose() {
  }

  /**
   * Simulates spaceship behaviour for the specified delta time
   * @param delta time since last render iteration
   */
  public void simulate(GameScreen gameScreen, float delta) {
    for (HardPoint hardPoint : hardpointList) {
      Vector2 direction = new Vector2(getPlayer().getMousePosition()).sub(getPosition().add(hardPoint.getPositionOnShip())).nor();
      Weapon weapon = hardPoint.getWeapon();

      float deltaAngle = weapon.getRotationDeg() - direction.angleDeg() % 360;

      if (deltaAngle > 180) deltaAngle -= 360;
      if (deltaAngle < -180) deltaAngle += 360;

      if (Math.abs(deltaAngle) > 0.01f) {
        float rotationStep = weapon.getRateOfRotation() * delta;
        if (Math.abs(deltaAngle) < rotationStep) {
          weapon.setRotationDeg(direction.angleDeg());
        } else {
          weapon.setRotationDeg(weapon.getRotationDeg() - Math.signum(deltaAngle) * rotationStep);
        }
      }

      if (isFiring()) {
          weapon.incrementRateOfFireTimer(delta);
          if (weapon.getRateOfFireTimer() > weapon.getRateOfFire()) {
            if (getEnergy() >= weapon.getEnergyPerShot()) {


              float bulletSpread = (float) Math.random() * 2 * weapon.getBulletSpread() - weapon.getBulletSpread();

              gameScreen.createNewBullet(
                this.getPosition().add(hardPoint.getPositionOnShip()),
                new Vector2(1, 0).setAngleDeg(weapon.getRotationDeg() + bulletSpread));
              weapon.setRateOfFireTimer(0);
              setEnergy(getEnergy() - hardPoint.getWeapon().getEnergyPerShot());
            }
          }
        }

      }

    float nextEnergy = getEnergy() + getEnergyRechargeRate() * delta;

    setEnergy(Math.min(nextEnergy, getMaxEnergy()));

    if (nextEnergy > getMaxEnergy()) {
      float nextShield = getShield()+ getEnergyRechargeRate() / 2 * delta;
      setShield(Math.min(nextShield, getMaxShield()));
    }
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

  public void setEnergyTimer(float energyTimer) {
    this.energyTimer = energyTimer;
  }

  public float getEnergy() {
    return energy;
  }

  public float getHull() {
    return hull;
  }

  public float getShield() {
    return shield;
  }

  public float getEnergyTimer() {
    return energyTimer;
  }

  public float getEnergyRechargeRate() {
    return energyRechargeRate;
  }

  public void setEnergy(float energy) {
    this.energy = energy;
  }

  public void setHull(float hull) {
    this.hull = hull;
  }

  public void setShield(float shield) {
    this.shield = shield;
  }

  public void setAlive(boolean alive) {
    isAlive = alive;
  }

  public boolean isAlive() {
    return isAlive;
  }

  public float getShotEnergy() {
    return shotEnergy;
  }

  public float getMaxEnergy() {
    return maxEnergy;
  }

  public float getMaxShield() {
    return maxShield;
  }
}

