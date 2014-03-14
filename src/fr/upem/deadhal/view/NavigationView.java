package fr.upem.deadhal.view;

import android.content.Context;
import android.view.MotionEvent;
import fr.upem.deadhal.components.Room;
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
			case DRAG:
				drag(event);
				break;
			case MOVE:
				move(event);
				break;
			case ZOOM:
				zoom(event);
				break;
			case NONE:
				getProcess(event);
				break;
			default:
				break;
			}
			break;
		}
		refresh();
		return true;
	}

	private void move(MotionEvent event) {
		if (event.getPointerCount() == 1) {
			float[] pts = convertCoordinates(event);
			m_levelHandler.move(pts[0], pts[1]);
		}
	}

	private void getProcess(MotionEvent event) {		
		m_mode = TouchEvent.DRAG;

		float[] pts = convertCoordinates(event);
		Room roomFromCoordinates = m_levelHandler.getRoomFromCoordinates(
				pts[0], pts[1]);
		if (roomFromCoordinates != null
				&& roomFromCoordinates.equals(m_levelHandler
						.getSelectedRoom())) {
			m_mode = TouchEvent.MOVE;
		} else {
			if (roomFromCoordinates != null) {
				// need to do this in order to display the error message
				move(event);
			}
			m_savedMatrix.set(m_matrix);
			m_start.set(event.getX(), event.getY());
			m_lastEvent = null;
		}
	}

}
