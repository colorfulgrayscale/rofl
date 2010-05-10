package objects.table;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JComponent;

import objects.animation.ColorTween;
import objects.animation.MotionTween;
import objects.animation.Tweenable;

public abstract class Table extends JComponent implements Tweenable {
	private static final long serialVersionUID = 6928153407534747443L;
	protected int size;
	private int id;
	protected double angle;
	protected TableStatus status;
	protected Color paintColor;
	protected Dimension parentCanvasSize;
	protected int padding = 10;
	protected boolean isDirty;
	Thread t;

	public Table(final Point aPoint, final int aSize,
			final Dimension parentCanvasSize) {
		this.parentCanvasSize = parentCanvasSize;
		setOpaque(false);
		setLocationWithChecks(aPoint);
		status = TableStatus.OPEN;
		paintColor = getColor();
		angle = 0;
		size = aSize * 10;
		final Dimension mySize = new Dimension(size, size);
		setSize(size, size);
		setMinimumSize(mySize);
		setPreferredSize(mySize);
		setMaximumSize(mySize);
		isDirty = true;
	}

	public void changeStatus(final TableStatus newStatus,
			final long animationSpeed) {
		if (newStatus == status)
			return;
		try {
			if (t.isAlive()) {
				try {
					t.join();
					if (t.isAlive()) {
						t.interrupt();
					}
				} catch (final Exception e) {
				}
			}
		} catch (final Exception e) {
		}
		isDirty = true;
		final Color oldColor = paintColor;
		setStatus(newStatus);
		final Color newColor = getColor();
		t = new Thread(new ColorTween(oldColor, newColor, animationSpeed, this));
		t.start();
	}

	@Override
	public boolean isAnimationInProgress() {
		// TODO Auto-generated method stub
		return t.isAlive();
	}

	public void setNewLocation(final Point p) {
		this.setLocation(p);
		repaint();
	}

	public void moveToNewLocation(final Point p, final long animationSpeed) {
		final Thread moveThread = new Thread(new MotionTween(
				this.getLocation(), p, 500, this));
		moveThread.start();
	}

	public void rotateLeft() {
		angle += Math.PI / 4;
		angle %= 2 * Math.PI;
		isDirty = true;
	}

	public void rotateRight() {
		angle -= Math.PI / 4;
		while (angle < 0) {
			angle += 2 * Math.PI;
		}
		isDirty = true;
	}

	public void setLocationWithChecks(final Point p) {
		if (p.x > parentCanvasSize.getWidth() - padding) {
			p.x = (int) (parentCanvasSize.getWidth() - padding);
		}
		if (p.y > parentCanvasSize.getHeight() - padding) {
			p.y = (int) (parentCanvasSize.getHeight() - padding);
		}
		if (p.x < getWidth() + padding) {
			p.x = getWidth() + padding;
		}
		if (p.y < getHeight() + padding) {
			p.y = getHeight() + padding;
		}

		p.x -= getWidth();
		p.y -= getHeight();
		setLocation(p);
		isDirty = true;
	}

	public void fadeColor(final Color c) {
		paintColor = c;
		repaint();
	}

	protected Color getColor() {
		if (status == null) {
			status = TableStatus.OPEN;
		}
		switch (status) {
		case OPEN:
			return Color.GREEN;
		case OCCUPIED:
			return Color.YELLOW;
		case DIRTY:
			return Color.RED;
		case UNAVAILABLE:
			return Color.DARK_GRAY;
		case ORDERREADY:
			return Color.BLUE;
		}
		return null;
	}

	public void setStatus(final TableStatus aStatus) {
		status = aStatus;
		paintColor = getColor();
		isDirty = true;
	}

	public TableStatus getStatus() {
		return status;
	}

	public void setId(final int anId) {
		id = anId;
	}

	public int getId() {
		return id;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(final boolean aDirty) {
		isDirty = aDirty;
	}

	public Dimension getCanvasSize() {
		return (Dimension) parentCanvasSize.clone();
	}
}
