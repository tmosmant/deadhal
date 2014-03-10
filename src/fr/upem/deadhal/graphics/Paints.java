package fr.upem.deadhal.graphics;

import android.graphics.Paint;
import android.graphics.Paint.Align;

public class Paints {

	public final static Paint ROOM_BACKGROUND = new Paint();
	public final static Paint ROOM_BORDER = new Paint();
	public final static Paint ROOM_SELECTED_BACKGROUND = new Paint();
	public final static Paint ROOM_SELECTED_BACKGROUND_ERROR = new Paint();
	public final static Paint ROOM_SELECTED_BORDER = new Paint();
	public final static Paint ROOM_SELECTED_POINT = new Paint();
	public final static Paint CORRIDOR = new Paint();
	public final static Paint CORRIDOR_HIGHLIGHT = new Paint();
	public final static Paint LOCALISATION = new Paint();

	public final static Paint ROOM_TITLE = new Paint();

	static {
		ROOM_BACKGROUND.setColor(Colors.BLUE);
		ROOM_BACKGROUND.setStyle(Paint.Style.FILL);
		ROOM_BACKGROUND.setAntiAlias(true);
		ROOM_BACKGROUND.setAlpha(255);

		ROOM_BORDER.setColor(Colors.BLACK);
		ROOM_BORDER.setStyle(Paint.Style.STROKE);
		ROOM_BORDER.setStrokeWidth(3);
		ROOM_BORDER.setAntiAlias(true);
		ROOM_BORDER.setAlpha(255);

		ROOM_SELECTED_BORDER.setColor(Colors.GREY);
		ROOM_SELECTED_BORDER.setStyle(Paint.Style.STROKE);
		ROOM_SELECTED_BORDER.setStrokeWidth(3);
		ROOM_SELECTED_BORDER.setAntiAlias(true);
		ROOM_SELECTED_BORDER.setAlpha(255);

		ROOM_SELECTED_BACKGROUND.setColor(Colors.BLUE);
		ROOM_SELECTED_BACKGROUND.setStyle(Paint.Style.FILL);
		ROOM_SELECTED_BACKGROUND.setAntiAlias(true);
		ROOM_SELECTED_BACKGROUND.setAlpha(255);

		ROOM_SELECTED_BACKGROUND_ERROR.setColor(Colors.RED);
		ROOM_SELECTED_BACKGROUND_ERROR.setStyle(Paint.Style.FILL);
		ROOM_SELECTED_BACKGROUND_ERROR.setAntiAlias(true);
		ROOM_SELECTED_BACKGROUND_ERROR.setAlpha(255);

		ROOM_SELECTED_POINT.setColor(Colors.GREY);
		ROOM_SELECTED_POINT.setStyle(Paint.Style.FILL);
		ROOM_SELECTED_POINT.setAntiAlias(true);
		ROOM_SELECTED_POINT.setAlpha(125);

		ROOM_TITLE.setColor(Colors.BLACK);
		ROOM_TITLE.setTextSize(20);
		ROOM_TITLE.setTextAlign(Align.CENTER);
		ROOM_TITLE.setAntiAlias(true);
		ROOM_TITLE.setAlpha(255);

		CORRIDOR.setColor(Colors.BLACK);
		CORRIDOR.setStyle(Paint.Style.FILL_AND_STROKE);
		CORRIDOR.setStrokeWidth(3);
		CORRIDOR.setAntiAlias(true);
		CORRIDOR.setAlpha(255);

		CORRIDOR_HIGHLIGHT.setColor(Colors.GREEN);
		CORRIDOR_HIGHLIGHT.setStyle(Paint.Style.FILL_AND_STROKE);
		CORRIDOR_HIGHLIGHT.setStrokeWidth(5);
		CORRIDOR_HIGHLIGHT.setAntiAlias(true);
		CORRIDOR_HIGHLIGHT.setAlpha(255);

		LOCALISATION.setColor(Colors.BLACK);
		LOCALISATION.setAntiAlias(true);
		LOCALISATION.setAlpha(255);
	}

	public static void setAntiAlias(boolean aa) {
		ROOM_BACKGROUND.setAntiAlias(aa);
		ROOM_BORDER.setAntiAlias(aa);
		ROOM_SELECTED_BORDER.setAntiAlias(aa);
		ROOM_SELECTED_BACKGROUND.setAntiAlias(aa);
		ROOM_SELECTED_BACKGROUND_ERROR.setAntiAlias(aa);
		ROOM_SELECTED_POINT.setAntiAlias(aa);
		ROOM_TITLE.setAntiAlias(aa);
		CORRIDOR.setAntiAlias(aa);
		CORRIDOR_HIGHLIGHT.setAntiAlias(aa);
		LOCALISATION.setAntiAlias(aa);
	}

	private Paints() {
	}
}
