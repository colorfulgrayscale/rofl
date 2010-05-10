package objects.wall;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JComponent;

public abstract class Wall extends JComponent {

	private static final long serialVersionUID = 825360766622615527L;

	private int id;
	int minWidth = 5;
	int minHeight = 5;
	protected boolean isDirty;
	protected int padding = 5;
	protected Dimension parentCanvasSize;

	public void setId(final int anID) {
		id = anID;
	}

	public int getId() {
		return id;
	}

	public void setDirty(final boolean aDirty) {
		isDirty = aDirty;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public abstract void setEndingCoordinates(int width, int height);

	@Override
	public abstract void paintComponent(Graphics g);

	public Wall(final Point startingPoint, final int width, final int height,
			final Dimension parentCanvasSize) {
		this.setBounds(startingPoint.x, startingPoint.y, width, height);
		this.parentCanvasSize = parentCanvasSize;
		id = 0;
	}

	public int getMinWidth() {
		return minWidth;
	}

	public void setMinWidth(final int minWidth) {
		this.minWidth = minWidth;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public void setMinHeight(final int minHeight) {
		this.minHeight = minHeight;
	}

	@Override
	public void setLocation(final Point p) {
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
		super.setLocation(p);
	}

	public void setLocationWithoutChecks(final Point p) {

		super.setLocation(p);
	}

}
