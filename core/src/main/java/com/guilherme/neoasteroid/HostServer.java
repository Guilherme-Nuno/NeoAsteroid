package com.guilherme.neoasteroid;

import java.util.ArrayList;

import com.esotericsoftware.minlog.Log;
import com.guilherme.neoasteroid.spaceship.SpaceShip;

public class HostServer {
  private Main game;

  public HostServer(Main game) {
    this.game = game;
  }

  /**
   * Sends a satelliteDTO to all clients connected.
   *
   * @param satellite
   */
  public void sendNewAsteroid(Satellite satellite) {
    SatelliteDTO satelliteDTO = new SatelliteDTO(satellite);

    Message<SatelliteDTO> sendAsteroidMessage = new Message<>();
    sendAsteroidMessage.setMessage("newAsteroid", satelliteDTO);

    game.server.sendToAllTCP(sendAsteroidMessage);
  }

  /**
   * Sends a message to update a particular asteroid on all clients. Should be
   * sent when something happens to any particular asteroid like a contact or hit
   * by a bullet.
   *
   * @param satellite
   */
  public void sendUpdateAsteroid(Satellite satellite) {
    SatelliteDTO satelliteDTO = new SatelliteDTO(satellite);

    Message<SatelliteDTO> sendAsteroidMessage = new Message<>();
    sendAsteroidMessage.setMessage("updateAsteroid", satelliteDTO);
  }

  public void sendAsteroidList() {
    ArrayList<SatelliteDTO> asteroidDTOList = new ArrayList<>();
    GameScreen gameScreen = (GameScreen) game.getScreen();

    for (Satellite satellite : gameScreen.satellites.values()) {
      SatelliteDTO satelliteDTO = new SatelliteDTO(satellite);
      asteroidDTOList.add(satelliteDTO);
    }

    Message<ArrayList<SatelliteDTO>> asteroidListMessage = new Message<>();
    asteroidListMessage.setMessage("asteroidList", asteroidDTOList);

    game.server.sendToAllTCP(asteroidListMessage);
  }

  /**
   * Message sent to all players to create a new SpaceShip for a player.
   *
   * @param spaceShip
   */
  public void sendNewSpaceShip(SpaceShip spaceShip) {
    SpaceShipDTO spaceShipDTO = new SpaceShipDTO(spaceShip);

    Message<SpaceShipDTO> newSpaceShipMessage = new Message<>();
    newSpaceShipMessage.setMessage("newSpaceShip", spaceShipDTO);

    game.server.sendToAllTCP(newSpaceShipMessage);
    Log.info("Sent SpaceShip info to clients!");
  }

  public void sendStartGameScreen() {
    Message<String> gameStartMessage = new Message<>();
    gameStartMessage.setMessage("gameStart", "gameStart");
    game.server.sendToAllTCP(gameStartMessage);
  }

  public void sendAllPlayersLoaded() {
    Message<String> allPlayersLoadedMessage = new Message<>();
    allPlayersLoadedMessage.setMessage("allPlayersLoaded", "");
    game.server.sendToAllTCP(allPlayersLoadedMessage);
  }

  public void updateAllPlayersPosition() {
    GameScreen gameScreen = (GameScreen) game.getScreen();

    Message<SpaceShipDTO> updateShipsMessage = new Message<>();

    for (SpaceShip spaceShip : gameScreen.playersSpaceShips) {
      updateShipsMessage.setMessage("updateShips", new SpaceShipDTO(spaceShip));
      game.server.sendToAllTCP(updateShipsMessage);
    }
  }
}
