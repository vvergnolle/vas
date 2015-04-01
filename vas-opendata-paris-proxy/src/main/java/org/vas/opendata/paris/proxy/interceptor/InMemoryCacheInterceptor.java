package org.vas.opendata.paris.proxy.interceptor;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.codec.digest.DigestUtils;
import org.vas.http.resource.HttpResourceInterceptor;
import org.vas.http.resource.HttpResponse;

public class InMemoryCacheInterceptor implements HttpResourceInterceptor {

	protected final Map<String, Reference<byte[]>> cachedBytes = new HashMap<>();

	@Override
	public HttpResponse intercept(Method method, String url, Supplier<HttpResponse> value) {
		String key = key(url);
		byte[] bytes = null;

		Reference<byte[]> ref = cachedBytes.get(key);

		if (ref == null) {
			HttpResponse response = value.get();
			bytes = response.bytes();
			ref = new WeakReference<>(bytes);
			cachedBytes.put(key, ref);
			return response;
		} else {
			bytes = ref.get();

			if (bytes == null) {
				HttpResponse response = value.get();
				bytes = response.bytes();
				ref = new WeakReference<>(bytes);
				cachedBytes.put(key, ref);
				return response;
			}
		}

		return new HttpResponse(bytes);
	}

	protected String key(String url) {
		return DigestUtils.sha1Hex(url);
	}
}
