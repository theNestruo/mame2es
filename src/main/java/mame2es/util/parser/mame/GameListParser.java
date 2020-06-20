package mame2es.util.parser.mame;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamSource;
import org.springframework.util.Assert;

import mame2es.model.Game;

/**
 * Parses a MAME {@code gamelist.txt} file
 */
public class GameListParser {

	private final Pattern regex = Pattern.compile("^(\\S+)\\s+\"(.+)\"$");

	private final InputStreamSource source;

	public GameListParser(final InputStreamSource source) {
		super();

		Assert.notNull(source, "The source must not be null");

		this.source = source;
	}

	public List<Game> get() throws IOException {

		return this.get(Collections.emptySet());
	}

	public List<Game> get(final Set<String> ignored) throws IOException {

		try (final InputStream is = this.source.getInputStream()) {

			return IOUtils.readLines(is, StandardCharsets.UTF_8)
					.stream()
					.map(line -> this.regex.matcher(line))
					.filter(matcher -> matcher.matches())
					.map(matcher -> new Game(matcher.group(1), matcher.group(2)))
					.filter(game -> !(StringUtils.contains(game.getRomName(), '_') && ignored.contains(game.getRomName())))
					.collect(Collectors.toList());
		}
	}
}
