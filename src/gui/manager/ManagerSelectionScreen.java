package gui.manager;

import gui.common.RestaurantPanel;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

import objects.animation.MotionTween;
import objects.animation.Tweenable;
import objects.user.User;
import database.DatabaseListener;
import database.DatabaseMessage;

public class ManagerSelectionScreen extends RestaurantPanel implements
		Tweenable {

	Thread t;
	private static final long serialVersionUID = -1504771938377640038L;

	public ManagerSelectionScreen(final ActionListener parentClass,
			final User user, final DatabaseListener aDBListener) {
		super(user, aDBListener);
		setLayout(null);
		final JPanel panel = new JPanel();
		this.add(panel);
		panel.setLayout(null);
		panel.setBounds(221, 232, 450, 217);

		final JButton btnManagerInterface = new JButton("New button");
		btnManagerInterface.setBounds(127, 44, 200, 29);
		btnManagerInterface.setText("Manager Interface");
		btnManagerInterface.addActionListener(parentClass);
		panel.add(btnManagerInterface);

		final JButton btnFloorLayoutEditor = new JButton("New button");
		btnFloorLayoutEditor.setBounds(127, 85, 200, 29);
		btnFloorLayoutEditor.setText("Floor Layout Editor");
		btnFloorLayoutEditor.addActionListener(parentClass);
		panel.add(btnFloorLayoutEditor);

		final JButton button_2 = new JButton("New button");
		button_2.setText("Log Out");
		button_2.addActionListener(parentClass);
		button_2.setBounds(127, 167, 200, 29);

		final Border etched = BorderFactory.createEtchedBorder();
		final Border titled = BorderFactory.createTitledBorder(etched,
				"Manager Selection Screen");
		panel.setBorder(titled);
		panel.add(button_2);

		final JButton btnMenuEditor = new JButton("Menu Editor");
		btnMenuEditor.setBounds(127, 126, 200, 29);
		btnMenuEditor.addActionListener(parentClass);
		panel.add(btnMenuEditor);

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

	@Override
	public void databaseUpdate(final DatabaseMessage aMessage) {
		// TODO Auto-generated method stub
	}
}
