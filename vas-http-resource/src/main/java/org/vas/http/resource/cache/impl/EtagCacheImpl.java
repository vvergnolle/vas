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
package org.vas.http.resource.cache.impl;

import org.vas.http.resource.cache.EtagCache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class EtagCacheImpl implements EtagCache {

  private final EtagCachePolicy policy;
  private final Cache<String, String> cache;

  public EtagCacheImpl(EtagCachePolicy policy) {
    super();
    this.policy = policy;
    this.cache = CacheBuilder.newBuilder().softValues().expireAfterWrite(policy.duration, policy.timeUnit).build();
  }

  @Override
  public EtagCachePolicy cachePolicy() {
    return policy;
  }

  @Override
  public String get(String uri) {
    return cache.getIfPresent(uri);
  }

  @Override
  public String getOrCreate(String uri) {
    String etag = get(uri);
    if(etag == null) {
      etag = refresh(uri);
    }

    return etag;
  }

  @Override
  public boolean matches(String etag, String uri) {
    if(etag == null) {
      return false;
    }

    return etag.equals(get(uri));
  }

  @Override
  public String refresh(String uri) {
    String etag = String.valueOf(System.nanoTime());
    cache.put(uri, etag);

    return etag;
  }
}
