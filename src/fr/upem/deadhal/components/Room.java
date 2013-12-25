package fr.upem.deadhal.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Room {

	private String title;
	private RectF rect;

	private final Paint background = new Paint();
	private final Paint borders = new Paint();

	private final Paint selectedBorders = new Paint();

	public Room(String title, float left, float top, float right, float bottom) {
		this.title = title;
		this.rect = new RectF(left, top, right, bottom);
		background.setColor(Color.CYAN);
		background.setStyle(Paint.Style.FILL);
		background.setAntiAlias(true);

		borders.setColor(Color.BLACK);
		borders.setStyle(Paint.Style.STROKE);
		borders.setStrokeWidth(2);
		borders.setAntiAlias(true);

		selectedBorders.setColor(Color.GREEN);
		selectedBorders.setStyle(Paint.Style.STROKE);
		selectedBorders.setStrokeWidth(3);
		selectedBorders.setAntiAlias(true);
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
	}

}
