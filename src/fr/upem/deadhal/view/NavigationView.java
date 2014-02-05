package fr.upem.deadhal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import fr.upem.deadhal.components.handlers.AbstractLevelHandler;
import fr.upem.deadhal.graphics.drawable.NavigationLevelDrawable;

public class NavigationView extends AbstractView {

	public NavigationView(Context context) {
		super(context);
	}

	public NavigationView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NavigationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public NavigationView(Context context, AbstractLevelHandler levelHandler,
			NavigationLevelDrawable drawable) {
		super(context, levelHandler, drawable);
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
