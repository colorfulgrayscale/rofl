package gui.manager;

import gui.common.RestaurantFloor;
import gui.common.RestaurantPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import objects.common.ToolsAvailable;
import objects.table.Table;
import objects.user.User;
import objects.wall.Wall;
import database.DataAdapter;
import database.DatabaseListener;
import database.DatabaseMessage;

public class LayoutEditor extends RestaurantPanel implements ActionListener,
Tweenable, MouseListener {
	private static final long serialVersionUID = -377445909075882049L;
	private JInternalFrame layoutEditor;
	private JInternalFrame toolBox;
	private final ActionListener parentCallingClass;
	private RestaurantFloor restaurant;
	private ToolsAvailable tool = ToolsAvailable.NONE;
	private Thread t;
	private final User user;

	public LayoutEditor(final int width, final int height, final User aUser,
			final DatabaseListener aDBListener,
			final ActionListener parentCallingClass) {
		super(aUser, aDBListener);
		super.setBackground(new Color(0, 78, 152));
		user = aUser;
		this.parentCallingClass = parentCallingClass;
		buildEditor(width, height);
		buildToolbox();
		consolidateDatabase();
	}

	private void consolidateDatabase() {
		dbListener.addMessage(this, DatabaseMessage.UPDATE_TABLES);
		dbListener.addMessage(this, DatabaseMessage.UPDATE_WALLS);
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
		layoutEditor = new JInternalFrame("Floor Layout Editor", false, false,
				false, false);
		layoutEditor.setBounds(180, 5, 200, 100);
		layoutEditor.setVisible(true);
		layoutEditor.setSize(width + widthPadding, height + heightPadding);
		layoutEditor.setFrameIcon(null);

		restaurant = new RestaurantFloor(0, 0, width, height, true, user, this);
		restaurant.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5,
				Color.DARK_GRAY));
		layoutEditor.getContentPane().add(restaurant);
		add(layoutEditor, new Integer(1));
	}

	private void buildToolbox() {
		toolBox = new JInternalFrame("Toolbox", false, false, false, false);
		toolBox.setBounds(1, 5, 175, 250);
		toolBox.setVisible(true);
		toolBox.setBackground(Color.white);
		toolBox.setFrameIcon(null);

		final Box p = new Box(BoxLayout.Y_AXIS);
		final ButtonGroup group = new ButtonGroup();
		final String[] radioButtonsList = { "Move", "Delete", "Add Table",
				"Add Booth", "Add Rectangular Wall", "Add Circular Wall" };
		for (int i = 0; i < radioButtonsList.length; ++i) {
			final JRadioButton b = new JRadioButton(radioButtonsList[i]);
			b.setOpaque(true);
			b.setBackground(Color.white);
			group.add(b);
			p.add(b);
			b.addActionListener(this);
			b.addActionListener(parentCallingClass);

		}
		p.add(Box.createRigidArea(new Dimension(0, 25)));

		final JButton backButton = new JButton();
		backButton.setText("Commit Floorplan");
		backButton.addActionListener(this);
		backButton.addActionListener(parentCallingClass);
		p.add(backButton);

		toolBox.getContentPane().add(p);
		add(toolBox, new Integer(1));
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand() == "Move") {
			tool = ToolsAvailable.MOVE;
		}
		if (e.getActionCommand() == "Delete") {
			tool = ToolsAvailable.DELETE;
		}
		if (e.getActionCommand() == "Add Table") {
			tool = ToolsAvailable.ADD_TABLE;
		}
		if (e.getActionCommand() == "Add Booth") {
			tool = ToolsAvailable.ADD_BOOTH;
		}
		if (e.getActionCommand() == "Add Rectangular Wall") {
			tool = ToolsAvailable.DRAW_SOLID_RECT;
		}
		if (e.getActionCommand() == "Add Circular Wall") {
			tool = ToolsAvailable.DRAW_SOLID_CIRCLE;
		}
		if (e.getActionCommand() == "Commit Floorplan") {
			dbListener.removeMessage(this, DatabaseMessage.UPDATE_TABLES);
			dbListener.removeMessage(this, DatabaseMessage.UPDATE_WALLS);
			final Runnable r = new Runnable() {
				public void run() {
					final List<Table> oldTables = DataAdapter.getTables(user,
							restaurant.getSize());
					final List<Table> currentTables = restaurant.getTables();
					final List<Wall> oldWalls = DataAdapter.getWalls(user,
							restaurant.getSize());
					final List<Wall> currentWalls = restaurant.getWalls();

					outer: for (final Table myTable : currentTables) {
						for (final Table aTable : oldTables)
							if (aTable.getId() == myTable.getId()) {
								DataAdapter.updateTable(user, myTable,
										restaurant.getSize());
								continue outer;
							}
						DataAdapter.insertTable(user, myTable);
					}
					outer2: for (final Table aTable : oldTables) {
						for (final Table myTable : currentTables)
							if (aTable.getId() == myTable.getId()) {
								continue outer2;
							}
						DataAdapter.deleteTable(user, aTable);
					}
					for (final Wall myWall : oldWalls) {
						DataAdapter.deleteWall(user, myWall);
					}
					for (final Wall myWall : currentWalls) {
						DataAdapter.insertWall(user, myWall);
					}
				}
			};
			final Thread moveOut = new Thread(r);
			moveOut.start();
			try
			{
				moveOut.join();
			} 
			catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}
			finally
			{
				dbListener.addMessage(this, DatabaseMessage.UPDATE_TABLES);
				dbListener.addMessage(this, DatabaseMessage.UPDATE_WALLS);
			}

		}
		restaurant.setTool(tool);
	}

	@Override
	public void databaseUpdate(final DatabaseMessage aMessage) {
		if (aMessage == DatabaseMessage.UPDATE_TABLES) {
			restaurant.setTables(DataAdapter.getTables(user, restaurant
					.getSize()));
		} else if (aMessage == DatabaseMessage.UPDATE_WALLS) {
			restaurant.setWalls(DataAdapter
					.getWalls(user, restaurant.getSize()));
		}
	}

	@Override
	public void fadeColor(final Color c) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAnimationInProgress() {
		// TODO Auto-generated method stub
		return t.isAlive();
	}

	public List<Table> getTables() {
		return restaurant.getTables();
	}

	public List<Wall> getWalls() {
		return restaurant.getWalls();
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
		// TODO Auto-generated method stub

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
