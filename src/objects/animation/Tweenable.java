package objects.animation;

import java.awt.Color;
import java.awt.Point;

public interface Tweenable {

	public void fadeColor(Color c);

	public void setNewLocation(Point p);

	public boolean isAnimationInProgress();

	public int getWidth();

	public int getHeight();

	public int getX();

	public int getY();

	public void repaint();

	public Point getLocation();
}
