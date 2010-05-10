package gui.manager;

import gui.common.RestaurantPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import objects.animation.MotionTween;
import objects.animation.Tweenable;
import objects.order.MenuItem;
import objects.user.User;
import database.DataAdapter;
import database.DatabaseListener;
import database.DatabaseMessage;

public class MenuManager extends RestaurantPanel implements Tweenable,
		ActionListener {
	private static final long serialVersionUID = 8231088019872677981L;
	private final JTable table;
	private final DefaultTableModel model;
	private List<MenuItem> items;
	private final String[] headers = { "Name", "Description", "Price", "Type" };
	private final JButton add, change, delete;
	private final JScrollPane tableScroll;

	public MenuManager(final User aUser, final DatabaseListener aDBListener) {
		super(aUser, aDBListener);
		setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(new BorderLayout(0, 0));

		final JPanel topPanel = new JPanel();

		topPanel.setBackground(Color.black);
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		model = new DefaultTableModel(headers, 4) {
			private static final long serialVersionUID = -3112145738015139281L;

			@Override
			public boolean isCellEditable(final int x, final int y) {
				return false;
			}
		};

		table = new JTable(model);
		add = new JButton("Add");
		add.addActionListener(this);
		change = new JButton("Change");
		change.addActionListener(this);
		delete = new JButton("Delete");
		delete.addActionListener(this);
		tableScroll = new JScrollPane(table);
		updateTable();

		topPanel.add(add);
		topPanel.add(change);
		topPanel.add(delete);
		new JButton("Log Out");
		topPanel.add(Box.createRigidArea(new Dimension(40, 0)));
		// topPanel.add(btnLogout);
		add(topPanel, BorderLayout.NORTH);
		add(tableScroll, BorderLayout.CENTER);
		setVisible(true);
	}

	public void updateTable() {
		items = DataAdapter.getMenuItems(user);
		if (items != null) {
			model.setRowCount(0);
			for (final MenuItem myItem : items) {
				model.addRow(new Object[] { myItem.getName(),
						myItem.getDescription(), myItem.getPrice(),
						myItem.getType() });
			}
		}
	}

	@Override
	public void databaseUpdate(final DatabaseMessage aMessage) {

	}

	public static void main(final String[] args) {
		DataAdapter.open();
		final MenuManager myManager = new MenuManager(DataAdapter.login(
				"manager", "manager"), new DatabaseListener());
		final JFrame myFrame = new JFrame();
		myFrame.add(myManager);
		myFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		myFrame.pack();
		myFrame.addWindowListener(new WindowListener() {
			public void windowActivated(final WindowEvent arg0) {
			}

			public void windowClosed(final WindowEvent arg0) {
				DataAdapter.close();
			}

			public void windowClosing(final WindowEvent arg0) {
			}

			public void windowDeactivated(final WindowEvent arg0) {
			}

			public void windowDeiconified(final WindowEvent arg0) {
			}

			public void windowIconified(final WindowEvent arg0) {
			}

			public void windowOpened(final WindowEvent arg0) {
			}
		});
		myFrame.setVisible(true);
	}

	public void actionPerformed(final ActionEvent arg0) {
		if (arg0.getSource() == add) {
			final MenuDialog myDialog = new MenuDialog(null, true);
			myDialog.setVisible(true);
			if (myDialog.getResponse() == JOptionPane.OK_OPTION) {
				DataAdapter.insertMenuItem(user, myDialog.getMenuItem());
				updateTable();
			}
		} else if (arg0.getSource() == change) {
			if (table.getSelectedRow() < 0)
				return;
			final MenuItem myItem = items.get(table.getSelectedRow());
			final MenuDialog myDialog = new MenuDialog(myItem, true);
			myDialog.setVisible(true);
			if (myDialog.getResponse() == JOptionPane.OK_OPTION) {
				DataAdapter.updateMenuItem(user, myItem);
				updateTable();
			}
		} else if (arg0.getSource() == delete) {
			if (table.getSelectedRow() < 0)
				return;
			final MenuItem myItem = items.get(table.getSelectedRow());
			final int result = JOptionPane.showConfirmDialog(MenuManager.this,
					"Do you wish to delete " + myItem.getName() + "?");
			if (result == JOptionPane.YES_OPTION) {
				DataAdapter.deleteMenuItem(user, myItem);
				updateTable();
			}
		}
	}

	@Override
	public void fadeColor(final Color c) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAnimationInProgress() {
		// TODO Auto-generated method stub
		return false;
	}

	public void moveToNewLocation(final Point p, final float animationSpeed) {
		final Thread t = new Thread(new MotionTween(this.getLocation(), p,
				animationSpeed, this));
		t.start();
	}

	@Override
	public void setNewLocation(final Point p) {
		this.setLocation(p);
		this.repaint();
	}

}
