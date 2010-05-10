package objects.animation;

public class Zoom implements Runnable {

	private Double startZoom;
	private final double endZoom;
	private final Zoomable object;
	private final long speed;

	public Zoom(final double startPoint, final double endPoint,
			final long speed, final Zoomable object) {
		startZoom = startPoint;
		endZoom = endPoint;
		this.speed = speed;
		this.object = object;
	}

	public void run() {

		double zoomIncrement = 0.0;
		if (startZoom > endZoom) {
			zoomIncrement = -0.01;
		} else if (startZoom < endZoom) {
			zoomIncrement = 0.01;
		}
		if (startZoom == endZoom)
			return;

		while (startZoom != endZoom) {
			startZoom = startZoom + zoomIncrement;
			if ((zoomIncrement == 0.01) && (startZoom >= endZoom)) {
				object.setZoomLevel(endZoom);
				break;
			} else if ((zoomIncrement == -0.01) && (startZoom <= endZoom)) {
				object.setZoomLevel(endZoom);
				break;
			} else {
				object.setZoomLevel(startZoom);
			}
			try {
				Thread.sleep(speed);
			} catch (final InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

}
