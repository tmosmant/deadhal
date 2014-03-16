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

/**
 * This class is used to provide basic function for level handlers.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public abstract class AbstractLevelHandler {

	protected Level m_level;
	protected List<SelectionRoomListener> m_selectionRoomListeners = new LinkedList<SelectionRoomListener>();
	protected AbstractView m_view;

	/**
	 * Constructs the level handler.
	 * 
	 * @param level
	 *            level to handle
	 */
	public AbstractLevelHandler(Level level) {
		m_level = level;
	}

	/**
	 * Returns the view.
	 * 
	 * @return the view
	 */
	public AbstractView getView() {
		return m_view;
	}

	/**
	 * Set the view.
	 * 
	 * @param view
	 *            the view to set
	 */
	public void setView(AbstractView view) {
		m_view = view;
	}

	/**
	 * Add a listener to be noticed when a room is selected or unselected.
	 * 
	 * @param listener
	 *            the listener
	 */
	public void addSelectionRoomListener(SelectionRoomListener listener) {
		m_selectionRoomListeners.add(listener);
	}

	/**
	 * Remove a listener.
	 * 
	 * @param listener
	 */
	public void removeSelectionRoomListener(SelectionRoomListener listener) {
		m_selectionRoomListeners.remove(listener);
	}

	/**
	 * Add a room.
	 * 
	 * @param room
	 *            the room to add
	 */
	public void addRoom(Room room) {
		getLevel().addRoom(room);
		refreshView();
	}

	/**
	 * Remove a room.
	 * 
	 * @param room
	 *            the room to remove
	 */
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

	/**
	 * Add a corridor.
	 * 
	 * @param corridor
	 *            the corridor to add
	 */
	public void addCorridor(Corridor corridor) {
		m_level.addCorridor(corridor);
	}

	/**
	 * Remove a corridor.
	 * 
	 * @param corridor
	 *            the corridor to remove
	 */
	public void removeCorridor(Corridor corridor) {
		m_level.removeCorridor(corridor);
	}

	/**
	 * Returns the level.
	 * 
	 * @return the level
	 */
	public Level getLevel() {
		return m_level;
	}

	/**
	 * Get the inital process for finger coordinates.
	 * 
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 * @return the touch event to handle
	 */
	public abstract TouchEvent getProcess(float x, float y);

	/**
	 * Put a term to the process.
	 */
	public abstract void endProcess();

	/**
	 * Refresh the view.
	 */
	public void refreshView() {
		if (m_view != null) {
			m_view.refresh();
		}
	}

	/**
	 * Get a room from coordinates.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @return the room for the previous coordinates
	 */
	public Room getRoomFromCoordinates(float x, float y) {
		List<Room> reverseRooms = reverseRooms();
		for (Room room : reverseRooms) {
			if (room.getRect().contains(x, y)) {
				return room;
			}
		}
		return null;
	}

	/**
	 * Reverse the list of room. This one is used to handle properly the
	 * superpositions of rooms on canvas.
	 * 
	 * @return the reversed list of rooms
	 */
	protected List<Room> reverseRooms() {
		Collection<Room> rooms = m_level.getRooms().values();
		LinkedList<Room> reverseRooms = new LinkedList<Room>(rooms);
		Collections.reverse(reverseRooms);
		return reverseRooms;
	}

	/**
	 * Compute the intersection between a line and a rectangle.
	 * 
	 * @param start
	 *            the start coordinate of the line
	 * @param end
	 *            the end coordinate of the line
	 * @param rect
	 *            the rectangle to work with
	 * @return the first known intersection
	 */
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

	/**
	 * Compute the intersection between two lines. Found on stackoverflow.
	 * 
	 * @param p0_x
	 *            the x1 coordinate of the first line
	 * @param p0_y
	 *            the y1 coordinate of the first line
	 * @param p1_x
	 *            the x2 coordinate of the first line
	 * @param p1_y
	 *            the y2 coordinate of the first line
	 * @param p2_x
	 *            the x1 coordinate of the second line
	 * @param p2_y
	 *            the y1 coordinate of the second line
	 * @param p3_x
	 *            the x2 coordinate of the second line
	 * @param p3_y
	 *            the y2 coordinate of the second line
	 * @return the point of intersection between the two lines, or null if none
	 */
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
