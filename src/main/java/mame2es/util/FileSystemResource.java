package mame2es.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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

		this.file = new File(Validate.notBlank(path, "The path must not be null nor blank"));
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
}