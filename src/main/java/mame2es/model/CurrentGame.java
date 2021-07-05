package mame2es.model;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class CurrentGame {

	private final Game game;

	private final File dir;

	private final File file;

	public CurrentGame(final Game game, final File dir, final File file) {
		super();

		this.game = Validate.notNull(game, "The game must not be null");
		this.dir = Validate.notNull(dir, "The dir must not be null");
		this.file = Validate.notNull(file, "The file must not be null");
	}

	public String getAbsolutePath() {
		return this.file.getAbsolutePath();
	}

	public String getRelativePath() {
		return StringUtils.removeStart(this.file.getAbsolutePath(), this.dir.getAbsolutePath());
	}

	public Game getGame() {
		return new Game(game);
	}
}