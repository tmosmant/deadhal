package fr.upem.android.deadhal.graphics.drawable;

import java.util.Collection;

import android.graphics.Canvas;
import fr.upem.android.deadhal.components.Corridor;
import fr.upem.android.deadhal.components.Level;
import fr.upem.android.deadhal.components.Room;
import fr.upem.android.deadhal.components.handlers.NavigationLevelHandler;
import fr.upem.android.deadhal.graphics.Paints;

/**
 * This class draw the navigation part.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public class NavigationLevelDrawable extends AbstractLevelDrawable {

	private NavigationLevelHandler m_levelHandler;

	/**
	 * Constructs the drawable with a level handler.
	 * 
	 * @param levelHandler
	 *            the level handler
	 */
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
			drawRoom(canvas, roomStart, Paints.ROOM_START_BACKGROUND,
					Paints.ROOM_BORDER);
		}
		if (roomEnd != null) {
			drawRoom(canvas, roomEnd, Paints.ROOM_END_BACKGROUND,
					Paints.ROOM_BORDER);
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
}
