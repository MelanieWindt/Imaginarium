package controller;

class ParsedMessage {
  public char type;
  public String[] body;
  public ParsedMessage(char type_, String[] body_) {
    type = type_;
    body = body_;
  }
}
