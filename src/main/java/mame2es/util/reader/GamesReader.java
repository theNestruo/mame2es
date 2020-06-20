package mame2es.util.reader;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;

import mame2es.model.Game;
import mame2es.util.parser.emulationstation.MameBiosesParser;
import mame2es.util.parser.emulationstation.MameDevicesParser;
import mame2es.util.parser.mame.GameListParser;
import mame2es.util.parser.mame.ListXmlParser;

public class GamesReader {

	private InputStreamSource gamelist = new ClassPathResource("data/arcade64-0.220/gamelist.txt");

	private InputStreamSource listxml = new ClassPathResource("data/mame64-0.220/listxml.xml");

	private InputStreamSource mamebioses = new ClassPathResource("data/emulationstation/mamebioses.xml");

	private InputStreamSource mamedevices = new ClassPathResource("data/emulationstation/mamedevices.xml");

	public GamesReader withGamelist(final InputStreamSource gamelist) {
		this.gamelist = gamelist;
		return this;
	}

	public GamesReader withListxml(final InputStreamSource listxml) {
		this.listxml = listxml;
		return this;
	}

	public GamesReader withMamebioses(final InputStreamSource mamebioses) {
		this.mamebioses = mamebioses;
		return this;
	}

	public GamesReader withMamedevices(final InputStreamSource mamedevices) {
		this.mamedevices = mamedevices;
		return this;
	}

	public Map<String, Game> read() throws IOException {

		final Set<String> ignored = new HashSet<>();
		ignored.addAll(new MameBiosesParser(this.mamebioses).get());
		ignored.addAll(new MameDevicesParser(this.mamedevices).get());

		final Map<String, Game> games = new LinkedHashMap<>();
		for (final Game game : new GameListParser(this.gamelist).get(ignored)) {
			games.put(game.getRomName(), game);
		}

		new ListXmlParser(this.listxml).readFor(games);

		return Collections.unmodifiableMap(games);
	}
}
