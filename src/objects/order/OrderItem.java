package objects.order;

public class OrderItem {
	private int id;
	private String name;
	private int menuItemID;
	private int quantity;
	private MenuItemType type;
	private String notes;
	private boolean isDirty;

	public OrderItem() {
		id = 0;
		name = "";
		menuItemID = 0;
		quantity = 0;
		type = MenuItemType.APPETIZER;
		notes = "";
		isDirty = true;
	}

	public int getId() {
		return id;
	}

	public void setId(final int anID) {
		id = anID;
	}

	public String getName() {
		return name;
	}

	public int getMenuItemID() {
		return menuItemID;
	}

	public void setMenuItem(final MenuItem anItem) {
		if (anItem.getID() != menuItemID) {
			isDirty = true;
		}
		menuItemID = anItem.getID();
		name = anItem.getName();
		type = anItem.getType();
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(final int aQuantity) {
		if (aQuantity != quantity) {
			isDirty = true;
		}
		quantity = aQuantity;
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(final String someNotes) {
		if (!notes.equals(someNotes)) {
			isDirty = false;
		}
		notes = someNotes;
	}

	public void setDirty(final boolean aValue) {
		isDirty = aValue;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public boolean equals(final OrderItem anItem) {
		return anItem.getId() == id;
	}

	@Override
	public String toString() {
		return name;
	}
}
