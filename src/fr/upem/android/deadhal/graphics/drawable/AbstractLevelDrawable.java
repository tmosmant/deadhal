package fr.upem.android.deadhal.graphics.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import fr.upem.android.deadhal.components.Corridor;
import fr.upem.android.deadhal.components.Room;
import fr.upem.android.deadhal.components.handlers.AbstractLevelHandler;
import fr.upem.android.deadhal.graphics.Paints;

/**
 * This class provides methods to draw levels and can be extended.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public abstract class AbstractLevelDrawable extends Drawable {

	protected AbstractLevelHandler m_levelHandler;
	private int m_alpha = 255;

	/**
	 * Constructs the drawable with a level handler.
	 * 
	 * @param levelHandler
	 *            the level handler
	 */
	public AbstractLevelDrawable(AbstractLevelHandler levelHandler) {
		m_levelHandler = levelHandler;
	}

	@Override
	public abstract void draw(Canvas canvas);

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

	/**
	 * Draw the title of a room. Trunc it if necessary.
	 * 
	 * @param canvas
	 *            the canvas to draw on
	 * @param room
	 *            the room to draw the title
	 */
	protected void drawTitle(Canvas canvas, Room room) {
		String title = room.getName();
		RectF rect = room.getRect();
		if (rect.height() >= 20) {
			Paint paint = new Paint(Paints.ROOM_TITLE);
			float width = rect.width();
			int numOfChars = paint.breakText(title + "...", true, width, null);
			if (numOfChars <= 3) {
				title = "";
			} else if (numOfChars < title.length() + 3) {
				title = title.subSequence(0, numOfChars - 3).toString();
				title += "...";
			}
			canvas.drawText(title, rect.centerX(), rect.centerY() + 7, paint);
		}
	}

	/**
	 * Draw a room on a canvas with paints.
	 * 
	 * @param canvas
	 *            the canvas to draw on
	 * @param room
	 *            the room to draw
	 * @param background
	 *            the background paint
	 * @param border
	 *            the border paint
	 */
	protected void drawRoom(Canvas canvas, Room room, Paint background,
			Paint border) {
		float borderSize = (float) 1.5;
		RectF rect = room.getRect();
		RectF rectB = new RectF(rect.left + borderSize, rect.top + borderSize,
				rect.right - borderSize, rect.bottom - borderSize);
		canvas.drawRect(rect, background);
		canvas.drawRect(rectB, border);
		drawTitle(canvas, room);
	}

	/**
	 * Draw a room on a canvas with default paints.
	 * 
	 * @param canvas
	 *            the canvas to draw on
	 * @param room
	 *            the room to draw
	 */
	protected void drawRoom(Canvas canvas, Room room) {
		float borderSize = (float) 1.5;
		RectF rect = room.getRect();
		RectF rectB = new RectF(rect.left + borderSize, rect.top + borderSize,
				rect.right - borderSize, rect.bottom - borderSize);
		canvas.drawRect(rect, Paints.ROOM_BACKGROUND);
		canvas.drawRect(rectB, Paints.ROOM_BORDER);
		drawTitle(canvas, room);
	}

	/**
	 * Draw a corridor.
	 * 
	 * @param canvas
	 *            the canvas to draw on
	 * @param corridor
	 *            the corridor to draw
	 * @param paint
	 *            the paint to use
	 */
	protected void drawCorridor(Canvas canvas, Corridor corridor, Paint paint) {
		Room start = m_levelHandler.getLevel().getRooms()
				.get(corridor.getSrc());
		Room end = m_levelHandler.getLevel().getRooms().get(corridor.getDst());
		if (RectF.intersects(start.getRect(), end.getRect())) {
			corridor.setWeight(0.0);
			return;
		}
		RectF rectStart = start.getRect();
		RectF rectEnd = end.getRect();

		PointF srcPoint = new PointF(corridor.getSrcPoint().x + rectStart.left,
				corridor.getSrcPoint().y + rectStart.top);
		PointF dstPoint = new PointF(corridor.getDstPoint().x + rectEnd.left,
				corridor.getDstPoint().y + rectEnd.top);

		PointF pStart = m_levelHandler.computeIntersection(srcPoint, dstPoint,
				rectStart);
		PointF pEnd = m_levelHandler.computeIntersection(srcPoint, dstPoint,
				rectEnd);
		corridor.setWeight(length(pStart, pEnd));
		if (corridor.isDirected()) {
			drawArrow(canvas, pStart, pEnd, paint);
		} else {
			drawArrow(canvas, pStart, pEnd, paint);
			drawArrow(canvas, pEnd, pStart, paint);
		}
	}

	/**
	 * Draw a corridor using the default painting.
	 * 
	 * @param canvas
	 *            the canvas to draw on
	 * @param corridor
	 *            the corridor to draw
	 */
	protected void drawCorridor(Canvas canvas, Corridor corridor) {
		drawCorridor(canvas, corridor, Paints.CORRIDOR);
	}

	/**
	 * Calculate the length between two point.
	 * 
	 * @param a
	 *            the first point
	 * @param b
	 *            the second point
	 * @return the calculated length
	 */
	private double length(PointF a, PointF b) {
		return Math.sqrt((b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y));
	}

	/**
	 * Draw an arrow.
	 * 
	 * @param canvas
	 *            the canvas to draw on
	 * @param startPoint
	 *            the start point of the arrow
	 * @param endPoint
	 *            the end point of the arrow
	 * @param paint
	 *            the paint to use
	 */
	private void drawArrow(Canvas canvas, PointF startPoint, PointF endPoint,
			Paint paint) {
		Path path = new Path();
		path.moveTo(startPoint.x, startPoint.y);
		path.lineTo(endPoint.x, endPoint.y);

		float deltaX = endPoint.x - startPoint.x;
		float deltaY = endPoint.y - startPoint.y;

		int ARROWHEAD_LENGTH = 10;
		float sideZ = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		float frac = ARROWHEAD_LENGTH < sideZ ? ARROWHEAD_LENGTH / sideZ : 1.0f;
		float point_x_1 = startPoint.x
				+ (float) ((1 - frac) * deltaX + frac * deltaY);
		float point_y_1 = startPoint.y
				+ (float) ((1 - frac) * deltaY - frac * deltaX);
		float point_x_2 = endPoint.x;
		float point_y_2 = endPoint.y;
		float point_x_3 = startPoint.x
				+ (float) ((1 - frac) * deltaX - frac * deltaY);
		float point_y_3 = startPoint.y
				+ (float) ((1 - frac) * deltaY + frac * deltaX);

		path.moveTo(point_x_1, point_y_1);
		path.lineTo(point_x_2, point_y_2);
		path.lineTo(point_x_3, point_y_3);
		canvas.drawPath(path, paint);
	}

}
