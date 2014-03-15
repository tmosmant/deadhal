package fr.upem.android.deadhal.graphics.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import fr.upem.android.deadhal.components.Room;
import fr.upem.android.deadhal.components.handlers.NavigationLevelHandler;
import fr.upem.android.deadhal.graphics.Paints;

public class Pawn extends Drawable {
	private final int m_radiusExtern = 25;
	private final int m_radiusMiddle = 20;
	private final int m_radiusIntern = 12;

	private PointF m_lastPosition = new PointF();
	private PointF m_position = new PointF();

	private int m_alpha = 255;
	private NavigationLevelHandler m_levelHandler;

	public Pawn(NavigationLevelHandler levelHandler) {
		m_levelHandler = levelHandler;
	}

	@Override
	public void draw(Canvas canvas) {
		Room selectedRoom = m_levelHandler.getSelectedRoom();
		if (selectedRoom != null) {
			float x = m_levelHandler.getPawn().getPosition().x;
			float y = m_levelHandler.getPawn().getPosition().y;
			canvas.drawCircle(x - m_radiusExtern / 2, y - m_radiusExtern / 2,
					m_radiusExtern, Paints.LOCALISATION_EXTERN);
			canvas.drawCircle(x - m_radiusExtern / 2, y - m_radiusExtern / 2,
					m_radiusMiddle, Paints.LOCALISATION_MIDDLE);
			canvas.drawCircle(x - m_radiusExtern / 2, y - m_radiusExtern / 2,
					m_radiusIntern, Paints.LOCALISATION_INTERN);
		}
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

	public void handleMove(float x, float y) {
		RectF rect = m_levelHandler.getSelectedRoom().getRect();
		if (x - 39 < rect.left) {
			x = rect.left + 39;
		}
		if (x + 15 > rect.right) {
			x = rect.right - 15;
		}
		if (y - 39 < rect.top) {
			y = rect.top + 39;
		}
		if (y + 15 > rect.bottom) {
			y = rect.bottom - 15;
		}

		m_lastPosition.set(x, y);
		m_position.set(x, y);
		m_levelHandler.refreshView();
	}

	public void slide(float x, float y) {
		Room selectedRoom = m_levelHandler.getSelectedRoom();
		if (selectedRoom != null) {
			RectF rect = selectedRoom.getRect();
			RectF rectCopy = new RectF(rect);
			// need to do this due to the implementation of RectF.contains().
			rectCopy.right++;
			rectCopy.bottom++;
			if (rectCopy != null) {
				if (rectCopy.contains(x, y)) {
					handleMove(x, y);
				} else {
					if (rectCopy.contains(m_lastPosition.x, y)) {
						if (x < rectCopy.left) {
							m_lastPosition.x = rectCopy.left;
						} else {
							m_lastPosition.x = rectCopy.right;
						}
						handleMove(m_lastPosition.x, y);
					} else if (rectCopy.contains(x, m_lastPosition.y)) {
						if (y < rectCopy.top) {
							m_lastPosition.y = rectCopy.top;
						} else {
							m_lastPosition.y = rectCopy.bottom;
						}
						handleMove(x, m_lastPosition.y);
					} else {
						if (x < rectCopy.left) {
							m_lastPosition.x = rectCopy.left;
						} else {
							m_lastPosition.x = rectCopy.right;
						}
						if (y < rectCopy.top) {
							m_lastPosition.y = rectCopy.top;
						} else {
							m_lastPosition.y = rectCopy.bottom;
						}
						handleMove(m_lastPosition.x, m_lastPosition.y);
					}
				}
			}
		}
	}

	public PointF getPosition() {
		return m_position;
	}
}
