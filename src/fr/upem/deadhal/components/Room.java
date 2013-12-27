package fr.upem.deadhal.components;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import fr.upem.deadhal.utils.Colors;

public class Room {

	private String title;
	private RectF rect;

	private final Paint background = new Paint();
	private final Paint borders = new Paint();

	private final Paint selectedBorders = new Paint();
	
	private final Paint points = new Paint();

	public Room(String title, float left, float top, float right, float bottom) {
		this.title = title;
		this.rect = new RectF(left, top, right, bottom);
		background.setColor(Colors.BLUE);
		background.setStyle(Paint.Style.FILL);
		background.setAntiAlias(true);

		borders.setColor(Colors.BLACK);
		borders.setStyle(Paint.Style.STROKE);
		borders.setStrokeWidth(5);
		borders.setAntiAlias(true);

		selectedBorders.setColor(Colors.GREY);
		selectedBorders.setStyle(Paint.Style.STROKE);
		selectedBorders.setStrokeWidth(5);
		selectedBorders.setAntiAlias(true);

		points.setColor(Colors.GREY);
		points.setStyle(Paint.Style.FILL);
		points.setAntiAlias(true);
	}

	public String getTitle() {
		return title;
	}

	public RectF getRect() {
		return rect;
	}

	public void draw(Canvas canvas) {
		canvas.drawRect(rect, background);
		canvas.drawRect(rect, borders);
	}

	public void drawSelected(Canvas canvas) {
		canvas.drawRect(rect, background);
		canvas.drawRect(rect, selectedBorders);
		
		drawPoints(canvas);
	}

	private void drawPoints(Canvas canvas) {
		PointF p = new PointF();
		float radius = 10;
		
		p.set(rect.left, rect.top);
		canvas.drawCircle(p.x, p.y, radius, points);
		p.set(rect.right, rect.top);
		canvas.drawCircle(p.x, p.y, radius, points);
		p.set(rect.left, rect.bottom);
		canvas.drawCircle(p.x, p.y, radius, points);
		p.set(rect.right, rect.bottom);
		canvas.drawCircle(p.x, p.y, radius, points);
	}

}
