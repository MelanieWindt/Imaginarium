package controller;

import common.EventHandler;

class EventFactory {
  private EventHandler<Integer> newConnection;
  public void setOnNewConnection(EventHandler<Integer> newConnection) {
    this.newConnection = newConnection;
  }

  public void processMessage(ParsedMessage message, Integer from) {
  }

  public void newConnection(Integer from) {
  }

  public void errorMessage(String msg, Integer from) {
  }
}
