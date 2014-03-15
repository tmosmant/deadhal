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

public abstract class AbstractLevelDrawable extends Drawable {

	protected AbstractLevelHandler m_levelHandler;
	private int m_alpha = 255;

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

	protected void drawRoom(Canvas canvas, Room room) {
		float borderSize = (float) 1.5;
		RectF rect = room.getRect();
		RectF rectB = new RectF(rect.left + borderSize, rect.top + borderSize,
				rect.right - borderSize, rect.bottom - borderSize);
		canvas.drawRect(rect, Paints.ROOM_BACKGROUND);
		canvas.drawRect(rectB, Paints.ROOM_BORDER);
		drawTitle(canvas, room);
	}

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

	protected void drawCorridor(Canvas canvas, Corridor corridor) {
		drawCorridor(canvas, corridor, Paints.CORRIDOR);
	}

	private double length(PointF a, PointF b) {
		return Math.sqrt((b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y));
	}

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
