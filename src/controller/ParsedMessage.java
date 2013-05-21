package controller;

class ParsedMessage {
  public String type;
  public String[] body;
  public ParsedMessage(String type_, String[] body_) {
    type = type_;
    body = body_;
  }
}
