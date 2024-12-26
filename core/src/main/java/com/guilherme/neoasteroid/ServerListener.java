package com.guilherme.neoasteroid;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class ServerListener extends Listener {
  private Main game;

  public ServerListener(Main game) {
    this.game = game;
  }

  @Override
  public void connected(Connection connection) {
    Log.info("Server", "A client connected: " + connection.getRemoteAddressTCP());
  }

  @Override
  public void disconnected(Connection connection) {
    Log.info("Server", "A client disconnected: " + connection.getRemoteAddressTCP());
  }

  @Override
  public void received(Connection connection, Object object) {
    if (game.getScreen() instanceof GameScreen) {
      handleGameScreenMessage(connection, object);
    } else if (game.getScreen() instanceof LobbyScreen) {
      handleLobbyMessage(connection, object);
    }
  }

  /**
   * Handles all messages coming from the Lobby Screen
   *
   * @param connection
   * @param object     Should always be a Message<?> type
   */
  private void handleLobbyMessage(Connection connection, Object object) {
    if (object instanceof Message<?>) {

      Message<?> message = (Message<?>) object;

      switch (message.getType()) {
        case "player":
          Player player = (Player) message.getPayload();
          game.players.add(player);

          Message<ArrayList<Player>> playerListMessage = new Message<>();
          playerListMessage.setMessage("playersList", game.players);

          this.game.server.sendToAllTCP(playerListMessage);

          Array<String> playerArray = new Array<>();
          for (Player player2 : game.players) {
            playerArray.add(player2.getName());
          }
          game.playerList.setItems(playerArray);

          break;
        case "chatMessage":
          this.game.server.sendToAllExceptTCP(connection.getID(), object);

          ChatMessage chatMessage = (ChatMessage) message.getPayload();
          game.chat.add(chatMessage.toStringChatMessage());
          game.chatList.setItems(game.chat);
          break;

        default:
          break;
      }
    }
  }

  /**
   * Handles all messages coming from the game itself
   *
   * @param connection
   * @param object     Should always be a Message<?> type
   */
  private void handleGameScreenMessage(Connection connection, Object object) {
    GameScreen gameScreen = (GameScreen) game.getScreen();
    SpaceShipDTO playerSpaceShip;

    if (object instanceof Message<?>) {

      Message<?> message = (Message<?>) object;

      switch (message.getType()) {
        case "inGameScreen":
          Log.info("Received inGameScreen from Client");
          String playerInGameScreen = (String) message.getPayload();

          for (Player player3 : game.players) {
            if (player3.getName().equals(playerInGameScreen)) {
              player3.setInGameScreen(true);
            }
          }
          break;

        case "loadComplete":
          Player loadCompletePlayer = (Player) message.getPayload();

          gameScreen.setPlayerLoadingComplete(loadCompletePlayer);
          break;

        case "asteroidsLoaded":
          Player playerAsteroidLoaded = (Player) message.getPayload();

          for (Player player : game.players) {
            if (player.getName().equals(playerAsteroidLoaded.getName())) {
              player.setAsteroidsLoaded(true);
            }
          }
          break;

        case "spaceShipsLoaded":
          Log.info("Received SpaceShipsLoaded message.");
          Player playerSpaceShipLoaded = (Player) message.getPayload();

          for (Player player : game.players) {
            if (player.getName().equals(playerSpaceShipLoaded.getName())) {
              player.setSpaceShipsLoaded(true);
            }
          }
          break;
        case "inputStartUp":
          playerSpaceShip = (SpaceShipDTO)  message.getPayload();

          for (SpaceShip spaceShip : gameScreen.playersSpaceShips) {
            if (spaceShip.getPlayer().getName().equals(playerSpaceShip.getPlayer().getName())) {
              spaceShip.setAccelerating(true);
            }
          }
          break;
        case "inputEndUp":
          playerSpaceShip = (SpaceShipDTO)  message.getPayload();

          for (SpaceShip spaceShip : gameScreen.playersSpaceShips) {
            if (spaceShip.getPlayer().getName().equals(playerSpaceShip.getPlayer().getName())) {
              spaceShip.setAccelerating(false);
            }
          }
          break;
        case "inputStartLeft":
          playerSpaceShip = (SpaceShipDTO)  message.getPayload();

          for (SpaceShip spaceShip : gameScreen.playersSpaceShips) {
            if (spaceShip.getPlayer().getName().equals(playerSpaceShip.getPlayer().getName())) {
              spaceShip.setTurningLeft(true);
            }
          }
          break;
        case "inputEndLeft":
          playerSpaceShip = (SpaceShipDTO)  message.getPayload();

          for (SpaceShip spaceShip : gameScreen.playersSpaceShips) {
            if (spaceShip.getPlayer().getName().equals(playerSpaceShip.getPlayer().getName())) {
              spaceShip.setTurningLeft(false);
            }
          }
          break;
        case "inputStartRight":
          playerSpaceShip = (SpaceShipDTO)  message.getPayload();

          for (SpaceShip spaceShip : gameScreen.playersSpaceShips) {
            if (spaceShip.getPlayer().getName().equals(playerSpaceShip.getPlayer().getName())) {
              spaceShip.setTurningRight(true);
            }
          }
          break;
        case "inputEndRight":
          playerSpaceShip = (SpaceShipDTO)  message.getPayload();

          for (SpaceShip spaceShip : gameScreen.playersSpaceShips) {
            if (spaceShip.getPlayer().getName().equals(playerSpaceShip.getPlayer().getName())) {
              spaceShip.setTurningRight(false);
            }
          }
          break;
        default:
          break;
      }
    }
  }
}
