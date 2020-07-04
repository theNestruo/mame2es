package mame2es.logic.writer.emulationstation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import mame2es.model.Game;

/**
 * Writes a Emulation Station {@code resources/mamenames.xml} file
 */
public class MameNamesWriter {

	public static final String FILENAME = "mamenames.xml";

	private final String xmlPattern = "<game><mamename>%s</mamename><realname>%s</realname></game>";

	private final File file;

	public MameNamesWriter(final File file) {
		super();

		Validate.notNull(file, "The file must not be null");

		this.file = file;
	}

	public void write(final Collection<Game> games) throws IOException {

		try (final BufferedWriter writer = IOUtils.buffer(new FileWriter(this.file))) {
			for (final Game game : games) {
				final String mamename = game.getRomName();
				final String title = game.getShortTitle();
				final String description = game.getDescription();

				final String realname = title.length() == description.length()
						? title
						: String.format("%s<!--%s-->", title, StringUtils.removeStart(description, title));

				writer.write(String.format(this.xmlPattern, mamename, realname));
				writer.newLine();
			}
		}
	}
}
