package mame2es.logic.parser.emulationstation;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

import com.github.thenestruo.util.ReadableResource;

/**
 * Parses a Emulation Station {@code resources/mamedevices.xml} file
 */
public class MameDevicesParser {

	private final Pattern regex = Pattern.compile("<device>(.*?)</device>");

	private final ReadableResource source;

	public MameDevicesParser(final ReadableResource source) {
		super();

		this.source = Validate.notNull(source, "The source must not be null");
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
