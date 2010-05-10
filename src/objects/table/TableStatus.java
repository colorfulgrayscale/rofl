package objects.table;

public enum TableStatus {
	OPEN('O'), OCCUPIED('X'), DIRTY('D'), UNAVAILABLE('U'), ORDERREADY('R');

	private char value;

	private TableStatus(final char aValue) {
		value = aValue;
	}

	public char getValue() {
		return value;
	}
}
