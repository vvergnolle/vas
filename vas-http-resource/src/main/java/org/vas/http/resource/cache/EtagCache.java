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
package org.vas.http.resource.cache;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public interface EtagCache {

  public static class EtagCachePolicy {

    public final int duration;
    public final TimeUnit timeUnit;

    public EtagCachePolicy(Properties properties) {
      super();
      duration = Integer.valueOf(properties.getProperty("vas.http.etag.cache.duration", "1"));
      timeUnit = TimeUnit.valueOf(properties.getProperty("vas.http.etag.cache.timeUnit", "hours").toUpperCase());
    }

    public long toSeconds() {
      return timeUnit.toSeconds(duration);
    }
  }

  EtagCachePolicy cachePolicy();

  /**
   * ETag for this uri
   */
  String get(String uri);

  /**
   * Call get first and if call the return of refresh
   * 
   * @see EtagCache#get(String)
   * @see EtagCache#refresh(String)
   */
  String getOrCreate(String uri);

  /**
   * Check if the passed etag equals to the cached etag for this uri
   */
  boolean matches(String etag, String uri);

  /**
   * Refresh the ETag for this uri
   * 
   * @return the generated etag
   */
  String refresh(String uri);
}
