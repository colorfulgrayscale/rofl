package objects.common;

public enum SelectedAction {
	NONE(0), CHANGESTATUS(1), MODIFYORDERS(2), ASSIGNTABLES(3);

	private int value;

	private SelectedAction(final int aValue) {
		value = aValue;
	}

	public int getValue() {
		return value;
	}

}
