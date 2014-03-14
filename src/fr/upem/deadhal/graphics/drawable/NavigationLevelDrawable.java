package fr.upem.deadhal.graphics.drawable;

import java.util.Collection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.Corridor;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.handlers.NavigationLevelHandler;
import fr.upem.deadhal.graphics.Paints;

public class NavigationLevelDrawable extends AbstractLevelDrawable {

	private NavigationLevelHandler m_levelHandler;
	private Bitmap m_localisationBitmap;

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
		drawLocalisation(canvas);

		for (Corridor corridor : level.getCorridors().values()) {
			if (m_levelHandler.getShortestPath().contains(corridor.getId())) {
				drawCorridor(canvas, corridor, Paints.CORRIDOR_HIGHLIGHT);
			} else {
				drawCorridor(canvas, corridor, Paints.CORRIDOR);
			}
		}
	}

	public void drawLocalisation(Canvas canvas) {
		Room selectedRoom = m_levelHandler.getSelectedRoom();
		if (selectedRoom != null) {
			canvas.drawBitmap(localisationBitmap(),
					m_levelHandler.getLocalisation().x
							- localisationBitmap().getWidth() / 2,
					m_levelHandler.getLocalisation().y
							- localisationBitmap().getHeight() / 2,
					Paints.LOCALISATION);
		}
	}

	private Bitmap localisationBitmap() {
		// no need to synchronize, this isn't static
		if (m_localisationBitmap == null) {
			m_localisationBitmap = BitmapFactory.decodeResource(m_levelHandler
					.getView().getResources(),
					R.drawable.ic_action_location_found);
		}
		return m_localisationBitmap;
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
