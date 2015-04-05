package org.vas.launcher;

public class Env {
	
	public enum Profile {
		SERVER, RUNTIME
	}

	public final String propertiesLocation;
	public final Profile profile;

	public Env() {
		super();
		this.propertiesLocation = System.getProperty(Const.CONF_PROPERTY, Const.DEFAULT_CONF);
		this.profile = Profile.SERVER;
	}

	public Env(String propertiesLocation, Profile profile) {
		super();
		this.propertiesLocation = propertiesLocation;
		this.profile = profile;
	}
}
