package org.vas.launcher;

public class VasFactory {

	private VasFactory() {}

	public static Vas create() {
		return new VasImpl();
	}

	public static Vas create(ServerConf conf) {
		return new VasImpl(conf);
	}

	public static Vas create(String host, int port) {
		return new VasImpl(new ServerConf(host, port));
	}
}
