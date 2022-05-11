package mame2es;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import mame2es.logic.CurrentGamesReader;
import mame2es.logic.GamesReaderCommandLineAdapter;
import mame2es.logic.writer.emulationstation.CollectionWriter;
import mame2es.logic.writer.emulationstation.GameListXmlWriter;
import mame2es.logic.writer.emulationstation.GameListXmlWriterCommandLineAdapter;
import mame2es.logic.writer.emulationstation.MameNamesWriter;
import mame2es.model.CurrentGame;
import mame2es.model.Game;

public class Mame2EsApp {

	private static final String HELP = "help";
	private static final String VERBOSE = "verbose";
	private static final String INPUT_DIR = "dir";
	private static final String OUTPUT_DIR = "out";

	private static final Logger logger = LoggerFactory.getLogger(Mame2EsApp.class);

	public static void main(final String[] args) throws ParseException, IOException {

		// Parses the command line
		final Options options = options();
		final CommandLine command = new DefaultParser().parse(options, args);

		// Main options
		if (showUsage(command, options)) {
			return;
		}
		setVerbose(command);

		// Initializes the games metadata
		final Map<String, Game> games = GamesReaderCommandLineAdapter.from(command).read();
		logger.info("{} games read", games.size());

		// Writes the Emulation Station resources/mamenames.xml file
		writeMameNames(command, games);

		// Writes the Emulation Station gamelist.xml and custom-collection.cfg files
		writeGameListAndCustomCollections(command, games);
	}

	private static Options options() {

		final Options options = new Options();
		options.addOption(HELP, "Shows usage");
		options.addOption(VERBOSE, "Verbose execution");
		options.addOption(INPUT_DIR, true, "Input directory (optional; to generate gamelist.xml and custom-collections.cfg)");
		options.addOption(OUTPUT_DIR, true, "Output directory (optional; current directory by default)");
		for (final Option option : GamesReaderCommandLineAdapter.options().getOptions()) {
			options.addOption(option);
		}
		for (final Option option : GameListXmlWriterCommandLineAdapter.options().getOptions()) {
			options.addOption(option);
		}
		return options;
	}

	private static boolean showUsage(final CommandLine command, final Options options) {

		if (!command.hasOption(HELP)) {
			return false;
		}

		// (prints in proper order)
		final HelpFormatter helpFormatter = new HelpFormatter();
		final PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.ISO_8859_1));
		helpFormatter.printUsage(pw, 114, "java -jar mame2es.jar");
		for (final Option option : options.getOptions()) {
			helpFormatter.printOptions(pw, 114, new Options().addOption(option), 2, 4);
		}
		pw.flush();

		return true;
	}

	private static boolean setVerbose(final CommandLine command) {

		if (!command.hasOption(VERBOSE)) {
			return false;
		}

		final Logger rootLogger = LoggerFactory.getILoggerFactory().getLogger(Logger.ROOT_LOGGER_NAME);
		ch.qos.logback.classic.Logger.class.cast(rootLogger).setLevel(Level.DEBUG);

		return true;
	}

	private static File getOutputDir(final CommandLine command) {

		final File outputDir = new File(command.getOptionValue(OUTPUT_DIR, "."));
		if (!outputDir.isDirectory()) {
			logger.warn("Invalid {} option: {} is not a directory", OUTPUT_DIR, outputDir.getAbsolutePath());
			return null;
		}

		return outputDir;
	}

	private static boolean writeMameNames(final CommandLine command, final Map<String, Game> games) throws IOException {

		final File outputDir = getOutputDir(command);
		if (outputDir == null) {
			return false;
		}

		final File mamenames = new File(outputDir, MameNamesWriter.FILENAME);
		if (mamenames.exists()) {
			logger.warn("Emulation Station resources/mamenames.xml output file {} already exists", mamenames.getAbsolutePath());
			return false;
		}

		new MameNamesWriter(mamenames).write(games.values());

		logger.info("Emulation Station resources/mamenames.xml output file written to {}", mamenames.getAbsolutePath());
		return true;
	}

	private static boolean writeGameListAndCustomCollections(final CommandLine command, final Map<String, Game> games) throws IOException {

		if (!command.hasOption(INPUT_DIR)) {
			return false;
		}

		final File inputDir = new File(command.getOptionValue(INPUT_DIR));
		if (!inputDir.isDirectory()) {
			logger.info("Invalid {} option: {} is not a directory", INPUT_DIR, inputDir.getAbsolutePath());
			return false;
		}

		Collection<CurrentGame> currentGames = new CurrentGamesReader(games).read(inputDir);
		if (currentGames.isEmpty()) {
			logger.warn("No games found in {}", inputDir.getAbsolutePath());
			return false;
		}

		final File outputDir = getOutputDir(command);
		if (outputDir == null) {
			return false;
		}

		final File gamelist = new File(outputDir, GameListXmlWriter.FILENAME);
		if (gamelist.exists()) {
			logger.warn("Emulation Station <system>/gamelist.xml output file {} already exists", gamelist.getAbsolutePath());
		} else {
			GameListXmlWriterCommandLineAdapter.from(gamelist, command).write(currentGames);
			logger.info("Emulation Station <system>/gamelist.xml output file written to {}", gamelist.getAbsolutePath());
		}

		new CollectionWriter(outputDir).write(currentGames);
		return true;
	}
}
