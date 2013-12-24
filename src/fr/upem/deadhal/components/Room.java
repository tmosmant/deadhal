package fr.upem.deadhal.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Room {

	private String title;
	private Rect rect;

	private final Paint background = new Paint();
	private final Paint borders = new Paint();

	public Room(String title, int left, int top, int right, int bottom) {
		this.title = title;
		this.rect = new Rect(left, top, right, bottom);
		background.setColor(Color.CYAN);
		background.setStyle(Paint.Style.FILL);
		background.setAntiAlias(true);
		borders.setColor(Color.BLACK);
		borders.setStyle(Paint.Style.STROKE);
		borders.setStrokeWidth(2);
		borders.setAntiAlias(true);
	}

	public String getTitle() {
		return title;
	}

	public Rect getRect() {
		return rect;
	}

	public void draw(Canvas canvas) {
		canvas.drawRect(rect, background);
		canvas.drawRect(rect, borders);
	}

}
