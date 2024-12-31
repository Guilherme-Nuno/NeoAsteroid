package com.guilherme.neoasteroid;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class LobbyScreen implements Screen {
  private final Main game;
  private final Stage stage;

  public LobbyScreen(Main game) {
    this.game = game;
    stage = new Stage();

    stage.setDebugAll(true);

    if (this.game.player.isHost()) {
      // Inicializa a lista de jogadores e exibe no Lobby
      this.game.players.add(this.game.player);
      String playerName = this.game.players.get(0).getName();
      this.game.playerList.setItems(playerName);

      this.game.server.start();
      try {
        this.game.server.bind(54555, 54777);
      } catch (IOException e) {
        e.printStackTrace();
        this.game.server.stop();
        this.game.setScreen(new MainMenuScreen(game));
        return;
      }

      this.game.server.addListener(new ServerListener(game));
    } else {
      this.game.client.addListener(new ClientListener(this.game));
      Message<Player> playerMessage = new Message<Player>();
      playerMessage.setMessage("player", game.player);
      this.game.client.sendTCP(playerMessage);
    }

    // Tabela para organizar elementos na tela
    Table table = new Table();
    table.setFillParent(true);
    stage.addActor(table);

    ScrollPane chatPane = new ScrollPane(this.game.chatList, this.game.uiSkin);

    TextField messageTextField = new TextField("", this.game.uiSkin);

    TextButton sendMessageButton = new TextButton("Send", this.game.uiSkin);
    sendMessageButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        String message = messageTextField.getText();

        if (!message.isEmpty()) {
          ChatMessage chatMessage = new ChatMessage();
          chatMessage.setAll(game, message);

          Message<ChatMessage> messageChatMessage = new Message<ChatMessage>();
          messageChatMessage.setMessage("chatMessage", chatMessage);

          if (game.player.isHost()) {
            game.server.sendToAllTCP(messageChatMessage);
          } else {
            game.client.sendTCP(messageChatMessage);
          }
          game.chat.add(chatMessage.toStringChatMessage());
          game.chatList.setItems(game.chat);
          messageTextField.setText("");
        }
      }
    });

    Table chatMessageTable = new Table();
    chatMessageTable.add(messageTextField).width(340).height(30).padRight(10);
    chatMessageTable.add(sendMessageButton).width(50).height(30);

    Table chatTable = new Table();
    chatTable.add(chatPane).width(400).height(360).padRight(20).padBottom(10);
    chatTable.row();
    chatTable.add(chatMessageTable);

    ScrollPane scrollPane = new ScrollPane(this.game.playerList, this.game.uiSkin);

    // Botão para sair do lobby
    TextButton exitButton = new TextButton("Exit Lobby", this.game.uiSkin);
    exitButton.addListener(new ClickListener() {

      @Override
      public void clicked(InputEvent event, float x, float y) {
        // Adicione a lógica para sair do lobby
        Gdx.app.log("Lobby", "Saindo do lobby...");
        // Mudar para a tela principal ou a tela anterior
        if (game.player.isHost()) {
          game.server.stop();
        } else {
          game.client.stop();
        }
        game.setScreen(new MainMenuScreen(game));
      }
    });

    // Botão para começar o jogo (visível apenas para o host)
    TextButton startGameButton = new TextButton("Start Game", this.game.uiSkin);
    startGameButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        // Adicione a lógica para iniciar o jogo
        Gdx.app.log("Lobby", "Iniciando o jogo...");

        game.setScreen(new GameScreen(game));
      }
    });

    // Labels e organização dos elementos na tabela
    table.add(new Label("Chat", this.game.uiSkin)).center().padBottom(20);
    table.add(new Label("Players", this.game.uiSkin)).center().padBottom(20);
    table.row();
    table.add(chatTable).width(400).height(400).padBottom(20);
    table.add(scrollPane).width(200).height(400).padBottom(20);
    table.row();

    Table buttonTable = new Table();
    if (this.game.player.isHost()) {
      buttonTable.add(startGameButton).width(200).height(50).left();
    }
    buttonTable.add(exitButton).width(200).height(50).right();
    table.add(buttonTable).colspan(2).center().padBottom(20);

    Gdx.input.setInputProcessor(stage);
  }

  @Override
  public void show() {
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    stage.act(delta);
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void pause() {
  }

  @Override
  public void resume() {
  }

  @Override
  public void hide() {
  }

  @Override
  public void dispose() {
    stage.dispose();
    this.game.uiSkin.dispose();
  }
}
