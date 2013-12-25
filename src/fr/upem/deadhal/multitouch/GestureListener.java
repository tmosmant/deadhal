package fr.upem.deadhal.multitouch;

import android.graphics.Matrix;
import android.view.GestureDetector;
import android.view.MotionEvent;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;

public class GestureListener extends GestureDetector.SimpleOnGestureListener {

	private TouchView m_view;
	private Level m_level;

	public GestureListener(TouchView view, Level level) {
		m_view = view;
		m_level = level;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {

		Matrix inverse = new Matrix();

		m_view.getMatrix().invert(inverse);

		float[] pts = new float[2];

		pts[0] = e.getX(0);
		pts[1] = e.getY(0);

		inverse.mapPoints(pts);

		if (e.getPointerCount() == 1) {
			Room selectedRoom = m_level.selectRoom(pts[0], pts[1]);
			if (selectedRoom == null) {
				m_view.reset();
				m_view.invalidate();
			}
		}

		return true;
	}

}