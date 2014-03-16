package fr.upem.android.deadhal.view;

import android.content.Context;
import android.view.MotionEvent;
import fr.upem.android.deadhal.components.Room;
import fr.upem.android.deadhal.components.handlers.NavigationLevelHandler;
import fr.upem.android.deadhal.graphics.drawable.NavigationLevelDrawable;
import fr.upem.android.deadhal.sensor.NavigationAccelerometer;

/**
 * This class is the view for the navigation part.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public class NavigationView extends AbstractView {

	private NavigationLevelHandler m_levelHandler;

	private NavigationView(Context context) {
		super(context);
	}

	/**
	 * Constructs the view.
	 * 
	 * @param context
	 *            the context
	 * @param levelHandler
	 *            the level handler
	 * @param drawable
	 *            the drawable
	 */
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

	private boolean move(MotionEvent event) {
		return move(event, true);
	}

	private boolean move(MotionEvent event, boolean mustSlide) {
		if (event.getPointerCount() == 1) {
			float[] pts = convertCoordinates(event);
			return m_levelHandler.move(pts[0], pts[1], mustSlide);
		}
		return false;
	}

	private void getProcess(MotionEvent event) {
		m_mode = TouchEvent.DRAG;

		float[] pts = convertCoordinates(event);
		Room roomFromCoordinates = m_levelHandler.getRoomFromCoordinates(
				pts[0], pts[1]);
		if (!NavigationAccelerometer.isActivated()
				&& roomFromCoordinates != null && move(event, false)) {
			m_mode = TouchEvent.MOVE;
		} else {
			m_savedMatrix.set(m_matrix);
			m_start.set(event.getX(), event.getY());
			m_lastEvent = null;
		}
	}

}
