package fr.upem.android.deadhal.graphics.drawable;

import java.util.Collection;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import fr.upem.android.deadhal.components.Corridor;
import fr.upem.android.deadhal.components.Level;
import fr.upem.android.deadhal.components.Room;
import fr.upem.android.deadhal.components.handlers.NavigationLevelHandler;
import fr.upem.android.deadhal.graphics.Paints;

public class NavigationLevelDrawable extends AbstractLevelDrawable {

	private NavigationLevelHandler m_levelHandler;

	public NavigationLevelDrawable(NavigationLevelHandler levelHandler) {
		super(levelHandler);
		m_levelHandler = levelHandler;
	}

	@Override
	public void draw(Canvas canvas) {
		Level level = m_levelHandler.getLevel();
		Collection<Room> rooms = level.getRooms().values();
		for (Room room : rooms) {
			drawRoom(canvas, room);
		}
		Room roomStart = m_levelHandler.getRoomStart();
		Room roomEnd = m_levelHandler.getRoomEnd();
		if (roomStart != null) {
			drawSelectedRoom(canvas, roomStart, Paints.ROOM_START_BACKGROUND);
		}
		if (roomEnd != null) {
			drawSelectedRoom(canvas, roomEnd, Paints.ROOM_END_BACKGROUND);
		}
		for (Corridor corridor : level.getCorridors().values()) {
			if (m_levelHandler.getShortestPath().contains(corridor.getId())) {
				drawCorridor(canvas, corridor, Paints.CORRIDOR_HIGHLIGHT);
			} else {
				drawCorridor(canvas, corridor, Paints.CORRIDOR);
			}
		}
		m_levelHandler.getPawn().draw(canvas);
	}

	private void drawSelectedRoom(Canvas canvas, Room room, Paint paint) {
		float borderSize = (float) 1.5;

		RectF rect = room.getRect();
		RectF rectB = new RectF(rect.left + borderSize, rect.top + borderSize,
				rect.right - borderSize, rect.bottom - borderSize);
		canvas.drawRect(rect, paint);
		canvas.drawRect(rectB, Paints.ROOM_BORDER);
		drawTitle(canvas, room);
	}
}
