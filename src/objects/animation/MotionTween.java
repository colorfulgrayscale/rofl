package objects.animation;

import java.awt.Point;

public class MotionTween implements Runnable {

	private final Point startPoint;
	private final Point endPoint;
	private final Tweenable object;
	int animationDuration;
	Point tempPoint;

	public MotionTween(final Point startPoint, final Point endPoint,
			final float duration, final Tweenable object) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.object = object;
		tempPoint = startPoint;
		animationDuration = (int) duration;
	}

	public void run() {
		long animStartTime = System.nanoTime() / 1000000;
		while ((endPoint.getX() != tempPoint.getX())
				|| (endPoint.getY() != tempPoint.getY())) {

			final long currentTime = System.nanoTime() / 1000000;
			final long totalTime = currentTime - animStartTime;
			if (totalTime > animationDuration) {
				animStartTime = currentTime;
			}
			float fraction = (float) totalTime / animationDuration;
			fraction = Math.min(1.0f, fraction);
			fraction = Math.max(0.0f, fraction);
			tempPoint.x = (int) (fraction * endPoint.getX() + (1 - fraction)
					* startPoint.getX());
			tempPoint.y = (int) (fraction * endPoint.getY() + (1 - fraction)
					* startPoint.getY());
			object.setNewLocation(tempPoint);
			object.repaint();
			try {
				Thread.sleep(10);
			} catch (final Exception e) {
			}
		}

	}

}
