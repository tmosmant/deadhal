package fr.upem.android.deadhal.graphics.drawable;

import android.graphics.Canvas;
import android.graphics.RectF;
import fr.upem.android.deadhal.components.Corridor;
import fr.upem.android.deadhal.components.Level;
import fr.upem.android.deadhal.components.Room;
import fr.upem.android.deadhal.components.handlers.EditionCorridorLevelHandler;
import fr.upem.android.deadhal.graphics.Paints;

/**
 * This class draws the edition corridor part.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public class EditionCorridorLevelDrawable extends AbstractLevelDrawable {

	private EditionCorridorLevelHandler m_levelHandler;

	/**
	 * Constructs the drawable with a level handler.
	 * 
	 * @param levelHandler
	 *            the level handler
	 */
	public EditionCorridorLevelDrawable(EditionCorridorLevelHandler levelHandler) {
		super(levelHandler);
		m_levelHandler = levelHandler;
	}

	@Override
	public void draw(Canvas canvas) {
		Level level = m_levelHandler.getLevel();
		for (Room room : level.getRooms().values()) {
			if (room.equals(m_levelHandler.getStart())
					|| room.equals(m_levelHandler.getEnd())) {
				drawRoomSelected(canvas, room);
			} else {
				drawRoom(canvas, room);
			}
		}
		for (Corridor corridor : level.getCorridors().values()) {
			drawCorridor(canvas, corridor);
		}
	}

	/**
	 * Draws a room on a canvas.
	 * 
	 * @param canvas
	 *            the canvas to draw on
	 * @param room
	 *            the room to draw
	 */
	private void drawRoomSelected(Canvas canvas, Room room) {
		float borderSize = (float) 1.5;

		RectF rect = room.getRect();
		RectF rectB = new RectF(rect.left + borderSize, rect.top + borderSize,
				rect.right - borderSize, rect.bottom - borderSize);
		canvas.drawRect(rect, Paints.ROOM_SELECTED_BACKGROUND);
		canvas.drawRect(rectB, Paints.ROOM_SELECTED_BORDER);
		drawTitle(canvas, room);
	}

}
