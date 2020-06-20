package mame2es.util.parser.emulationstation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamSource;
import org.springframework.util.Assert;

/**
 * Parses a Emulation Station {@code resources/mamebioses.xml} file
 */
public class MameBiosesParser {

	private final Pattern regex = Pattern.compile("<bios>(.*?)</bios>");

	private final InputStreamSource source;

	public MameBiosesParser(final InputStreamSource source) {
		super();

		Assert.notNull(source, "The source must not be null");

		this.source = source;
	}

	public Set<String> get() throws IOException {

		try (final InputStream is = this.source.getInputStream()) {

			return IOUtils.readLines(is, StandardCharsets.UTF_8)
					.stream()
					.map(line -> this.regex.matcher(line))
					.filter(matcher -> matcher.matches())
					.map(matcher -> matcher.group(1))
					.collect(Collectors.toSet());
		}
	}
}
