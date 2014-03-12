package fr.upem.deadhal.view.listeners;

import android.view.GestureDetector;
import android.view.MotionEvent;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.handlers.EditionLevelHandler;
import fr.upem.deadhal.view.AbstractView;
import fr.upem.deadhal.view.TouchEvent;

public class EditionGestureListener extends
		GestureDetector.SimpleOnGestureListener {

	private AbstractView m_view;
	private EditionLevelHandler m_levelHandler;

	public EditionGestureListener(AbstractView view,
			EditionLevelHandler levelHandler) {
		m_view = view;
		m_levelHandler = levelHandler;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		float[] pts = m_view.convertCoordinates(e);
		if (e.getPointerCount() == 1) {
			Room roomFromCoordinates = m_levelHandler.getRoomFromCoordinates(pts[0], pts[1]);
			if (roomFromCoordinates!= null) {
				System.out.println(roomFromCoordinates.getName());
			}
			m_levelHandler.selectRoomFromCoordinates(pts[0], pts[1]);
			m_view.setMode(TouchEvent.NONE);
		}
		super.onLongPress(e);
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		m_view.reset();
		m_view.refresh();
		return true;
	}

}