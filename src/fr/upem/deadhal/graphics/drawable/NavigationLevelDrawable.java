package fr.upem.deadhal.graphics.drawable;

import java.util.Collection;

import android.graphics.Canvas;
import android.graphics.RectF;
import fr.upem.deadhal.components.Corridor;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.handlers.NavigationLevelHandler;
import fr.upem.deadhal.graphics.Paints;

public class NavigationLevelDrawable extends AbstractLevelDrawable {

	private NavigationLevelHandler m_navigationLevelHandler;

	public NavigationLevelDrawable(NavigationLevelHandler levelHandler) {
		super(levelHandler);
		m_navigationLevelHandler = levelHandler;
	}

	@Override
	public void draw(Canvas canvas) {
		Level level = m_navigationLevelHandler.getLevel();
		Collection<Room> rooms = level.getRooms().values();
		for (Room room : rooms) {
			drawRoom(canvas, room);
		}
		Room roomStart = m_navigationLevelHandler.getRoomStart();
		Room roomEnd = m_navigationLevelHandler.getRoomEnd();
		if (roomStart != null) {
			drawRoomStart(canvas, roomStart);
		}
		if (roomEnd != null) {
			drawRoomEnd(canvas, roomEnd);
		}
		for (Corridor corridor : level.getCorridors().values()) {
			if (m_navigationLevelHandler.getShortestPath().contains(
					corridor.getId())) {
				drawCorridor(canvas, corridor, Paints.CORRIDOR_HIGHLIGHT);
			} else {
				drawCorridor(canvas, corridor, Paints.CORRIDOR);
			}
		}
	}

	private void drawRoomStart(Canvas canvas, Room room) {
		float borderSize = (float) 1.5;

		RectF rect = room.getRect();
		RectF rectB = new RectF(rect.left + borderSize, rect.top + borderSize,
				rect.right - borderSize, rect.bottom - borderSize);
		canvas.drawRect(rect, Paints.CORRIDOR_HIGHLIGHT);
		canvas.drawRect(rectB, Paints.ROOM_SELECTED_BORDER);
		drawTitle(canvas, room);
	}

	private void drawRoomEnd(Canvas canvas, Room room) {
		float borderSize = (float) 1.5;

		RectF rect = room.getRect();
		RectF rectB = new RectF(rect.left + borderSize, rect.top + borderSize,
				rect.right - borderSize, rect.bottom - borderSize);
		canvas.drawRect(rect, Paints.ROOM_SELECTED_BACKGROUND_ERROR);
		canvas.drawRect(rectB, Paints.ROOM_SELECTED_BORDER);
		drawTitle(canvas, room);
	}

}
