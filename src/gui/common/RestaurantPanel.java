package gui.common;

import javax.swing.JLayeredPane;

import objects.user.User;
import database.DatabaseListener;
import database.DatabaseMessage;

public abstract class RestaurantPanel extends JLayeredPane {
	private static final long serialVersionUID = 3578709414337964230L;
	protected User user;
	protected DatabaseListener dbListener;

	public RestaurantPanel(final User aUser, final DatabaseListener aDBListener) {
		user = aUser;
		dbListener = aDBListener;
	}

	public abstract void databaseUpdate(DatabaseMessage aMessage);

}
