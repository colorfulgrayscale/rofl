package gui.cook;

import gui.common.RestaurantPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import objects.animation.MotionTween;
import objects.animation.Tweenable;
import objects.order.Order;
import objects.user.User;
import database.DataAdapter;
import database.DatabaseListener;
import database.DatabaseMessage;

public class CookScreen extends RestaurantPanel implements ActionListener,
MouseListener, Tweenable {
	private static final long serialVersionUID = 8359743330213976841L;
	private final ArrayList<OrderPanel> orderPanels;
	private Point lastPoint = new Point(0, 0);
	private final int panelWidth = OrderPanel.getPanelwidth();
	private final int panelHeight = OrderPanel.getPanelheight();
	private final int paddingX = 10;
	private final int paddingY = 10;
	private final JScrollPane scrollPane;
	private int orderCount = 1;
	private final JPanel mainPanel;
	private final JLabel updateCounter;

	public CookScreen(final User aUser, final ActionListener parentClass,
			final DatabaseListener aDBListener) {
		super(aUser, aDBListener);
		setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(new BorderLayout(0, 0));
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.addMouseListener(this);
		scrollPane = new JScrollPane(mainPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);
		final JPanel topPanel = new JPanel();
		add(topPanel, BorderLayout.NORTH);
		topPanel.setBackground(Color.black);
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		final JButton btnLogout = new JButton("Log Out");
		btnLogout.addActionListener(parentClass);
		btnLogout.setHorizontalAlignment(SwingConstants.LEFT);
		topPanel.add(btnLogout);
		final JButton btnOpenOlderOrder = new JButton("Open Archived Order");
		btnOpenOlderOrder.addActionListener(this);
		btnOpenOlderOrder.setHorizontalAlignment(SwingConstants.LEFT);
		topPanel.add(btnOpenOlderOrder);
		updateCounter = new JLabel("Total Orders: 0");
		updateCounter.setFont(new Font(updateCounter.getFont().getFontName(),
				Font.BOLD, 14));
		updateCounter.setForeground(Color.white);
		updateCounter.setHorizontalAlignment(SwingConstants.RIGHT);
		topPanel.add(Box.createRigidArea(new Dimension(40, 0)));

		topPanel.add(updateCounter);
		orderPanels = new ArrayList<OrderPanel>();
		setVisible(true);
		DataAdapter.getOrders(user);
		dbListener.addMessage(this, DatabaseMessage.UPDATE_ORDERS);
	}

	public void updateCounter() {
		updateCounter.setText("Total Orders: " + orderPanels.size());
		updateCounter.repaint();
	}

	public void addTestOrders(final int total) {
		Order tempOrder;
		for (int i = 0; i < total; i++) {
			tempOrder = new Order();
			tempOrder.setId(orderCount);
			orderCount++;
			addOrder(tempOrder);
		}
	}

	public void addOrder(final Order order) {
		final OrderPanel panel = new OrderPanel(order, order.getId(), this);
		panel.setLocation(getWidth() + 10, 0);
		mainPanel.add(panel);
		orderPanels.add(panel);
		updateCounter();
		final Point placeToPutIt = getNextSpot();
		panel.moveToNewLocation(placeToPutIt, 1);
		panel.setFinalResting(placeToPutIt);
		mainPanel.repaint();
	}

	private int findPanelID(final int pID) {
		for (int i = 0; i < orderPanels.size(); i++)
			if (orderPanels.get(i).getpID() == pID)
				return i;
		return -1;
	}

	private void updateNextPosition() {
		try {
			lastPoint = new Point(orderPanels.get(orderPanels.size() - 2)
					.getFinalResting().x, orderPanels.get(
							orderPanels.size() - 2).getFinalResting().y);

		} catch (final Exception e) {
			lastPoint = new Point(0, 0);
		}
		updateScrollBars();
	}

	private void removeOrder(final OrderPanel panel) {
		updateCounter();
		final Runnable r = new Runnable() {
			public void run() {
				final Thread t = new Thread(new MotionTween(
						panel.getLocation(), new Point(-panelWidth,
								-panelHeight), 500, panel));
				t.start();
				try {
					t.join();
					mainPanel.remove(panel);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		final Thread moveOut = new Thread(r);
		moveOut.start();
	}

	public void removeOrderID(final int pID) {
		updateNextPosition();
		final int index = findPanelID(pID);
		final ArrayList<OrderPanel> tempList = new ArrayList<OrderPanel>();
		tempList.addAll(orderPanels);
		final OrderPanel panel = orderPanels.get(index);
		orderPanels.remove(index);
		removeOrder(panel);
		for (int i = index + 1; i < tempList.size(); i++) {
			tempList.get(i).moveToNewLocation(
					tempList.get(i - 1).getFinalResting(), 1);
		}
	}

	private Point getNextSpot() {
		final int xPadding = paddingX;
		final int yPadding = paddingY;
		final Point returnPoint = new Point();
		if ((lastPoint.x == 0) && (lastPoint.y == 0)) {
			lastPoint.x = xPadding;
			lastPoint.y = yPadding;
			return lastPoint;
		}
		returnPoint.x = lastPoint.x + panelWidth + xPadding;
		returnPoint.y = lastPoint.y;
		if (returnPoint.x + panelWidth >= getWidth()) {
			returnPoint.x = xPadding;
			returnPoint.y = lastPoint.y + panelHeight + yPadding;
		}
		lastPoint = returnPoint;
		updateScrollBars();
		return returnPoint;
	}

	private void updateScrollBars() {
		mainPanel.setPreferredSize(new Dimension(-1, lastPoint.y + panelHeight
				+ paddingY));
		mainPanel.repaint();
	}

	public static void main(final String[] args) {
		DataAdapter.open();
		final DatabaseListener listener = new DatabaseListener();
		final JFrame testFrame = new JFrame();
		testFrame
		.setTitle("Restaurant Organization and Functional Layout - Cook Screen");
		testFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final CookScreen test = new CookScreen(DataAdapter
				.login("cook", "cook"), null, listener);
		testFrame.setBounds(0, 0, testFrame.getWidth(), testFrame.getHeight());
		testFrame.add(test);
		testFrame.setVisible(true);
		test.setVisible(true);
		DataAdapter.close();
	}

	@Override
	public void databaseUpdate(final DatabaseMessage aMessage) {
		if (aMessage == DatabaseMessage.UPDATE_ORDERS) {
			dbListener.removeMessage(this, DatabaseMessage.UPDATE_ORDERS);
			final List<Order> temp = DataAdapter.getOrders(user);
			for (final Order myOrder : temp)
				if (findPanelID(myOrder.getId()) == -1) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							addOrder(myOrder);
						}
					});
				}
			final List<Order> toRemove = new ArrayList<Order>();
			outer: for (final OrderPanel myPanel : orderPanels) {
				for (final Order myOrder : temp)
					if (myOrder.getId() == myPanel.getpID()) {
						continue outer;
					}
				final Order remove = new Order();
				remove.setId(myPanel.getpID());
				toRemove.add(remove);
			}
			for (final Order myOrder : toRemove) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {

						removeOrderID(myOrder.getId());
					}
				});
			}
			dbListener.addMessage(this, DatabaseMessage.UPDATE_ORDERS);
		}
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if ((e.getSource() instanceof JButton)
				&& (e.getActionCommand() == "Order Complete")) {
			final JButton button = (JButton) e.getSource();
			button.setEnabled(false);
			try {
				final int value = Integer.parseInt(button.getName());
				final Order result = DataAdapter.selectOrder(user, value);
				if (result != null) {
					result.setOrderReady(new Date(System.currentTimeMillis()));
					DataAdapter.updateOrder(user, result);
					removeOrderID(value);
				}
			} catch (final Exception ex) {

			}
		}
		if (e.getActionCommand() == "Open Archived Order") {
			final String orderID = JOptionPane.showInputDialog(null,
					"Open Archived Order", "Enter Order ID",
					JOptionPane.QUESTION_MESSAGE);
			final Order result = DataAdapter.selectOrder(user, Integer
					.parseInt(orderID));
			if (result != null) {
				addOrder(result);
			}
		}
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
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
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fadeColor(final Color c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNewLocation(final Point p) {
		this.setLocation(p);
		this.repaint();
	}

	@Override
	public boolean isAnimationInProgress() {
		return false;
	}
}
