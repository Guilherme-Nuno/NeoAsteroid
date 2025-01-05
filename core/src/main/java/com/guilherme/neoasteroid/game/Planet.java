package com.guilherme.neoasteroid.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.guilherme.neoasteroid.Constants;

public class Planet {
  private int healthPoints;
  private float radius;
  private Texture planetImage;
  private Vector2 position;
  private float mass;
  private Body body;
  private float gravity;

  public Planet(World world, Vector2 position, float radius, float mass, float gravity, Texture image) {
    this.healthPoints = 1000;

    this.position = position;
    this.radius = radius;
    this.mass = mass;
    this.gravity = gravity;

    this.planetImage = image;

    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.StaticBody;
    bodyDef.position.set(position);
    this.body = world.createBody(bodyDef);

    CircleShape circleShape = new CircleShape();
    circleShape.setRadius(radius);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = circleShape;
    fixtureDef.filter.categoryBits = Constants.CATEGORY_PLANET;

    Fixture fixture = body.createFixture(fixtureDef);
    fixture.setUserData(this);

    circleShape.dispose();
  }

  public void render(SpriteBatch batch) {
    batch.draw(planetImage,
        (position.x - radius),
        (position.y - radius),
        radius * 2,
        radius * 2);
  }

  public void dispose() {
    planetImage.dispose();
  }

  public Vector2 getPosition() {
    return position;
  }

  public float getMass() {
    return mass;
  }

  public float getGravity() {
    return gravity;
  }

  public int getHealthPoints() {
    return healthPoints;
  }

  public boolean isAlive() {
    if (healthPoints > 0)
      return true;
    else
      return false;
  }

  public void applyDamage(int damage) {
    this.healthPoints -= damage;
  }
}
