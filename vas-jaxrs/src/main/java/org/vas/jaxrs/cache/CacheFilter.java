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
package org.vas.jaxrs.cache;

import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vas.http.resource.cache.EtagCache;
import org.vas.http.resource.cache.EtagCache.EtagCachePolicy;

public class CacheFilter implements Filter {

  private static final String CONTROLS = "no-cache, private";

  private final EtagCache etagCache;

  public CacheFilter(EtagCache etagCache) {
    super();
    this.etagCache = etagCache;
  }

  @Override
  public void destroy() {}

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
    ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;

    /*
     * Check request etag
     */

    String uri = req.getRequestURI();
    String etag = req.getHeader(Headers.IF_NONE_MATCH_STRING);
    if(etag != null && etagCache.matches(etag, req.getRequestURI())) {
      // Return a not modified response
      resp.setStatus(StatusCodes.NOT_MODIFIED);
      resp.setHeader(Headers.CACHE_CONTROL_STRING, CONTROLS);
      resp.setContentLength(0);

      return;
    }

    /*
     * Continue
     */

    EtagCachePolicy etagCachePolicy = etagCache.cachePolicy();
    String cachedEtag = etagCache.getOrCreate(uri);
    resp.setIntHeader(Headers.EXPIRES_STRING, 0);
    resp.setHeader(Headers.ETAG_STRING, cachedEtag);
    resp.setHeader(Headers.CACHE_CONTROL_STRING, "max-age=" + etagCachePolicy.toSeconds() + ", " + CONTROLS);
    chain.doFilter(request, response);
  }

}
