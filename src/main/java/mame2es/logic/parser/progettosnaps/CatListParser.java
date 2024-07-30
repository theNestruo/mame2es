package mame2es.logic.parser.progettosnaps;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import mame2es.model.Game;
import com.github.thenestruo.util.ReadableResource;

/**
 * Parses a progettoSNAPS.net MAME {@code CATVER.ini} file
 */
public class CatListParser {

	private static final String ARCADE_PREFIX = "Arcade:";

	private final Pattern categoryRegex = Pattern.compile("^\\[(.*)\\]$", Pattern.CASE_INSENSITIVE);

	private final ReadableResource source;

	public CatListParser(final ReadableResource source) {
		super();

		this.source = Validate.notNull(source, "The source must not be null");
	}

	public int readFor(final Map<String, Game> games) throws IOException {

		Validate.notNull(games, "The games map must not be null");

		try (final BufferedReader reader = this.source.getBufferedReader(StandardCharsets.UTF_8)) {

			int readCount = 0;
			boolean isCurrentCategoryArcade = false;
			String currentCategory = null;
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
					readCount++;
				}
			}

			return readCount;
		}
	}
}
