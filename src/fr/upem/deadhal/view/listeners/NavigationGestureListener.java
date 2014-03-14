package fr.upem.deadhal.view.listeners;

import android.view.GestureDetector;
import android.view.MotionEvent;
import fr.upem.deadhal.components.Room;
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
		m_view.setMode(TouchEvent.NONE);
		float[] pts = m_view.convertCoordinates(e);
		if (e.getPointerCount() == 1) {
			Room roomFromCoordinates = m_levelHandler.getRoomFromCoordinates(
					pts[0], pts[1]);
			Room selectedRoom = m_levelHandler.getSelectedRoom();
			if (selectedRoom != null
					&& selectedRoom.equals(roomFromCoordinates)) {
				m_levelHandler.selectRoom(null);
			} else {
				if (selectedRoom == null && selectedRoom == null) {
					m_levelHandler.selectRoom(roomFromCoordinates);
					m_view.setMode(TouchEvent.MOVE);
				}
			}
		}

		super.onLongPress(e);
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		m_view.reset();
		return super.onDoubleTap(e);
	}

}