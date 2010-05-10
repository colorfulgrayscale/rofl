package objects.common;

public enum ToolsAvailable {
	NONE(0), MOVE(1), DELETE(2), ADD_TABLE(3), ADD_BOOTH(4), DRAW_SOLID_RECT(5), DRAW_SOLID_CIRCLE(
			6);

	private int value;

	private ToolsAvailable(final int aValue) {
		value = aValue;
	}

	public int getValue() {
		return value;
	}

}
