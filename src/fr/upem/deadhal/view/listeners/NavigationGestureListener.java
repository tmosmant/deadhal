package fr.upem.deadhal.view.listeners;

import android.graphics.Matrix;
import android.view.GestureDetector;
import android.view.MotionEvent;
import fr.upem.deadhal.components.handlers.NavigationLevelHandler;
import fr.upem.deadhal.view.AbstractView;
import fr.upem.deadhal.view.TouchEvent;

public class NavigationGestureListener extends
		GestureDetector.SimpleOnGestureListener {

	private AbstractView m_view;
	private NavigationLevelHandler m_levelHandler;

	public NavigationGestureListener(AbstractView view,
			NavigationLevelHandler levelHandler) {
		m_view = view;
		m_levelHandler = levelHandler;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		Matrix inverse = new Matrix();

		m_view.getMatrix().invert(inverse);

		float[] pts = new float[2];

		pts[0] = e.getX(0);
		pts[1] = e.getY(0);

		inverse.mapPoints(pts);
		if (e.getPointerCount() == 1) {
			m_levelHandler.selectRoomFromCoordinates(pts[0], pts[1]);
			m_view.setMode(TouchEvent.NONE);
		}

		super.onLongPress(e);
	}
//
//	@Override
//	public boolean onSingleTapConfirmed(MotionEvent e) {
//		Matrix inverse = new Matrix();
//
//		m_view.getMatrix().invert(inverse);
//
//		float[] pts = new float[2];
//
//		pts[0] = e.getX(0);
//		pts[1] = e.getY(0);
//
//		inverse.mapPoints(pts);
//		if (e.getPointerCount() == 1) {
//
//			m_levelHandler.moveMinotaur(pts[0], pts[1]);
//
//			m_view.setMode(TouchEvent.NONE);
//			return true;
//		}
//
//		return super.onSingleTapConfirmed(e);
//	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		m_view.reset();
		return super.onDoubleTap(e);
	}

}