package org.vas.commons.messages;

public interface MessageHelper {

  MessageHelper switchTo(String lang);

  String get(String key, Object... params);
}
