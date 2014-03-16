package fr.upem.android.deadhal.view;

import android.content.Context;
import android.graphics.Point;
import android.view.MotionEvent;
import fr.upem.android.deadhal.components.Corridor;
import fr.upem.android.deadhal.components.handlers.EditionLevelHandler;
import fr.upem.android.deadhal.graphics.drawable.EditionLevelDrawable;

public class EditionView extends AbstractView {

	private EditionLevelHandler m_levelHandler;

	private EditionView(Context context) {
		super(context);
	}

	public EditionView(Context context, EditionLevelHandler levelHandler,
			EditionLevelDrawable drawable) {
		super(context, levelHandler, drawable);
		m_levelHandler = levelHandler;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		m_gestureDetector.onTouchEvent(event);

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			getProcess(event);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			initZoom(event);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			cancel();
			break;
		case MotionEvent.ACTION_MOVE:
			switch (m_mode) {
			case DRAG_ROOM:
				dragRoom(event);
				break;
			case DRAG:
				drag(event);
				break;
			case NONE:
				getProcess(event);
				break;
			case RESIZE_ROOM:
				resizeRoom(event);
				break;
			case ZOOM:
				zoom(event);
				break;
			default:
				break;
			}
			break;
		}
		refresh();
		return true;
	}

	private void resizeRoom(MotionEvent event) {
		final float[] pts = convertCoordinates(event);
		final float dx = pts[0] - m_start.x;
		final float dy = pts[1] - m_start.y;

		float scaleWidth = m_levelHandler.getSelectedRoom().getRect().width();
		float scaleHeight = m_levelHandler.getSelectedRoom().getRect().height();

		final Point point = m_levelHandler.resizeSelectedRoom(dx, dy);

		scaleWidth /= m_levelHandler.getSelectedRoom().getRect().width();
		scaleHeight /= m_levelHandler.getSelectedRoom().getRect().height();

		if (point.x != 0) {
			m_start.x = pts[0];
		}
		if (point.y != 0) {
			m_start.y = pts[1];
		}

		for (Corridor corridor : m_levelHandler.getLevel().getCorridors()
				.values()) {
			if (m_levelHandler.getLevel().getRooms().get(corridor.getSrc())
					.equals(m_levelHandler.getSelectedRoom())) {
				corridor.getSrcPoint().x /= scaleWidth;
				corridor.getSrcPoint().y /= scaleHeight;
			}
			if (m_levelHandler.getLevel().getRooms().get(corridor.getDst())
					.equals(m_levelHandler.getSelectedRoom())) {
				corridor.getDstPoint().x /= scaleWidth;
				corridor.getDstPoint().y /= scaleHeight;
			}
		}

	}

	private void dragRoom(MotionEvent event) {
		float[] pts = convertCoordinates(event);

		// Calculate the distance moved
		final float dx = pts[0] - m_start.x;
		final float dy = pts[1] - m_start.y;

		m_levelHandler.translateSelectedRoom(dx, dy);

		// Remember this touch position for the next move
		// event
		m_start.set(pts[0], pts[1]);
	}

	private void getProcess(MotionEvent event) {

		boolean isRoomSelected = m_levelHandler.isRoomSelected();

		m_mode = TouchEvent.DRAG;
		m_matrix.invert(m_savedInverseMatrix);
		float[] pts = { event.getX(), event.getY() };
		m_savedInverseMatrix.mapPoints(pts);

		if (isRoomSelected) {
			m_mode = m_levelHandler.getProcess(pts[0], pts[1]);
			if (m_mode != TouchEvent.DRAG) {
				m_start.set(pts[0], pts[1]);
			}
		}
		if (m_mode == TouchEvent.DRAG) {
			m_savedMatrix.set(m_matrix);
			m_start.set(event.getX(), event.getY());
		}
		m_lastEvent = null;
	}
}
