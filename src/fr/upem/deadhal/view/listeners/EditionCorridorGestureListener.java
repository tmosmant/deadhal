package fr.upem.deadhal.view.listeners;

import android.view.GestureDetector;
import android.view.MotionEvent;
import fr.upem.deadhal.components.handlers.EditionCorridorLevelHandler;
import fr.upem.deadhal.view.AbstractView;
import fr.upem.deadhal.view.TouchEvent;

public class EditionCorridorGestureListener extends
		GestureDetector.SimpleOnGestureListener {

	private AbstractView m_view;
	private EditionCorridorLevelHandler m_levelHander;

	public EditionCorridorGestureListener(AbstractView view,
			EditionCorridorLevelHandler levelHandler) {
		m_view = view;
		m_levelHander = levelHandler;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		float[] pts = m_view.convertCoordinates(e);
		if (e.getPointerCount() == 1) {
			m_levelHander.selectRoomFromCoordinates(pts[0], pts[1]);
			m_view.setMode(TouchEvent.NONE);
			m_view.refresh();
		}
		super.onLongPress(e);
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		m_view.reset();
		m_view.refresh();
		return super.onDoubleTap(e);
	}

}