package mame2es.logic.writer.emulationstation;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mame2es.model.CurrentGame;
import mame2es.model.CustomCollection;
import mame2es.model.CustomCollections;
import mame2es.model.Game;

/**
 * Writes Emulation Station {@code collections/custom-collection.cfg} files
 */
public class CollectionWriter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final File dir;

	private final String filenamePattern = "custom-%s.cfg";

	public CollectionWriter(final File dir) {
		super();

		Validate.notNull(dir, "The dir must not be null");

		this.dir = dir;
	}

	public void write(final Collection<CurrentGame> currentGames) throws IOException {

		final Map<CustomCollection, Collection<String>> customCollections = new LinkedHashMap<>();

		for (final CurrentGame currentGame : currentGames) {
			final Game game = currentGame.getGame();
			final String path = FilenameUtils.separatorsToUnix(currentGame.getAbsolutePath());

			for (final CustomCollections customCollection : CustomCollections.values()) {
				if (customCollection.matches(game)) {
					customCollections.computeIfAbsent(customCollection, k -> new LinkedHashSet<String>()).add(path);
				}
			}
		}

		for (final Entry<CustomCollection, Collection<String>> entry : customCollections.entrySet()) {
			final File file = new File(this.dir, String.format(this.filenamePattern, entry.getKey().toString()));
			FileUtils.writeLines(file, entry.getValue(), true);
			logger.info("Emulation Station collections/custom-collection.cfg output file written to {}", file.getAbsolutePath());
		}
	}
}
