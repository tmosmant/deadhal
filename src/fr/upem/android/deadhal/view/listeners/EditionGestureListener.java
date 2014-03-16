package fr.upem.android.deadhal.view.listeners;

import android.view.GestureDetector;
import android.view.MotionEvent;
import fr.upem.android.deadhal.components.handlers.EditionLevelHandler;
import fr.upem.android.deadhal.view.AbstractView;
import fr.upem.android.deadhal.view.TouchEvent;

/**
 * This class handles motion event on the edition room part.
 * 
 * @author Thomas
 * 
 */
public class EditionGestureListener extends
		GestureDetector.SimpleOnGestureListener {

	private AbstractView m_view;
	private EditionLevelHandler m_levelHandler;

	/**
	 * Constructs the listener.
	 * 
	 * @param view
	 *            the view
	 * @param levelHandler
	 *            the level handler
	 */
	public EditionGestureListener(AbstractView view,
			EditionLevelHandler levelHandler) {
		m_view = view;
		m_levelHandler = levelHandler;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		float[] pts = m_view.convertCoordinates(e);
		if (e.getPointerCount() == 1) {
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