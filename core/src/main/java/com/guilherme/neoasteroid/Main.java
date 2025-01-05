package com.guilherme.neoasteroid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends Game {
  public SpriteBatch spriteBatch;

  /** Player object that represents in game player */
  public Player player;

  public Server server;
  public Client client;

  public Skin uiSkin;
  /** List of String that has the player names of the current game */
  public List<String> playerList;
  /** ArrayList of Player object that stores the players in the current game */
  public ArrayList<Player> players;
  /** Show all current messages sent to the chat pane */
  public List<String> chatList;
  /** Array to store all chat messages. Used to set chatList */
  public Array<String> chat;

  @Override
  public void create() {

    setupGame();

    setScreen(new MainMenuScreen(this));
  }

  @Override
  public void render() {
    super.render();
  }

  @Override
  public void dispose() {
    spriteBatch.dispose();
    getScreen().dispose();
    try {
      server.dispose();
    } catch (IOException ignored) {}
  }

  @Override
  public void resize(int w, int h) {
  }

  @Override
  public void pause() {
  }

  @Override
  public void resume() {
  }

  /** Registration of classes necessary for serialization of KryoNet */
  public void registerClasses(EndPoint endPoint) {
    endPoint.getKryo().register(ChatMessage.class);
    endPoint.getKryo().register(Player.class);
    endPoint.getKryo().register(java.util.ArrayList.class);
    endPoint.getKryo().register(Message.class);
    endPoint.getKryo().register(HashMap.class);
    endPoint.getKryo().register(Satellite.class);
    endPoint.getKryo().register(SatelliteDTO.class);
    endPoint.getKryo().register(SpaceShipDTO.class);
    endPoint.getKryo().register(Vector2.class);
    endPoint.getKryo().register(BulletDTO.class);
    endPoint.getKryo().register(MousePositionDTO.class);
  }

  /** Function to set up initial game structure */
  public void setupGame() {
    this.uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
    this.spriteBatch = new SpriteBatch();

    this.server = new Server(4000000, 2000000);
    registerClasses(server);

    this.client = new Client(4000000, 2000000);
    registerClasses(client);

    this.player = new Player();

    this.players = new ArrayList<>();
    this.playerList = new List<>(uiSkin);

    this.chat = new Array<>();
    this.chatList = new List<>(uiSkin);
  }

  /**
   * Function to test connection with the server. Doesn't check if Ip is
   * possible.
   *
   * @param client   Kryonet client
   * @param ipString String with ip of server
   * @return true if connection established
   */
  public boolean testServerConnection(Client client, String ipString) {

    try {
      client.connect(5000, ipString, 54555, 54777);
    } catch (Exception e) {
      Log.info("Connection test", "Connection to server failed: " + e.getMessage());
      client.stop();
      return false;
    }
    return true;
  }

  public boolean allPlayersInLobby() {
    for (Player player : players) {
      if (!player.isInLobby()) {
        return false;
      }
    }
    return true;
  }

  public boolean allPlayersInGameScreen() {
    for (Player player : players) {
      if (!player.isInGameScreen()) {
        return false;
      }
    }
    return true;
  }

  public boolean allPlayersLoadedSpaceShips() {
    for (Player player : players) {
      if (!player.areSpaceShipsLoaded()) {
        return false;
      }
    }
    return true;
  }

  public boolean allPlayersLoadedAsteroids() {
    for (Player player : players) {
      if (!player.areAsteroidsLoaded()) {
        return false;
      }
    }
    return true;
  }
}
