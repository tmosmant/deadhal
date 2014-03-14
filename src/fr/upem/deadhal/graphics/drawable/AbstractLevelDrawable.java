package fr.upem.deadhal.graphics.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import fr.upem.deadhal.components.Corridor;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.handlers.AbstractLevelHandler;
import fr.upem.deadhal.graphics.Paints;

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

		canvas.save();
		canvas.concat(room.getMatrix());

		canvas.drawRect(rect, Paints.ROOM_BACKGROUND);
		canvas.drawRect(rectB, Paints.ROOM_BORDER);
		drawTitle(canvas, room);
		canvas.restore();
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

		PointF centerStart = new PointF(rectStart.centerX(),
				rectStart.centerY());
		PointF centerEnd = new PointF(rectEnd.centerX(), rectEnd.centerY());
		PointF[] pStart = computeIntersections(centerStart, centerEnd, rectStart);
		
		PointF pStartMin = null;
		for (PointF p : pStart) {
			if (pStartMin == null && p != null) {
				pStartMin = p;
			}
			if (pStartMin != null && p != null && length(pStartMin, centerEnd) > length(p, centerEnd) ) {
				pStartMin = p;
			}
		}
		
		PointF[] pEnd = computeIntersections(centerStart, centerEnd, rectEnd);

		PointF pEndMin = null;
		for (PointF p : pEnd) {
			if (pEndMin == null && p != null) {
				pEndMin = p;
			}
			if (pEndMin != null && p != null && length(pEndMin, centerEnd) > length(p, centerEnd) ) {
				pEndMin = p;
			}
		}
		
		float[] pts = { pStartMin.x, pStartMin.y };
		start.getMatrix().mapPoints(pts);
		PointF pointF = new PointF(pts[0], pts[1]);

		corridor.setWeight(length(pStartMin, pEndMin));
		if (corridor.isDirected()) {
			drawArrow(canvas, pointF, pEndMin, paint);
		} else {
			drawArrow(canvas, pointF, pEndMin, paint);
			drawArrow(canvas, pEndMin, pointF, paint);
		}
	}

	protected void drawCorridor(Canvas canvas, Corridor corridor) {
		drawCorridor(canvas, corridor, Paints.CORRIDOR);
	}

	private double length(PointF a, PointF b) {
		return Math.sqrt((b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y));
	}

	private PointF[] computeIntersections(PointF start, PointF end, RectF rect) {
		PointF points[] = new PointF[4];
		points[0] = intersection(start.x, start.y, end.x, end.y, rect.left,
				rect.top, rect.right, rect.top);
		points[1] =  intersection(start.x, start.y, end.x, end.y, rect.left,
				rect.top, rect.left, rect.bottom);
		points[2] = intersection(start.x, start.y, end.x, end.y, rect.left,
				rect.bottom, rect.right, rect.bottom);
		points[3] = intersection(start.x, start.y, end.x, end.y, rect.right,
				rect.top, rect.right, rect.bottom);
		return points;
//		point = intersection(start.x, start.y, end.x, end.y, rect.left,
//				rect.top, rect.right, rect.top);
//		if (point != null) {
//			return point;
//		}
//		point = intersection(start.x, start.y, end.x, end.y, rect.left,
//				rect.top, rect.left, rect.bottom);
//		if (point != null) {
//			return point;
//		}
//		point = intersection(start.x, start.y, end.x, end.y, rect.left,
//				rect.bottom, rect.right, rect.bottom);
//		if (point != null) {
//			return point;
//		}
//		point = intersection(start.x, start.y, end.x, end.y, rect.right,
//				rect.top, rect.right, rect.bottom);
//		if (point != null) {
//			return point;
//		}
//		return start;
	}

	PointF intersection(float p0_x, float p0_y, float p1_x, float p1_y,
			float p2_x, float p2_y, float p3_x, float p3_y) {
		float s02_x, s02_y, s10_x, s10_y, s32_x, s32_y, s_numer, t_numer, denom, t;
		s10_x = p1_x - p0_x;
		s10_y = p1_y - p0_y;
		s32_x = p3_x - p2_x;
		s32_y = p3_y - p2_y;

		denom = s10_x * s32_y - s32_x * s10_y;
		if (denom == 0) {
			return null;
		}
		boolean denomPositive = denom > 0;

		s02_x = p0_x - p2_x;
		s02_y = p0_y - p2_y;
		s_numer = s10_x * s02_y - s10_y * s02_x;
		if ((s_numer <= 0) == denomPositive) {
			return null;
		}

		t_numer = s32_x * s02_y - s32_y * s02_x;
		if ((t_numer < 0) == denomPositive) {
			return null;
		}

		if (((s_numer > denom) == denomPositive)
				|| ((t_numer > denom) == denomPositive)) {
			return null;
		}
		t = t_numer / denom;
		return new PointF(p0_x + (t * s10_x), p0_y + (t * s10_y));
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
