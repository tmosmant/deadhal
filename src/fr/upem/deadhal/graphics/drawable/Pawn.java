package fr.upem.deadhal.graphics.drawable;

import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.handlers.NavigationLevelHandler;
import fr.upem.deadhal.graphics.Paints;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

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

			if (rect != null) {
				if (rect.contains(x, y)) {
					handleMove(x, y);
				} else {
					if (rect.contains(m_lastPosition.x, y)) {
						if (x < rect.left) {
							m_lastPosition.x = rect.left;
						} else {
							m_lastPosition.x = rect.right - 1;
						}
						handleMove(m_lastPosition.x, y);
					} else if (rect.contains(x, m_lastPosition.y)) {
						if (y < rect.top) {
							m_lastPosition.y = rect.top;
						} else {
							m_lastPosition.y = rect.bottom - 1;
						}
						handleMove(x, m_lastPosition.y);
					} else {
						if (x < rect.left) {
							m_lastPosition.x = rect.left;
						} else {
							m_lastPosition.x = rect.right - 1;
						}
						if (y < rect.top) {
							m_lastPosition.y = rect.top;
						} else {
							m_lastPosition.y = rect.bottom - 1;
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
