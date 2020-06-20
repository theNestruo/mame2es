package mame2es.util.writer.emulationstation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import mame2es.model.Game;

public class GameListXmlWriter {

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

		Assert.notNull(file, "The file must not be null");
		Assert.hasText(pathPrefix, "The pathPrefix must not be null nor blank");
		Assert.hasText(imagePrefix, "The imagePrefix must not be null nor blank");
		Assert.hasText(imageSuffix, "The imageSuffix must not be null nor blank");
		Assert.hasText(marqueePrefix, "The marqueePrefix must not be null nor blank");
		Assert.hasText(marqueeSuffix, "The marqueeSuffix must not be null nor blank");

		this.file = file;
		this.pathPrefix = pathPrefix;
		this.imagePrefix = imagePrefix;
		this.imageSuffix = imageSuffix;
		this.marqueePrefix = marqueePrefix;
		this.marqueeSuffix = marqueeSuffix;
	}

	public void write(final Collection<String> filenames, final Map<String, Game> games) throws IOException {

		try (final BufferedWriter writer = IOUtils.buffer(new FileWriter(this.file))) {
			writer.write("<?xml version=\"1.0\"?>");
			writer.newLine();

			writer.write("<gameList>");
			writer.newLine();

			for (final String filename : filenames) {
				writer.write("\t<game>");
				writer.newLine();
				writer.write(String.format("\t\t<path>%s%s</path>", this.pathPrefix, filename));
				writer.newLine();

				final Game game = games.get(FilenameUtils.getBaseName(filename));
				if (game != null) {
					final String name = game.getShortTitle();
					final String desc = game.getDescription();
					final String parentRomName = game.getParentRomName();
//					final String year = game.getYear(); // (not used)
					final String manufacturer = game.getManufacturer();
					final String players = game.getPlayers();

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
				}

				writer.write("\t</game>");
				writer.newLine();
			}

			writer.write("</gameList>");
			writer.newLine();
		}
	}
}
