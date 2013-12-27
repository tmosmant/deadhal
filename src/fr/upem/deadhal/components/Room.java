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
	// private final Paint borders = new Paint();

	// private final Paint selectedBorders = new Paint();

	private final Paint selectedBackground = new Paint();

	private final Paint points = new Paint();

	public Room(String title, float left, float top, float right, float bottom) {
		this.title = title;
		this.rect = new RectF(left, top, right, bottom);
		background.setColor(Colors.BLUE);
		background.setStyle(Paint.Style.FILL);
		background.setAntiAlias(true);

		// borders.setColor(Colors.BLACK);
		// borders.setStyle(Paint.Style.STROKE);
		// borders.setStrokeWidth(10);
		// borders.setAntiAlias(true);

		// selectedBorders.setColor(Colors.GREY);
		// selectedBorders.setStyle(Paint.Style.STROKE);
		// selectedBorders.setStrokeWidth(5);
		// selectedBorders.setAntiAlias(true);
		// selectedBorders.setAlpha(125);

		selectedBackground.setColor(Colors.BLUE);
		selectedBackground.setStyle(Paint.Style.FILL);
		selectedBackground.setAntiAlias(true);
		selectedBackground.setAlpha(200);
		
		points.setColor(Colors.GREY);
		points.setStyle(Paint.Style.FILL);
		points.setAntiAlias(true);
		points.setAlpha(125);
	}

	public String getTitle() {
		return title;
	}

	public RectF getRect() {
		return rect;
	}

	public void draw(Canvas canvas) {
		// canvas.drawRect(rect, borders);
		canvas.drawRect(rect, background);
	}

	public void drawSelected(Canvas canvas) {
		drawPoints(canvas);

		// canvas.drawRect(rect, selectedBorders);
		canvas.drawRect(rect, selectedBackground);

	}

	private void drawPoints(Canvas canvas) {
		PointF p = new PointF();
		float radius = 12;

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
