package fr.upem.deadhal.components;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import fr.upem.deadhal.utils.Colors;

public class Room implements Parcelable {

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

	public Room(Parcel source) {
		this.title = source.readString();
		float left = source.readFloat();
		float right = source.readFloat();
		float top = source.readFloat();
		float bottom = source.readFloat();
		this.rect = new RectF(left, top, right, bottom);
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeFloat(rect.left);
		dest.writeFloat(rect.right);
		dest.writeFloat(rect.top);
		dest.writeFloat(rect.bottom);
	}

	public static final Parcelable.Creator<Room> CREATOR = new Parcelable.Creator<Room>() {
		@Override
		public Room createFromParcel(Parcel source) {
			return new Room(source);
		}

		@Override
		public Room[] newArray(int size) {
			return new Room[size];
		}
	};

}
