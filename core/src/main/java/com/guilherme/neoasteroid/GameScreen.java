package com.guilherme.neoasteroid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.esotericsoftware.minlog.Log;

public class GameScreen implements Screen {
  private final Main game;

  private final HostServer hostServer;

  public World world;

  private float updateTimer = 0.0f;

  public OrthographicCamera camera;
  private final ExtendViewport viewport;

  private float timeToAsteroid = 0.0f;
  // private Texture backgroundTexture;
  public Texture satelliteTexture;
  public Texture bulletTexture;
  public Texture tracerBulletTexture;

  private int satelliteID = 0;
  public Map<Integer, Satellite> satellites;

  private final Array<Planet> planets;
  private int earthHP;

  public SpaceShip playerShip;
  private final Array<Bullet> bullets;
  private int bulletCount = 0;
  private float rateOfFireTimer = 0.0f;
  public List<SpaceShip> playersSpaceShips;

  public boolean allPlayersLoadingComplete = false;

  private final List<String> playersLoadingCompleteTempList;
  private InputProcessor inputHandler;

  public GameScreen(Main game) {
    this.game = game;

    hostServer = new HostServer(game);

    if (game.player.isHost()) {
      this.game.player.setInGameScreen(true);
      hostServer.sendStartGameScreen();
    }

    world = new World(new Vector2(0, 0), true);
    world.setContactListener(new WorldContactListener());

    // backgroundTexture = new Texture("background.png");
    satelliteTexture = new Texture("satellite_min.png");

    planets = new Array<>();

    // Create Earth
    Texture earthImage = new Texture("planet.png");
    Planet earthPlanet = new Planet(world, new Vector2(0, 0),
        10, 100, 9.8f, earthImage);
    earthHP = earthPlanet.getHealthPoints();
    planets.add(earthPlanet);

    // Moon creation (experimental)
    // Texture moonImage = new Texture("moon.png");
    // Planet moonPlanet = new Planet(world, new Vector2(500, 0), 1.5f, 160, 1.625f,
    // moonImage);
    // planets.add(moonPlanet);

    satellites = new ConcurrentHashMap<>();
    playersLoadingCompleteTempList = new CopyOnWriteArrayList<>();
    playersSpaceShips = new CopyOnWriteArrayList<>();

    bullets = new Array<>();
    bulletTexture = new Texture("bullet.png");
    tracerBulletTexture = new Texture("tracer_bullet.png");

    camera = new OrthographicCamera();
    viewport = new ExtendViewport(250, 250, camera);

    Log.info("Entering render...");
  }

  @Override
  public void show() {
  }

