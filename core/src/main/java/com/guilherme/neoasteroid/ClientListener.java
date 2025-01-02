package com.guilherme.neoasteroid;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.guilherme.neoasteroid.spaceship.SpaceShip;

public class ClientListener extends Listener {
  private Main game;

  public ClientListener(Main game) {
    this.game = game;
  }

  @Override
  public void connected(Connection connection) {
  }

  @Override
  public void disconnected(Connection connection) {
  }

  @Override
  public void received(Connection connection, Object object) {
    GameScreen gameScreen;
    SpaceShipDTO spaceShipDTO;

    if (object instanceof Message<?>) {

      Message<?> message = (Message<?>) object;

      switch (message.getType()) {
        case "chatMessage":
          ChatMessage chatMessage = (ChatMessage) message.getPayload();

          game.chat.add(chatMessage.toStringChatMessage());
          game.chatList.setItems(game.chat);
          break;

        case "playersList":
          @SuppressWarnings("unchecked")
          ArrayList<Player> players = (ArrayList<Player>) message.getPayload();

          this.game.players = players;

          Array<String> playerArray = new Array<>();
          for (Player player : game.players) {
            playerArray.add(player.getName());
          }
          game.playerList.setItems(playerArray);
          break;

        case "gameStart":
          Gdx.app.postRunnable(() -> {
            game.setScreen(new GameScreen(game));
          });
          break;

        case "asteroidList":
          @SuppressWarnings("unchecked")
          ArrayList<SatelliteDTO> satelliteList = (ArrayList<SatelliteDTO>) message.getPayload();

          gameScreen = (GameScreen) game.getScreen();

          for (SatelliteDTO satelliteDTO : satelliteList) {
            int id = satelliteDTO.getId();

            if (gameScreen.satellites.containsKey(id)) {
              Satellite existingSatellite = gameScreen.satellites.get(id);
              existingSatellite.updateFromDTO(satelliteDTO);
            } else {
              gameScreen.satellites.put(satelliteDTO.getId(),
                  new Satellite(gameScreen.world, satelliteDTO, gameScreen.satelliteTexture));
            }
          }
          break;

        case "newAsteroid":
          gameScreen = (GameScreen) game.getScreen();
          Satellite satellite = new Satellite(gameScreen.world, (SatelliteDTO) message.getPayload(),
              gameScreen.satelliteTexture);

          gameScreen.satellites.put(satellite.getId(), satellite);

          // TODO Checking for total number of asteroids. Need to change to get number of
          // asteroids from outside. Will create problems when adding or deleting
          // asteroids
          if (gameScreen.satellites.size() == 1000 && !game.player.areAsteroidsLoaded()) {
            Message<Player> loadCompleteMessage = new Message<>();
            loadCompleteMessage.setMessage("asteroidsLoaded", game.player);

            game.client.sendTCP(loadCompleteMessage);
            game.player.setAsteroidsLoaded(true);

            Log.info("Load Complete!");
          }
          break;
        case "updateAsteroid":
          gameScreen = (GameScreen) game.getScreen();
          SatelliteDTO updateSatellite = (SatelliteDTO) message.getPayload();

          gameScreen.satellites.get(updateSatellite.getId()).updateFromDTO(updateSatellite);
        break;

        case "allPlayersLoaded":
          gameScreen = (GameScreen) game.getScreen();
          gameScreen.allPlayersLoadingComplete = true;
          break;

        case "newSpaceShip":
          spaceShipDTO = (SpaceShipDTO) message.getPayload();

          gameScreen = (GameScreen) game.getScreen();

          Gdx.app.postRunnable(() -> {
            gameScreen.playersSpaceShips.add(new SpaceShip(gameScreen.world, spaceShipDTO));

            if (gameScreen.playersSpaceShips.size() == game.players.size()) {

              for (SpaceShip spaceShip : gameScreen.playersSpaceShips) {
                if (spaceShip.getPlayer().getName().equals(game.player.getName())) {
                  gameScreen.playerShip = spaceShip;
                }
              }

              gameScreen.camera.position.set(gameScreen.playerShip.getPosition(), 0);
              gameScreen.camera.update();

              game.player.setSpaceShipsLoaded(true);
              Message<Player> spaceShipsLoadedMessage = new Message<>();
              spaceShipsLoadedMessage.setMessage("spaceShipsLoaded", game.player);
              game.client.sendTCP(spaceShipsLoadedMessage);
              Log.info("SpaceShips Loaded");
            }
          });

          break;

        case "updateShips":
          spaceShipDTO = (SpaceShipDTO) message.getPayload();
          gameScreen = (GameScreen) game.getScreen();

          Gdx.app.postRunnable(() -> {
            for (SpaceShip spaceShip : gameScreen.playersSpaceShips) {
              if (spaceShip.getPlayer().getName().equals(spaceShipDTO.getPlayer().getName())) {
                spaceShip.setBodyPosition(spaceShipDTO.getPosition(), spaceShipDTO.getAngle(), spaceShipDTO.getLinearVelocity());
              }
            }
          });
          break;
        case "newBullet":
          BulletDTO bulletDTO = (BulletDTO) message.getPayload();
          gameScreen = (GameScreen) game.getScreen();

          Gdx.app.postRunnable(() -> {
            gameScreen.bullets.add(new Bullet(
              gameScreen.world,
              bulletDTO.getDirection(),
              bulletDTO.getOrigin(),
              gameScreen.tracerBulletTexture,
              30.0f * 2,
              10,
              2.0f * 2,
              0.6f * 2));
          });

        default:
          break;
      }
    }
  }
}
