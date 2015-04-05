package org.vas.commons.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileLoader {

	protected final String fileLocation;
	protected final ClassLoader classLoader;

	public FileLoader(String file) {
		this(file, FileLoader.class.getClassLoader());
	}

	public FileLoader(String file, ClassLoader classLoader) {
		super();
		this.fileLocation = file;
		this.classLoader = classLoader;
	}

	public InputStream load() throws FileNotFoundException {
		InputStream stream = classLoader.getResourceAsStream(fileLocation);
		if(stream == null) {
			return new FileInputStream(fileLocation);
		}

		return stream;
	}

	public Properties toProperties() throws IOException {
		Properties properties = new Properties();
		properties.load(load());

		return properties;
	}
}
