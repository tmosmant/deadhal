package fr.upem.deadhal.graphics;

import android.graphics.Paint;

public class Paints {

	public final static Paint ROOM_BACKGROUND = new Paint();
	public final static Paint ROOM_BORDER = new Paint();
	public final static Paint ROOM_SELECTED_BACKGROUND = new Paint();
	public final static Paint ROOM_SELECTED_BORDER = new Paint();
	public final static Paint ROOM_SELECTED_POINT = new Paint();

	static {
		ROOM_BACKGROUND.setColor(Colors.BLUE);
		ROOM_BACKGROUND.setStyle(Paint.Style.FILL);
		ROOM_BACKGROUND.setAntiAlias(true);

		ROOM_BORDER.setColor(Colors.BLACK);
		ROOM_BORDER.setStyle(Paint.Style.STROKE);
		ROOM_BORDER.setStrokeWidth(3);
		ROOM_BORDER.setAntiAlias(true);

		ROOM_SELECTED_BORDER.setColor(Colors.GREY);
		ROOM_SELECTED_BORDER.setStyle(Paint.Style.STROKE);
		ROOM_SELECTED_BORDER.setStrokeWidth(3);
		ROOM_SELECTED_BORDER.setAntiAlias(true);
		ROOM_SELECTED_BORDER.setAlpha(255);

		ROOM_SELECTED_BACKGROUND.setColor(Colors.BLUE);
		ROOM_SELECTED_BACKGROUND.setStyle(Paint.Style.FILL);
		ROOM_SELECTED_BACKGROUND.setAntiAlias(true);
		ROOM_SELECTED_BACKGROUND.setAlpha(255);

		ROOM_SELECTED_POINT.setColor(Colors.GREY);
		ROOM_SELECTED_POINT.setStyle(Paint.Style.FILL);
		ROOM_SELECTED_POINT.setAntiAlias(true);
		ROOM_SELECTED_POINT.setAlpha(125);
	}

	private Paints() {
	}
}
