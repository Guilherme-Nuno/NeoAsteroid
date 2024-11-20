package com.guilherme.neoasteroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class JoinScreen implements Screen {
  private Main game;
  private Stage stage;

  public JoinScreen(Main game) {
    this.game = game;
    this.stage = new Stage(new ScreenViewport());
    Gdx.input.setInputProcessor(stage);

    TextField getIpTextField = new TextField("", this.game.skin);

    TextButton connectIPButton = new TextButton("Connect", this.game.skin);
    connectIPButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        game.client.start();
        boolean connectionSuccessful = game.testServerConnection(game.client, getIpTextField.getText());

        if (connectionSuccessful) {
          game.setScreen(new LobbyScreen(game));
        } else {
          // TODO Add Connection unsuccessfull
        }
      }
    });

    Table joinTable = new Table();
    joinTable.setFillParent(true);
    joinTable.add(getIpTextField).width(200).height(50).padBottom(20);
    joinTable.row();
    joinTable.add(connectIPButton);
    stage.addActor(joinTable);
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
  }

}
