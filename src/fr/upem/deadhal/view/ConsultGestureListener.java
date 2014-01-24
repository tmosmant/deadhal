package fr.upem.deadhal.view;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class ConsultGestureListener extends GestureDetector.SimpleOnGestureListener {

	private ConsultView m_view;

	public ConsultGestureListener(ConsultView view) {
		m_view = view;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		m_view.reset();
		m_view.invalidate();
		return true;
	}

}