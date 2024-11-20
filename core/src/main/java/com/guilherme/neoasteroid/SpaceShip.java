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
  private Player player;
  private Vector2 position;
  private float angularForce = 2500.0f;
  private float aceleration;
  private Texture spaceShipImg = new Texture("space_ship_medium.png");
  public Body body;
  private Sprite sprite;
  // private float spaceShipRadius = 1.0f;
  private float spaceShipWidth = 2.0f * 2;
  private float spaceShipHeight = 2.6f * 2;

  public SpaceShip() {

  }

  public SpaceShip(World world, SpaceShipDTO spaceShipDTO) {
    this.player = spaceShipDTO.getPlayer();
    this.position = spaceShipDTO.getPosition();

    aceleration = 33.3f;

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
    aceleration = 33.3f;

    sprite = new Sprite(spaceShipImg);
    sprite.setSize(spaceShipWidth, spaceShipHeight);
    sprite.setPosition(position.x, position.y);

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

  public void acelerate() {
    float angle = body.getAngle();
    Vector2 direction = new Vector2((float) MathUtils.sin(angle), (float) MathUtils.cos(angle));
    body.applyForceToCenter(direction.scl(aceleration), true);
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

  public Player getPlayer() {
    return player;
  }

  private Vector2 setCoordinates(float minDistance, float maxDistance) {
    float distance = (float) Math.random() * (maxDistance - minDistance) + minDistance;
    float angle = (float) (Math.random() * 2 * Math.PI);

    float x = distance * (float) Math.cos(angle);
    float y = distance * (float) Math.sin(angle);

    Vector2 position = new Vector2(x, y);

    return position;
  }
}
