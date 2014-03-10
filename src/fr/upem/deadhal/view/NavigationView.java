package fr.upem.deadhal.view;

import android.content.Context;
import android.graphics.Matrix;
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

			if (event.getPointerCount() == 1) {
				Matrix inverse = new Matrix();

				m_matrix.invert(inverse);

				float[] pts = new float[2];

				pts[0] = event.getX(0);
				pts[1] = event.getY(0);

				inverse.mapPoints(pts);
				if (m_levelHandler.moveMinotaur(pts[0], pts[1])) {
					setMode(TouchEvent.NONE);
					return true;
				}
			}
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
			break;
		}

		refresh();

		return true;
	}

	private void getProcess(MotionEvent event) {
		m_mode = TouchEvent.DRAG;
		m_savedMatrix.set(m_matrix);
		m_start.set(event.getX(), event.getY());
		m_lastEvent = null;
	}

}
