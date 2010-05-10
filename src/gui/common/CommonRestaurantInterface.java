package gui.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JRadioButton;

import objects.animation.MotionTween;
import objects.animation.Tweenable;
import objects.animation.Zoom;
import objects.common.SelectedAction;
import objects.order.Order;
import objects.table.Table;
import objects.table.TableStatus;
import objects.user.User;
import objects.user.UserType;
import database.DataAdapter;
import database.DatabaseListener;
import database.DatabaseMessage;

public class CommonRestaurantInterface extends RestaurantPanel implements
		ActionListener, Tweenable, MouseListener {
	private static final long serialVersionUID = -377445909075882049L;
	private JInternalFrame layoutEditor;
	private JInternalFrame toolBox;
	private RestaurantFloor restaurant;
	private final ActionListener parentCallingClass;
	private SelectedAction action;
	private Thread t;
	private final User user;
	private Table tableName;

	private InfoPanel info;

	public CommonRestaurantInterface(final int width, final int height,
			final User aUser, final DatabaseListener aDBListener,
			final ActionListener parentCallingClass) {
		super(aUser, aDBListener);

		user = aUser;
		info = null;
		super.setBackground(new Color(0, 78, 152));
		this.parentCallingClass = parentCallingClass;
		buildEditor(width, height);
		buildToolbox();
		consolidateDatabase();
		action = SelectedAction.NONE;
	}

	private void consolidateDatabase() {
		dbListener.addMessage(this, DatabaseMessage.UPDATE_TABLES);
		dbListener.addMessage(this, DatabaseMessage.UPDATE_WALLS);
		dbListener.addMessage(this, DatabaseMessage.UPDATE_ORDERS);
		final Runnable r = new Runnable() {
			public void run() {
				restaurant.setTables(DataAdapter.getTables(user, restaurant
						.getSize()));
				restaurant.setWalls(DataAdapter.getWalls(user, restaurant
						.getSize()));
			}
		};
		final Thread moveOut = new Thread(r);
		moveOut.start();
	}

	private void buildEditor(final int width, final int height) {

		final int widthPadding = 8;
		final int heightPadding = 30;
		layoutEditor = new JInternalFrame("Restaurant", false, false, false,
				false);
		layoutEditor.setBounds(180, 5, 200, 100);
		layoutEditor.setVisible(true);
		layoutEditor.setSize(width + widthPadding, height + heightPadding);
		layoutEditor.setFrameIcon(null);

		restaurant = new RestaurantFloor(0, 0, width, height, false, user, this);
		restaurant.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5,
				Color.DARK_GRAY));
		restaurant.addMouseListener(this);
		addMouseListener(this);
		layoutEditor.getContentPane().add(restaurant);
		add(layoutEditor, new Integer(1));
	}

	private void buildToolbox() {
		toolBox = new JInternalFrame("Toolbox", false, false, false, false);
		toolBox.setBounds(1, 5, 175, 250);
		toolBox.setVisible(true);
		toolBox.setBackground(Color.DARK_GRAY);
		toolBox.setFrameIcon(null);

		final Box p = new Box(BoxLayout.Y_AXIS);

		final ButtonGroup group = new ButtonGroup();

		final ArrayList<String> radioButtonsList = new ArrayList<String>();
		if (user.getType() == UserType.MANAGER) {
			radioButtonsList.add("Change Status");
			radioButtonsList.add("Add Order");
		} else if (user.getType() == UserType.BUSBOY) {
			radioButtonsList.add("Change Status");
		} else if (user.getType() == UserType.WAITER) {
			radioButtonsList.add("Change Status");
			radioButtonsList.add("Add Order");
		} else if (user.getType() == UserType.HOST) {
			radioButtonsList.add("Change Status");
		}

		for (int i = 0; i < radioButtonsList.size(); ++i) {
			final JRadioButton b = new JRadioButton(radioButtonsList.get(i));
			b.setOpaque(true);
			b.setBackground(Color.white);
			group.add(b);
			p.add(b);
			b.addActionListener(this);
			b.addActionListener(parentCallingClass);

		}
		p.add(Box.createRigidArea(new Dimension(0, 25)));

		final JButton backButton = new JButton();
		backButton.setText("Log Out");
		backButton.addActionListener(parentCallingClass);
		p.add(backButton);

		toolBox.getContentPane().add(p);
		add(toolBox, new Integer(1));
	}

	@Override
	public void actionPerformed(final ActionEvent e) {

		dbListener.removeMessage(this, DatabaseMessage.UPDATE_TABLES);
		if (e.getActionCommand() == "Close") {
			destryInfoPanel();
		} else if (e.getActionCommand() == "Mark as Unavailable") {
			destryInfoPanel();
			restaurant.setTableStatus(tableName.getName(),
					TableStatus.UNAVAILABLE);
			final Table myTable = restaurant.getTable(tableName.getName());
			DataAdapter.updateTable(user, myTable, myTable.getCanvasSize());
		} else if (e.getActionCommand() == "Mark as Dirty") {
			destryInfoPanel();
			restaurant.setTableStatus(tableName.getName(), TableStatus.DIRTY);
			final Table myTable = restaurant.getTable(tableName.getName());
			DataAdapter.updateTable(user, myTable, myTable.getCanvasSize());
			final Order order = DataAdapter.selectOrder(user, tableName);
			if (order != null) {
				order.setCustomerLeft(new Date(System.currentTimeMillis()));
				DataAdapter.updateOrder(user, order);
			}
		} else if (e.getActionCommand() == "Mark as Open") {
			destryInfoPanel();
			restaurant.setTableStatus(tableName.getName(), TableStatus.OPEN);
			final Table myTable = restaurant.getTable(tableName.getName());
			DataAdapter.updateTable(user, myTable, myTable.getCanvasSize());
		} else if (e.getActionCommand() == "Mark as Occupied") {
			destryInfoPanel();
			restaurant
					.setTableStatus(tableName.getName(), TableStatus.OCCUPIED);
			final Table myTable = restaurant.getTable(tableName.getName());
			DataAdapter.updateTable(user, myTable, myTable.getCanvasSize());
			final Order order = new Order();
			order.setTable(myTable.getId());
			order.setSeatedTime(new Date(System.currentTimeMillis()));
			DataAdapter.insertOrder(user, order);
		} else if (e.getActionCommand() == "Change Status") {
			action = SelectedAction.CHANGESTATUS;
		} else if (e.getActionCommand() == "Add Order") {
			action = SelectedAction.MODIFYORDERS;
		} else if (e.getActionCommand() == "Assign Tables") {
			action = SelectedAction.ASSIGNTABLES;
		} else if (e.getActionCommand() == "Save Order") {
			destryInfoPanel();
		}

		dbListener.addMessage(this, DatabaseMessage.UPDATE_TABLES);
	}

	@Override
	public void databaseUpdate(final DatabaseMessage aMessage) {
		if (aMessage == DatabaseMessage.UPDATE_TABLES) {
			dbListener.removeMessage(this, DatabaseMessage.UPDATE_TABLES);
			restaurant.setTables(DataAdapter.getTables(user, restaurant
					.getSize()));
			dbListener.addMessage(this, DatabaseMessage.UPDATE_TABLES);
		} else if (aMessage == DatabaseMessage.UPDATE_WALLS) {
			dbListener.removeMessage(this, DatabaseMessage.UPDATE_WALLS);
			restaurant.setWalls(DataAdapter
					.getWalls(user, restaurant.getSize()));
			dbListener.addMessage(this, DatabaseMessage.UPDATE_WALLS);
		} else if (aMessage == DatabaseMessage.UPDATE_ORDERS) {
			dbListener.removeMessage(this, DatabaseMessage.UPDATE_ORDERS);
			final List<Order> orderList = DataAdapter.getOrders(user);
			final List<Table> tableList = DataAdapter.getTables(user, this
					.getSize());
			for (final Order tempOrder : orderList) {
				if ((tempOrder.getOrderReady() != null)
						&& (tempOrder.getOrderDelivered() == null)) {
					for (final Table myTable : tableList) {
						restaurant.setTableStatus(myTable.getName(),
								TableStatus.ORDERREADY);
					}
				}
			}
			dbListener.addMessage(this, DatabaseMessage.UPDATE_ORDERS);
		}
	}

	public void moveToNewLocation(final Point p, final float animationSpeed) {
		t = new Thread(new MotionTween(this.getLocation(), p, animationSpeed,
				this));
		t.start();

	}

	@Override
	public void setNewLocation(final Point p) {
		this.setLocation(p);
		this.repaint();
	}

	@Override
	public boolean isAnimationInProgress() {
		return t.isAlive();
	}

	@Override
	public void fadeColor(final Color c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(final MouseEvent e) {
		if (e.getSource() instanceof Table) {
			final Table tempTable = (Table) e.getSource();
			if (tempTable.getStatus() == TableStatus.ORDERREADY) {
				restaurant.setTableStatus(tempTable.getName(),
						TableStatus.OCCUPIED);
				final Table myTable = restaurant.getTable(tempTable.getName());
				final Order myorder = DataAdapter.selectOrder(user, myTable);
				myorder.setOrderDelivered(new Date(System.currentTimeMillis()));
				DataAdapter.updateOrder(user, myorder);
			}
			if (action != SelectedAction.NONE) {

				if (info == null) {
					createInfoPanel(e, tempTable);
				} else {
					destryInfoPanel();
				}

			}
		} else if (info == null) {
			destryInfoPanel();
		}
	}

	private void createInfoPanel(final MouseEvent e, final Table tableName) {

		final Table tempTable = (Table) e.getSource();
		this.tableName = tableName;
		info = new InfoPanel(this, action, tableName, user);
		add(info);
		setLayer(info, 10);
		info.setBounds(tempTable.getX(), tempTable.getY(), info.getWidth(),
				info.getHeight());
		info.setVisible(true);
		int xLoc = tempTable.getLocation().x - 3 * tempTable.getWidth();
		int yLoc = tempTable.getLocation().y - tempTable.getHeight();
		if (xLoc < this.getLocation().x) {
			xLoc = tempTable.getLocation().x + 5 * tempTable.getWidth();
		}
		if (yLoc < this.getLocation().y) {
			yLoc = tempTable.getLocation().y - tempTable.getHeight();
		}
		info.moveToNewLocation(new Point(xLoc, yLoc), 500);
		info.Zoom(0, 1, 2);
		repaint();
	}

	private void destryInfoPanel() {
		if (info != null) {
			final Runnable r = new Runnable() {
				public void run() {
					final Thread t = new Thread(new Zoom(1.0, 0.0, 1, info));
					t.start();
					try {
						t.join(500);
						if (t.isAlive()) {
							t.interrupt();
						}
						remove(info);
						info = null;
						repaint();
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			final Thread moveOut = new Thread(r);
			moveOut.start();

		}
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(final MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(final MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
