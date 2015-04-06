package org.vas.domain.repository.test;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.vas.domain.repository.User;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public final class YamlFixtureDump {

  public static void main(String[] args) {
    Writer writer = new StringWriter();
    new Yaml(options()).dump(users(), writer);

    System.out.println(writer.toString());
    IOUtils.closeQuietly(writer);
  }

  private static DumperOptions options() {
    DumperOptions options = new DumperOptions();
    options.setIndent(4);
    options.setPrettyFlow(true);

    return options;
  }

  static List<Object> users() {
    User user = new User();
    user.username = "root";
    user.password = "root";

    User user2 = new User();
    user2.username = "vincent";
    user2.password = "vincent";

    return Arrays.asList(user, user2);
  }
}
