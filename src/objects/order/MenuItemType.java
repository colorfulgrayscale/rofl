package objects.order;

public enum MenuItemType {
	APPETIZER('A', "Appetizer"), ENTREE('E', "Entree"), DESSERT('D', "Dessert"), DRINK(
			'R', "Drink");

	private char value;
	private String description;

	private MenuItemType(final char aValue, final String aDescription) {
		value = aValue;
		description = aDescription;
	}

	public char getValue() {
		return value;
	}

	@Override
	public String toString() {
		return description;
	}

	public static MenuItemType getType(final char value) {
		for (final MenuItemType myType : MenuItemType.values())
			if (myType.getValue() == value)
				return myType;
		return null;
	}
}
