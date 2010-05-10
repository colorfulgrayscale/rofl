package gui.cook;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;

import objects.animation.MotionTween;
import objects.animation.Tweenable;
import objects.order.MenuItemType;
import objects.order.Order;
import objects.order.OrderItem;

public class OrderPanel extends JPanel implements Tweenable {
	private static final long serialVersionUID = 8859936294098567062L;

	private static final int panelWidth = 190;
	private static final int panelHeight = 330;
	private Point finalResting;
	private final int pID;
	private Thread t;
	private Thread moveOut;
	private final JScrollPane scrollPane;

	public OrderPanel(final Order order, final int pID,
			final ActionListener parentClass) {
		setBackground(Color.black);
		setSize(OrderPanel.panelWidth, OrderPanel.panelHeight);
		setLayout(null);
		this.pID = pID;
		final JLabel label = new JLabel();
		label.setText("Order #" + order.getId());
		label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, 20));
		label.setForeground(Color.YELLOW);
		label.setBounds(42, 6, 117, 16);
		add(label);
		final JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setForeground(Color.WHITE);
		textArea.setFont(new Font(textArea.getFont().getFontName(), Font.BOLD,
				14));
		textArea.setTabSize(4);
		textArea.setText(getOrderInfo(order));

		textArea.setBackground(getBackground());

		scrollPane = new JScrollPane(textArea,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(6, 42, 180, 251);
		scrollPane.setBorder(null);
		add(scrollPane);
		final JButton button = new JButton();
		button.setName(pID + "");
		button.setText("Order Complete");
		button.addActionListener(parentClass);
		button.setBounds(16, 295, 157, 29);
		final Border etched = BorderFactory.createEtchedBorder();
		final Border titled = BorderFactory.createTitledBorder(etched, "");
		setBorder(titled);
		add(button);
	}

	public int getpID() {
		return pID;
	}

	public static int getPanelwidth() {
		return OrderPanel.panelWidth;
	}

	public static int getPanelheight() {
		return OrderPanel.panelHeight;
	}

	@Override
	public void fadeColor(final Color c) {
		// TODO Auto-generated method stub

	}

	private Tweenable getMe() {
		return this;
	}

	public void moveToNewLocation(final Point p, final long animationSpeed) {

		final Runnable r = new Runnable() {
			public void run() {
				if ((t != null) && t.isAlive()) {
					t.interrupt();
				}
				t = new Thread(new MotionTween(getMe().getLocation(), p, 500,
						getMe()));
				setFinalResting(p);
				t.start();
				try {
					t.join();

				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		moveOut = new Thread(r);
		moveOut.start();

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

	public void setFinalResting(final Point finalResting) {
		this.finalResting = finalResting;
	}

	public Point getFinalResting() {
		return finalResting;
	}

	public String getOrderInfo(final Order order) {
		String appHolder = "\t" + MenuItemType.APPETIZER.toString() + " \n", mainHolder = "\n\t"
				+ MenuItemType.ENTREE.toString() + " \n", dessertHolder = "\n\t"
				+ MenuItemType.DESSERT.toString() + " \n";

		for (int i = 0; i < order.getItems().size(); i++) {
			final OrderItem oi = order.getItems().get(i);
			if (oi.getType() == MenuItemType.APPETIZER)
				if (oi.getQuantity() > 1) {
					appHolder += oi.getQuantity() + " " + oi.getName() + "\n";
				} else {
					appHolder += oi.getName() + "\n";
				}
			if (oi.getType() == MenuItemType.ENTREE)
				if (oi.getQuantity() > 1) {
					mainHolder += oi.getQuantity() + " " + oi.getName() + "\n";
				} else {
					mainHolder += oi.getName() + "\n";
				}
			if (oi.getType() == MenuItemType.DESSERT)
				if (oi.getQuantity() > 1) {
					dessertHolder += oi.getQuantity() + " " + oi.getName()
							+ "\n";
				} else {
					dessertHolder += oi.getName() + "\n";
				}
		}// end for
		return appHolder + mainHolder + dessertHolder;

	}

	public String appendOrderInfo(final Order order) {
		return "";
	}

}
