package mame2es.logic.writer.emulationstation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import mame2es.model.CurrentGame;
import mame2es.model.Game;

/**
 * Writes a Emulation Station {@code <system>/gamelist.xml} file
 */
public class GameListXmlWriter {

	public static final String FILENAME = "gamelist.xml";

	private final File file;

	private final String pathPrefix;

	private final String imagePrefix;

	private final String imageSuffix;

	private final String marqueePrefix;

	private final String marqueeSuffix;

	public GameListXmlWriter(
			final File file,
			final String pathPrefix,
			final String imagePrefix,
			final String imageSuffix,
			final String marqueePrefix,
			final String marqueeSuffix) {
		super();

		this.file = Validate.notNull(file, "The file must not be null");
		this.pathPrefix = Validate.notBlank(pathPrefix, "The pathPrefix must not be null nor blank");
		this.imagePrefix = Validate.notBlank(imagePrefix, "The imagePrefix must not be null nor blank");
		this.imageSuffix = Validate.notBlank(imageSuffix, "The imageSuffix must not be null nor blank");
		this.marqueePrefix = Validate.notBlank(marqueePrefix, "The marqueePrefix must not be null nor blank");
		this.marqueeSuffix = Validate.notBlank(marqueeSuffix, "The marqueeSuffix must not be null nor blank");
	}

	public void write(final Collection<CurrentGame> currentGames) throws IOException {

		try (final BufferedWriter writer = IOUtils.buffer(new FileWriterWithEncoding(this.file, StandardCharsets.UTF_8))) {
			writer.write("<?xml version=\"1.0\"?>");
			writer.newLine();

			writer.write("<gameList>");
			writer.newLine();

			for (final CurrentGame currentGame : currentGames) {
				final Game game = currentGame.getGame();

				final String relativePath = FilenameUtils.separatorsToUnix(currentGame.getRelativePath());
				final String name = game.getShortTitle();
				final String desc = game.getDescription();
				final String parentRomName = game.getParentRomName();
//				final String year = game.getYear(); // (not used)
				final String manufacturer = game.getManufacturer();
				final String players = game.getPlayers();
				final String genre = game.getCategory();

				writer.write("\t<game>");
				writer.newLine();
				writer.write(String.format("\t\t<path>%s%s</path>", this.pathPrefix, relativePath));
				writer.newLine();
				writer.write(String.format("\t\t<name>%s</name>", name));
				writer.newLine();
				writer.write(String.format("\t\t<desc>%s</desc>", desc));
				writer.newLine();
				writer.write(String.format("\t\t<image>%s%s%s</image>", this.imagePrefix, parentRomName, this.imageSuffix));
				writer.newLine();
				writer.write(String.format("\t\t<marquee>%s%s%s</marquee>", this.marqueePrefix, parentRomName, this.marqueeSuffix));
				writer.newLine();
				if (StringUtils.isNotBlank(manufacturer)) {
					writer.write(String.format("\t\t<developer>%s</developer>", manufacturer));
					writer.newLine();
				}
				if (StringUtils.isNotBlank(players)) {
					writer.write(String.format("\t\t<players>%s</players>", players));
					writer.newLine();
				}
				if (StringUtils.isNotBlank(genre)) {
					writer.write(String.format("\t\t<genre>%s</genre>", genre));
					writer.newLine();
				}
				writer.write("\t</game>");
				writer.newLine();
			}

			writer.write("</gameList>");
			writer.newLine();
		}
	}
}
