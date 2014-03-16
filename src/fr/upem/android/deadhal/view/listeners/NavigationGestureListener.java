package fr.upem.android.deadhal.view.listeners;

import android.view.GestureDetector;
import android.view.MotionEvent;
import fr.upem.android.deadhal.components.Room;
import fr.upem.android.deadhal.components.handlers.NavigationLevelHandler;
import fr.upem.android.deadhal.view.AbstractView;
import fr.upem.android.deadhal.view.TouchEvent;

/**
 * This class handler the motion event on the navigation part.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public class NavigationGestureListener extends
		GestureDetector.SimpleOnGestureListener {

	private AbstractView m_view;
	private NavigationLevelHandler m_levelHandler;

	/**
	 * Constructs the listener.
	 * 
	 * @param view
	 *            the view
	 * @param levelHandler
	 *            the level handler.
	 */
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
					m_levelHandler.getPawn().handleMove(pts[0], pts[1]);
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