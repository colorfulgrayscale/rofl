package objects.table;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class SquareTable extends Table {
	private static final long serialVersionUID = -2240809117819323401L;

	public SquareTable(final Point aPoint, final int aSize,
			final Dimension parentCanvasSize) {
		super(aPoint, aSize, parentCanvasSize);
	}

	@Override
	public void paintComponent(final Graphics g) {
		final Graphics2D g2 = (Graphics2D) g;

		final BufferedImage image = new BufferedImage(getWidth(),
				getHeight() - 5, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D doubleBuffer = image.createGraphics();

		doubleBuffer.setStroke(new BasicStroke(2.0f));
		doubleBuffer
				.setFont(new Font("LucidaSansOblique", Font.BOLD, size / 2));
		doubleBuffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		doubleBuffer.setColor(super.paintColor);
		doubleBuffer.fillRect(0, 0, getWidth() + 5, getHeight() - 5);
		doubleBuffer.setColor(Color.black);
		final int intValue = Integer.parseInt(getName());
		int fontXoffset = 8;
		final int fontYoffset = 8;
		if (intValue > 9) {
			fontXoffset = fontXoffset + 8;
		}
		doubleBuffer.drawString(intValue + "", getWidth() / 2 - fontXoffset,
				getHeight() / 2 + fontYoffset);

		g2.drawImage(image, 0, 0, this);

	}

}
