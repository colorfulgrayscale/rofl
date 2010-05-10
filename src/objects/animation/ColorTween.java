package objects.animation;

import java.awt.Color;

public class ColorTween implements Runnable {
	private Color startColor;
	private final Color endColor;
	private final Tweenable object;
	private final long speed;

	public ColorTween(final Color startColor, final Color endColor,
			final long speed, final Tweenable object) {
		this.startColor = startColor;
		this.endColor = endColor;
		this.speed = speed;
		this.object = object;
	}

	public void run() {
		int redIncrement = 0;
		int blueIncrement = 0;
		int greenIncrement = 0;
		if (endColor.getRed() > startColor.getRed()) {
			redIncrement = 1;
		} else if (endColor.getRed() < startColor.getRed()) {
			redIncrement = -1;
		}
		if (endColor.getBlue() > startColor.getBlue()) {
			blueIncrement = 1;
		} else if (endColor.getBlue() < startColor.getBlue()) {
			blueIncrement = -1;
		}
		if (endColor.getGreen() > startColor.getGreen()) {
			greenIncrement = 1;
		} else if (endColor.getGreen() < startColor.getGreen()) {
			greenIncrement = -1;
		}
		while ((endColor.getRed() != startColor.getRed())
				|| (endColor.getBlue() != startColor.getBlue())
				|| (endColor.getGreen() != startColor.getGreen())) {
			if (endColor.getRed() != startColor.getRed()) {
				final int newRed = startColor.getRed() + redIncrement;
				startColor = new Color(newRed, startColor.getGreen(),
						startColor.getBlue());
			}
			if (endColor.getGreen() != startColor.getGreen()) {
				final int newGreen = startColor.getGreen() + greenIncrement;
				startColor = new Color(startColor.getRed(), newGreen,
						startColor.getBlue());
			}
			if (endColor.getBlue() != startColor.getBlue()) {
				final int newBlue = startColor.getBlue() + blueIncrement;
				startColor = new Color(startColor.getRed(), startColor
						.getGreen(), newBlue);
			}
			object.fadeColor(startColor);
			try {
				Thread.sleep(speed);
			} catch (final InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

}
