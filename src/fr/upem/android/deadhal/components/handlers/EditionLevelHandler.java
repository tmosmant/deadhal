package fr.upem.android.deadhal.components.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.graphics.Point;
import android.graphics.RectF;
import fr.upem.android.deadhal.components.Corridor;
import fr.upem.android.deadhal.components.Level;
import fr.upem.android.deadhal.components.Room;
import fr.upem.android.deadhal.components.listeners.SelectionRoomListener;
import fr.upem.android.deadhal.view.TouchEvent;

/**
 * This class handle the level for the edition part.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public class EditionLevelHandler extends AbstractLevelHandler {

	private Room m_selectedRoom;
	private ResizeType m_resizeType = null;
	private final static int MIN_MARGIN = 100;
	private Corridor m_selectedCorridor;

	/**
	 * Constructs the level handler.
	 * 
	 * @param level
	 *            the level to handle
	 */
	public EditionLevelHandler(Level level) {
		super(level);
	}

	/**
	 * Returns the selected room.
	 * 
	 * @return the selected room
	 */
	public Room getSelectedRoom() {
		return m_selectedRoom;
	}

	/**
	 * Return the selected room state.
	 * 
	 * @return true if there is a selected room, false otherwise
	 */
	public boolean isRoomSelected() {
		return m_selectedRoom != null;
	}

	@Override
	public void removeRoom(Room room) {
		if (room.equals(m_selectedRoom)) {
			m_selectedRoom = null;
		}
		super.removeRoom(room);
	}

	/**
	 * Force the selection a room. If the room to select is already selected,
	 * unselect it.
	 * 
	 * @param room
	 *            the room to select
	 * @return true if a room is selected, false otherwise
	 */
	public boolean selectRoom(Room room) {
		if (room.equals(m_selectedRoom)) {
			for (SelectionRoomListener listener : m_selectionRoomListeners) {
				listener.onUnselectRoom(getLevel().getRooms().get(
						m_selectedRoom.getId()));
			}
			m_selectedRoom = null;
			refreshView();
			return false;
		} else if (m_selectedRoom != null) {
			for (SelectionRoomListener listener : m_selectionRoomListeners) {
				listener.onUnselectRoom(getLevel().getRooms().get(
						m_selectedRoom.getId()));
				listener.onSelectRoom(getLevel().getRooms().get(room.getId()));
			}
		} else {
			for (SelectionRoomListener listener : m_selectionRoomListeners) {
				listener.onSelectRoom(getLevel().getRooms().get(room.getId()));
			}
		}
		m_selectedRoom = room;
		refreshView();
		return true;
	}

	/**
	 * Selects a room from coordinates.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 */
	public void selectRoomFromCoordinates(float x, float y) {
		if (m_selectedRoom != null && m_selectedRoom.getRect().contains(x, y)) {
			for (SelectionRoomListener listener : m_selectionRoomListeners) {
				listener.onUnselectRoom(m_selectedRoom);
			}
			m_selectedRoom = null;
			refreshView();
			return;
		}
		List<Room> reverseRooms = reverseRooms();
		for (Room room : reverseRooms) {
			if (!room.equals(m_selectedRoom) && room.getRect().contains(x, y)) {
				if (m_selectedRoom != null) {
					for (SelectionRoomListener listener : m_selectionRoomListeners) {
						listener.onUnselectRoom(m_selectedRoom);
					}
				}
				for (SelectionRoomListener listener : m_selectionRoomListeners) {
					listener.onSelectRoom(room);
				}
				m_selectedRoom = room;
				refreshView();
				return;
			}
		}
	}

	@Override
	public TouchEvent getProcess(float x, float y) {
		TouchEvent mode = TouchEvent.DRAG;

		int radius = 20;

		RectF leftTop = new RectF(m_selectedRoom.getRect().left - radius,
				m_selectedRoom.getRect().top - radius,
				m_selectedRoom.getRect().left + radius,
				m_selectedRoom.getRect().top + radius);
		RectF rightTop = new RectF(m_selectedRoom.getRect().right - radius,
				m_selectedRoom.getRect().top - radius,
				m_selectedRoom.getRect().right + radius,
				m_selectedRoom.getRect().top + radius);
		RectF leftBottom = new RectF(m_selectedRoom.getRect().left - radius,
				m_selectedRoom.getRect().bottom - radius,
				m_selectedRoom.getRect().left + radius,
				m_selectedRoom.getRect().bottom + radius);
		RectF rightBottom = new RectF(m_selectedRoom.getRect().right - radius,
				m_selectedRoom.getRect().bottom - radius,
				m_selectedRoom.getRect().right + radius,
				m_selectedRoom.getRect().bottom + radius);

		RectF left = new RectF(m_selectedRoom.getRect().left - radius,
				m_selectedRoom.getRect().top, m_selectedRoom.getRect().left
						+ radius, m_selectedRoom.getRect().bottom);
		RectF top = new RectF(m_selectedRoom.getRect().left,
				m_selectedRoom.getRect().top - radius,
				m_selectedRoom.getRect().right, m_selectedRoom.getRect().top
						+ radius);
		RectF right = new RectF(m_selectedRoom.getRect().right - radius,
				m_selectedRoom.getRect().top, m_selectedRoom.getRect().right
						+ radius, m_selectedRoom.getRect().bottom);
		RectF bottom = new RectF(m_selectedRoom.getRect().left,
				m_selectedRoom.getRect().bottom - radius,
				m_selectedRoom.getRect().right, m_selectedRoom.getRect().bottom
						+ radius);

		if (leftTop.contains(x, y)) {
			mode = TouchEvent.RESIZE_ROOM;
			m_resizeType = ResizeType.RESIZE_ROOM_LEFT_TOP;
		} else if (rightTop.contains(x, y)) {
			mode = TouchEvent.RESIZE_ROOM;
			m_resizeType = ResizeType.RESIZE_ROOM_RIGHT_TOP;
		} else if (leftBottom.contains(x, y)) {
			mode = TouchEvent.RESIZE_ROOM;
			m_resizeType = ResizeType.RESIZE_ROOM_LEFT_BOTTOM;
		} else if (rightBottom.contains(x, y)) {
			mode = TouchEvent.RESIZE_ROOM;
			m_resizeType = ResizeType.RESIZE_ROOM_RIGHT_BOTTOM;
		} else if (left.contains(x, y)) {
			mode = TouchEvent.RESIZE_ROOM;
			m_resizeType = ResizeType.RESIZE_ROOM_LEFT;
		} else if (top.contains(x, y)) {
			mode = TouchEvent.RESIZE_ROOM;
			m_resizeType = ResizeType.RESIZE_ROOM_TOP;
		} else if (right.contains(x, y)) {
			mode = TouchEvent.RESIZE_ROOM;
			m_resizeType = ResizeType.RESIZE_ROOM_RIGHT;
		} else if (bottom.contains(x, y)) {
			mode = TouchEvent.RESIZE_ROOM;
			m_resizeType = ResizeType.RESIZE_ROOM_BOTTOM;
		} else if (m_selectedRoom.getRect().contains(x, y)) {
			mode = TouchEvent.DRAG_ROOM;
			m_resizeType = null;
		}
		return mode;
	}

	@Override
	public void endProcess() {
		m_resizeType = null;
	}

	/**
	 * Translates the position of the selected room.
	 * 
	 * @param dx
	 *            the x distance to add
	 * @param dy
	 *            the y distance to add
	 */
	public void translateSelectedRoom(float dx, float dy) {
		RectF rect = m_selectedRoom.getRect();
		rect.left += dx;
		rect.top += dy;
		rect.right += dx;
		rect.bottom += dy;
	}

	private final static Point m_minNone = new Point(1, 1);
	private final static Point m_minX = new Point(0, 1);
	private final static Point m_minY = new Point(1, 0);
	private final static Point m_minBoth = new Point(0, 0);

	/**
	 * Resizes the selected room.
	 * 
	 * @param dx
	 *            the x distance to add
	 * @param dy
	 *            the y distance to add
	 * @return a point used to know which size have been resized
	 */
	public Point resizeSelectedRoom(float dx, float dy) {

		switch (m_resizeType) {
		case RESIZE_ROOM_LEFT_TOP:
			if (m_selectedRoom.getRect().left + dx >= m_selectedRoom.getRect().right
					- MIN_MARGIN
					&& m_selectedRoom.getRect().top + dy < m_selectedRoom
							.getRect().bottom - MIN_MARGIN) {
				m_selectedRoom.getRect().top += dy;
				return m_minX;
			} else if (m_selectedRoom.getRect().left + dx < m_selectedRoom
					.getRect().right - MIN_MARGIN
					&& m_selectedRoom.getRect().top + dy >= m_selectedRoom
							.getRect().bottom - MIN_MARGIN) {
				m_selectedRoom.getRect().left += dx;
				return m_minY;
			} else if (m_selectedRoom.getRect().left + dx < m_selectedRoom
					.getRect().right - MIN_MARGIN
					&& m_selectedRoom.getRect().top + dy < m_selectedRoom
							.getRect().bottom - MIN_MARGIN) {
				m_selectedRoom.getRect().left += dx;
				m_selectedRoom.getRect().top += dy;
				return m_minNone;
			}
			break;
		case RESIZE_ROOM_RIGHT_TOP:
			if (m_selectedRoom.getRect().right + dx <= m_selectedRoom.getRect().left
					+ MIN_MARGIN
					&& m_selectedRoom.getRect().top + dy < m_selectedRoom
							.getRect().bottom - MIN_MARGIN) {
				m_selectedRoom.getRect().top += dy;
				return m_minX;
			} else if (m_selectedRoom.getRect().right + dx > m_selectedRoom
					.getRect().left + MIN_MARGIN
					&& m_selectedRoom.getRect().top + dy >= m_selectedRoom
							.getRect().bottom - MIN_MARGIN) {
				m_selectedRoom.getRect().right += dx;
				return m_minY;
			} else if (m_selectedRoom.getRect().right + dx > m_selectedRoom
					.getRect().left + MIN_MARGIN
					&& m_selectedRoom.getRect().top + dy < m_selectedRoom
							.getRect().bottom - MIN_MARGIN) {
				m_selectedRoom.getRect().right += dx;
				m_selectedRoom.getRect().top += dy;
				return m_minNone;
			}
			break;
		case RESIZE_ROOM_LEFT_BOTTOM:
			if (m_selectedRoom.getRect().left + dx >= m_selectedRoom.getRect().right
					- MIN_MARGIN
					&& m_selectedRoom.getRect().bottom + dy > m_selectedRoom
							.getRect().top + MIN_MARGIN) {
				m_selectedRoom.getRect().bottom += dy;
				return m_minX;
			} else if (m_selectedRoom.getRect().left + dx < m_selectedRoom
					.getRect().right - MIN_MARGIN
					&& m_selectedRoom.getRect().bottom + dy <= m_selectedRoom
							.getRect().top + MIN_MARGIN) {
				m_selectedRoom.getRect().left += dx;
				return m_minY;
			} else if (m_selectedRoom.getRect().left + dx < m_selectedRoom
					.getRect().right - MIN_MARGIN
					&& m_selectedRoom.getRect().bottom + dy > m_selectedRoom
							.getRect().top + MIN_MARGIN) {
				m_selectedRoom.getRect().left += dx;
				m_selectedRoom.getRect().bottom += dy;
				return m_minNone;
			}
			break;
		case RESIZE_ROOM_RIGHT_BOTTOM:
			if (m_selectedRoom.getRect().right + dx <= m_selectedRoom.getRect().left
					+ MIN_MARGIN
					&& m_selectedRoom.getRect().bottom + dy > m_selectedRoom
							.getRect().top + MIN_MARGIN) {
				m_selectedRoom.getRect().bottom += dy;
				return m_minX;
			} else if (m_selectedRoom.getRect().right + dx > m_selectedRoom
					.getRect().left + MIN_MARGIN
					&& m_selectedRoom.getRect().bottom + dy <= m_selectedRoom
							.getRect().top + MIN_MARGIN) {
				m_selectedRoom.getRect().right += dx;
				return m_minY;
			} else if (m_selectedRoom.getRect().right + dx > m_selectedRoom
					.getRect().left + MIN_MARGIN
					&& m_selectedRoom.getRect().bottom + dy > m_selectedRoom
							.getRect().top + MIN_MARGIN) {
				m_selectedRoom.getRect().right += dx;
				m_selectedRoom.getRect().bottom += dy;
				return m_minNone;
			}
			break;
		case RESIZE_ROOM_LEFT:
			if (m_selectedRoom.getRect().left + dx < m_selectedRoom.getRect().right
					- MIN_MARGIN) {
				m_selectedRoom.getRect().left += dx;
				return m_minNone;
			}
			return m_minX;
		case RESIZE_ROOM_TOP:
			if (m_selectedRoom.getRect().top + dy < m_selectedRoom.getRect().bottom
					- MIN_MARGIN) {
				m_selectedRoom.getRect().top += dy;
				return m_minNone;
			}
			return m_minY;
		case RESIZE_ROOM_RIGHT:
			if (m_selectedRoom.getRect().right + dx > m_selectedRoom.getRect().left
					+ MIN_MARGIN) {
				m_selectedRoom.getRect().right += dx;
				return m_minNone;
			}
			return m_minX;
		case RESIZE_ROOM_BOTTOM:
			if (m_selectedRoom.getRect().bottom + dy > m_selectedRoom.getRect().top
					+ MIN_MARGIN) {
				m_selectedRoom.getRect().bottom += dy;
				return m_minNone;
			}
			return m_minY;
		}
		return m_minBoth;
	}

	/**
	 * Returns the resize type.
	 * 
	 * @return the resize type
	 */
	public ResizeType getResizeType() {
		return m_resizeType;
	}

	/**
	 * Returns the list of rooms ordered by their name.
	 * 
	 * @return the list of rooms ordered by their name
	 */
	public List<Room> getRoomByName() {
		Collection<Room> collection = m_level.getRooms().values();

		ArrayList<Room> list = new ArrayList<Room>(collection);

		Collections.sort(list, new Comparator<Room>() {

			@Override
			public int compare(Room lhs, Room rhs) {
				return lhs.getName().compareTo(rhs.getName());
			}
		});
		return list;
	}

	/**
	 * Returns the list of corridors by their source name.
	 * 
	 * @return the list of corridors by their source name
	 */
	public List<Corridor> getCorridorBySrc() {
		Collection<Corridor> collection = m_level.getCorridors().values();

		ArrayList<Corridor> list = new ArrayList<Corridor>(collection);

		Collections.sort(list, new Comparator<Corridor>() {

			@Override
			public int compare(Corridor lhs, Corridor rhs) {
				Room roomL = m_level.getRooms().get(lhs.getSrc());
				Room roomR = m_level.getRooms().get(rhs.getSrc());
				return roomL.getName().compareTo(roomR.getName());
			}
		});
		return list;
	}

	/**
	 * Force the unselection of a room.
	 */
	public void unselectRoom() {
		m_selectedRoom = null;
		refreshView();
	}

	/**
	 * Toggle the selection of a corridor.
	 * 
	 * @param corridor
	 *            the corridor to select
	 * @return true if a corridor is selected, false otherwise
	 */
	public boolean selectCorridor(Corridor corridor) {
		if (m_selectedCorridor == null || !m_selectedCorridor.equals(corridor)) {
			m_selectedCorridor = corridor;
			m_view.getVibrator().vibrate(100);
			refreshView();
			return true;
		} else {
			m_view.getVibrator().vibrate(100);
			unselectCorridor();
		}
		return false;
	}

	/**
	 * For the unselection of a corridor.
	 */
	public void unselectCorridor() {
		m_selectedCorridor = null;
		refreshView();
	}

	/**
	 * Returns the selected corridor.
	 * 
	 * @return the selected corridor
	 */
	public Corridor getSelectedCorridor() {
		return m_selectedCorridor;
	}

}
