package fr.upem.deadhal.graphics.drawable;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.UUID;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.listeners.SelectionRoomListener;
import fr.upem.deadhal.graphics.Paints;
import fr.upem.deadhal.view.TouchEvent;

public class EditionLevelDrawable extends AbstractDrawable {

	private UUID m_selectedRoomId = null;
	private ResizeType m_resizeType = null;
	private final static int MIN_MARGIN = 100;


	public EditionLevelDrawable(Level level) {
		super(level);
	}

	@Override
	public void draw(Canvas canvas) {
		for (Room room : m_level.getRooms().values()) {
			if (!room.getId().equals(m_selectedRoomId)) {
				drawRoom(canvas, room);
			}
		}
		if (m_selectedRoomId != null) {

			Room selectedRoom = m_level.getRooms().get(m_selectedRoomId);

			for (Room room : m_level.getRooms().values()) {
				if (!room.getId().equals(m_selectedRoomId)) {
					if (RectF
							.intersects(room.getRect(), selectedRoom.getRect())) {
						drawRoomSelectedError(canvas, selectedRoom);
						return;
					}
				}
			}

			drawRoomSelected(canvas, selectedRoom);
		}
	}

	private void drawRoomSelected(Canvas canvas, Room room) {
		float borderSize = (float) 1.5;

		RectF rect = room.getRect();
		RectF rectB = new RectF(rect.left + borderSize, rect.top + borderSize,
				rect.right - borderSize, rect.bottom - borderSize);
		drawPoints(rect, canvas);
		canvas.drawRect(rect, Paints.ROOM_SELECTED_BACKGROUND);
		canvas.drawRect(rectB, Paints.ROOM_SELECTED_BORDER);
		drawTitle(canvas, room);
	}

	private void drawRoomSelectedError(Canvas canvas, Room room) {
		float borderSize = (float) 1.5;

		RectF rect = room.getRect();
		RectF rectB = new RectF(rect.left + borderSize, rect.top + borderSize,
				rect.right - borderSize, rect.bottom - borderSize);
		drawPoints(rect, canvas);
		canvas.drawRect(rect, Paints.ROOM_SELECTED_BACKGROUND_ERROR);
		canvas.drawRect(rectB, Paints.ROOM_SELECTED_BORDER);
		drawTitle(canvas, room);
	}

	private void drawPoints(RectF rect, Canvas canvas) {
		PointF p = new PointF();
		float radius = 12;

		if (m_resizeType == null
				|| m_resizeType == ResizeType.RESIZE_ROOM_LEFT_TOP
				|| m_resizeType == ResizeType.RESIZE_ROOM_LEFT
				|| m_resizeType == ResizeType.RESIZE_ROOM_LEFT_BOTTOM) {
			p.set(rect.left, rect.centerY());
			canvas.drawCircle(p.x, p.y, radius, Paints.ROOM_SELECTED_POINT);
		}

		if (m_resizeType == null
				|| m_resizeType == ResizeType.RESIZE_ROOM_LEFT_TOP
				|| m_resizeType == ResizeType.RESIZE_ROOM_TOP
				|| m_resizeType == ResizeType.RESIZE_ROOM_RIGHT_TOP) {
			p.set(rect.centerX(), rect.top);
			canvas.drawCircle(p.x, p.y, radius, Paints.ROOM_SELECTED_POINT);
		}
		if (m_resizeType == null
				|| m_resizeType == ResizeType.RESIZE_ROOM_RIGHT_TOP
				|| m_resizeType == ResizeType.RESIZE_ROOM_RIGHT
				|| m_resizeType == ResizeType.RESIZE_ROOM_RIGHT_BOTTOM) {
			p.set(rect.right, rect.centerY());
			canvas.drawCircle(p.x, p.y, radius, Paints.ROOM_SELECTED_POINT);
		}
		if (m_resizeType == null
				|| m_resizeType == ResizeType.RESIZE_ROOM_RIGHT_BOTTOM
				|| m_resizeType == ResizeType.RESIZE_ROOM_BOTTOM
				|| m_resizeType == ResizeType.RESIZE_ROOM_LEFT_BOTTOM) {
			p.set(rect.centerX(), rect.bottom);
			canvas.drawCircle(p.x, p.y, radius, Paints.ROOM_SELECTED_POINT);
		}
	}

