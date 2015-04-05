package org.vas.launcher;

public class ServerConf {

	public final int port;
	public final String host;
	public final int ioThreads;
	public final int bufferSize;

	public ServerConf() {
		this("localhost", 8080, 4, 8096);
	}

	public ServerConf(String host, int port) {
		this(host, port, 4, 8096);
	}

	public ServerConf(int ioThreads, int bufferSize) {
		this("localhost", 8080, ioThreads, bufferSize);
	}

	public ServerConf(String host, int port, int ioThreads, int bufferSize) {
		super();
		this.host = host;
		this.port = port;
		this.ioThreads = ioThreads;
		this.bufferSize = bufferSize;
	}
}
