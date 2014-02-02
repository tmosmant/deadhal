package fr.upem.deadhal.graphics.drawable;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.RectF;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.listeners.SelectionRoomListener;
import fr.upem.deadhal.graphics.Paints;

public class NavigationLevelDrawable extends AbstractDrawable {

	private Room m_startRoom = null;
	private Room m_endRoom = null;

	public NavigationLevelDrawable(Level level) {
		super(level);
	}

	@Override
	public void draw(Canvas canvas) {
		for (Room room : m_level.getRooms().values()) {
			drawRoom(canvas, room);
		}
		if (m_startRoom != null) {
			drawRoomStart(canvas, m_startRoom);
		}
		if (m_endRoom != null) {
			drawRoomEnd(canvas, m_endRoom);
		}
	}

	private void drawRoomStart(Canvas canvas, Room room) {
		float borderSize = (float) 1.5;

		RectF rect = room.getRect();
		RectF rectB = new RectF(rect.left + borderSize, rect.top + borderSize,
				rect.right - borderSize, rect.bottom - borderSize);
		canvas.drawRect(rect, Paints.ROOM_SELECTED_BACKGROUND_ERROR);
		canvas.drawRect(rectB, Paints.ROOM_SELECTED_BACKGROUND);
		drawTitle(canvas, room);
	}

	private void drawRoomEnd(Canvas canvas, Room room) {
		float borderSize = (float) 1.5;

		RectF rect = room.getRect();
		RectF rectB = new RectF(rect.left + borderSize, rect.top + borderSize,
				rect.right - borderSize, rect.bottom - borderSize);
		canvas.drawRect(rect, Paints.ROOM_SELECTED_BACKGROUND_ERROR);
		canvas.drawRect(rectB, Paints.ROOM_SELECTED_BACKGROUND);
		drawTitle(canvas, room);
	}

	public boolean selectRoomFromCoordinates(float x, float y) {
		Collection<Room> rooms = m_level.getRooms().values();
		LinkedList<Room> reverseRooms = new LinkedList<Room>(rooms);
		Collections.reverse(reverseRooms);

		for (Room room : reverseRooms) {
			if (room.getRect().contains(x, y)) {
				for (SelectionRoomListener listener : selectionRoomListeners) {
					listener.onSelectRoom(room);
				}
				return true;
			}
		}
		return false;
	}

	public void selectRoomStart(Room room) {
		m_startRoom = room;
	}

	public void selectRoomEnd(Room room) {
		m_endRoom = room;
	}

}
