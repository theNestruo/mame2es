package mame2es.logic.writer.emulationstation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import mame2es.model.Game;

/**
 * Writes a Emulation Station {@code resources/mamenames.xml} file
 */
public class MameNamesWriter {

	public static final String FILENAME = "mamenames.xml";

	private static final String XML_PATTERN = "<game><mamename>%s</mamename><realname>%s</realname></game>";

	private final File file;

	public MameNamesWriter(final File file) {
		super();

		this.file = Validate.notNull(file, "The file must not be null");
	}

	public void write(final Collection<Game> games) throws IOException {

		try (final BufferedWriter writer = IOUtils.buffer(new FileWriterWithEncoding(this.file, StandardCharsets.UTF_8))) {
			for (final Game game : games) {
				final String mamename = game.getRomName();
				final String title = game.getShortTitle();
				final String description = game.getDescription();

				final String realname = title.length() == description.length()
						? title
						: String.format("%s<!--%s-->", title, StringUtils.removeStart(description, title));

				writer.write(String.format(XML_PATTERN, mamename, realname));
				writer.newLine();
			}
		}
	}
}
