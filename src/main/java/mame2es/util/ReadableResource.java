package mame2es.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

/**
 * A readable resource
 */
public interface ReadableResource {

	InputStream getInputStream();

	default Reader getReader() {
		return this.getReader(StandardCharsets.UTF_8);
	}

	Reader getReader(Charset charset);

	default BufferedReader getBufferedReader() {
		return IOUtils.buffer(this.getReader());
	}

	default BufferedReader getBufferedReader(Charset charset) {
		return IOUtils.buffer(this.getReader(charset));
	}
}