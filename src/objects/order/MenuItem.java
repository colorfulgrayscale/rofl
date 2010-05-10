package objects.order;

public class MenuItem {
	private int id;
	private String name;
	private String description;
	private double price;
	private MenuItemType type;
	private boolean isDirty;

	public MenuItem() {
		id = 0;
		name = "";
		description = "";
		price = 0;
		type = MenuItemType.APPETIZER;
		isDirty = true;
	}

	public int getID() {
		return id;
	}

	public void setID(final int anID) {
		id = anID;
	}

	public String getName() {
		return name;
	}

	public void setName(final String aName) {
		if (!name.equals(aName)) {
			isDirty = true;
		}
		name = aName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String aDescription) {
		if (!description.equals(aDescription)) {
			isDirty = true;
		}
		description = aDescription;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(final double aPrice) {
		if (price != aPrice) {
			isDirty = true;
		}
		price = aPrice;
	}

	public MenuItemType getType() {
		return type;
	}

	public void setType(final MenuItemType aType) {
		if (!type.equals(aType)) {
			isDirty = true;
		}
		type = aType;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(final boolean aDirty) {
		isDirty = aDirty;
	}

	@Override
	public String toString() {
		return name;
	}
}