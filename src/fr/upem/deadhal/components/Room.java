package fr.upem.deadhal.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.Toast;

public class Room {

	private String title;
	private Rect rect;

	private boolean selected = false;

	private final Paint background = new Paint();
	private final Paint borders = new Paint();

	private final Paint selectedBorders = new Paint();

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

		selectedBorders.setColor(Color.GREEN);
		selectedBorders.setStyle(Paint.Style.STROKE);
		selectedBorders.setStrokeWidth(3);
		selectedBorders.setAntiAlias(true);
	}

	public String getTitle() {
		return title;
	}

	public Rect getRect() {
		return rect;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void draw(Canvas canvas) {
		canvas.drawRect(rect, background);
		if (selected) {
			canvas.drawRect(rect, selectedBorders);
		} else {
			canvas.drawRect(rect, borders);
		}
	}

}
