package gui.common;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import objects.common.ToolsAvailable;
import objects.table.RoundTable;
import objects.table.SquareTable;
import objects.table.Table;
import objects.table.TableStatus;
import objects.user.User;
import objects.user.UserType;
import objects.wall.CircularWall;
import objects.wall.RectangularWall;
import objects.wall.Wall;

public class RestaurantFloor extends JPanel implements MouseListener,
		MouseMotionListener, ActionListener {
	private static final long serialVersionUID = -2158822093329864564L;
	private Table tableToDrag;
	private Wall wallToDrag;
	private ArrayList<Table> tablesList;
	private ArrayList<Wall> wallList;
	private ArrayList<Wall> foundationList;
	private ArrayList<JComponent> objects;
	private TableStatus tempStatus;
	private Point tempLocation;
	private final Dimension canvasSize;
	private ToolsAvailable selectedTool;
	private boolean isWallToolInAction = false;
	private final Boolean isFloorEditable;
	private Cursor toolCursor;
	private Cursor defaultCursor;
	private Cursor corshairCursor;
	private Cursor moveCursor;
	private Cursor handCursor;
	private final User user;
	private final MouseListener parentClass;

	public RestaurantFloor(final int x, final int y, final int width,
			final int height, final Boolean isFloorEditable, final User user,
			final MouseListener parentClass) {
		canvasSize = new Dimension(width, height);
		this.setBounds(x, y, width, height);
		this.isFloorEditable = isFloorEditable;
		this.parentClass = parentClass;

		this.user = user;
		init();
	}

	public void init() {

		tablesList = new ArrayList<Table>();
		wallList = new ArrayList<Wall>();
		objects = new ArrayList<JComponent>();
		foundationList = new ArrayList<Wall>();
		defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
		corshairCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
		moveCursor = new Cursor(Cursor.MOVE_CURSOR);
		handCursor = new Cursor(Cursor.HAND_CURSOR);
		setMinimumSize(canvasSize);
		setSize(canvasSize);
		setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);
		setBackground(Color.black);
		setVisible(true);
		selectedTool = ToolsAvailable.NONE;
		addFoundationStruts();
		setCursor(defaultCursor);

	}

	public void setTool(final ToolsAvailable tool) {
		selectedTool = tool;
		if (tool == ToolsAvailable.MOVE) {
			toolCursor = moveCursor;
		} else if ((tool == ToolsAvailable.DRAW_SOLID_CIRCLE)
				|| (tool == ToolsAvailable.DRAW_SOLID_RECT)) {
			toolCursor = corshairCursor;
		} else if ((tool == ToolsAvailable.ADD_BOOTH)
				|| (tool == ToolsAvailable.ADD_TABLE)
				|| (tool == ToolsAvailable.DELETE)) {
			toolCursor = handCursor;
		} else {
			toolCursor = defaultCursor;
		}
	}

	@Override
	public void actionPerformed(final ActionEvent action) {
		repaint();
	}

	@Override
	public void mouseDragged(final MouseEvent e) {
		if ((user.getType() == UserType.MANAGER) && isFloorEditable) {
			tableMouseDragged(e);
			wallToolMouseDragged(e);
		}
	}

	@Override
	public void mousePressed(final MouseEvent e) {
		if ((user.getType() == UserType.MANAGER) && isFloorEditable) {
			tableMousePressed(e);
			wallToolMousePressed(e);
			PanelMousePressed(e);
		}
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		if ((user.getType() == UserType.MANAGER) && isFloorEditable) {
			tableMouseReleased(e);
			wallToolMouseReleased(e);
		}
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
		if ((user.getType() == UserType.MANAGER) && isFloorEditable) {
			PanelClicked(e);
		}
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
	}

	@Override
	public void mouseExited(final MouseEvent e) {
	}

	@Override
	public void mouseMoved(final MouseEvent e) {
		if ((user.getType() == UserType.MANAGER) && isFloorEditable) {
			updateCursor(e);
		}
	}

	private void updateCursor(final MouseEvent e) {
		if ((selectedTool == ToolsAvailable.MOVE)
				|| (selectedTool == ToolsAvailable.DELETE)) {
			if ((e.getSource() instanceof Table)
					|| (e.getSource() instanceof Wall)) {
				setCursor(toolCursor);
				return;
			} else {
				setCursor(defaultCursor);
				return;
			}
		} else if ((selectedTool == ToolsAvailable.ADD_BOOTH)
				|| (selectedTool == ToolsAvailable.ADD_TABLE)) {
			if (e.getSource() instanceof JPanel) {
				final Table tempTable = new SquareTable(
						new Point((int) e.getPoint().getX() - 30, (int) e
								.getPoint().getY() - 30), 5, canvasSize);
				if (!detectCollission(tempTable, e)) {
					setCursor(toolCursor);
					return;
				} else {
					setCursor(defaultCursor);
					return;
				}

			} else {
				setCursor(defaultCursor);
				return;
			}
		} else if ((selectedTool == ToolsAvailable.DRAW_SOLID_CIRCLE)
				|| (selectedTool == ToolsAvailable.DRAW_SOLID_RECT)) {
			if (e.getSource() instanceof JPanel) {
				setCursor(toolCursor);
				return;
			} else {
				setCursor(defaultCursor);
				return;
			}
		} else {
			setCursor(defaultCursor);
		}
	}

	private void PanelClicked(final MouseEvent e) {
		if (e.getSource() instanceof JPanel) {
		}
	}

	private void addTable(final Table table) {
		table.setName("" + (tablesList.size() + 1));
		table.setOpaque(false);
		tablesList.add(table);
		final Insets insets = getInsets();
		table.addMouseListener(this);
		table.addMouseListener(parentClass);
		table.addMouseMotionListener(this);
		add(table);
		table.setBounds(table.getLocation().x + insets.left, table
				.getLocation().y
				+ insets.top, table.getWidth(), table.getHeight());
		objects = getmergedObjectList();
		setCursor(defaultCursor);
		repaint();
	}

	private void removeTable(final Table table) {
		final String tableName = table.getName();
		for (int i = 0; i < tablesList.size(); i++)
			if (tableName.compareTo(tablesList.get(i).getName()) == 0) {
				tablesList.remove(i);
			}
		final ArrayList<Table> tempTablesList = new ArrayList<Table>();
		Table tempTable;
		for (int i = 0; i < tablesList.size(); i++) {
			tempTable = tablesList.get(i);
			tempTable.setName(i + 1 + "");
			tempTablesList.add(tempTable);
		}
		tablesList = tempTablesList;
		objects = getmergedObjectList();
		remove(table);
		setCursor(defaultCursor);
		repaint();
	}

	public void setTableStatus(final String tableID, final TableStatus status) {

		final String tableName = tableID;
		for (int i = 0; i < tablesList.size(); i++)
			if (tableName.compareTo(tablesList.get(i).getName()) == 0) {
				final Table tempTable = tablesList.get(i);
				tempTable.changeStatus(status, 3);
			}
	}

	public Table getTable(final String tableName) {
		for (final Table myTable : tablesList)
			if (myTable.getName().equals(tableName))
				return myTable;
		return null;
	}

	private void PanelMousePressed(final MouseEvent e) {
		if (e.getSource() instanceof JPanel) {
			if ((e.getButton() == MouseEvent.BUTTON1)
					&& (selectedTool == ToolsAvailable.ADD_TABLE)) {
				final Table tempTable = new RoundTable(
						new Point((int) e.getPoint().getX() - 30, (int) e
								.getPoint().getY() - 30), 5, canvasSize);
				tempTable.setStatus(TableStatus.OPEN);
				if (!detectCollission(tempTable, e)) {
					addTable(tempTable);
				}
			}
			if ((e.getButton() == MouseEvent.BUTTON1)
					&& (selectedTool == ToolsAvailable.ADD_BOOTH)) {
				final Table tempTable = new SquareTable(
						new Point((int) e.getPoint().getX() - 30, (int) e
								.getPoint().getY() - 30), 5, canvasSize);
				tempTable.setStatus(TableStatus.OPEN);
				if (!detectCollission(tempTable, e)) {
					addTable(tempTable);
				}
			}
		}
	}

	private void tableMousePressed(final MouseEvent e) {
		if ((e.getSource() instanceof Table)
				&& (e.getButton() == MouseEvent.BUTTON1)) {
			tableToDrag = (Table) e.getSource();
			tempLocation = tableToDrag.getLocation();
			tempStatus = tableToDrag.getStatus();
		}
		if ((e.getSource() instanceof Table)
				&& (e.getButton() == MouseEvent.BUTTON1)
				&& (selectedTool == ToolsAvailable.DELETE)) {
			final Table tempTable = (Table) e.getSource();
			removeTable(tempTable);
			repaint();
		}

	}

	private void tableMouseReleased(final MouseEvent e) {
		if ((e.getSource() instanceof Table)
				&& (e.getButton() == MouseEvent.BUTTON1)) {
			if (detectCollission((Table) e.getSource(), e)) {
				tableToDrag.moveToNewLocation(tempLocation, 500);
				tableToDrag.changeStatus(tempStatus, 2);
			}
			tableToDrag = null;
			tempStatus = null;
			tempLocation = null;
		}
	}

	private void tableMouseDragged(final MouseEvent e) {
		if ((tableToDrag != null) && (e.getSource() instanceof Table)
				&& (selectedTool == ToolsAvailable.MOVE)) {
			final Point p = e.getLocationOnScreen();
			final Point i = getLocationOnScreen();
			p.x -= i.x - tableToDrag.getWidth() / 2;
			p.y -= i.y - tableToDrag.getHeight() / 2;
			detectCollission((Table) e.getSource(), e);
			tableToDrag.setLocationWithChecks(p);
			repaint();
		}
	}

	private ArrayList<JComponent> getmergedObjectList() {
		final ArrayList<JComponent> returnList = new ArrayList<JComponent>();
		for (int i = 0; i < tablesList.size(); i++) {
			returnList.add(tablesList.get(i));
		}
		for (int i = 0; i < wallList.size(); i++) {
			returnList.add(wallList.get(i));
		}
		for (int i = 0; i < foundationList.size(); i++) {
			returnList.add(foundationList.get(i));
		}
		return returnList;
	}

	public boolean detectCollission(final Table selectedtable,
			final MouseEvent e) {
		final Iterator<JComponent> iterator = objects.iterator();
		final int padding = 10;
		final Rectangle source = new Rectangle();
		final Rectangle destination = new Rectangle();

		source.setBounds(selectedtable.getX(), selectedtable.getY(),
				selectedtable.getWidth() + padding, selectedtable.getHeight()
						+ padding);
		int counter = 0;
		while (iterator.hasNext()) {

			final JComponent collissionComponent = iterator.next();
			destination.setBounds(collissionComponent.getX(),
					collissionComponent.getY(), collissionComponent.getWidth()
							+ padding, collissionComponent.getHeight()
							+ padding);

			if (destination.intersects(source) || destination.contains(source)) {
				if (selectedtable.getName().compareTo(
						collissionComponent.getName()) != 0) {
					if (selectedtable.getStatus() != TableStatus.UNAVAILABLE) {
						selectedtable.changeStatus(TableStatus.UNAVAILABLE, 0);
					}
					objects.remove(counter); // predict next collision
					objects.add(0, collissionComponent);
					return true;
				}
			} else if (selectedtable.getStatus() != tempStatus) {
				selectedtable.changeStatus(tempStatus, 0);
			}
			counter++;
		}
		return false;
	}

	private void addWall(final Wall wall) {
		wall.setName("" + (wallList.size() + 1));
		wall.setOpaque(false);
		wallList.add(wall);
		add(wall);
		wall.addMouseListener(this);
		wall.addMouseMotionListener(this);
		objects = getmergedObjectList();
		repaint();
	}

	private void addFoundationWall(final Wall wall) {
		add(wall);
		foundationList.add(wall);
		repaint();
	}

	private void removeWall(final Wall wall) {

		final String wallName = wall.getName();

		for (int i = 0; i < wallList.size(); i++)
			if (wallName.compareTo(wallList.get(i).getName()) == 0) {
				wallList.remove(i);
			}
		final ArrayList<Wall> tempWallList = new ArrayList<Wall>();
		Wall tempWall;
		for (int i = 0; i < wallList.size(); i++) {
			tempWall = wallList.get(i);
			tempWall.setName(i + 1 + "");
			tempWallList.add(tempWall);
		}
		wallList = tempWallList;
		objects = getmergedObjectList();
		remove(wall);
		repaint();
		setCursor(defaultCursor);
	}

	private void addFoundationStruts() {
		Wall wallStrut = new RectangularWall(new Point(0, -5), canvasSize);
		wallStrut.setEndingCoordinates(getWidth(), 10);
		wallStrut.setName("foundationTop");
		addFoundationWall(wallStrut);
		wallStrut = new RectangularWall(new Point(-5, 0), canvasSize);
		wallStrut.setEndingCoordinates(10, getHeight());
		wallStrut.setName("foundationLeft");
		addFoundationWall(wallStrut);
		wallStrut = new RectangularWall(new Point(getWidth() - 5, 0),
				canvasSize);
		wallStrut.setEndingCoordinates(10, getHeight());
		wallStrut.setName("foundationRight");
		addFoundationWall(wallStrut);
		wallStrut = new RectangularWall(new Point(0, getHeight() - 5),
				canvasSize);
		wallStrut.setEndingCoordinates(getWidth() + 5, 10);
		wallStrut.setName("foundationBottom");
		addFoundationWall(wallStrut);
	}

	private void wallToolMousePressed(final MouseEvent e) {
		if ((selectedTool.getValue() > 4) && (e.getSource() instanceof JPanel)) {
			Wall wallStrut;
			if (selectedTool == ToolsAvailable.DRAW_SOLID_CIRCLE) {
				wallStrut = new CircularWall(new Point(e.getX(), e.getY()),
						canvasSize);
			} else if (selectedTool == ToolsAvailable.DRAW_SOLID_RECT) {
				wallStrut = new RectangularWall(new Point(e.getX(), e.getY()),
						canvasSize);
			} else {
				wallStrut = new RectangularWall(new Point(e.getX(), e.getY()),
						canvasSize);
			}
			addWall(wallStrut);
			isWallToolInAction = true;
		}
		if ((e.getSource() instanceof Wall)
				&& (e.getButton() == MouseEvent.BUTTON1)) {
			wallToDrag = (Wall) e.getSource();
		}
		if ((e.getSource() instanceof Wall)
				&& (e.getButton() == MouseEvent.BUTTON1)
				&& (selectedTool == ToolsAvailable.DELETE)) {
			final Wall tempWall = (Wall) e.getSource();
			removeWall(tempWall);
		}

	}

	private void wallToolMouseReleased(final MouseEvent e) {
		isWallToolInAction = false;
		if ((e.getSource() instanceof Wall)
				&& (e.getButton() == MouseEvent.BUTTON1)) {
			wallToDrag = null;
		}
	}

	private void wallToolMouseDragged(final MouseEvent e) {
		if (isWallToolInAction && (e.getSource() instanceof JPanel)) {
			Wall wallStrut;
			wallStrut = wallList.get(wallList.size() - 1);
			int h = wallStrut.getMinHeight(), w = wallStrut.getMinWidth();
			w = e.getX() - wallStrut.getX();
			h = e.getY() - wallStrut.getY();
			if (h < wallStrut.getMinHeight()) {
				h = wallStrut.getMinHeight();
			}
			if (w < wallStrut.getMinWidth()) {
				w = wallStrut.getMinWidth();
			}
			wallStrut.setEndingCoordinates(w, h);
		}
		if ((wallToDrag != null) && (e.getSource() instanceof Wall)
				&& (selectedTool == ToolsAvailable.MOVE)) {
			final Point p = e.getLocationOnScreen();
			final Point i = getLocationOnScreen();
			p.x -= i.x - wallToDrag.getWidth() / 2;
			p.y -= i.y - wallToDrag.getHeight() / 2;
			wallToDrag.setLocation(p);
			repaint();
		}
	}

	public void setTables(final List<Table> tables) {
		final List<Table> temp = new ArrayList<Table>(tablesList);
		for (final Table myTable : tables) {
			int location;
			if ((location = temp.indexOf(myTable)) != -1) {
				temp.get(location).changeStatus(myTable.getStatus(), 2);
			} else {
				addTable(myTable);
			}
		}
		for (final Table myTable : temp)
			if (!tables.contains(myTable)) {
				removeTable(myTable);
			}
	}

	public void setWalls(final List<Wall> walls) {
		final List<Wall> temp = new ArrayList<Wall>(wallList);
		for (final Wall myWall : temp) {
			removeWall(myWall);
		}
		for (final Wall myWall : walls) {
			myWall.setLocation(myWall.getX(), myWall.getY());
			addWall(myWall);
		}

	}

	public List<Table> getTables() {
		return tablesList;
	}

	public List<Wall> getWalls() {
		return wallList;
	}
}
