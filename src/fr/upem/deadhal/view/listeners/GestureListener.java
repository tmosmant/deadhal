package fr.upem.deadhal.view.listeners;

import android.graphics.Matrix;
import android.view.GestureDetector;
import android.view.MotionEvent;
import fr.upem.deadhal.components.handlers.AbstractLevelHandler;
import fr.upem.deadhal.view.AbstractView;

public class GestureListener extends GestureDetector.SimpleOnGestureListener {

	private AbstractView m_view;
	private AbstractLevelHandler m_levelHander;

	public GestureListener(AbstractView view, AbstractLevelHandler levelHandler) {
		m_view = view;
		m_levelHander = levelHandler;
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
			boolean isSelectedRoom = m_levelHander.selectRoomFromCoordinates(
					pts[0], pts[1]);
			if (isSelectedRoom == false
					&& m_levelHander.isRoomSelected() == false) {
				m_view.reset();
				m_view.invalidate();
			}
		}

		return true;
	}

}