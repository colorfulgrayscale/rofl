package objects.wall;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class CircularWall extends Wall {

	private static final long serialVersionUID = -6627675045210368582L;

	public CircularWall(final Point startingPoint,
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
		final BufferedImage image = new BufferedImage(getWidth(), getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		final Graphics2D doubleBuffer = image.createGraphics();
		doubleBuffer.setStroke(new BasicStroke(2.0f));
		doubleBuffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		doubleBuffer.setColor(Color.DARK_GRAY);
		doubleBuffer.fillOval(0, 0, getWidth(), getHeight());
		g2.drawImage(image, 0, 0, this);

	}

}