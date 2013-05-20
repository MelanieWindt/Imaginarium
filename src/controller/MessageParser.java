package controller;

import java.util.Arrays;

class MessageParser {
  static ParsedMessage parse(String message) throws Exception {
    String [] spliced = message.split(":");
    if (spliced.length < 2 || spliced[0].length() != 1) {
      throw new Exception("Wrong message receive! " + message);
    } else {
      final char type = spliced[0].charAt(0);
      final String[] body = Arrays.copyOfRange(spliced, 1, spliced.length - 1);
      return new ParsedMessage(type, body);
    }
  }
}

