package gui.manager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

//@SuppressWarnings("serial")
public class TaxManager extends JFrame // extends RestaurantPanel
{

	private static final long serialVersionUID = 1L;

	String data[][] = { { "Tax 1", "12%" }, { "Tax 2", "9.5%" }, };
	String fields[] = { "Tax #", "Rate" };
	JButton add = new JButton("ADD");

	public static void main(final String[] argv) {
		new TaxManager();

	}

	// public TaxManager(User aUser, DatabaseListener aDBListener) {
	// super(aUser, aDBListener);
	// }
	//	

	public TaxManager() {
		super("Tax menu");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent we) {
				dispose();
				System.exit(0);
			}
		});
		init();
		pack();
		setVisible(true);
	}

	// @Override
	// public void databaseUpdate(DatabaseMessage aMessage) {
	//
	// }
	private void init() {
		add.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				final TaxDialogue myDialogue = new TaxDialogue(null, true);
				myDialogue.show();
				updateTable();

			}
		});
		final JTable jt = new JTable(data, fields);
		final JScrollPane pane = new JScrollPane(jt);
		final GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(
				pane).addComponent(add));
		layout.setVerticalGroup(layout.createParallelGroup().addComponent(pane)
				.addComponent(add));

	}

	public void updateTable() {

	}
}
