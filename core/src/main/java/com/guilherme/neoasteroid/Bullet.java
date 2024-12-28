package com.guilherme.neoasteroid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Bullet {
  private final World world;
  private final Vector2 origin;
  private Vector2 direction;
  private int healthPoint;
  private float velocity;
  private float density = 0.01f;
  private final Sprite sprite;
  public Body body;
  private float bulletWidth = 0.25f;
  private float bulletHeight = 0.075f;

  public Bullet(World world, Vector2 direction, Vector2 origin, Texture bulletTexture, float velocity, int damage,
      float bulletWidth, float bulletHeight) {
    this.world = world;
    this.origin = origin;
    this.direction = direction;
    this.healthPoint = damage;
    this.velocity = velocity;
    this.bulletWidth = bulletWidth;
    this.bulletHeight = bulletHeight;

    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DynamicBody;
    bodyDef.position.set(this.origin);
    this.body = world.createBody(bodyDef);

    sprite = new Sprite(bulletTexture);
    sprite.setSize(this.bulletWidth, this.bulletHeight);
    sprite.setPosition(this.origin.x, this.origin.y);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(this.bulletWidth / 2, this.bulletHeight / 2);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = density;
    fixtureDef.friction = 0.0f;
    fixtureDef.restitution = 0.0f;
    fixtureDef.filter.categoryBits = Constants.CATEGORY_BULLET;
    fixtureDef.filter.maskBits = Constants.CATEGORY_SATELLITE;

    Fixture fixture = body.createFixture(fixtureDef);
    fixture.setUserData(this);

    shape.dispose();

    body.setLinearVelocity(this.direction.scl(this.velocity));
  }

  public void render(SpriteBatch spriteBatch) {
    sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    sprite.setRotation(MathUtils.radiansToDegrees * body.getAngle());
    sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
    sprite.draw(spriteBatch);
  }

  public void dispose() {
    world.destroyBody(body);
  }

  public float getDistanceToOrigin() {
    return origin.dst(body.getPosition());
  }

  public int getDamage() {
    return healthPoint;
  }

  public void applyDamage(int damage) {
    healthPoint -= damage;
  }

  public boolean isAlive() {
    return healthPoint > 0;
  }
}
