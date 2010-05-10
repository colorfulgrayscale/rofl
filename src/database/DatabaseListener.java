package database;

import gui.common.RestaurantPanel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.postgresql.PGConnection;
import org.postgresql.PGNotification;

public class DatabaseListener extends Thread {
	private final Map<RestaurantPanel, HashSet<DatabaseMessage>> map;
	private final Connection dbConnection;
	private final PGConnection pgDBConnection;
	private Boolean running;

	public DatabaseListener() {
		map = Collections
				.synchronizedMap(new HashMap<RestaurantPanel, HashSet<DatabaseMessage>>());
		dbConnection = DataAdapter.getConnection();
		pgDBConnection = (PGConnection) dbConnection;
		running = true;
	}

	@Override
	public void run() {
		while (running) {
			try {
				dbConnection.createStatement().executeQuery("SELECT 1");
				final org.postgresql.PGNotification notifications[] = pgDBConnection
						.getNotifications();
				if (notifications != null) {
					for (final PGNotification notification : notifications) {
						DatabaseMessage myMessage = null;
						for (final DatabaseMessage value : DatabaseMessage
								.values())
							if (value.equals(notification.getName())) {
								myMessage = value;
								break;
							}
						for (final RestaurantPanel myPanel : map.keySet())
							if (map.get(myPanel).contains(myMessage)) {
								myPanel.databaseUpdate(myMessage);
							}
					}
				}
				Thread.sleep(500);
			} catch (final SQLException e) {
				e.printStackTrace();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			dbConnection.close();
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	public void kill() {
		synchronized (running) {
			running = false;
		}
	}

	public boolean addMessage(final RestaurantPanel aPanel,
			final DatabaseMessage aMessage) {
		if (!map.containsKey(aPanel)) {
			map.put(aPanel, new HashSet<DatabaseMessage>());
		}
		if (map.get(aPanel).add(aMessage)) {
			for (final RestaurantPanel myPanel : map.keySet())
				if ((myPanel != aPanel) && map.get(myPanel).contains(aMessage))
					return true;
			try {
				dbConnection.createStatement().execute(aMessage.getListener());
			} catch (final SQLException e) {
				map.get(aPanel).remove(aMessage);
				return false;
			}
		}
		return false;
	}

	public boolean removeMessage(final RestaurantPanel aPanel,
			final DatabaseMessage aMessage) {
		if (!map.containsKey(aPanel))
			return false;
		if (map.get(aPanel) == null)
			return false;
		if (!map.get(aPanel).contains(aMessage))
			return false;
		map.get(aPanel).remove(aMessage);
		try {
			dbConnection.createStatement().execute(aMessage.getUnlistener());
		} catch (final SQLException e) {
			map.get(aPanel).add(aMessage);
			return false;
		}
		return true;
	}
}
