package fr.upem.android.deadhal.graphics.drawable;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import fr.upem.android.deadhal.components.Corridor;
import fr.upem.android.deadhal.components.Level;
import fr.upem.android.deadhal.components.Room;
import fr.upem.android.deadhal.components.handlers.EditionLevelHandler;
import fr.upem.android.deadhal.components.handlers.ResizeType;
import fr.upem.android.deadhal.graphics.Paints;

/**
 * This class draws the edition level part.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public class EditionLevelDrawable extends AbstractLevelDrawable {

	private EditionLevelHandler m_levelHandler;

	/**
	 * Constructs the drawable with a level handler.
	 * 
	 * @param levelHandler
	 *            the level handler
	 */
	public EditionLevelDrawable(EditionLevelHandler levelHandler) {
		super(levelHandler);
		m_levelHandler = levelHandler;
	}

	@Override
	public void draw(Canvas canvas) {
		boolean superposing = false;

		Level level = m_levelHandler.getLevel();
		Room selectedRoom = m_levelHandler.getSelectedRoom();
		for (Room room : level.getRooms().values()) {
			if (!room.equals(selectedRoom)) {
				drawRoom(canvas, room);
			}
		}
		if (selectedRoom != null) {
			for (Room room : level.getRooms().values()) {
				if (!room.equals(selectedRoom)) {
					if (RectF
							.intersects(room.getRect(), selectedRoom.getRect())) {
						superposing = true;
					}
				}
			}

			drawRoomSelected(canvas, selectedRoom);
		}
		if (superposing) {
			drawRoomSelectedError(canvas, selectedRoom);
		}
		for (Corridor corridor : level.getCorridors().values()) {
			drawCorridor(canvas, corridor);
		}
		Corridor selectedCorridor = m_levelHandler.getSelectedCorridor();
		if (selectedCorridor != null) {
			drawCorridor(canvas, selectedCorridor, Paints.CORRIDOR_HIGHLIGHT);
		}
	}

	/**
	 * Draw a room on a canvas.
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
		drawPoints(rect, canvas);
		canvas.drawRect(rect, Paints.ROOM_SELECTED_BACKGROUND);
		canvas.drawRect(rectB, Paints.ROOM_SELECTED_BORDER);
		drawTitle(canvas, room);
	}

	/**
	 * Draw a room on a canvas with error.
	 * 
	 * @param canvas
	 *            the canvas to draw on
	 * @param room
	 *            the room to draw
	 */
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

	/**
	 * Draw the resize points.
	 * 
	 * @param rect
	 *            the rect to calculate the points
	 * @param canvas
	 *            the canvas to draw on
	 */
	private void drawPoints(RectF rect, Canvas canvas) {
		PointF p = new PointF();
		float radius = 12;

		ResizeType resizeType = m_levelHandler.getResizeType();

		if (resizeType == null || resizeType == ResizeType.RESIZE_ROOM_LEFT_TOP
				|| resizeType == ResizeType.RESIZE_ROOM_LEFT
				|| resizeType == ResizeType.RESIZE_ROOM_LEFT_BOTTOM) {
			p.set(rect.left, rect.centerY());
			canvas.drawCircle(p.x, p.y, radius, Paints.ROOM_SELECTED_POINT);
		}

		if (resizeType == null || resizeType == ResizeType.RESIZE_ROOM_LEFT_TOP
				|| resizeType == ResizeType.RESIZE_ROOM_TOP
				|| resizeType == ResizeType.RESIZE_ROOM_RIGHT_TOP) {
			p.set(rect.centerX(), rect.top);
			canvas.drawCircle(p.x, p.y, radius, Paints.ROOM_SELECTED_POINT);
		}
		if (resizeType == null
				|| resizeType == ResizeType.RESIZE_ROOM_RIGHT_TOP
				|| resizeType == ResizeType.RESIZE_ROOM_RIGHT
				|| resizeType == ResizeType.RESIZE_ROOM_RIGHT_BOTTOM) {
			p.set(rect.right, rect.centerY());
			canvas.drawCircle(p.x, p.y, radius, Paints.ROOM_SELECTED_POINT);
		}
		if (resizeType == null
				|| resizeType == ResizeType.RESIZE_ROOM_RIGHT_BOTTOM
				|| resizeType == ResizeType.RESIZE_ROOM_BOTTOM
				|| resizeType == ResizeType.RESIZE_ROOM_LEFT_BOTTOM) {
			p.set(rect.centerX(), rect.bottom);
			canvas.drawCircle(p.x, p.y, radius, Paints.ROOM_SELECTED_POINT);
		}
	}

}
