package mame2es.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

/**
 * A readable resource
 */
public interface ReadableResource {

	InputStream getInputStream();

	default BufferedReader getBufferedReader(Charset charset) {

		final InputStream is = getInputStream();
		return is != null
				? IOUtils.buffer(new InputStreamReader(is, charset))
				: null;
	}
}