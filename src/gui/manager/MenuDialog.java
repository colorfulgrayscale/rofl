package gui.manager;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;

import objects.order.MenuItem;
import objects.order.MenuItemType;

public class MenuDialog extends JDialog {
	private static final long serialVersionUID = -2671510353753100345L;
	private MenuItem myItem;
	private final JTextField textName, textPrice;
	private final JTextArea textDescription;
	private final JLabel nameLabel, descriptionLabel, priceLabel;
	private final JComboBox comboType;
	private final JButton save, cancel;
	private final JScrollPane descriptionPane;
	private int response;

	public MenuDialog(final MenuItem anItem, final boolean modal) {
		super((JFrame) null, modal);

		setResizable(false);
		setLocationRelativeTo(null);
		final ButtonListener bl = new ButtonListener();
		response = JOptionPane.CANCEL_OPTION;
		nameLabel = new JLabel("Name:");
		descriptionLabel = new JLabel("Description:");
		priceLabel = new JLabel("Price:");
		textName = new JTextField();
		textName.setPreferredSize(new Dimension(150, 15));
		textName.setMaximumSize(new Dimension(150, 15));
		textPrice = new JTextField();
		textPrice.setPreferredSize(new Dimension(100, 15));
		textPrice.setMaximumSize(new Dimension(100, 15));
		textDescription = new JTextArea();
		textDescription.setPreferredSize(new Dimension(150, 100));
		textDescription.setMaximumSize(new Dimension(150, 100));
		textDescription.setWrapStyleWord(true);
		textDescription.setLineWrap(true);
		descriptionPane = new JScrollPane(textDescription);
		save = new JButton("Save");
		save.addActionListener(bl);
		cancel = new JButton("Cancel");
		cancel.addActionListener(bl);
		comboType = new JComboBox(MenuItemType.values());
		comboType.setSelectedIndex(0);
		comboType.setPreferredSize(new Dimension(100, 15));
		comboType.setMaximumSize(new Dimension(100, 15));

		final GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
				.addComponent(nameLabel).addComponent(textName).addComponent(
						descriptionLabel).addComponent(descriptionPane)
				.addComponent(priceLabel).addComponent(textPrice).addComponent(
						comboType).addGroup(
						layout.createSequentialGroup().addComponent(save)
								.addComponent(cancel)));
		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(
				nameLabel).addComponent(textName)
				.addComponent(descriptionLabel).addComponent(descriptionPane)
				.addComponent(priceLabel).addComponent(textPrice).addComponent(
						comboType).addGroup(
						layout.createParallelGroup().addComponent(save)
								.addComponent(cancel)));

		pack();

		if (anItem != null) {
			myItem = anItem;
			textName.setText(myItem.getName());
			textName.setEditable(false);
			textDescription.setText(myItem.getDescription());
			textPrice.setText("" + myItem.getPrice());
			comboType.setSelectedItem(myItem.getType());
			setTitle("Change Menu Item");
		} else {
			myItem = new MenuItem();
			setTitle("Create Menu Item");
		}
	}

	private class ButtonListener implements ActionListener {
		public void actionPerformed(final ActionEvent arg0) {
			if (arg0.getSource() == save) {
				if (textName.getText().equals("")) {
					JOptionPane.showMessageDialog(MenuDialog.this,
							"Name cannot be empty.");
					return;
				}
				myItem.setName(textName.getText());
				myItem.setDescription(textDescription.getText());
				myItem.setType((MenuItemType) comboType.getSelectedItem());
				try {
					myItem.setPrice(Double.parseDouble(textPrice.getText()));
				} catch (final Exception e) {
					JOptionPane.showMessageDialog(MenuDialog.this,
							"Price is invalid.");
					return;
				}
				response = JOptionPane.OK_OPTION;
			} else if (arg0.getSource() == cancel) {

			}
			setVisible(false);
		}
	}

	public MenuItem getMenuItem() {
		return myItem;
	}

	public int getResponse() {
		return response;
	}
}
