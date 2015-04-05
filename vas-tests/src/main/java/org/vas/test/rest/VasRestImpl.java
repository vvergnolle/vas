package org.vas.test.rest;

import java.util.Map;

import javax.inject.Inject;

import org.vas.launcher.ServerConf;

public class VasRestImpl extends RestImpl implements VasRest {

	protected String rootUri;
	
	public VasRestImpl() {
	  super();
  }

	VasRestImpl(String rootUri, Map<String, String> defaultHeaders) {
	  super(defaultHeaders);
	  this.rootUri = rootUri;
  }

	@Inject
	public void setup(ServerConf conf) {
		rootUri = "http://" + conf.host + ":" + conf.port + "/vas";
	}

	/**
	 * Force Vas restful api base url
	 *
	 */
	@Override
	protected String formattedUri(String baseUri, Object... args) {
	  String uri = super.formattedUri(baseUri, args);
	  
	  /**
	   * The uri isnt' relative - don't touch it
	   */
	  if(uri.startsWith("http://") || uri.startsWith("https://")) {
	  	return uri;
	  }
	  
	  if(uri.startsWith("/")) {
	  	return rootUri + uri;
	  }
	  else {
	  	return rootUri + "/" + uri;
	  }
	}

	@Override
	protected RestImpl createRestBasicInstance(Map<String, String> map) {
		return new VasRestImpl(rootUri, map);
	}
}