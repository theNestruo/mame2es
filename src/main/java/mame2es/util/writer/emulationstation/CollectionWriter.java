package mame2es.util.writer.emulationstation;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import mame2es.model.CustomCollection;
import mame2es.model.CustomCollections;
import mame2es.model.Game;

public class CollectionWriter {

	private final File dir;

	private final String filenamePattern = "custom-%s.cfg";

	public CollectionWriter(final File dir) {
		super();

		Assert.notNull(dir, "The dir must not be null");

		this.dir = dir;
	}

	public void write(final Collection<File> files, final Map<String, Game> games) throws IOException {

		final MultiValueMap<CustomCollection, String> customCollections = new LinkedMultiValueMap<>();

		for (final File file : files) {
			final String path = file.getAbsolutePath();
			final Game game = games.get(FilenameUtils.getBaseName(path));
			if (game == null) {
				continue;
			}

			for (final CustomCollections customCollection : CustomCollections.values()) {
				if (customCollection.matches(game)) {
					customCollections.add(customCollection, path);
				}
			}
		}

		for (final Entry<CustomCollection, List<String>> entry : customCollections.entrySet()) {
			final File file = new File(this.dir, String.format(this.filenamePattern, entry.getKey().toString()));
			System.out.println(file.getAbsolutePath());
			FileUtils.writeLines(file, entry.getValue(), true);
		}
	}

}