	public boolean isRoomSelected() {
		return m_selectedRoomId != null;
	}

	public boolean selectRoomFromCoordinates(float x, float y) {
		Collection<Room> rooms = m_level.getRooms().values();
		LinkedList<Room> reverseRooms = new LinkedList<Room>(rooms);
		Collections.reverse(reverseRooms);
		for (Room room : reverseRooms) {
			if (m_selectedRoomId != null) {
				if (room.getRect().contains(x, y)) {
					if (room.getId().equals(m_selectedRoomId)) {
						for (SelectionRoomListener listener : selectionRoomListeners) {
							listener.onUnselectRoom(room);
						}
						m_selectedRoomId = null;
						return true;
					}
				}
			} else {
				if (room.getRect().contains(x, y)) {
					for (SelectionRoomListener listener : selectionRoomListeners) {
						listener.onSelectRoom(room);
					}
					m_selectedRoomId = room.getId();
					return true;
				}
			}
		}
		return false;
	}

	public boolean selectRoom(Room room) {
		if (room.getId() == m_selectedRoomId) {
			for (SelectionRoomListener listener : selectionRoomListeners) {
				listener.onUnselectRoom(m_level.getRooms()
						.get(m_selectedRoomId));
			}
			m_selectedRoomId = null;
			return false;
		} else if (m_selectedRoomId != null) {
			for (SelectionRoomListener listener : selectionRoomListeners) {
				listener.onUnselectRoom(m_level.getRooms()
						.get(m_selectedRoomId));
				listener.onSelectRoom(m_level.getRooms().get(room.getId()));
			}
		} else {
			for (SelectionRoomListener listener : selectionRoomListeners) {
				listener.onSelectRoom(m_level.getRooms().get(room.getId()));
			}
		}
		m_selectedRoomId = room.getId();
		return true;
	}

	public void endProcess() {
		m_resizeType = null;
	}

