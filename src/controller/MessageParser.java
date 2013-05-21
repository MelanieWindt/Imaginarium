package controller;

import java.util.Arrays;

class MessageParser {
  static ParsedMessage parse(String message) throws Exception {
    String [] spliced = message.split("\\|");
    if (spliced.length < 2 || spliced[0].length() == 0) {
      throw new Exception("Wrong message: " + message);
    } else {
      final String type = spliced[0];
      final String[] body = Arrays.copyOfRange(spliced, 1, spliced.length);
      return new ParsedMessage(type, body);
    }
  }

  static public String toMessage(ParsedMessage message) {
    return toMessage(message.type, message.body);
  }

  static String toMessage(String type, String[] body) {
    String result = type;
    for (int i = 0; i < body.length; ++i) {
      result += "|" + body[i];
    }
    result += "\n";
    return result;
  }

  static String toMessage(String type, String arg1) {
    return type + "|" + arg1 + "\n";
  }

  static String toMessage(String type, String arg1, String arg2) {
    return type + "|" + arg1 + "|" + arg2 + "\n";
  }

  static String toMessage(String type, String arg1, String arg2, String arg3) {
    return type + "|" + arg1 + "|" + arg2 + "|" + arg3 + "\n";
  }
}
