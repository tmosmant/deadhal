package fr.upem.android.deadhal.view;

import android.content.Context;
import android.view.MotionEvent;
import fr.upem.android.deadhal.components.handlers.EditionCorridorLevelHandler;
import fr.upem.android.deadhal.graphics.drawable.EditionCorridorLevelDrawable;

/**
 * This class is the view for the edition corridor part.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public class EditionCorridorView extends AbstractView {

	private EditionCorridorLevelHandler m_levelHandler;

	/**
	 * Constructs the view.
	 * 
	 * @param context
	 *            the context
	 */
	private EditionCorridorView(Context context) {
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
	public EditionCorridorView(Context context,
			EditionCorridorLevelHandler levelHandler,
			EditionCorridorLevelDrawable drawable) {
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
			case NONE:
				getProcess(event);
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
		m_matrix.invert(m_savedInverseMatrix);
		float[] pts = { event.getX(), event.getY() };
		m_savedInverseMatrix.mapPoints(pts);

		if (m_mode == TouchEvent.NONE) {
			m_levelHandler.getProcess(pts[0], pts[1]);
		}
		m_mode = TouchEvent.DRAG;

		if (m_mode == TouchEvent.DRAG) {
			m_savedMatrix.set(m_matrix);
			m_start.set(event.getX(), event.getY());
		}
		m_lastEvent = null;
	}
}
