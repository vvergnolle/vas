/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Vincent Vergnolle
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.vas.domain.repository.fixture;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.domain.repository.User;
import org.vas.domain.repository.UserRepository;
import org.yaml.snakeyaml.Yaml;

public class UserFixtureLoader {

  protected final Logger logger = LoggerFactory.getLogger(UserFixtureLoader.class);
  protected Yaml yaml = new Yaml();

  @SuppressWarnings("unchecked")
  public void load(InputStream in, UserRepository userRepository) throws Exception {
    if(logger.isDebugEnabled()) {
      logger.debug("Load default users");
    }

    Iterable<Object> iterable = (Iterable<Object>) yaml.load(in);
    userRepository.callBatchTasks(() -> {
      iterable.forEach(o -> {
        User user = (User) o;
        user.hash();

        try {
          userRepository.create(user);

          if(logger.isDebugEnabled()) {
            logger.debug("User created : {}", user.username);
          }
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });

      return null;
    });
  }
}
