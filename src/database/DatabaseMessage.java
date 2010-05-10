package database;

public enum DatabaseMessage {
	UPDATE_TABLES("updateTables"), UPDATE_ORDERS("updateOrders"), UPDATE_WALLS(
			"updateWalls");

	private String message;

	private DatabaseMessage(final String aMessage) {
		message = aMessage;
	}

	public String getListener() {
		return "LISTEN " + message + "; ";
	}

	public String getUnlistener() {
		return "UNLISTEN " + message + "; ";
	}

	public String getNotifier() {
		return "NOTIFY " + message + "; ";
	}

	public String getMessage() {
		return message;
	}

	public boolean equals(final String aString) {
		return message.equalsIgnoreCase(aString);
	}
}
