package com.guilherme.neoasteroid.network;

public class Message<T> {
  private String type;
  private T payload;

  public Message() {
  }

  public void setMessage(String type, T payload) {
    this.type = type;
    this.payload = payload;
  }

  public String getType() {
    return type;
  }

  public T getPayload() {
    return payload;
  }
}
