package mame2es.logic;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mame2es.logic.parser.emulationstation.MameBiosesParser;
import mame2es.logic.parser.emulationstation.MameDevicesParser;
import mame2es.logic.parser.mame.GameListParser;
import mame2es.logic.parser.mame.ListXmlParser;
import mame2es.logic.parser.progettosnaps.CatListParser;
import mame2es.model.Game;
import mame2es.util.ClassPathResource;
import mame2es.util.ReadableResource;

/**
 * Convenience wrapper for MAME, progettoSNAPS.net MAME, and Emulation Station parsers
 */
public class GamesReader {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private ReadableResource gamelist = new ClassPathResource("data/arcade64-0.220/gamelist.txt");

	private ReadableResource listxml = new ClassPathResource("data/mame64-0.220/listxml.xml");

	private ReadableResource catlist = new ClassPathResource("data/catver-0.221/catlist.ini");

	private ReadableResource mamebioses = new ClassPathResource("data/emulationstation/mamebioses.xml");

	private ReadableResource mamedevices = new ClassPathResource("data/emulationstation/mamedevices.xml");

	public GamesReader withGamelist(final ReadableResource gamelist) {
		this.gamelist = gamelist;
		return this;
	}

	public GamesReader withListxml(final ReadableResource listxml) {
		this.listxml = listxml;
		return this;
	}

	public GamesReader withCatlist(final ReadableResource catlist) {
		this.catlist = catlist;
		return this;
	}

	public GamesReader withMamebioses(final ReadableResource mamebioses) {
		this.mamebioses = mamebioses;
		return this;
	}

	public GamesReader withMamedevices(final ReadableResource mamedevices) {
		this.mamedevices = mamedevices;
		return this;
	}

	public Map<String, Game> read() throws IOException {

		final Set<String> ignored = new HashSet<>();
		ignored.addAll(new MameBiosesParser(this.mamebioses).get());
		ignored.addAll(new MameDevicesParser(this.mamedevices).get());
		logger.debug("Will ignore {} bioses and devices", ignored.size());

		final Map<String, Game> games = new LinkedHashMap<>();
		for (final Game game : new GameListParser(this.gamelist).get(ignored)) {
			games.put(StringUtils.lowerCase(game.getRomName()), game);
		}
		logger.debug("{} games found in the gamelist", games.size());

		final int listxmlCount = new ListXmlParser(this.listxml).readFor(games);
		logger.debug("{} games metadata found in the --listxml", listxmlCount);

		final int catlistCount = new CatListParser(this.catlist).readFor(games);
		logger.debug("{} games metadata found in the catlist", catlistCount);

		if (catlistCount < games.size()) {
			for (final Game game : games.values()) {
				if (StringUtils.isNotBlank(game.getCategory())) {
					continue;
				}
				final String cloneOf = game.getCloneOf();
				if (StringUtils.isBlank(cloneOf)
						|| !games.containsKey(cloneOf)) {
					// (most likely, non "Arcade:" categories such as "MultiGame / Compilation",
					// or "Game Console / Home Videogame" for "... (CPS Changer...)")
					logger.debug("{} ({}) does not have arcade category",
							game.getRomName(), game.getDescription());
					continue;
				}
				final Game parent = games.get(cloneOf);
				logger.debug("{} ({}) is cloneof {} ({}): assuming arcade category \"{}\"",
						game.getRomName(), game.getDescription(),
						parent.getRomName(), parent.getDescription(),
						parent.getCategory());
				game.setCategory(parent.getCategory());
			}
		}

		return Collections.unmodifiableMap(games);
	}
}
