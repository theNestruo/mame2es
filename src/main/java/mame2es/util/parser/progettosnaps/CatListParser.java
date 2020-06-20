package mame2es.util.parser.progettosnaps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamSource;
import org.springframework.util.Assert;

import mame2es.model.Game;

/**
 * Parses a progettoSNAPS.net MAME {@code CATVER.ini} file
 */
public class CatListParser {

	private static final String ARCADE_PREFIX = "Arcade:";

	private final Pattern categoryRegex = Pattern.compile("^\\[(.*)\\]$", Pattern.CASE_INSENSITIVE);

	private final InputStreamSource source;

	public CatListParser(final InputStreamSource source) {
		super();

		Assert.notNull(source, "The source must not be null");

		this.source = source;
	}

	public void readFor(final Map<String, Game> games) throws IOException {

		Assert.notNull(games, "The games map must not be null");

		boolean isCurrentCategoryArcade = false;
		String currentCategory = null;

		try (final BufferedReader reader = IOUtils.buffer(new InputStreamReader(this.source.getInputStream(), StandardCharsets.UTF_8))) {
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				line = StringUtils.trimToEmpty(line);

				final Matcher categoryMatcher = this.categoryRegex.matcher(line);
				if (categoryMatcher.matches()) {
					final String category = categoryMatcher.group(1);
					isCurrentCategoryArcade = StringUtils.startsWithIgnoreCase(category, ARCADE_PREFIX);
					currentCategory = StringUtils.trim(StringUtils.removeStartIgnoreCase(category, ARCADE_PREFIX));
					continue;
				}

				if (!isCurrentCategoryArcade) {
					continue;
				}

				final String romName = line;
				if (games.containsKey(romName)) {
					games.get(romName).setCategory(currentCategory);
				}
			}
		}
	}
}
