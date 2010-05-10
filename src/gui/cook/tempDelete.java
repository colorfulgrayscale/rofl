package gui.cook;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class tempDelete extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public tempDelete() {
		getContentPane().setLayout(null);

		final JPanel panel = new JPanel();
		panel.setBounds(6, 6, 438, 266);
		getContentPane().add(panel);
		panel.setLayout(null);

		final JPanel panel_1 = new JPanel();
		panel_1.setBounds(6, 6, 163, 254);
		panel.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		final JLabel label = new JLabel("New label");
		panel_1.add(label, BorderLayout.NORTH);

		final JButton button = new JButton("New button");
		panel_1.add(button, BorderLayout.CENTER);

		final JPanel panel_2 = new JPanel();
		panel_2.setBounds(171, 6, 261, 254);
		panel.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		final DefaultTableModel dm = new DefaultTableModel();
		dm.setDataVector(new Object[][] { { "button 1", "foo" },
				{ "button 2", "bar" } }, new Object[] { "Button", "String" });

		final JTable table = new JTable(dm);
		table.getColumn("Button").setCellRenderer(new ButtonRenderer());
		table.getColumn("Button").setCellEditor(
				new ButtonEditor(new JCheckBox()));
		final JScrollPane scroll = new JScrollPane(table);
		panel_2.add(scroll, BorderLayout.CENTER);

		setSize(450, 303);
		setVisible(true);

		// List<MenuItem> items = DataAdapter.getMenuItems(user);
		// if (items != null) {
		// for (final MenuItem myItem : items)
		// System.out.println(myItem.toString());
		// }

	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		final tempDelete frame = new tempDelete();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				System.exit(0);
			}
		});
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
			// 
			// 
			JOptionPane.showMessageDialog(button, label + ": Ouch!");
		}
		// System.out.println(label + ": Ouch!");
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
