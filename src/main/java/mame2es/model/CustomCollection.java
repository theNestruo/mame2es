package mame2es.model;

public interface CustomCollection {

	default boolean matches(final Game game) {
		return (game != null) && this.matches(game.getCategory());
	}

	default boolean matches(final String category) {
		return false;
	}
}
