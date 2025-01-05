package com.guilherme.neoasteroid.lobby;

import com.guilherme.neoasteroid.Main;

public class ChatMessage {
  public String playerName;
  public String message;

  public ChatMessage() {
  }

  public void setAll(Main game, String message) {
    playerName = game.player.getName();
    this.message = message;
  }

  public void setName(Main game) {
    playerName = game.player.getName();
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String toStringChatMessage() {
    return playerName + ": " + message;
  }
}
