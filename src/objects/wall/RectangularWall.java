package objects.wall;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class RectangularWall extends Wall {

	private static final long serialVersionUID = -6627675045210368582L;

	public RectangularWall(final Point startingPoint,
			final Dimension parentCanvasSize) {
		super(startingPoint, 1, 1, parentCanvasSize);
	}

	@Override
	public void setEndingCoordinates(final int width, final int height) {
		this.setBounds(getX(), getY(), width, height);
		repaint();
	}

	public void setStartingCoordinates(final int x, final int y) {
		this.setLocation(x, y);
		repaint();
	}

	@Override
	public void paintComponent(final Graphics g) {
		final Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.DARK_GRAY);
		g2.fillRect(0, 0, getWidth(), getHeight());
	}

}
