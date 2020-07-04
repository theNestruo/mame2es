package mame2es.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class Game {

	private static final String[] ARTICLES = new String[] {"A ", "An ", "The "};

	private final String romName;

	private final String description;

	private String cloneOf;

	private String year;

	private String manufacturer;

	private String players;

	private String category;

	public Game(final String romName, final String longDescription) {
		super();

		Validate.notBlank(romName, "The romName must not be null or blank");
		Validate.notBlank(longDescription, "The longDescription must not be null or blank");

		this.romName = romName;
		this.description = longDescription;
	}

	/**
	 * @return either {@link #romName} or {@link #cloneOf}
	 */
	public String getParentRomName() {

		return StringUtils.defaultString(this.cloneOf, this.romName);
	}

	/**
	 * @return a title of the game, without translations, and with the article shifted
	 */
	public String getShortTitle() {

		final String shortTitle = StringUtils.substringBefore(this.getTitle(), " /");
		return StringUtils.startsWithAny(shortTitle, ARTICLES)
				? StringUtils.substringAfter(shortTitle, " ") + ", " + StringUtils.substringBefore(shortTitle, " ")
				: shortTitle;
	}

	/**
	 * @return the title of the game; may include multiple translations
	 */
	public String getTitle() {

		return StringUtils.substringBefore(this.description, " (");
	}

	public String getRomName() {
		return this.romName;
	}

	public String getDescription() {
		return this.description;
	}

	public String getCloneOf() {
		return this.cloneOf;
	}

	public void setCloneOf(final String cloneOf) {
		this.cloneOf = cloneOf;
	}

	public String getYear() {
		return this.year;
	}

	public void setYear(final String year) {
		this.year = year;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(final String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getPlayers() {
		return this.players;
	}

	public void setPlayers(final String players) {
		this.players = players;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(final String category) {
		this.category = category;
	}
}
