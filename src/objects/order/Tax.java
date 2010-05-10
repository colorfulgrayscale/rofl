package objects.order;

public class Tax {
	private int id;
	private String name;
	private String description;
	private double percent;
	private boolean isDirty;

	public Tax() {
		id = 0;
		name = "";
		description = "";
		percent = 0.0;
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

	public void setName(final String aName) {
		if (name.equals(aName)) {
			isDirty = true;
		}
		name = aName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String aDescription) {
		if (description.equals(aDescription)) {
			isDirty = true;
		}
		description = aDescription;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(final double aPercent) {
		if (percent != aPercent) {
			isDirty = true;
		}
		percent = aPercent;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(final boolean isDirty) {
		this.isDirty = isDirty;
	}
}
