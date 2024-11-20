package com.guilherme.neoasteroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class WorldContactListener implements ContactListener {

  @Override
  public void beginContact(Contact contact) {
    Fixture fixtureA = contact.getFixtureA();
    Fixture fixtureB = contact.getFixtureB();

    if (fixtureA.getUserData() instanceof Satellite && fixtureB.getUserData() instanceof Bullet) {
      Satellite satellite = (Satellite) fixtureA.getUserData();
      Bullet bullet = (Bullet) fixtureB.getUserData();

      satellite.applyDamage(bullet.getDamage());
      bullet.applyDamage(bullet.getDamage());
    } else if (fixtureB.getUserData() instanceof Satellite && fixtureA.getUserData() instanceof Bullet) {
      Satellite satellite = (Satellite) fixtureB.getUserData();
      Bullet bullet = (Bullet) fixtureA.getUserData();

      satellite.applyDamage(bullet.getDamage());
      bullet.applyDamage(bullet.getDamage());
    }

    if (fixtureA.getUserData() instanceof Satellite && fixtureB.getUserData() instanceof Planet) {
      Satellite satellite = (Satellite) fixtureA.getUserData();
      Planet planet = (Planet) fixtureB.getUserData();

      planet.applyDamage(satellite.getHealthPoints());
      satellite.applyDamage(satellite.getHealthPoints());
      Gdx.app.log("Contact", "Asteroid life: " + satellite.getHealthPoints() + " Alive: " + satellite.isAlive());
    } else if (fixtureB.getUserData() instanceof Satellite && fixtureA.getUserData() instanceof Planet) {
      Satellite satellite = (Satellite) fixtureB.getUserData();
      Planet planet = (Planet) fixtureA.getUserData();

      planet.applyDamage(satellite.getHealthPoints());
      satellite.applyDamage(satellite.getHealthPoints());
      Gdx.app.log("Contact", "Asteroid life: " + satellite.getHealthPoints() + " Alive: " + satellite.isAlive());
    }
  }

  @Override
  public void preSolve(Contact contact, Manifold manifold) {
  }

  @Override
  public void postSolve(Contact contact, ContactImpulse contactImpulse) {
  }

  @Override
  public void endContact(Contact contact) {
  }
}