  @Override
  public void render(float delta) {

    if (!game.player.isHost() && !game.player.isInGameScreen()) {
      Message<String> clientInGameScreenMessage = new Message<>();
      clientInGameScreenMessage.setMessage("inGameScreen", game.player.getName());
      game.client.sendTCP(clientInGameScreenMessage);
      game.player.setInGameScreen(true);
      Log.info("Sent inGameMessage");
    }

    // Loading sequence
    if (!allPlayersLoadingComplete) {

      renderLoadingScreen();

      // Setting up the game by the host
      if (game.player.isHost()) {
        if (!game.allPlayersInGameScreen()) {
          return;
        }

        initialAsteroidCreation();

        if (!game.allPlayersLoadedAsteroids()) {
          return;
        }

        initialSpaceShipsCreation();

        if (!game.allPlayersLoadedSpaceShips()) {
          return;
        } else {
          allPlayersLoadingComplete = true;
          hostServer.sendAllPlayersLoaded();
        }
      }
      return;
    }

    if (inputHandler == null) {
      if (game.player.isHost()) {
        inputHandler = new InputHandlerHost(game, playerShip);
        Gdx.input.setInputProcessor(inputHandler);
        Log.info("Created Host InputHandler");
      } else {
        inputHandler = new InputHandlerClient(game, playerShip);
        Gdx.input.setInputProcessor(inputHandler);
        Log.info("Created Client InputHandler");
      }
    }

    // TODO Add logic to handle ship movements.
    spaceShipsMovements();

    // Loop for timed events
    if (game.player.isHost()) {

      // Create new asteroids every 5 seconds
      timeToAsteroid += delta;

      if (timeToAsteroid > 5f) {
        createAsteroidThreat(world, satelliteTexture, planets.get(0), satellites);
        timeToAsteroid = 0.0f;
      }

      // Update players and asteroids position
      updateTimer += delta;

      if (updateTimer > 0.1) {
        hostServer.updateAllPlayersPosition();

        for (Satellite asteroid : satellites.values()) {
          hostServer.sendUpdateAsteroid(asteroid);
        }
        updateTimer = 0.0f;
      }
    }

    rateOfFireTimer += delta;

    // Log for Earth Health
    int tempHP = planets.get(0).getHealthPoints();

    if (earthHP != tempHP) {
      earthHP = tempHP;
      Gdx.app.log("Earth", "HP: " + earthHP);
    }

    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // Flag asteroids to remove
    List<Integer> toRemove = new ArrayList<>();
    for (Map.Entry<Integer, Satellite> entry : satellites.entrySet()) {
      if (!entry.getValue().isAlive()) {
        entry.getValue().dispose();
        toRemove.add(entry.getKey());
      }
    }

    for (Integer key : toRemove) {
      satellites.remove(key);
    }

    for (Satellite satellite : satellites.values()) {

      Planet highestPlanet = findPlanetHighestGravForce(satellite.body);

      // TODO Temporarily acessing the body directly. Need to change
      applyGravitationalForce(satellite.body, highestPlanet);

      float distance = planets.get(0).getPosition().dst(satellite.body.getPosition());

      if (distance < 90.0f) {
        simulateAtmosphericDrag(satellite);
      }
    }

    applyGravitationalForce(playerShip.body, planets.get(0));

    if (planets.get(0).getPosition().dst(playerShip.body.getPosition()) < 90)
      simulateAtmosphericDrag(playerShip);

    for (int i = 0; i < bullets.size; i++) {
      if (!bullets.get(i).isAlive()) {
        Bullet bullet = bullets.get(i);
        bullet.dispose();
        bullets.removeIndex(i);
      }
    }

    game.spriteBatch.setProjectionMatrix(camera.combined);
    game.spriteBatch.begin();
    // spriteBatch.draw(backgroundTexture, -600 / 2, -340 / 2, 600, 340);

    for (

    Planet planet : planets) {
      planet.render(game.spriteBatch);
    }

    for (Satellite satellite : satellites.values()) {
      satellite.render(game.spriteBatch);
    }

    for (Bullet bullet : bullets) {
      bullet.render(game.spriteBatch);
    }

    for (SpaceShip spaceShip : playersSpaceShips) {
      spaceShip.render(game.spriteBatch);
    }

    game.spriteBatch.end();

    camera.position.set(playerShip.getPosition(), 0);
    camera.update();

    world.step(delta, 6, 2);

    // for (Satellite satellite : satellites) {
    // // Gdx.app.log("Main", "Position: " + satellite.body.getPosition());
    // // Gdx.app.log("Satellite", "Velocity: " +
    // satellite.body.getLinearVelocity());
    // }
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height);
    camera.update();
  }

  @Override
  public void pause() {
    // TODO Auto-generated method stub
  }

  @Override
  public void resume() {
    // TODO Auto-generated method stub
  }

  @Override
  public void hide() {
    // TODO Auto-generated method stub
  }

  @Override
  public void dispose() {
    world.dispose();
  }

  private void applyGravitationalForce(Body satelliteBody, Planet planet) {
    // Distância entre os corpos
    Vector2 direction = planet.getPosition().cpy().sub(satelliteBody.getPosition());
    float distance = planet.getPosition().dst(satelliteBody.getPosition());

    // Evitar divisão por zero
    // if (distance > 200f)
    // return;

    // Normaliza a direção (vetor unitário)
    direction.nor();

    // Calcula a força gravitacional (simplificada)
    float forceMagnitude = Constants.EARTH_GRAVITY * (planet.getMass() * satelliteBody.getMass())
        / (distance * distance);
    Vector2 force = direction.scl(forceMagnitude);

    // Gdx.app.log("Gravidade",
    // "x: " + direction.x + "y: " + direction.y + " Mag: " + forceMagnitude + "
    // Pos: "
    // + planet.getPosition());

    satelliteBody.applyForceToCenter(force, true);
  }

  private void setInicialOrbitVelocity(Body satelliteBody, Planet planet, float rotationDirection) {
    float satelliteDistance = planet.getPosition().dst(satelliteBody.getPosition());
    float setInitialVelocityMagnitude = rotationDirection * (float) Math
        .sqrt(planet.getGravity() * planet.getMass() / satelliteDistance);

    Vector2 direction = satelliteBody.getPosition().cpy().sub(planet.getPosition()).nor();

    Vector2 perpendicularDirection = new Vector2(-direction.y, direction.x);

    satelliteBody.setLinearVelocity(perpendicularDirection.scl(setInitialVelocityMagnitude));

    // Gdx.app.log("Function", "Dist: " + satelliteDistance + " V: " +
    // setInitialVelocityMagnitude);
  }



  private void createAsteroidThreat(World world, Texture satelliteTexture, Planet planet,
      Map<Integer, Satellite> satelliteList) {
    Satellite asteroid = new Satellite(world, satelliteID, 1000, 1500, 4, satelliteTexture);
    satelliteID++;
    Vector2 direction = planet.getPosition().cpy().sub(asteroid.body.getPosition());
    Vector2 force = direction.scl(5);

    asteroid.body.setLinearVelocity(force);

    satelliteList.put(asteroid.getId(), asteroid);
    hostServer.sendNewAsteroid(asteroid);
  }

  private void simulateAtmosphericDrag(Satellite satellite) {
    float dragCoeficient = 0.53f;
    float area = (float) Math.pow(satellite.getRadius(), 2) * 0.005f * (float) Math.PI;
    float airDensitySeaLevel = 1.255f;
    float maxAltitude = 90f;

    Vector2 direction = satellite.body.getLinearVelocity();
    float velocity = direction.len();

    float height = planets.get(0).getPosition().dst(satellite.body.getPosition());

    float airDensity = airDensitySeaLevel * (float) Math.exp(-height / maxAltitude);

    float dragForce = 0.5f * dragCoeficient * airDensity * area * velocity * velocity;

    satellite.body.applyForceToCenter(direction.nor().scl(-dragForce), false);

    // Gdx.app.log("Drag", "Magnitude :" + dragForce);
  }

  private void simulateAtmosphericDrag(SpaceShip ship) {
    float dragCoeficient = 0.53f;
    float area = (float) Math.pow(5f, 2) * 0.005f * (float) Math.PI;
    float airDensitySeaLevel = 1.255f;
    float maxAltitude = 90f;

    Vector2 direction = ship.body.getLinearVelocity();
    float velocity = direction.len();

    float height = planets.get(0).getPosition().dst(ship.body.getPosition());

    float airDensity = airDensitySeaLevel * (float) Math.exp(-height / maxAltitude);

    float dragForce = 0.5f * dragCoeficient * airDensity * area * velocity * velocity;

    ship.body.applyForceToCenter(direction.nor().scl(-dragForce), false);

    // Gdx.app.log("Drag", "Velocity: " + velocity + " Magnitude: " + dragForce + "
    // Height: " + height);
  }

  private Planet findPlanetHighestGravForce(Body satelliteBody) {
    float force;
    float highestForce = 0;
    Planet highestPlanet = null;

    for (Planet planet : planets) {
      float distance = planet.getPosition().dst(satelliteBody.getPosition());
      force = planet.getGravity() * (planet.getMass() * satelliteBody.getMass()) / (distance * distance);

      if (force > highestForce) {
        highestForce = force;
        highestPlanet = planet;
      }
    }

    return highestPlanet;
  }

  private void renderLoadingScreen() {

  }

  public void setPlayerLoadingComplete(Player player) {
    playersLoadingCompleteTempList.add(player.getName());
  }

  /**
   * Initialize asteroid creation on game start
   */
  private void initialAsteroidCreation() {
    if (satellites.isEmpty()) {
      for (int i = 0; i < 1000; i++) {
        Satellite newSatellite = new Satellite(world, satelliteID, 500, 530, (float) Math.random() * 1.4f + 0.8f,
          satelliteTexture);
        satelliteID++;
        satellites.put(newSatellite.getId(), newSatellite);

        Planet planet = findPlanetHighestGravForce(newSatellite.body);

        // Random direction for orbit. Add random num to rotation
        // int randomNum = Math.random() < 0.5 ? -1 : 1;
        setInicialOrbitVelocity(newSatellite.body, planet, 1);

        hostServer.sendNewAsteroid(newSatellite);
      }
      Log.info("Sent asteroids");

      game.player.setAsteroidsLoaded(true);
    }
  }

  /**
   * Creates player ships and set initial orbits around origin planet.
   * Also assigns player ship.
   */
  private void initialSpaceShipsCreation() {
    if (playersSpaceShips.isEmpty()) {
      for (Player player : game.players) {
        playersSpaceShips.add(new SpaceShip(world, player));
      }

      for (SpaceShip spaceShip : playersSpaceShips) {
        setInicialOrbitVelocity(spaceShip.body, planets.get(0), 1);

        hostServer.sendNewSpaceShip(spaceShip);
      }

      // Selects playerShip
      for (SpaceShip spaceShip : playersSpaceShips) {
        if (spaceShip.getPlayer().getName().equals(game.player.getName())) {
          playerShip = spaceShip;
        }
      }

      camera.position.set(playerShip.getPosition(), 0);
      camera.update();

      game.player.setSpaceShipsLoaded(true);
    }
  }

  private void spaceShipsMovements() {
    for (SpaceShip spaceShip : playersSpaceShips) {
      if (spaceShip.isAccelerating()) {
        spaceShip.accelerate();
      }

      if (spaceShip.isTurningLeft()) {
        spaceShip.rotateLeft();
      }

      if (spaceShip.isTurningRight()) {
        spaceShip.rotateRight();
      }
    }
  }
}
