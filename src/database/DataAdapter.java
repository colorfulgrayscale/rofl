package database;

import java.awt.Dimension;
import java.awt.Point;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import objects.order.MenuItem;
import objects.order.MenuItemType;
import objects.order.Order;
import objects.order.OrderItem;
import objects.order.Tax;
import objects.table.RoundTable;
import objects.table.SquareTable;
import objects.table.Table;
import objects.table.TableStatus;
import objects.user.User;
import objects.user.UserType;
import objects.wall.CircularWall;
import objects.wall.RectangularWall;
import objects.wall.Wall;

public class DataAdapter {
	private static Connection dbConnection;

	private DataAdapter() {
	}

	public static boolean open() {
		DataAdapter.dbConnection = DataAdapter.getConnection();
		return true;
	}

	public static boolean close() {
		try {
			DataAdapter.dbConnection.close();
		} catch (final SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static User login(final String userName, final String password) {
		final String query = "Select * from RestaurantUser where username='"
				+ userName + "' and password='" + password
				+ "' and active=true;";
		ResultSet results = null;
		try {
			results = DataAdapter.dbConnection.createStatement().executeQuery(
					query);
		} catch (final SQLException e) {
			e.printStackTrace();
		}
		if (results == null)
			return null;
		try {
			while (results.next()) {
				final int myId = results.getInt("id");
				final String myFirstName = results.getString("firstname");
				final String myLastName = results.getString("lastname");
				final String myPassword = results.getString("password");
				final String myUserName = results.getString("username");
				UserType myType = null;
				for (final UserType aType : UserType.values())
					if (aType.getValue() == results.getString("type").charAt(0)) {
						myType = aType;
					}
				return new User(myId, myFirstName, myLastName, myUserName,
						myPassword, myType);
			}
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public static boolean insertTable(final User aUser, final Table aTable) {
		if (aUser.getType() != UserType.MANAGER)
			return false;
		TableStatus status = aTable.getStatus();
		if (status == TableStatus.UNAVAILABLE) {
			status = TableStatus.OPEN;
		}
		String query = "Insert into RestaurantTable (locationX, locationY, status, tableType) "
				+ "values ("
				+ aTable.getLocation().getX()
				+ ","
				+ aTable.getLocation().getY() + ",'" + status.getValue() + "',";
		if (aTable instanceof RoundTable) {
			query += "'R') ";
		} else if (aTable instanceof SquareTable) {
			query += "'S') ";
		}
		query += "returning id;";
		try {
			final ResultSet results = DataAdapter.dbConnection
					.createStatement().executeQuery(query);
			results.next();
			DataAdapter.dbConnection.createStatement().execute(
					DatabaseMessage.UPDATE_TABLES.getNotifier());
			aTable.setId(results.getInt("id"));
		} catch (final SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean deleteTable(final User aUser, final Table aTable) {
		if (aUser.getType() != UserType.MANAGER)
			return false;
		final String query = "update restauranttable set active=false where id="
				+ aTable.getId() + ";";
		try {
			DataAdapter.dbConnection.createStatement().execute(query);
			DataAdapter.dbConnection.createStatement().execute(
					DatabaseMessage.UPDATE_TABLES.getNotifier());
		} catch (final SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static List<Table> getTables(final User aUser,
			final Dimension aDimension) {
		// if (aUser.getType() == UserType.WAITER) {
		// final String query =
		// "Select rt.id, rt.locationX, rt.locationY, rt.tableType, rt.status, ru.id as ruid "
		// + "from RestaurantTable rt "
		// + "left join ScheduleAssignment sa "
		// + "on rt.id = sa.restaurantTable "
		// + "left join RestaurantUser ru "
		// + "on sa.waiter = ru.id "
		// + "left join ScheduleTracker st "
		// + "on sa.workSchedule = st.workSchedule "
		// + "where st.endTime is null " + "and rt.active=true;";
		// final List<Table> tables = new ArrayList<Table>();
		// ResultSet results = null;
		// try {
		// results = DataAdapter.dbConnection.createStatement()
		// .executeQuery(query);
		// } catch (final SQLException e) {
		// e.printStackTrace();
		// System.err.println(query);
		// return null;
		// }
		// try {
		// while (results.next()) {
		// final String type = results.getString("tableType");
		// final double locX = results.getDouble("locationX");
		// final double locY = results.getDouble("locationY");
		// final Point point = new Point();
		// point.setLocation(locX, locY);
		// TableStatus status = null;
		// for (final TableStatus myStatus : TableStatus.values())
		// if (myStatus.getValue() == results.getString("status")
		// .charAt(0)) {
		// status = myStatus;
		// }
		// if (results.getInt("ruid") != aUser.getID()) {
		// status = TableStatus.UNAVAILABLE;
		// }
		// Table myTable = null;
		// if ("S".equals(type)) {
		// myTable = new SquareTable(point, 5, aDimension);
		// } else if ("R".equals(type)) {
		// myTable = new RoundTable(point, 5, aDimension);
		// }
		// myTable.setStatus(status);
		// myTable.setId(results.getInt("id"));
		// tables.add(myTable);
		// }
		// } catch (final SQLException e) {
		// e.printStackTrace();
		// return new ArrayList<Table>();
		// }
		// return tables;
		// }
		if ((aUser.getType() == UserType.HOST)
				|| (aUser.getType() == UserType.MANAGER)
				|| (aUser.getType() == UserType.BUSBOY)
				|| (aUser.getType() == UserType.WAITER)) {
			final String query = "Select * from RestaurantTable where active=true order by id; ";
			final List<Table> tables = new ArrayList<Table>();
			ResultSet results = null;
			try {
				results = DataAdapter.dbConnection.createStatement()
						.executeQuery(query);
			} catch (final SQLException e) {
				e.printStackTrace();
				System.err.println(query);
				return null;
			}
			try {
				while (results.next()) {
					final String type = results.getString("tableType");
					final double locX = results.getDouble("locationX");
					final double locY = results.getDouble("locationY");
					final Point point = new Point();
					point.setLocation(locX, locY);
					TableStatus status = null;
					for (final TableStatus myStatus : TableStatus.values())
						if (myStatus.getValue() == results.getString("status")
								.charAt(0)) {
							status = myStatus;
						}
					Table myTable = null;
					if ("S".equals(type)) {
						myTable = new SquareTable(point, 5, aDimension);
					} else if ("R".equals(type)) {
						myTable = new RoundTable(point, 5, aDimension);
					}
					myTable.setStatus(status);
					myTable.setId(results.getInt("id"));
					tables.add(myTable);
				}
			} catch (final SQLException e) {
				e.printStackTrace();
				return new ArrayList<Table>();
			}
			return tables;
		}
		return new ArrayList<Table>();
	}

	public static boolean updateTable(final User aUser, final Table aTable,
			final Dimension aDimension) {
		String query = "begin; ";
		if (aUser.getType() == UserType.MANAGER) {
			if (aTable.isDirty()) {
				query += "Update RestaurantTable set " + "locationX="
						+ aTable.getLocation().getX() + ", " + "locationY="
						+ aTable.getLocation().getY() + ", " + "status='"
						+ aTable.getStatus().getValue() + "' " + "where id="
						+ aTable.getId() + "; ";
				query += DatabaseMessage.UPDATE_TABLES.getNotifier();
			}
		} else if ((aUser.getType() == UserType.WAITER)
				|| (aUser.getType() == UserType.HOST)
				|| (aUser.getType() == UserType.BUSBOY)) {
			query += "Update RestaurantTable set status = '"
					+ aTable.getStatus().getValue() + "' where" + " id="
					+ aTable.getId() + ";";
		}
		query += "commit;";
		try {
			DataAdapter.dbConnection.createStatement().execute(query);
			DataAdapter.dbConnection.createStatement().execute(
					DatabaseMessage.UPDATE_TABLES.getNotifier());
		} catch (final SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean insertWall(final User aUser, final Wall aWall) {
		if (aUser.getType() != UserType.MANAGER)
			return false;
		final String query = "Insert into Walls (locationX, locationY, width, height, wallType) values "
				+ "("
				+ aWall.getLocation().getX()
				+ ", "
				+ aWall.getLocation().getY()
				+ ", "
				+ aWall.getWidth()
				+ ", "
				+ aWall.getHeight()
				+ ", "
				+ (aWall instanceof CircularWall ? "'C'" : "'R'")
				+ ") returning id;";
		try {
			final ResultSet results = DataAdapter.dbConnection
					.createStatement().executeQuery(query);
			if (results.next()) {
				aWall.setId(results.getInt("id"));
				DataAdapter.dbConnection.createStatement().execute(
						DatabaseMessage.UPDATE_WALLS.getNotifier());
				return true;
			}
			return false;
		} catch (final SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean deleteWall(final User aUser, final Wall aWall) {
		if (aUser.getType() != UserType.MANAGER)
			return false;
		final String query = "Delete from Walls where id=" + aWall.getId()
				+ ";";
		try {
			if (DataAdapter.dbConnection.createStatement().execute(query)) {
				DataAdapter.dbConnection.createStatement().execute(
						DatabaseMessage.UPDATE_WALLS.getNotifier());
				return true;
			}
			return false;
		} catch (final SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static List<Wall> getWalls(final User aUser, final Dimension canvas) {
		final List<Wall> walls = new ArrayList<Wall>();
		final String query = "Select id, locationX, locationY, width, height, wallType from Walls;";
		try {
			final ResultSet results = DataAdapter.dbConnection
					.createStatement().executeQuery(query);
			while (results.next()) {
				Wall myWall;
				final Point myPoint = new Point();
				myPoint.setLocation(results.getDouble("locationX"), results
						.getDouble("locationY"));
				if (results.getString("wallType").charAt(0) == 'C') {
					myWall = new CircularWall(myPoint, canvas);
					((CircularWall) myWall).setEndingCoordinates((int) results
							.getDouble("width"), (int) results
							.getDouble("height"));
				} else {
					myWall = new RectangularWall(myPoint, canvas);
					((RectangularWall) myWall).setEndingCoordinates(
							(int) results.getDouble("width"), (int) results
									.getDouble("height"));
				}
				myWall.setDirty(false);
				myWall.setId(results.getInt("id"));
				walls.add(myWall);
			}
			return walls;
		} catch (final SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean insertOrder(final User aUser, final Order anOrder) {
		if ((aUser.getType() != UserType.WAITER)
				&& (aUser.getType() != UserType.MANAGER))
			return false;
		String query = "Insert into RestaurantOrder (restaurantTable, seatedTime) "
				+ "values ("
				+ anOrder.getTable()
				+ ", "
				+ System.currentTimeMillis() + ") returning id; ";
		try {

			final ResultSet results = DataAdapter.dbConnection
					.createStatement().executeQuery(query);
			while (results.next()) {
				anOrder.setId(results.getInt("id"));
			}
		} catch (final SQLException e) {
			e.printStackTrace();
			return false;
		}
		query = "begin; ";
		query += DataAdapter.updateOrderItems(anOrder, new Order());
		query += " commit;";
		try {
			DataAdapter.dbConnection.createStatement().execute(query);
			DataAdapter.dbConnection.createStatement().execute(
					DatabaseMessage.UPDATE_ORDERS.getNotifier());
			return true;
		} catch (final SQLException e) {
			return false;
		}
	}

	public static Order updateOrder(final User aUser, final Order anOrder) {
		if ((aUser.getType() != UserType.WAITER)
				&& (aUser.getType() != UserType.MANAGER)
				&& (aUser.getType() != UserType.COOK))
			return null;
		if (anOrder.isDirty()) {
			final Order original = DataAdapter.selectOrder(anOrder);
			if (original == null)
				return null;
			String query = "begin; ";
			query += "Update RestaurantOrder set restauranttable = "
					+ anOrder.getTable() + ", " + "seatedTime = "
					+ anOrder.getSeatedTime().getTime();
			if (anOrder.getOrderTaken() != null) {
				query += ", orderTaken=" + anOrder.getOrderTaken().getTime()
						+ " ";
			}
			if (anOrder.getOrderReady() != null) {
				query += ", orderReady=" + anOrder.getOrderReady().getTime()
						+ " ";
			}
			if (anOrder.getOrderDelivered() != null) {
				query += ", orderDelivered="
						+ anOrder.getOrderDelivered().getTime() + " ";
			}
			if (anOrder.getOrderPaid() != null) {
				query += ", orderPaid=" + anOrder.getOrderPaid().getTime()
						+ " ";
			}
			if (anOrder.getCustomerLeft() != null) {
				query += ", customerLeft="
						+ anOrder.getCustomerLeft().getTime() + " ";
			}
			query += " where id = " + anOrder.getId() + "; ";
			query += DataAdapter.updateOrderItems(anOrder, original);
			query += "commit;";
			try {
				DataAdapter.dbConnection.createStatement().execute(query);
				DataAdapter.dbConnection.createStatement().execute(
						DatabaseMessage.UPDATE_ORDERS.getNotifier());
			} catch (final SQLException e) {
				System.err.println(query);
				e.printStackTrace();
				return null;
			}
			return DataAdapter.selectOrder(anOrder);
		}
		return anOrder;
	}

	// TODO Adam: Incomplete
	public static List<Order> getOrders(final User aUser) {
		final List<Order> orders = new ArrayList<Order>();
		final String query;
		if (aUser.getType() == UserType.COOK) {
			query = "Select id from RestaurantOrder where orderTaken is not Null and orderReady is Null;";
		} else {
			query = "Select id from RestaurantOrder where customerLeft is null; ";
		}
		try {
			final ResultSet results = DataAdapter.dbConnection
					.createStatement().executeQuery(query);
			while (results.next()) {
				final Order myOrder = new Order();
				myOrder.setId(results.getInt("id"));
				orders.add(DataAdapter.selectOrder(myOrder));
			}
		} catch (final SQLException e) {
			return new ArrayList<Order>();
		}
		return orders;
	}

	public static Order selectOrder(final User aUser, final int anOrderNumber) {
		final Order anOrder = new Order();
		int orderNumber = anOrderNumber;
		if (orderNumber >= 1000) {
			orderNumber %= 1000;
		}
		final String query = "Select Max(id) from RestaurantOrder;";
		try {
			final ResultSet results = DataAdapter.dbConnection
					.createStatement().executeQuery(query);
			if (results.next()) {
				final int max = results.getInt("max");
				final int leading = max / 1000;
				final int trailing = max % 1000;
				if (orderNumber > trailing) {
					if (leading > 0) {
						orderNumber = (leading - 1) * 1000 + orderNumber;
					} else
						return null;
				} else {
					orderNumber = leading * 1000 + orderNumber;
				}
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		}
		anOrder.setId(orderNumber);
		return DataAdapter.selectOrder(anOrder);
	}

	public static Order selectOrder(final User aUser, final Table aTable) {
		if (!((aUser.getType() == UserType.WAITER)
				|| (aUser.getType() == UserType.MANAGER) || (aUser.getType() == UserType.COOK)))
			return null;
		final String query;
		query = "Select id from RestaurantOrder " + "where restaurantTable="
				+ aTable.getId() + " and customerLeft is null;";
		try {
			final ResultSet results = DataAdapter.dbConnection
					.createStatement().executeQuery(query);
			while (results.next()) {
				final Order myOrder = new Order();
				myOrder.setId(results.getInt("id"));
				return DataAdapter.selectOrder(myOrder);
			}
			return null;
		} catch (final SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Order selectOrder(final Order anOrder) {
		String query = "Select restaurantTable, seatedTime, orderTaken, orderREady, orderDelivered, orderPaid, customerLeft from RestaurantOrder where id="
				+ anOrder.getId() + "; ";
		try {
			final Order myOrder = new Order();
			ResultSet results = DataAdapter.dbConnection.createStatement()
					.executeQuery(query);
			while (results.next()) {
				myOrder.setTable(results.getInt("restaurantTable"));
				myOrder.setId(anOrder.getId());
				long value;
				if ((value = results.getLong("seatedTime")) != 0) {
					myOrder.setSeatedTime(new Date(value));
				}
				if ((value = results.getLong("orderTaken")) != 0) {
					myOrder.setOrderTaken(new Date(value));
				}
				if ((value = results.getLong("orderReady")) != 0) {
					myOrder.setOrderReady(new Date(value));
				}
				if ((value = results.getLong("orderDelivered")) != 0) {
					myOrder.setOrderDelivered(new Date(0));
				}
				if ((value = results.getLong("orderPaid")) != 0) {
					myOrder.setOrderPaid(new Date(0));
				}
				if ((value = results.getLong("customerLeft")) != 0) {
					myOrder.setCustomerLeft(new Date(value));
				}
			}
			query = "Select OrderItem.id, MenuItem.name, MenuItem.id as menuItemID, quantity, notes, OrderItem.menuType "
					+ "from OrderItem "
					+ "inner join MenuItem "
					+ "on MenuItem.id = OrderItem.menuItem "
					+ "inner join RestaurantOrder "
					+ "on OrderItem.restaurantOrder = RestaurantOrder.id "
					+ "where restaurantOrder=" + anOrder.getId() + ";";
			results = DataAdapter.dbConnection.createStatement().executeQuery(
					query);
			while (results.next()) {
				final OrderItem myItem = new OrderItem();
				myItem.setId(results.getInt("id"));
				final MenuItem myMenuItem = new MenuItem();
				myMenuItem.setID(results.getInt("menuItemID"));
				myMenuItem.setName(results.getString("name"));
				myItem.setMenuItem(myMenuItem);
				myItem.setNotes(results.getString("notes"));
				myItem.setQuantity(results.getInt("quantity"));
				for (final MenuItemType myType : MenuItemType.values())
					if (myType.getValue() == results.getString("menuType")
							.charAt(0)) {
						myItem.setType(myType);
						break;
					}
				myItem.setDirty(false);
				myOrder.addOrderItem(myItem);
			}
			myOrder.setDirty(false);
			return myOrder;
		} catch (final SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String updateOrderItems(final Order anOrder,
			final Order original) {
		String result = "";
		final List<OrderItem> orderItems = anOrder.getItems(), originalItems = original
				.getItems();
		for (final OrderItem myItem : orderItems)
			if (!originalItems.contains(myItem)) {
				result += "Insert into OrderItem (menuItem, restaurantOrder, quantity, notes, menuType) values "
						+ " ("
						+ myItem.getMenuItemID()
						+ ", "
						+ anOrder.getId()
						+ ", "
						+ myItem.getQuantity()
						+ ", '"
						+ myItem.getNotes()
						+ "', '"
						+ myItem.getType().getValue() + "'); ";
			} else if (myItem.isDirty()) {
				result += "Update OrderItem set menuItem="
						+ myItem.getMenuItemID() + ", restaurantOrder="
						+ anOrder.getId() + ", quantity="
						+ myItem.getQuantity() + ", notes='"
						+ myItem.getNotes() + ", menutype='"
						+ myItem.getType().getValue() + "' where id="
						+ myItem.getId() + "; ";
			}
		for (final OrderItem myItem : originalItems)
			if (!orderItems.contains(myItem)) {
				result += "Delete from OrderItem where id=" + myItem.getId()
						+ "; ";
			}
		return result;
	}

	public static boolean insertTax(final User aUser, final Tax aTax) {
		if (aUser.getType() != UserType.MANAGER)
			return false;
		final String query = "Insert into Tax (name, description, percent) values "
				+ "('"
				+ aTax.getName()
				+ "', '"
				+ aTax.getDescription()
				+ "', " + aTax.getPercent() + "') " + "returning id;";
		try {
			final ResultSet results = DataAdapter.dbConnection
					.createStatement().executeQuery(query);
			if (results.next()) {
				aTax.setId(results.getInt("id"));
				return true;
			}
			return false;
		} catch (final SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean deleteTax(final User aUser, final Tax aTax) {
		if (aUser.getType() != UserType.MANAGER)
			return false;
		final String query = "Update Tax set isActive=false where id="
				+ aTax.getId() + ";";
		try {
			DataAdapter.dbConnection.createStatement().execute(query);
			return true;
		} catch (final SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static List<Tax> getTaxes(final User aUser) {
		final List<Tax> taxes = new ArrayList<Tax>();
		final String query = "Select id, name, description, percent from Tax where isActive=true;";
		try {
			final ResultSet results = DataAdapter.dbConnection
					.createStatement().executeQuery(query);
			while (results.next()) {
				final Tax myTax = new Tax();
				myTax.setId(results.getInt("id"));
				myTax.setName(results.getString("name"));
				myTax.setDescription(results.getString("description"));
				myTax.setPercent(results.getDouble("percent"));
				myTax.setDirty(false);
				taxes.add(myTax);
			}
			return taxes;
		} catch (final SQLException e) {
			e.printStackTrace();
			return new ArrayList<Tax>();
		}
	}

	public static boolean updateTax(final User aUser, final Tax aTax) {
		if (aUser.getType() != UserType.MANAGER)
			return false;
		final String query = "Update Tax set " + "name='" + aTax.getName()
				+ "', " + "description='" + aTax.getDescription() + "', "
				+ "percent=" + aTax.getPercent() + " where id=" + aTax.getId()
				+ ";";
		try {
			DataAdapter.dbConnection.createStatement().execute(query);
			return true;
		} catch (final SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean insertMenuItem(final User aUser, final MenuItem anItem) {
		if (aUser.getType() != UserType.MANAGER)
			return false;
		final String query = "Insert into MenuItem (name, description, price, menuType) values "
				+ "('"
				+ anItem.getName()
				+ "', "
				+ "'"
				+ anItem.getDescription()
				+ "', "
				+ anItem.getPrice()
				+ ", "
				+ "'" + anItem.getType().getValue() + "') returning id;";
		try {
			final ResultSet result = DataAdapter.dbConnection.createStatement()
					.executeQuery(query);
			if (result.next()) {
				anItem.setID(result.getInt("id"));
				anItem.setDirty(false);
				return true;
			}
			return false;
		} catch (final SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static List<MenuItem> getMenuItems(final User aUser) {
		final List<MenuItem> items = new ArrayList<MenuItem>();
		if ((aUser.getType() != UserType.MANAGER)
				&& (aUser.getType() != UserType.WAITER))
			return items;
		final String query = "Select id, name, description, price, menuType from MenuItem where active=true order by name, menuType;";
		try {
			final ResultSet results = DataAdapter.dbConnection
					.createStatement().executeQuery(query);
			while (results.next()) {
				final MenuItem myItem = new MenuItem();
				myItem.setID(results.getInt("id"));
				myItem.setName(results.getString("name"));
				myItem.setDescription(results.getString("description"));
				myItem.setPrice(results.getDouble("price"));
				myItem.setType(MenuItemType.getType(results.getString(
						"menuType").charAt(0)));
				myItem.setDirty(false);
				items.add(myItem);
			}
		} catch (final SQLException e) {
			e.printStackTrace();
			return null;
		}
		return items;
	}

	public static boolean updateMenuItem(final User aUser, final MenuItem anItem) {
		if (aUser.getType() != UserType.MANAGER)
			return false;
		if (!anItem.isDirty())
			return true;
		final String query = "Update MenuItem set " + "name='"
				+ anItem.getName() + "', " + "description='"
				+ anItem.getDescription() + "', " + "price="
				+ anItem.getPrice() + ", " + "menuType='"
				+ anItem.getType().getValue() + "' " + "where id="
				+ anItem.getID() + ";";
		try {
			final boolean result = DataAdapter.dbConnection.createStatement()
					.execute(query);
			if (result) {
				anItem.setDirty(false);
			}
			return result;
		} catch (final SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean deleteMenuItem(final User aUser, final MenuItem anItem) {
		if (aUser.getType() != UserType.MANAGER)
			return false;
		final String query = "Update MenuItem set active=false where id="
				+ anItem.getID() + ";";
		try {
			return DataAdapter.dbConnection.createStatement().execute(query);
		} catch (final SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	static Connection getConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			final Properties props = new Properties();
			props.setProperty("user", "postgres");
			props.setProperty("password", "Typewriter39");
			return DriverManager
					.getConnection(
							"jdbc:postgresql://cortex.ddns.uark.edu:5432/cortexrestaurant",
							props);
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (final SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
