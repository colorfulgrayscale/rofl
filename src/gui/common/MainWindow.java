package gui.common;

import gui.cook.CookScreen;
import gui.manager.LayoutEditor;
import gui.manager.ManagerSelectionScreen;
import gui.manager.MenuManager;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import objects.animation.MotionTween;
import objects.animation.Tweenable;
import objects.user.User;
import objects.user.UserType;
import database.DataAdapter;
import database.DatabaseListener;
import database.DatabaseMessage;

public class MainWindow extends JFrame implements ActionListener,
		WindowListener, KeyListener {

	private static final long serialVersionUID = 2740437090361841747L;
	private DatabaseListener dbListener;
	private User user;
	final int editorWidth = 650;
	final int editorHeight = 650;
	LayoutEditor editableFloor;
	CommonRestaurantInterface nonEditableFloor;
	LoginWindow login;
	ManagerSelectionScreen managerSelection;
	String activeWindow;
	JLayeredPane layeredPane;
	Thread moveOut;
	CookScreen cook;
	MenuManager menuDialog;

	public MainWindow() {
		super("Restaurant Organization and Floor Layout");

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(850, 720);
		setResizable(false);
		setupDatabaseListner();
		addWindowListener(this);
		layeredPane = new JLayeredPane();
		layeredPane.setBackground(Color.green);
		getContentPane().add(layeredPane);
		layeredPane.setLayout(null);
		addKeyListener(this);
		activateLoginWindow(false);
		setLocationRelativeTo(null);
	}

	private void setupDatabaseListner() {
		try {
			DataAdapter.open();
			dbListener = new DatabaseListener();
			dbListener.start();
		} catch (final Exception e) {
		}
	}

	private void unloadActiveWindow() {
		if (activeWindow == "managerselection") {
			deactivateManagerSelectionScreen();
		} else if (activeWindow == "layouteditor") {
			deactivateLayoutEditor();
		} else if (activeWindow == "login") {
			deactivateLoginWindow();
		} else if (activeWindow == "managerinterface") {
			deactivateManagerInterface();
		} else if (activeWindow == "cook") {
			deactivateCookWindow();
		} else if (activeWindow == "menueditor") {
			deactivateMenuEditor();
		}
		repaint();
	}

	private void moveOutOfWindow(final Tweenable object) {
		final Runnable r = new Runnable() {
			public void run() {
				final Thread t = new Thread(new MotionTween(object
						.getLocation(), new Point(-object.getWidth(), object
						.getY()), 500, object));
				t.start();
				try {
					t.join();
					layeredPane.remove((JComponent) object);
				} catch (final InterruptedException e) {
				}
			}
		};
		moveOut = new Thread(r);
		moveOut.start();
	}

	private void moveIntoWindow(final Tweenable object, final Point newLocation) {
		final Runnable r = new Runnable() {
			public void run() {
				final Thread t = new Thread(new MotionTween(new Point(object
						.getWidth(), object.getY()), newLocation, 500, object));
				t.start();
			}
		};
		moveOut = new Thread(r);
		moveOut.start();
	}

	private void activateCookWindow() {
		cook = new CookScreen(user, this, dbListener);
		cook.setBounds(editorWidth + 200, 0, editorWidth + 200,
				editorHeight + 50);
		layeredPane.add(cook);
		activeWindow = "cook";
		cook.setVisible(true);
		cook.repaint();
		cook.addKeyListener(this);
		cook.databaseUpdate(DatabaseMessage.UPDATE_ORDERS);
		moveIntoWindow(cook, new Point(0, 0));
	}

	private void deactivateCookWindow() {
		moveOutOfWindow(cook);
	}

	private void activateLoginWindow(final Boolean animation) {
		login = new LoginWindow(this, user, dbListener);
		if (animation) {
			login.setBounds(editorWidth + 200, 0, editorWidth + 200,
					editorHeight + 50);
		} else {
			login.setBounds(0, 0, editorWidth + 200, editorHeight + 50);
		}
		layeredPane.add(login);
		activeWindow = "login";
		login.setVisible(true);
		login.repaint();
		if (animation) {
			moveIntoWindow(login, new Point(0, 0));
		}
	}

	private void deactivateLoginWindow() {
		moveOutOfWindow(login);
	}

	private void activateManagerSelectionScreen() {
		managerSelection = new ManagerSelectionScreen(this, user, dbListener);
		layeredPane.add(managerSelection);
		managerSelection.setBounds(editorWidth + 200, 0, editorWidth + 200,
				editorHeight + 50);
		activeWindow = "managerselection";
		managerSelection.repaint();
		managerSelection.addKeyListener(this);
		moveIntoWindow(managerSelection, new Point(0, 0));
	}

	private void deactivateManagerSelectionScreen() {
		moveOutOfWindow(managerSelection);
	}

	private void activateLayoutEditor() {
		editableFloor = new LayoutEditor(editorWidth, editorWidth, user,
				dbListener, this);
		layeredPane.add(editableFloor);
		editableFloor.setBounds(editorWidth + 200, 0, editorWidth + 200,
				editorHeight + 50);
		activeWindow = "layouteditor";
		editableFloor.repaint();
		editableFloor.addKeyListener(this);
		moveIntoWindow(editableFloor, new Point(0, 0));
	}

	private void activateMenuEditor() {
		final MenuManager myManager = new MenuManager(user, dbListener);
		final JFrame myFrame = new JFrame();
		myFrame.add(myManager);
		myFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		myFrame.pack();
		myFrame.setLocationRelativeTo(null);
		myFrame.setSize(500, 500);
		myFrame.setVisible(true);
	}

	private void deactivateMenuEditor() {
		moveOutOfWindow(menuDialog);

	}

	private void deactivateLayoutEditor() {
		moveOutOfWindow(editableFloor);

	}

	private void activateManagerInterface() {
		nonEditableFloor = new CommonRestaurantInterface(editorWidth,
				editorWidth, user, dbListener, this);
		layeredPane.add(nonEditableFloor);
		nonEditableFloor.setBounds(editorWidth + 200, 0, editorWidth + 200,
				editorHeight + 50);
		activeWindow = "managerinterface";
		nonEditableFloor.repaint();
		nonEditableFloor.addKeyListener(this);
		moveIntoWindow(nonEditableFloor, new Point(0, 0));
	}

	private void deactivateManagerInterface() {
		moveOutOfWindow(nonEditableFloor);
	}

	private void login() {
		user = DataAdapter.login(login.getUsername(), login.getPassword());
		if (user == null) {
			JOptionPane.showMessageDialog(this,
					"Login Failed, Please verify your credentials.",
					"Login Error", JOptionPane.ERROR_MESSAGE);
		} else if (user.getType() == UserType.MANAGER) {
			unloadActiveWindow();
			activateManagerSelectionScreen();
		} else if (user.getType() == UserType.COOK) {
			unloadActiveWindow();
			activateCookWindow();
		} else if ((user.getType() == UserType.BUSBOY)
				|| (user.getType() == UserType.WAITER)
				|| (user.getType() == UserType.HOST)) {
			unloadActiveWindow();
			activateManagerInterface();
		}
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand() == "Login") {
			login();
		} else if (e.getActionCommand() == "Floor Layout Editor") {
			unloadActiveWindow();
			activateLayoutEditor();
		} else if (e.getActionCommand() == "Manager Interface") {
			unloadActiveWindow();
			activateManagerInterface();
		} else if (e.getActionCommand() == "Log Out") {
			unloadActiveWindow();
			activateLoginWindow(true);
		} else if (e.getActionCommand() == "Menu Editor") {
			activateMenuEditor();
		} else if ((e.getActionCommand() == "Commit Floorplan")
				|| (e.getActionCommand() == "Return to MainMenu")) {
			unloadActiveWindow();
			activateManagerSelectionScreen();
		}
	}

	public void windowClosed(final WindowEvent arg0) {
		try {
			dbListener.kill();
			DataAdapter.close();
		} catch (final Exception e) {
		}
	}

	public void windowActivated(final WindowEvent arg0) {
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

	@Override
	public void keyPressed(final KeyEvent e) {
	}

	@Override
	public void keyReleased(final KeyEvent e) {
		System.out.println(e);
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			if (activeWindow != "Login") {
				unloadActiveWindow();
				activateLoginWindow(true);
			}
	}

	@Override
	public void keyTyped(final KeyEvent e) {
	}
}
