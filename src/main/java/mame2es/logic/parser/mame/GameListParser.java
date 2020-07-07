package mame2es.logic.parser.mame;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import mame2es.model.Game;
import mame2es.util.ReadableResource;

/**
 * Parses a MAME {@code gamelist.txt} file
 */
public class GameListParser {

	private final Pattern regex = Pattern.compile("^(\\S+)\\s+\"(.+)\"$");

	private final ReadableResource source;

	public GameListParser(final ReadableResource source) {
		super();

		this.source = Validate.notNull(source, "The source must not be null");
	}

	public List<Game> get() throws IOException {

		return this.get(Collections.emptySet());
	}

	public List<Game> get(final Set<String> ignored) throws IOException {

		try (final Reader reader = this.source.getBufferedReader(StandardCharsets.UTF_8)) {

			return IOUtils.readLines(reader)
					.stream()
					.map(line -> this.regex.matcher(line))
					.filter(matcher -> matcher.matches())
					.map(matcher -> new Game(matcher.group(1), matcher.group(2)))
					.filter(game -> !(StringUtils.contains(game.getRomName(), '_') || ignored.contains(game.getRomName())))
					.collect(Collectors.toList());
		}
	}
}
