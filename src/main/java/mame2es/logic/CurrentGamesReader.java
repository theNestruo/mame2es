package mame2es.logic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mame2es.model.CurrentGame;
import mame2es.model.Game;

public class CurrentGamesReader {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final Map<String, Game> games;

	public CurrentGamesReader(final Map<String, Game> games) {
		super();

		this.games = Validate.notNull(games, "The games must not be null");
	}

	public Collection<CurrentGame> read(final File dir) throws IOException {

		final Collection<CurrentGame> currentGames = new ArrayList<>();

		int fileCount = 0;
		for (final File file : FileUtils.listFiles(dir, null, true)) {
			fileCount++;
			final String filename = file.getName();
			final String romName = StringUtils.lowerCase(FilenameUtils.getBaseName(filename));
			final Game game = games.get(romName);
			if (game == null) {
				logger.debug("{} will be ignored; '{}' not recognized as a game", file.getPath(), romName);
				continue;
			}
			currentGames.add(new CurrentGame(game, dir, file));
		}

		logger.info("{} games found in {} files", currentGames.size(), fileCount);
		return currentGames;
	}
}