package mame2es.logic.parser.emulationstation;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

import mame2es.util.ReadableResource;

/**
 * Parses a Emulation Station {@code resources/mamebioses.xml} file
 */
public class MameBiosesParser {

	private final Pattern regex = Pattern.compile("<bios>(.*?)</bios>");

	private final ReadableResource source;

	public MameBiosesParser(final ReadableResource source) {
		super();

		Validate.notNull(source, "The source must not be null");

		this.source = source;
	}

	public Set<String> get() throws IOException {

		try (final Reader reader = this.source.getBufferedReader(StandardCharsets.UTF_8)) {

			return IOUtils.readLines(reader)
					.stream()
					.map(line -> this.regex.matcher(line))
					.filter(matcher -> matcher.matches())
					.map(matcher -> matcher.group(1))
					.collect(Collectors.toSet());
		}
	}
}
