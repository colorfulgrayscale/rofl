package gui.common;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import objects.animation.MotionTween;
import objects.animation.Tweenable;
import objects.user.User;
import database.DatabaseListener;
import database.DatabaseMessage;

public class LoginWindow extends RestaurantPanel implements Tweenable,
		KeyListener {
	private static final long serialVersionUID = -2703218464554490997L;
	private final JTextField textField;
	private final JPasswordField passwordField;
	private Thread t;
	final JButton button;

	public LoginWindow(final ActionListener parentClass, final User user,
			final DatabaseListener aDBListener) {
		super(user, aDBListener);
		setLayout(null);
		final JInternalFrame jiFrame = new JInternalFrame();
		jiFrame.setBounds(221, 255, 450, 150);
		jiFrame.setTitle("Login");
		jiFrame.setLayout(null);

		jiFrame.setFrameIcon(null);

		final JLabel label = new JLabel("New label");
		label.setBounds(6, 12, 102, 16);
		label.setText("Username: ");
		jiFrame.add(label);

		textField = new JTextField();
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(final FocusEvent e) {
				textField.selectAll();
			}
		});
		textField.setBounds(120, 6, 300, 28);
		jiFrame.add(textField);
		textField.setText("manager");
		textField.setColumns(10);

		final JLabel label_1 = new JLabel("New label");
		label_1.setText("Password: ");
		label_1.setBounds(6, 40, 102, 16);
		jiFrame.add(label_1);

		passwordField = new JPasswordField();
		passwordField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(final FocusEvent e) {
				passwordField.selectAll();
			}
		});
		passwordField.setBounds(120, 34, 300, 28);
		passwordField.setText("manager");
		jiFrame.add(passwordField);

		button = new JButton("New button");
		button.setText("Login");
		button.setBounds(151, 74, 117, 29);
		button.addActionListener(parentClass);
		jiFrame.add(button);
		jiFrame.setVisible(true);
		add(jiFrame);

		textField.addKeyListener(this);
		passwordField.addKeyListener(this);
		repaint();
	}

	public String getUsername() {
		return textField.getText();
	}

	@SuppressWarnings("deprecation")
	public String getPassword() {
		return passwordField.getText();
	}

	public void clearFields() {
		textField.setText("");
		passwordField.setText("");
	}

	@Override
	public void fadeColor(final Color c) {
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
	public void databaseUpdate(final DatabaseMessage aMessage) {
	}

	@Override
	public void keyPressed(final KeyEvent e) {
	}

	@Override
	public void keyReleased(final KeyEvent e) {
		if (e.getKeyCode() == 10) {
			button.requestFocus();
			button.doClick();
		}
	}

	@Override
	public void keyTyped(final KeyEvent e) {
	}

}
