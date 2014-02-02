package fr.upem.deadhal.view.listeners;

import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.listeners.SelectionRoomListener;
import fr.upem.deadhal.graphics.drawable.LevelDrawable;
import fr.upem.deadhal.view.NavigationView;
import android.graphics.Matrix;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class NavigationGestureListener extends
		GestureDetector.SimpleOnGestureListener {

	private NavigationView m_view;
	private LevelDrawable m_levelDrawable;

	public NavigationGestureListener(NavigationView m_view,
			LevelDrawable m_levelDrawable) {
		this.m_view = m_view;
		this.m_levelDrawable = m_levelDrawable;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	// @Override
	// public boolean onDoubleTap(MotionEvent e) {
	// m_view.reset();
	// m_view.invalidate();
	// return true;
	// }
	public boolean onDoubleTap(MotionEvent e) {
		Matrix inverse = new Matrix();

		m_view.getMatrix().invert(inverse);

		float[] pts = new float[2];

		pts[0] = e.getX(0);
		pts[1] = e.getY(0);

		inverse.mapPoints(pts);
		if (e.getPointerCount() == 1) {
			boolean isSelectedRoom = m_levelDrawable.selectRoomFromCoordinates(
					pts[0], pts[1]);
			if (isSelectedRoom == false
					&& m_levelDrawable.isRoomSelected() == false) {
				m_view.reset();
				m_view.invalidate();
			}
		}

		return true;
	}

	public boolean unselectRoom(Room room) {
		boolean selectRoom = m_levelDrawable.selectRoom(room);
		m_view.invalidate();
		return selectRoom;
	}
	
	public void setStart(Room room) {
		m_levelDrawable.selectRoom(room);
		m_view.invalidate();
	}
	
	public void setEnd(Room room) {
		m_levelDrawable.selectRoom(room);
		m_view.invalidate();
	}

	public void addSelectionRoomListener(SelectionRoomListener listener) {
		m_levelDrawable.addSelectionRoomListener(listener);
	}

	public void removeSelectionRoomListener(SelectionRoomListener listener) {
		m_levelDrawable.removeSelectionRoomListener(listener);
	}

}