package mame2es.logic;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.github.thenestruo.util.FileSystemResource;

/**
 * Apache Commons CLI {@link CommandLine} adapter for {@link GamesReader}
 */
public class GamesReaderCommandLineAdapter {

	private static final String IN_GAMELIST = "gamelist";
	private static final String IN_LISTXML = "listxml";
	private static final String IN_CATLIST = "catlist";
	private static final String IN_MAMEBIOSES = "mamebioses";
	private static final String IN_MAMEDEVICES = "mamedevices";

	public static Options options() {

		final Options options = new Options();

		options.addOption(IN_GAMELIST, true, "MAME gamelist.txt input file (optional)");
		options.addOption(IN_LISTXML, true, "MAME --listxml input file (optional)");
		options.addOption(IN_CATLIST, true, "progettoSNAPS.net MAME CATVER.ini input file (optional)");
		options.addOption(IN_MAMEBIOSES, true, "Emulation Station resources/mamebioses.xml input file (optional)");
		options.addOption(IN_MAMEDEVICES, true, "Emulation Station resources/mamedevices.xml input file (optional)");

		return options;
	}

	public static GamesReader from(final CommandLine command) {

		final GamesReader gamesReader = new GamesReader();

		if (command.hasOption(IN_GAMELIST)) {
			gamesReader.withGamelist(new FileSystemResource(command.getOptionValue(IN_GAMELIST)));
		}

		if (command.hasOption(IN_LISTXML)) {
			gamesReader.withListxml(new FileSystemResource(command.getOptionValue(IN_LISTXML)));
		}

		if (command.hasOption(IN_CATLIST)) {
			gamesReader.withCatlist(new FileSystemResource(command.getOptionValue(IN_CATLIST)));
		}

		if (command.hasOption(IN_MAMEBIOSES)) {
			gamesReader.withMamebioses(new FileSystemResource(command.getOptionValue(IN_MAMEBIOSES)));
		}

		if (command.hasOption(IN_MAMEDEVICES)) {
			gamesReader.withMamedevices(new FileSystemResource(command.getOptionValue(IN_MAMEDEVICES)));
		}

		return gamesReader;
	}

	private GamesReaderCommandLineAdapter() {
		super();
	}
}
