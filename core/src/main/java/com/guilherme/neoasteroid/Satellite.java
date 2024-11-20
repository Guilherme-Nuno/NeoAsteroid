package com.guilherme.neoasteroid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Satellite {
  private int id;
  private World world;
  private int healthPoints;
  public Body body;
  private float radius;
  private float density;
  private Texture satelliteImg;

  public Satellite() {
    this.density = 1f;
  }

  public Satellite(World world, int id, float minDistance, float maxDistance, float radius,
      Texture satteliteTexture) {
    this.world = world;
    this.id = id;
    this.healthPoints = MathUtils.round(radius * 10);

    this.radius = radius;
    this.satelliteImg = satteliteTexture;

    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set(setCoordinates(minDistance, maxDistance));
    this.body = world.createBody(bodyDef);

    CircleShape shape = new CircleShape();
    shape.setRadius(radius / 2);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = density;
    fixtureDef.restitution = 0.6f;
    fixtureDef.filter.categoryBits = Constants.CATEGORY_SATELLITE;
    fixtureDef.filter.maskBits = Constants.CATEGORY_BULLET | Constants.CATEGORY_SATELLITE | Constants.CATEGORY_PLANET;

    Fixture fixture = body.createFixture(fixtureDef);
    fixture.setUserData(this);

    shape.dispose();
  }

  public Satellite(World world, SatelliteDTO satelliteDTO, Texture texture) {
    this.world = world;
    this.id = satelliteDTO.getId();
    this.healthPoints = satelliteDTO.getHealthPoints();
    this.radius = satelliteDTO.getRadius();
    this.satelliteImg = texture;

    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set(satelliteDTO.getPosition());
    this.body = world.createBody(bodyDef);

    this.body.setLinearVelocity(satelliteDTO.getLinearVelocity());

    CircleShape shape = new CircleShape();
    shape.setRadius(radius / 2);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = density;
    fixtureDef.restitution = 0.6f;
    fixtureDef.filter.categoryBits = Constants.CATEGORY_SATELLITE;
    fixtureDef.filter.maskBits = Constants.CATEGORY_BULLET | Constants.CATEGORY_SATELLITE | Constants.CATEGORY_PLANET;

    Fixture fixture = body.createFixture(fixtureDef);
    fixture.setUserData(this);

    shape.dispose();
  }

  private Vector2 setCoordinates(float minDistance, float maxDistance) {
    float distance = (float) Math.random() * (maxDistance - minDistance) + minDistance;
    float angle = (float) (Math.random() * 2 * Math.PI);

    float x = distance * (float) Math.cos(angle);
    float y = distance * (float) Math.sin(angle);

    Vector2 position = new Vector2(x, y);

    return position;
  }

  public void render(SpriteBatch spriteBatch) {

    spriteBatch.draw(satelliteImg,
        (body.getPosition().x - radius),
        (body.getPosition().y - radius),
        radius * 2,
        radius * 2);
  }

  public void dispose() {
    world.destroyBody(body);
  }

  public void setInitialVelocity(Vector2 velocity) {
    body.setLinearVelocity(velocity);
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

  public float getRadius() {
    return radius;
  }

  public int getId() {
    return this.id;
  }

  public void updateFromDTO(SatelliteDTO satelliteDTO) {
    this.body.getPosition().set(satelliteDTO.getPosition());
    this.body.setLinearVelocity(satelliteDTO.getLinearVelocity());
    this.healthPoints = satelliteDTO.getHealthPoints();
  }
}
