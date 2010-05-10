package objects.user;

public enum UserType {
	WAITER('W'), COOK('C'), HOST('H'), MANAGER('M'), BUSBOY('B');

	private char value;

	private UserType(final char aValue) {
		value = aValue;
	}

	public char getValue() {
		return value;
	}

	@Override
	public String toString() {
		if (value == 'W')
			return "Waiter";
		else if (value == 'C')
			return "Cook";
		else if (value == 'H')
			return "Host";
		else if (value == 'M')
			return "Manager";
		else if (value == 'B')
			return "Busboy";
		else
			return "Unknown";
	}
}
