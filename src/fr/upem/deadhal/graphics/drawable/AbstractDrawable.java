package fr.upem.deadhal.graphics.drawable;

import java.util.LinkedList;
import java.util.List;

import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.listeners.SelectionRoomListener;
import fr.upem.deadhal.graphics.Paints;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public abstract class AbstractDrawable extends Drawable {

	protected Level m_level;
	private int m_alpha = 255;
	protected List<SelectionRoomListener> selectionRoomListeners = new LinkedList<SelectionRoomListener>();

	public AbstractDrawable(Level m_level) {
		this.m_level = m_level;
	}

	@Override
	public abstract void draw(Canvas canvas);

	public abstract boolean selectRoomFromCoordinates(float x, float y);

	public void addSelectionRoomListener(SelectionRoomListener listener) {
		selectionRoomListeners.add(listener);
	}

	public void removeSelectionRoomListener(SelectionRoomListener listener) {
		selectionRoomListeners.remove(listener);
	}

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
		String title = room.getTitle();
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

}
