package com.guilherme.neoasteroid;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

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
    if (object instanceof Message<?>) {

      Message<?> message = (Message<?>) object;

      switch (message.getType()) {
        case "chatMessage":
          Log.info("Recebi");
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

          GameScreen gameScreen = (GameScreen) game.getScreen();

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
          GameScreen gameScreen2 = (GameScreen) game.getScreen();
          Satellite satellite = new Satellite(gameScreen2.world, (SatelliteDTO) message.getPayload(),
              gameScreen2.satelliteTexture);

          gameScreen2.satellites.put(satellite.getId(), satellite);

          // TODO Checking for total number of asteroids. Need to change to get number of
          // asteroids from outside.
          if (gameScreen2.satellites.size() == 1000) {
            Message<Player> loadCompleteMessage = new Message<>();
            loadCompleteMessage.setMessage("asteroidsLoaded", game.player);

            game.client.sendTCP(loadCompleteMessage);

            Log.info("Load Complete!");
          }
          break;

        case "allPlayersLoaded":
          GameScreen gameScreen3 = (GameScreen) game.getScreen();
          gameScreen3.allPlayersLoadingComplete = true;
          break;

        case "newSpaceShip":
          Log.info("Received SpaceShip from server!");
          SpaceShipDTO spaceShipDTO = (SpaceShipDTO) message.getPayload();

          GameScreen gameScreen4 = (GameScreen) game.getScreen();

          Gdx.app.postRunnable(() -> {
            gameScreen4.playersSpaceShips.add(new SpaceShip(gameScreen4.world, spaceShipDTO));

            if (gameScreen4.playersSpaceShips.size() == game.players.size()) {

              for (SpaceShip spaceShip : gameScreen4.playersSpaceShips) {
                if (spaceShip.getPlayer().getName().equals(game.player.getName())) {
                  gameScreen4.playerShip = spaceShip;
                }
              }

              gameScreen4.camera.position.set(gameScreen4.playerShip.getPosition(), 0);
              gameScreen4.camera.update();

              game.player.setSpaceShipsLoaded(true);
              Message<Player> spaceShipsLoadedMessage = new Message<>();
              spaceShipsLoadedMessage.setMessage("spaceShipsLoaded", game.player);
              game.client.sendTCP(spaceShipsLoadedMessage);
              Log.info("SpaceShips Loaded");
            }
          });

          break;

        default:
          break;
      }
    }
  }
}
