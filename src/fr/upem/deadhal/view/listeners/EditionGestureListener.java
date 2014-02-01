package fr.upem.deadhal.view.listeners;

import android.graphics.Matrix;
import android.view.GestureDetector;
import android.view.MotionEvent;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.listeners.SelectionRoomListener;
import fr.upem.deadhal.graphics.drawable.LevelDrawable;
import fr.upem.deadhal.view.CustomView;

public class EditionGestureListener extends
		GestureDetector.SimpleOnGestureListener {

	private CustomView m_view;
	private LevelDrawable m_levelDrawable;

	public EditionGestureListener(CustomView view, LevelDrawable levelDrawable) {
		m_view = view;
		m_levelDrawable = levelDrawable;
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

	public void removeRoom(Room room) {
		m_levelDrawable.removeRoom(room);
		m_view.invalidate();
	}

	public void addRoom(Room room) {
		m_levelDrawable.addRoom(room);
		m_view.invalidate();
	}

	public boolean selectRoom(Room room) {
		boolean selectRoom = m_levelDrawable.selectRoom(room);
		m_view.invalidate();
		return selectRoom;
	}

	public void addSelectionRoomListener(SelectionRoomListener listener) {
		m_levelDrawable.addSelectionRoomListener(listener);
	}

	public void removeSelectionRoomListener(SelectionRoomListener listener) {
		m_levelDrawable.removeSelectionRoomListener(listener);
	}

	public Level getLevel() {
		return m_levelDrawable.getCurrentLevel();
	}

}