package mame2es.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.commons.lang3.Validate;

/**
 * A readable file system resource
 */
public class FileSystemResource implements ReadableResource {

	private final File file;

	/**
	 * Constructor
	 * @param path the path of the file system resource
	 */
	public FileSystemResource(String path) {
		super();

		Validate.notBlank(path, "The path must not be null nor blank");

		this.file = new File(path);
	}


	@Override
	public InputStream getInputStream() {

		try {
			return file.exists() && file.canRead()
					? new FileInputStream(file)
					: null;
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	@Override
	public Reader getReader(Charset charset) {

		try {
			return file.exists() && file.canRead()
					? new FileReader(file)
					: null;
		} catch (FileNotFoundException e) {
			return null;
		}
	}

}