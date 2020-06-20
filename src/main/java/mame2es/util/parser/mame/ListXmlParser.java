package mame2es.util.parser.mame;

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
 * Parses a MAME {@code --listxml} file
 */
public class ListXmlParser {

	private final Pattern machineBeginRegex = Pattern.compile("<machine\\s+name=\"(.*?)\"(?:.*cloneof=\"(.*?)\")?.*");

	private final Pattern yearRegex = Pattern.compile("<year>(.*?)</year>");

	private final Pattern manufacturerRegex = Pattern.compile("<manufacturer>(.*?)</manufacturer>");

	private final Pattern playersRegex = Pattern.compile("<input\\s+players=\"(.*?)\".*");

	private final InputStreamSource source;

	public ListXmlParser(final InputStreamSource source) {
		super();

		Assert.notNull(source, "The source must not be null");

		this.source = source;
	}

	public void readFor(final Map<String, Game> games) throws IOException {

		Assert.notNull(games, "The games map must not be null");

		Game currentGame = null;

		try (final BufferedReader reader = IOUtils.buffer(new InputStreamReader(this.source.getInputStream(), StandardCharsets.UTF_8))) {
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				line = StringUtils.trimToEmpty(line);

				final Matcher machineBeginMatcher = this.machineBeginRegex.matcher(line);
				if (machineBeginMatcher.matches()) {
					final String name = machineBeginMatcher.group(1);
					final String cloneOf = machineBeginMatcher.group(2);
					currentGame = games.get(name);
					if ((currentGame != null) && (cloneOf != null)) {
						currentGame.setCloneOf(cloneOf);
					}
					continue;
				}

				if (currentGame == null) {
					continue;
				}

				final Matcher yearMatcher = this.yearRegex.matcher(line);
				if (yearMatcher.matches()) {
					currentGame.setYear(yearMatcher.group(1));
					continue;
				}

				final Matcher manufacturerMatcher = this.manufacturerRegex.matcher(line);
				if (manufacturerMatcher.matches()) {
					currentGame.setManufacturer(manufacturerMatcher.group(1));
					continue;
				}

				final Matcher playersMatcher = this.playersRegex.matcher(line);
				if (playersMatcher.matches()) {
					currentGame.setPlayers(playersMatcher.group(1));
					continue;
				}
			}
		}
	}

}
