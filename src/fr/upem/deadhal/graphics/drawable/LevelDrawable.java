package fr.upem.deadhal.graphics.drawable;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.listeners.SelectionRoomListener;
import fr.upem.deadhal.graphics.Paints;
import fr.upem.deadhal.view.TouchEvent;

public class LevelDrawable extends Drawable {

	private Level m_level;
	private int m_alpha = 255;
	private UUID m_selectedRoomId = null;
	private List<SelectionRoomListener> selectionRoomListeners = new LinkedList<SelectionRoomListener>();

	private ResizeType m_resizeType = null;

	public LevelDrawable(Level level) {
		m_level = level;
	}

	public void addSelectionRoomListener(SelectionRoomListener listener) {
		selectionRoomListeners.add(listener);
	}

	public void removeSelectionRoomListener(SelectionRoomListener listener) {
		selectionRoomListeners.remove(listener);
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

	@Override
	public void setAlpha(int alpha) {
		m_alpha = alpha;
	}

	@Override
	public void setColorFilter(ColorFilter cf) {

	}

	@Override
	public int getOpacity() {
		return m_alpha;
	}

	private void drawRoom(Canvas canvas, Room room) {
		float borderSize = (float) 1.5;
		RectF rect = room.getRect();
		RectF rectB = new RectF(rect.left + borderSize, rect.top + borderSize,
				rect.right - borderSize, rect.bottom - borderSize);
		canvas.drawRect(rect, Paints.ROOM_BACKGROUND);
		canvas.drawRect(rectB, Paints.ROOM_BORDER);
	}

	private void drawRoomSelected(Canvas canvas, Room room) {
		float borderSize = (float) 1.5;

		RectF rect = room.getRect();
		RectF rectB = new RectF(rect.left + borderSize, rect.top + borderSize,
				rect.right - borderSize, rect.bottom - borderSize);
		drawPoints(rect, canvas);
		canvas.drawRect(rect, Paints.ROOM_SELECTED_BACKGROUND);
		canvas.drawRect(rectB, Paints.ROOM_SELECTED_BORDER);
	}

	private void drawRoomSelectedError(Canvas canvas, Room room) {
		float borderSize = (float) 1.5;

		RectF rect = room.getRect();
		RectF rectB = new RectF(rect.left + borderSize, rect.top + borderSize,
				rect.right - borderSize, rect.bottom - borderSize);
		drawPoints(rect, canvas);
		canvas.drawRect(rect, Paints.ROOM_SELECTED_BACKGROUND_ERROR);
		canvas.drawRect(rectB, Paints.ROOM_SELECTED_BORDER);
	}

	private void drawPoints(RectF rect, Canvas canvas) {
		PointF p = new PointF();
		float radius = 12;

		p.set(rect.left, rect.top);
		canvas.drawCircle(p.x, p.y, radius, Paints.ROOM_SELECTED_POINT);
		p.set(rect.right, rect.top);
		canvas.drawCircle(p.x, p.y, radius, Paints.ROOM_SELECTED_POINT);
		p.set(rect.left, rect.bottom);
		canvas.drawCircle(p.x, p.y, radius, Paints.ROOM_SELECTED_POINT);
		p.set(rect.right, rect.bottom);
		canvas.drawCircle(p.x, p.y, radius, Paints.ROOM_SELECTED_POINT);
	}

	public boolean isRoomSelected() {
		return m_selectedRoomId != null;
	}

	public boolean selectRoom(float x, float y) {
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

	public TouchEvent getProcess(float x, float y) {
		Room room = m_level.getRooms().get(m_selectedRoomId);
		TouchEvent mode = TouchEvent.DRAG;

		int radius = 20;

		RectF m_leftTop = new RectF(room.getRect().left - radius,
				room.getRect().top - radius, room.getRect().left + radius,
				room.getRect().top + radius);
		RectF m_rightTop = new RectF(room.getRect().right - radius,
				room.getRect().top - radius, room.getRect().right + radius,
				room.getRect().top + radius);
		RectF m_leftBottom = new RectF(room.getRect().left - radius,
				room.getRect().bottom - radius, room.getRect().left + radius,
				room.getRect().bottom + radius);
		RectF m_rightBottom = new RectF(room.getRect().right - radius,
				room.getRect().bottom - radius, room.getRect().right + radius,
				room.getRect().bottom + radius);

		if (m_leftTop.contains(x, y)) {
			mode = TouchEvent.RESIZE_ROOM;
			m_resizeType = ResizeType.RESIZE_ROOM_LEFT_TOP;
		} else if (m_rightTop.contains(x, y)) {
			mode = TouchEvent.RESIZE_ROOM;
			m_resizeType = ResizeType.RESIZE_ROOM_RIGHT_TOP;
		} else if (m_leftBottom.contains(x, y)) {
			mode = TouchEvent.RESIZE_ROOM;
			m_resizeType = ResizeType.RESIZE_ROOM_LEFT_BOTTOM;
		} else if (m_rightBottom.contains(x, y)) {
			mode = TouchEvent.RESIZE_ROOM;
			m_resizeType = ResizeType.RESIZE_ROOM_RIGHT_BOTTOM;
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

	public void resizeSelectedRoom(float dx, float dy) {
		Room room = m_level.getRooms().get(m_selectedRoomId);
		if (m_resizeType == ResizeType.RESIZE_ROOM_LEFT_TOP) {
			room.getRect().left += dx;
			room.getRect().top += dy;
		}

		else if (m_resizeType == ResizeType.RESIZE_ROOM_RIGHT_TOP) {
			room.getRect().right += dx;
			room.getRect().top += dy;
		}

		else if (m_resizeType == ResizeType.RESIZE_ROOM_LEFT_BOTTOM) {
			room.getRect().left += dx;
			room.getRect().bottom += dy;

		} else if (m_resizeType == ResizeType.RESIZE_ROOM_RIGHT_BOTTOM) {
			room.getRect().right += dx;
			room.getRect().bottom += dy;
		}
	}
}