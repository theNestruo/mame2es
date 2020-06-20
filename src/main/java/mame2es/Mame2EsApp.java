package mame2es;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;

import mame2es.model.Game;
import mame2es.util.reader.GamesReader;
import mame2es.util.writer.emulationstation.GameListXmlWriter;
import mame2es.util.writer.emulationstation.MameNamesWriter;

public class Mame2EsApp {

	private static final String IN_GAMELIST = "gamelist";
	private static final String IN_LISTXML = "listxml";
	private static final String IN_MAMEBIOSES = "mamebioses";
	private static final String IN_MAMEDEVICES = "mamedevices";
	private static final String OUT_MAMENAMES = "mamenames";
	private static final String OUT_GAMELIST = "gamelistout";
	private static final String DIR_GAMELIST = "gamelistdir";
	private static final String PATHPREFIX = "pathprefix";
	private static final String IMAGEPREFIX = "imageprefix";
	private static final String IMAGESUFFIX = "imagesuffix";
	private static final String MARQUEEPREFIX = "marqueeprefix";
	private static final String MARQUEESUFFIX = "marqueesuffix";

	public static void main(final String[] args) throws Exception {

		final Options options = new Options();
		options.addOption(IN_GAMELIST, true, "MAME gamelist.txt input file (optional)");
		options.addOption(IN_LISTXML, true, "MAME --listxml input file (optional)");
		options.addOption(IN_MAMEBIOSES, true, "Emulation Station resources/mamebioses.xml input file (optional)");
		options.addOption(IN_MAMEDEVICES, true, "Emulation Station resources/mamedevices.xml input file (optional)");
		options.addOption(OUT_MAMENAMES, true, "Emulation Station resources/mamenames.xml output file");
		options.addOption(OUT_GAMELIST, true, "Emulation Station gamelist.xml output file");
		options.addOption(DIR_GAMELIST, true, "Directory for filtering Emulation Station gamelist.xml output file");
		options.addOption(PATHPREFIX, true, "Emulation Station gamelist.xml <path> tag prefix (optional)");
		options.addOption(IMAGEPREFIX, true, "Emulation Station gamelist.xml <image> tag prefix (optional)");
		options.addOption(IMAGESUFFIX, true, "Emulation Station gamelist.xml <image> tag suffix (optional)");
		options.addOption(MARQUEEPREFIX, true, "Emulation Station gamelist.xml <marquee> tag prefix (optional)");
		options.addOption(MARQUEESUFFIX, true, "Emulation Station gamelist.xml <marquee> tag suffix (optional)");
		final CommandLine command = new DefaultParser().parse(options, args);
		if (command.getOptions().length == 0) {
			// (prints in proper order)
			final HelpFormatter helpFormatter = new HelpFormatter();
			try (PrintWriter pw = new PrintWriter(System.out)) {
				helpFormatter.printUsage(pw, 114, "java -jar mame2es.jar");
				for (final Option option : options.getOptions()) {
					helpFormatter.printOptions(pw, 114, new Options().addOption(option), 2, 4);
				}
			}
			return;
		}

		//

		final Map<String, Game> games = readGames(command);
		writeMameNames(command, games);
		writeGameList(command, games);
	}

	private static Map<String, Game> readGames(final CommandLine command) throws IOException {

		final GamesReader gamesReader = new GamesReader();

		if (command.hasOption(IN_GAMELIST)) {
			gamesReader.withGamelist(new FileSystemResource(command.getOptionValue(IN_GAMELIST)));
		}

		if (command.hasOption(IN_LISTXML)) {
			gamesReader.withListxml(new FileSystemResource(command.getOptionValue(IN_LISTXML)));
		}

		if (command.hasOption(IN_MAMEBIOSES)) {
			gamesReader.withMamebioses(new FileSystemResource(command.getOptionValue(IN_MAMEBIOSES)));
		}

		if (command.hasOption(IN_MAMEDEVICES)) {
			gamesReader.withMamedevices(new FileSystemResource(command.getOptionValue(IN_MAMEDEVICES)));
		}

		final Map<String, Game> games = gamesReader.read();

		System.out.printf("%d games read\n", games.size());
		return games;
	}

	private static boolean writeMameNames(final CommandLine command, final Map<String, Game> games) throws IOException {

		if (!command.hasOption(OUT_MAMENAMES)) {
			return false;
		}

		final File mamenames = new File(command.getOptionValue(OUT_MAMENAMES));
		if (mamenames.exists()) {
			System.out.printf("Invalid %s option: %s already exists\n", OUT_MAMENAMES, mamenames.getAbsolutePath());
			return false;
		}

		FileUtils.forceMkdirParent(mamenames);
		new MameNamesWriter(mamenames).write(games.values());
		System.out.printf("Emulation Station resources/mamenames.xml output file written to %s\n", mamenames.getAbsolutePath());
		return true;
	}

	private static boolean writeGameList(final CommandLine command, final Map<String, Game> games) throws IOException {

		if (!command.hasOption(OUT_GAMELIST)) {
			return false;
		}

		final File gamelist = new File(command.getOptionValue(OUT_GAMELIST));
		if (gamelist.exists()) {
			System.out.printf("Invalid %s option: %s already exists\n", OUT_GAMELIST, gamelist.getAbsolutePath());
			return false;
		}

		if (!command.hasOption(DIR_GAMELIST)) {
			System.out.printf("Invalid %s option\n", DIR_GAMELIST, gamelist.getAbsolutePath());
			return false;
		}

		final File dir = new File(command.getOptionValue(DIR_GAMELIST));
		if (!dir.isDirectory()) {
			System.out.printf("Invalid %s option: %s is not a directory\n", DIR_GAMELIST, dir.getAbsolutePath());
			return false;
		}

		final Set<String> filenames = new LinkedHashSet<>();
		for (final File file : FileUtils.listFiles(dir, null, false)) {
			filenames.add(file.getName());
		}
		if (filenames.isEmpty()) {
			System.out.printf("Invalid %s option: No files found in %s\n", DIR_GAMELIST, dir.getAbsolutePath());
			return false;
		}

		FileUtils.forceMkdirParent(gamelist);
		new GameListXmlWriter(gamelist,
				command.getOptionValue(PATHPREFIX, "./"),
				command.getOptionValue(IMAGEPREFIX, "~/.emulationstation/art/images/"),
				command.getOptionValue(IMAGESUFFIX, ".png"),
				command.getOptionValue(MARQUEEPREFIX, "~/.emulationstation/art/marquees/"),
				command.getOptionValue(MARQUEESUFFIX, ".png")).write(filenames, games);
		System.out.printf("%s written\n", gamelist.getAbsolutePath());
		return true;
	}

}
