package fr.upem.deadhal.components.handlers;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import android.graphics.Point;
import android.graphics.RectF;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.listeners.SelectionRoomListener;
import fr.upem.deadhal.view.TouchEvent;

public class EditionLevelHandler extends AbstractLevelHandler {

	private Room m_selectedRoom;
	private ResizeType m_resizeType = null;
	private final static int MIN_MARGIN = 100;

	public EditionLevelHandler(Level level) {
		super(level);
	}

	public Room getSelectedRoom() {
		return m_selectedRoom;
	}

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

	public boolean selectRoom(Room room) {
		if (room.equals(m_selectedRoom)) {
			for (SelectionRoomListener listener : selectionRoomListeners) {
				listener.onUnselectRoom(getLevel().getRooms().get(
						m_selectedRoom.getId()));
			}
			m_selectedRoom = null;
			invalidateView();
			return false;
		} else if (m_selectedRoom != null) {
			for (SelectionRoomListener listener : selectionRoomListeners) {
				listener.onUnselectRoom(getLevel().getRooms().get(
						m_selectedRoom.getId()));
				listener.onSelectRoom(getLevel().getRooms().get(room.getId()));
			}
		} else {
			for (SelectionRoomListener listener : selectionRoomListeners) {
				listener.onSelectRoom(getLevel().getRooms().get(room.getId()));
			}
		}
		m_selectedRoom = room;
		invalidateView();
		return true;
	}

	public boolean selectRoomFromCoordinates(float x, float y) {
		Collection<Room> rooms = m_level.getRooms().values();
		LinkedList<Room> reverseRooms = new LinkedList<Room>(rooms);
		Collections.reverse(reverseRooms);
		for (Room room : reverseRooms) {
			if (m_selectedRoom != null) {
				if (room.getRect().contains(x, y)) {
					if (room.equals(m_selectedRoom)) {
						for (SelectionRoomListener listener : selectionRoomListeners) {
							listener.onUnselectRoom(room);
						}
						m_selectedRoom = null;
						invalidateView();
						return true;
					}
				}
			} else {
				if (room.getRect().contains(x, y)) {
					for (SelectionRoomListener listener : selectionRoomListeners) {
						listener.onSelectRoom(room);
					}
					m_selectedRoom = room;
					invalidateView();
					return true;
				}
			}
		}
		return false;
	}

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

	public void translateSelectedRoom(float dx, float dy) {
		m_selectedRoom.getRect().left += dx;
		m_selectedRoom.getRect().top += dy;
		m_selectedRoom.getRect().right += dx;
		m_selectedRoom.getRect().bottom += dy;
	}

	private final static Point m_minNone = new Point(1, 1);
	private final static Point m_minX = new Point(0, 1);
	private final static Point m_minY = new Point(1, 0);
	private final static Point m_minBoth = new Point(0, 0);

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
			return m_minY;
		case RESIZE_ROOM_BOTTOM:
			if (m_selectedRoom.getRect().bottom + dy > m_selectedRoom.getRect().top
					+ MIN_MARGIN) {
				m_selectedRoom.getRect().bottom += dy;
				return m_minNone;
			}
			return m_minX;
		}
		return m_minBoth;
	}

	public ResizeType getResizeType() {
		return m_resizeType;
	}

}