package fr.upem.deadhal.view;

import android.graphics.Matrix;
import android.view.GestureDetector;
import android.view.MotionEvent;
import fr.upem.deadhal.graphics.drawable.LevelDrawable;

public class GestureListener extends GestureDetector.SimpleOnGestureListener {

	private TouchView m_view;
	private LevelDrawable m_levelDrawable;

	public GestureListener(TouchView view, LevelDrawable levelDrawable) {
		m_view = view;
		m_levelDrawable = levelDrawable;
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
			boolean isSelectedRoom = m_levelDrawable.selectRoom(pts[0], pts[1]);
			if (isSelectedRoom == false
					&& m_levelDrawable.isRoomSelected() == false) {
				m_view.reset();
				m_view.invalidate();
			}
		}

		return true;
	}

}