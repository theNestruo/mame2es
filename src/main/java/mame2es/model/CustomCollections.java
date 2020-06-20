package mame2es.model;

import org.apache.commons.lang3.StringUtils;

public enum CustomCollections implements CustomCollection {

	action{
		@Override
		public boolean matches(final String category) {
			return StringUtils.startsWithIgnoreCase(category, "shooter")
					&& !shmups.matches(category);
		}
	},

	adventure,

	btmups{
		@Override
		public boolean matches(final String category) {
			return StringUtils.startsWithIgnoreCase(category, "fighter")
					&& !fighting.matches(category);
		}
	},

	fighting{
		@Override
		public boolean matches(final String category) {
			return StringUtils.startsWithIgnoreCase(category, "fighter")
					&& StringUtils.containsIgnoreCase(category, "versus");
		}
	},

	fixedscreen,

	kids,

	platformers{
		@Override
		public boolean matches(final String category) {
			return StringUtils.startsWithIgnoreCase(category, "platform");
		}
	},

	puzzle{
		@Override
		public boolean matches(final String category) {
			return StringUtils.startsWithIgnoreCase(category, "puzzle");
		}
	},

	racing{
		@Override
		public boolean matches(final String category) {
			return StringUtils.startsWithIgnoreCase(category, "driving");
		}
	},

	rpgs,

	shmups{
		@Override
		public boolean matches(final String category) {
			return StringUtils.startsWithIgnoreCase(category, "shooter")
					&& StringUtils.containsIgnoreCase(category, "flying")
					&& !StringUtils.containsIgnoreCase(category, "flying (chase view)")
					&& !StringUtils.containsIgnoreCase(category, "flying 1st person");

		}
	},

	sports{
		@Override
		public boolean matches(final String category) {
			return StringUtils.startsWithIgnoreCase(category, "sports");
		}
	},

	strategy;
}