	public TouchEvent getProcess(float x, float y) {
		Room room = m_level.getRooms().get(m_selectedRoomId);
		TouchEvent mode = TouchEvent.DRAG;

		int radius = 20;

		RectF leftTop = new RectF(room.getRect().left - radius,
				room.getRect().top - radius, room.getRect().left + radius,
				room.getRect().top + radius);
		RectF rightTop = new RectF(room.getRect().right - radius,
				room.getRect().top - radius, room.getRect().right + radius,
				room.getRect().top + radius);
		RectF leftBottom = new RectF(room.getRect().left - radius,
				room.getRect().bottom - radius, room.getRect().left + radius,
				room.getRect().bottom + radius);
		RectF rightBottom = new RectF(room.getRect().right - radius,
				room.getRect().bottom - radius, room.getRect().right + radius,
				room.getRect().bottom + radius);

		RectF left = new RectF(room.getRect().left - radius,
				room.getRect().top, room.getRect().left + radius,
				room.getRect().bottom);
		RectF top = new RectF(room.getRect().left, room.getRect().top - radius,
				room.getRect().right, room.getRect().top + radius);
		RectF right = new RectF(room.getRect().right - radius,
				room.getRect().top, room.getRect().right + radius,
				room.getRect().bottom);
		RectF bottom = new RectF(room.getRect().left, room.getRect().bottom
				- radius, room.getRect().right, room.getRect().bottom + radius);

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
		}
		return mode;
	}

	public void translateSelectedRoom(float dx, float dy) {
		Room room = m_level.getRooms().get(m_selectedRoomId);
		room.getRect().left += dx;
		room.getRect().top += dy;
		room.getRect().right += dx;
		room.getRect().bottom += dy;
	}

	private Point m_minNone = new Point(1, 1);
	private Point m_minX = new Point(0, 1);
	private Point m_minY = new Point(1, 0);
	private Point m_minBoth = new Point(0, 0);

	public Point resizeSelectedRoom(float dx, float dy) {
		Room room = m_level.getRooms().get(m_selectedRoomId);
		switch (m_resizeType) {
		case RESIZE_ROOM_LEFT_TOP:
			if (room.getRect().left + dx >= room.getRect().right - MIN_MARGIN
					&& room.getRect().top + dy < room.getRect().bottom
							- MIN_MARGIN) {
				room.getRect().top += dy;
				return m_minX;
			} else if (room.getRect().left + dx < room.getRect().right
					- MIN_MARGIN
					&& room.getRect().top + dy >= room.getRect().bottom
							- MIN_MARGIN) {
				room.getRect().left += dx;
				return m_minY;
			} else if (room.getRect().left + dx < room.getRect().right
					- MIN_MARGIN
					&& room.getRect().top + dy < room.getRect().bottom
							- MIN_MARGIN) {
				room.getRect().left += dx;
				room.getRect().top += dy;
				return m_minNone;
			}
			break;
		case RESIZE_ROOM_RIGHT_TOP:
			if (room.getRect().right + dx <= room.getRect().left + MIN_MARGIN
					&& room.getRect().top + dy < room.getRect().bottom
							- MIN_MARGIN) {
				room.getRect().top += dy;
				return m_minX;
			} else if (room.getRect().right + dx > room.getRect().left
					+ MIN_MARGIN
					&& room.getRect().top + dy >= room.getRect().bottom
							- MIN_MARGIN) {
				room.getRect().right += dx;
				return m_minY;
			} else if (room.getRect().right + dx > room.getRect().left
					+ MIN_MARGIN
					&& room.getRect().top + dy < room.getRect().bottom
							- MIN_MARGIN) {
				room.getRect().right += dx;
				room.getRect().top += dy;
				return m_minNone;
			}
			break;
		case RESIZE_ROOM_LEFT_BOTTOM:
			if (room.getRect().left + dx >= room.getRect().right - MIN_MARGIN
					&& room.getRect().bottom + dy > room.getRect().top
							+ MIN_MARGIN) {
				room.getRect().bottom += dy;
				return m_minX;
			} else if (room.getRect().left + dx < room.getRect().right
					- MIN_MARGIN
					&& room.getRect().bottom + dy <= room.getRect().top
							+ MIN_MARGIN) {
				room.getRect().left += dx;
				return m_minY;
			} else if (room.getRect().left + dx < room.getRect().right
					- MIN_MARGIN
					&& room.getRect().bottom + dy > room.getRect().top
							+ MIN_MARGIN) {
				room.getRect().left += dx;
				room.getRect().bottom += dy;
				return m_minNone;
			}
			break;
		case RESIZE_ROOM_RIGHT_BOTTOM:
			if (room.getRect().right + dx <= room.getRect().left + MIN_MARGIN
					&& room.getRect().bottom + dy > room.getRect().top
							+ MIN_MARGIN) {
				room.getRect().bottom += dy;
				return m_minX;
			} else if (room.getRect().right + dx > room.getRect().left
					+ MIN_MARGIN
					&& room.getRect().bottom + dy <= room.getRect().top
							+ MIN_MARGIN) {
				room.getRect().right += dx;
				return m_minY;
			} else if (room.getRect().right + dx > room.getRect().left
					+ MIN_MARGIN
					&& room.getRect().bottom + dy > room.getRect().top
							+ MIN_MARGIN) {
				room.getRect().right += dx;
				room.getRect().bottom += dy;
				return m_minNone;
			}
			break;
		case RESIZE_ROOM_LEFT:
			if (room.getRect().left + dx < room.getRect().right - MIN_MARGIN) {
				room.getRect().left += dx;
			}
			return m_minX;
		case RESIZE_ROOM_TOP:
			if (room.getRect().top + dy < room.getRect().bottom - MIN_MARGIN) {
				room.getRect().top += dy;
			}
			return m_minY;
		case RESIZE_ROOM_RIGHT:
			if (room.getRect().right + dx > room.getRect().left + MIN_MARGIN) {
				room.getRect().right += dx;
			}
			return m_minY;
		case RESIZE_ROOM_BOTTOM:
			if (room.getRect().bottom + dy > room.getRect().top + MIN_MARGIN) {
				room.getRect().bottom += dy;
			}
			return m_minX;
		}
		return m_minBoth;
	}

	public void removeRoom(Room room) {
		if (room.getId().equals(m_selectedRoomId)) {
			m_selectedRoomId = null;
		}
		m_level.removeRoom(room);
	}

	public void testAddRoom() {
		m_level.addRoom(new Room(UUID.randomUUID(), "test", new RectF(0, 0,
				150, 150)));
	}

	public void addRoom(Room room) {
		m_level.addRoom(room);
	}

}
