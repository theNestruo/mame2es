package mame2es.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.commons.lang3.Validate;

/**
 * A readable classpath resource
 */
public class ClassPathResource implements ReadableResource {

	private final String path;

	/**
	 * Constructor
	 * @param path the path of the classpath resource
	 * @throws IOException if the classpath resource does not exists
	 */
	public ClassPathResource(String path) {
		super();

		Validate.notBlank(path, "The path must not be null nor blank");

		this.path = path;

		// Checks existence
		try (InputStream is = this.getInputStream()) {
			// (no-op)
		} catch (IOException e) {

		}
	}

	@Override
	public InputStream getInputStream() {

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		return classLoader.getResourceAsStream(path);
	}

	@Override
	public Reader getReader(Charset charset) {

		return new InputStreamReader(this.getInputStream(), charset);
	}
}