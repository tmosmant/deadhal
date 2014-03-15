package fr.upem.android.deadhal.components.handlers;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.PointF;
import android.graphics.RectF;
import fr.upem.android.deadhal.components.Corridor;
import fr.upem.android.deadhal.components.Level;
import fr.upem.android.deadhal.components.Room;
import fr.upem.android.deadhal.components.listeners.SelectionRoomListener;
import fr.upem.android.deadhal.view.AbstractView;
import fr.upem.android.deadhal.view.TouchEvent;

public abstract class AbstractLevelHandler {

	protected Level m_level;
	protected List<SelectionRoomListener> m_selectionRoomListeners = new LinkedList<SelectionRoomListener>();
	protected AbstractView m_view;

	public AbstractLevelHandler(Level level) {
		m_level = level;
	}

	public AbstractView getView() {
		return m_view;
	}

	public void setView(AbstractView view) {
		m_view = view;
	}

	public void addSelectionRoomListener(SelectionRoomListener listener) {
		m_selectionRoomListeners.add(listener);
	}

	public void removeSelectionRoomListener(SelectionRoomListener listener) {
		m_selectionRoomListeners.remove(listener);
	}

	public void addRoom(Room room) {
		getLevel().addRoom(room);
		refreshView();
	}

	public void removeRoom(Room room) {
		for (Corridor corridor : m_level.getCorridors().values()) {
			if (room.getId().equals(corridor.getSrc())
					|| room.getId().equals(corridor.getDst())) {
				m_level.removeCorridor(corridor);
			}
		}
		getLevel().removeRoom(room);
		refreshView();
	}

	public void addCorridor(Corridor corridor) {
		m_level.addCorridor(corridor);
	}

	public void removeCorridor(Corridor corridor) {
		m_level.removeCorridor(corridor);
	}

	public Level getLevel() {
		return m_level;
	}

	public abstract TouchEvent getProcess(float x, float y);

	public abstract void endProcess();

	public void refreshView() {
		if (m_view != null) {
			m_view.refresh();
		}
	}

	public Room getRoomFromCoordinates(float x, float y) {
		List<Room> reverseRooms = reverseRooms();
		for (Room room : reverseRooms) {
			if (room.getRect().contains(x, y)) {
				return room;
			}
		}
		return null;
	}

	protected List<Room> reverseRooms() {
		Collection<Room> rooms = m_level.getRooms().values();
		LinkedList<Room> reverseRooms = new LinkedList<Room>(rooms);
		Collections.reverse(reverseRooms);
		return reverseRooms;
	}

	public PointF computeIntersection(PointF start, PointF end, RectF rect) {
		PointF point = null;
		point = intersection(start.x, start.y, end.x, end.y, rect.left,
				rect.top, rect.right, rect.top);
		if (point != null) {
			return point;
		}
		point = intersection(start.x, start.y, end.x, end.y, rect.left,
				rect.top, rect.left, rect.bottom);
		if (point != null) {
			return point;
		}
		point = intersection(start.x, start.y, end.x, end.y, rect.left,
				rect.bottom, rect.right, rect.bottom);
		if (point != null) {
			return point;
		}
		point = intersection(start.x, start.y, end.x, end.y, rect.right,
				rect.top, rect.right, rect.bottom);
		if (point != null) {
			return point;
		}
		return start;
	}

	PointF intersection(float p0_x, float p0_y, float p1_x, float p1_y,
			float p2_x, float p2_y, float p3_x, float p3_y) {
		float s02_x, s02_y, s10_x, s10_y, s32_x, s32_y, s_numer, t_numer, denom, t;
		s10_x = p1_x - p0_x;
		s10_y = p1_y - p0_y;
		s32_x = p3_x - p2_x;
		s32_y = p3_y - p2_y;

		denom = s10_x * s32_y - s32_x * s10_y;
		if (denom == 0) {
			return null;
		}
		boolean denomPositive = denom > 0;

		s02_x = p0_x - p2_x;
		s02_y = p0_y - p2_y;
		s_numer = s10_x * s02_y - s10_y * s02_x;
		if ((s_numer <= 0) == denomPositive) {
			return null;
		}

		t_numer = s32_x * s02_y - s32_y * s02_x;
		if ((t_numer < 0) == denomPositive) {
			return null;
		}

		if (((s_numer > denom) == denomPositive)
				|| ((t_numer > denom) == denomPositive)) {
			return null;
		}
		t = t_numer / denom;
		return new PointF(p0_x + (t * s10_x), p0_y + (t * s10_y));
	}
}
