package fr.upem.deadhal.view;

import android.content.Context;
import android.view.MotionEvent;
import fr.upem.deadhal.components.handlers.NavigationLevelHandler;
import fr.upem.deadhal.graphics.drawable.NavigationLevelDrawable;

public class NavigationView extends AbstractView {

	private NavigationLevelHandler m_levelHandler;

	private NavigationView(Context context) {
		super(context);
	}

	public NavigationView(Context context, NavigationLevelHandler levelHandler,
			NavigationLevelDrawable drawable) {
		super(context, levelHandler, drawable);
		m_levelHandler = levelHandler;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		m_gestureDetector.onTouchEvent(event);

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:

			if (!move(event)) {
				getProcess(event);
			}
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			initZoom(event);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			cancel();
			break;
		case MotionEvent.ACTION_MOVE:
			if (!move(event)) {
				switch (m_mode) {
				case DRAG:
					drag(event);
					break;
				case NONE:
					break;
				case RESIZE_ROOM:
					break;
				case ZOOM:
					zoom(event);
					break;
				default:
					break;
				}
			}
			break;

		}

		refresh();

		return true;
	}

	private boolean move(MotionEvent event) {
		if (event.getPointerCount() == 1) {
			float[] pts = convertCoordinates(event);

//			return m_levelHandler.getRoomFromCoordinates(pts[0], pts[1]) != null;

			if (m_mode == TouchEvent.NONE
					&& (m_levelHandler.move(pts[0], pts[1]) || m_levelHandler
							.getRoomFromCoordinates(pts[0], pts[1]) != null)) {
				setMode(TouchEvent.NONE);
				return true;
			}
		}
		return false;
	}

	private void getProcess(MotionEvent event) {
		m_mode = TouchEvent.DRAG;
		m_savedMatrix.set(m_matrix);
		m_start.set(event.getX(), event.getY());
		m_lastEvent = null;
	}

}
