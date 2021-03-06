package fr.upem.android.deadhal.view.listeners;

import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import fr.upem.android.deadhal.components.Room;
import fr.upem.android.deadhal.components.handlers.EditionCorridorLevelHandler;
import fr.upem.android.deadhal.view.AbstractView;
import fr.upem.android.deadhal.view.TouchEvent;

/**
 * This class handles motion event on the edition corridor part.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public class EditionCorridorGestureListener extends
		GestureDetector.SimpleOnGestureListener {

	private AbstractView m_view;
	private EditionCorridorLevelHandler m_levelHander;

	/**
	 * Constructs the listener.
	 * 
	 * @param view
	 *            the view
	 * @param levelHandler
	 *            the level handler
	 */
	public EditionCorridorGestureListener(AbstractView view,
			EditionCorridorLevelHandler levelHandler) {
		m_view = view;
		m_levelHander = levelHandler;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		float[] pts = m_view.convertCoordinates(e);
		if (e.getPointerCount() == 1) {
			Room roomFromCoordinates = m_levelHander.getRoomFromCoordinates(
					pts[0], pts[1]);
			if (roomFromCoordinates != null) {
				m_levelHander.setRoom(roomFromCoordinates, new PointF(pts[0],
						pts[1]));
			}
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