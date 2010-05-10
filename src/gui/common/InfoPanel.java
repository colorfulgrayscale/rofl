package gui.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import objects.animation.MotionTween;
import objects.animation.Tweenable;
import objects.animation.Zoom;
import objects.animation.Zoomable;
import objects.common.SelectedAction;
import objects.order.MenuItem;
import objects.order.Order;
import objects.order.OrderItem;
import objects.table.Table;
import objects.user.User;
import database.DataAdapter;

public class InfoPanel extends JPanel implements Zoomable, Tweenable,
		ActionListener {
	private static final long serialVersionUID = 3193650187457603472L;
	private double zoomLevel = 0.5;
	private final Table tableName;
	private Order order;
	private final User user;
	private DefaultTableModel dm;
	private JTable table;
	private JScrollPane scrollPane;
	private final List<MenuItem> items;

	public InfoPanel(final ActionListener parentClass,
			final SelectedAction action, final Table tableName, final User user) {
		this.user = user;
		setOpaque(false);
		this.tableName = tableName;
		order = DataAdapter.selectOrder(user, tableName);
		items = DataAdapter.getMenuItems(user);
		if (order == null) {
			order = new Order();
			order.setTable(tableName.getId());
			DataAdapter.insertOrder(user, order);
		}
		if (action == SelectedAction.CHANGESTATUS) {
			this.setSize(316, 225);
			modifyStatus(parentClass);
		} else if (action == SelectedAction.MODIFYORDERS) {
			setSize(450, 303);
			addOrder(parentClass);
		}
	}

	private void addOrder(final ActionListener parentClass) {
		setLayout(null);
		final JPanel panel = new JPanel();
		panel.setBackground(Color.black);
		panel.setBounds(6, 6, 438, 266);
		add(panel);
		final Border etched = BorderFactory.createEtchedBorder();
		final Border titled = BorderFactory.createTitledBorder(etched, "");
		panel.setBorder(titled);

		panel.setLayout(null);

		final JPanel panel_1 = new JPanel();
		panel_1.setBounds(6, 6, 163, 254);
		panel_1.setLayout(new BorderLayout(0, 0));

		final JLabel lblTable = new JLabel("Table #");
		lblTable.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblTable.setForeground(Color.WHITE);
		lblTable.setBounds(119, 6, 166, 29);
		lblTable.setText("Table #" + tableName.getId());
		panel_1.add(lblTable, BorderLayout.NORTH);

		scrollPane = new JScrollPane(panel_1,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(panel_1.getBounds());

		// panel.add(panel_1);
		panel.add(scrollPane);

		final JPanel panel_2 = new JPanel();
		panel_2.setBounds(171, 6, 261, 254);
		panel.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		dm = new DefaultTableModel();

		// dm.setDataVector(new Object[][] { { "Remove", "Tacos" },
		// { "Remove", "Fish Taco" } }, new Object[] { "Action",
		// / "Menu Item" });
		// dm.setColumnCount(2);
		dm.setColumnIdentifiers(new Object[] { "Action", "Menu Item" });
		for (final OrderItem myItem : order.getItems()) {
			for (int i = 0; i < myItem.getQuantity(); i++) {
				dm.addRow(new Object[] { "Remove", myItem });
			}
		}

		table = new JTable(dm);

		table.getColumn("Action").setCellRenderer(new ButtonRenderer());
		table.getColumn("Action").setCellEditor(
				new ButtonEditor(new JCheckBox()));
		final JScrollPane scroll = new JScrollPane(table);
		panel_2.add(scroll, BorderLayout.CENTER);

		setSize(450, 303);
		setVisible(true);

		final Box p = new Box(BoxLayout.Y_AXIS);
		final ButtonGroup group = new ButtonGroup();

		p.add(Box.createRigidArea(new Dimension(0, 20)));

		final JLabel lblTable2 = new JLabel();
		lblTable2.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblTable2.setForeground(Color.WHITE);
		lblTable2.setBounds(119, 6, 166, 29);
		lblTable2.setText("All Available Menu Items");
		p.add(lblTable2);
		p.add(Box.createRigidArea(new Dimension(0, 10)));

		if (items != null) {
			for (final MenuItem myItem : items) {
				final JButton button = new JButton();
				button.setText(myItem.toString());
				group.add(button);
				p.add(button);
				button.addActionListener(this);
			}
		}
		panel_1.add(p, BorderLayout.CENTER);
		final JButton button_1 = new JButton("Save Order");
		button_1.addActionListener(this);
		button_1.addActionListener(parentClass);
		panel_2.add(button_1, BorderLayout.SOUTH);
	}

	private void modifyStatus(final ActionListener parentClass) {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		final JPanel panel = new JPanel();
		add(panel);
		panel.setBackground(Color.black);
		final Border etched = BorderFactory.createEtchedBorder();
		final Border titled = BorderFactory.createTitledBorder(etched, "");
		panel.setBorder(titled);
		panel.setLayout(null);

		final JLabel lblTable = new JLabel("Table #");
		lblTable.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblTable.setForeground(Color.WHITE);
		lblTable.setBounds(119, 6, 166, 29);
		lblTable.setText("Table #" + tableName.getId());
		panel.add(lblTable);

		final JButton btnClose = new JButton("Close");
		btnClose.setBounds(6, 196, 304, 23);
		btnClose.addActionListener(parentClass);
		panel.add(btnClose);

		final JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.gray);
		panel_1.setBorder(titled);
		panel_1.setBounds(6, 41, 304, 143);
		panel.add(panel_1);
		panel_1.setLayout(null);

		final JButton btnMarkAsUnavailable = new JButton("Mark as Open");
		btnMarkAsUnavailable.addActionListener(parentClass);
		btnMarkAsUnavailable.setBounds(47, 14, 216, 23);
		panel_1.add(btnMarkAsUnavailable);
		btnMarkAsUnavailable.addActionListener(parentClass);

		final JButton btnMarkAsDirty = new JButton("Mark as Dirty");
		btnMarkAsDirty.setBounds(47, 47, 216, 23);
		panel_1.add(btnMarkAsDirty);

		final JButton button = new JButton("Mark as Unavailable");
		button.addActionListener(parentClass);
		button.setBounds(47, 113, 216, 23);
		panel_1.add(button);

		final JButton btnMarkAsOccupied = new JButton("Mark as Occupied");
		btnMarkAsOccupied.addActionListener(parentClass);
		btnMarkAsOccupied.setBounds(47, 80, 216, 23);
		panel_1.add(btnMarkAsOccupied);
		btnMarkAsDirty.addActionListener(parentClass);

	}

	@Override
	public void fadeColor(final Color c) {
	}

	@Override
	public boolean isAnimationInProgress() {
		return false;
	}

	public void setNewLocation(final Point p) {
		this.setLocation(p);
		repaint();
	}

	public void moveToNewLocation(final Point p, final float animationSpeed) {
		final Thread moveThread = new Thread(new MotionTween(
				this.getLocation(), p, animationSpeed, this));
		moveThread.start();
	}

	@Override
	public void paint(final Graphics g) {
		super.paintComponent(g);
		final Graphics2D g2 = (Graphics2D) g;
		final AffineTransform backup = g2.getTransform();
		if (zoomLevel > 1) {
			zoomLevel = 1;
		}
		g2.scale(zoomLevel, zoomLevel);
		try {
			super.paint(g);
		} catch (final Exception e) {
		}
		g2.setTransform(backup);
	}

	@Override
	public void Zoom(final double start, final double end, final long speed) {
		final Thread t = new Thread(new Zoom(start, end, speed, this));
		t.start();
	}

	@Override
	public void setZoomLevel(final double zoom) {
		zoomLevel = zoom;
		repaint();

	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand() != "Save Order") {
			for (final MenuItem myItem : items) {
				if (e.getActionCommand().equals(myItem.getName())) {
					final OrderItem myOrderItem = new OrderItem();
					myOrderItem.setMenuItem(myItem);
					myOrderItem.setQuantity(1);
					dm.addRow(new Object[] { "Remove", myOrderItem });
				}
			}
		} else {
			final List<OrderItem> toRemove = new ArrayList<OrderItem>();
			outer: for (final OrderItem myItem : order.getItems()) {
				final int rows = dm.getRowCount();
				for (int i = 0; i < rows; i++) {
					final Vector row = (Vector) dm.getDataVector().elementAt(i);
					final OrderItem value = (OrderItem) row.elementAt(1);
					if (value.getId() == myItem.getId()) {
						continue outer;
					}
				}
				toRemove.add(myItem);
			}
			for (final OrderItem myRemove : toRemove) {
				order.removeOrderItem(myRemove);
			}

			final int rows = dm.getRowCount();
			for (int i = 0; i < rows; i++) {
				final Vector row = (Vector) dm.getDataVector().elementAt(i);
				final OrderItem value = (OrderItem) row.elementAt(1);
				if (value.getId() == 0) {
					order.addOrderItem(value);
				}
			}
			order.setOrderTaken(new Date(System.currentTimeMillis()));
			DataAdapter.updateOrder(user, order);
		}

	}

	/**
	 * @version 1.0 11/09/98
	 *          http://www.java2s.com/Code/Java/Swing-Components/ButtonTableExample
	 *          .htm
	 */

	class ButtonRenderer extends JButton implements TableCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ButtonRenderer() {
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(final JTable table,
				final Object value, final boolean isSelected,
				final boolean hasFocus, final int row, final int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(UIManager.getColor("Button.background"));
			}
			setText(value == null ? "" : value.toString());
			return this;
		}
	}

	/**
	 * @version 1.0 11/09/98
	 */

	class ButtonEditor extends DefaultCellEditor {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		protected JButton button;

		private String label;

		private boolean isPushed;

		public ButtonEditor(final JCheckBox checkBox) {
			super(checkBox);
			button = new JButton();
			button.setOpaque(true);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					fireEditingStopped();
				}
			});
		}

		@Override
		public Component getTableCellEditorComponent(final JTable table,
				final Object value, final boolean isSelected, final int row,
				final int column) {
			if (isSelected) {
				button.setForeground(table.getSelectionForeground());
				button.setBackground(table.getSelectionBackground());
			} else {
				button.setForeground(table.getForeground());
				button.setBackground(table.getBackground());
			}
			label = value == null ? "" : value.toString();
			button.setText(label);
			isPushed = true;
			return button;
		}

		@Override
		public Object getCellEditorValue() {
			if (isPushed) {
				// JOptionPane.showMessageDialog(button, label + ": Removed!");
				if (table.getSelectedRow() < dm.getRowCount()) {
					dm.removeRow(table.getSelectedRow());
				}

			}

			isPushed = false;
			return new String(label);
		}

		@Override
		public boolean stopCellEditing() {
			isPushed = false;
			return super.stopCellEditing();
		}

		@Override
		protected void fireEditingStopped() {
			super.fireEditingStopped();
		}
	}

}