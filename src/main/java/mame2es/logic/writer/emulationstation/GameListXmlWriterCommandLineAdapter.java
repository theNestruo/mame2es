package mame2es.logic.writer.emulationstation;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

/**
 * Apache Commons CLI {@link CommandLine} adapter for {@link GameListXmlWriter}
 */
public class GameListXmlWriterCommandLineAdapter {

	private static final String PATHPREFIX = "pathprefix";
	private static final String IMAGEPREFIX = "imageprefix";
	private static final String IMAGESUFFIX = "imagesuffix";
	private static final String MARQUEEPREFIX = "marqueeprefix";
	private static final String MARQUEESUFFIX = "marqueesuffix";

	public static Options options() {

		final Options options = new Options();

		options.addOption(PATHPREFIX, true, "Emulation Station gamelist.xml <path> tag prefix (optional)");
		options.addOption(IMAGEPREFIX, true, "Emulation Station gamelist.xml <image> tag prefix (optional)");
		options.addOption(IMAGESUFFIX, true, "Emulation Station gamelist.xml <image> tag suffix (optional)");
		options.addOption(MARQUEEPREFIX, true, "Emulation Station gamelist.xml <marquee> tag prefix (optional)");
		options.addOption(MARQUEESUFFIX, true, "Emulation Station gamelist.xml <marquee> tag suffix (optional)");

		return options;
	}

	public static GameListXmlWriter from(final File file, final CommandLine command) {

		return new GameListXmlWriter(file,
				command.getOptionValue(PATHPREFIX, "./"),
				command.getOptionValue(IMAGEPREFIX, "~/.emulationstation/art/images/"),
				command.getOptionValue(IMAGESUFFIX, ".png"),
				command.getOptionValue(MARQUEEPREFIX, "~/.emulationstation/art/marquees/"),
				command.getOptionValue(MARQUEESUFFIX, ".png"));
	}

	private GameListXmlWriterCommandLineAdapter() {
		super();
	}
}

