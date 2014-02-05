package fr.upem.deadhal.view.listeners;

import android.view.GestureDetector;
import android.view.MotionEvent;
import fr.upem.deadhal.components.handlers.NavigationLevelHandler;
import fr.upem.deadhal.view.AbstractView;

public class NavigationGestureListener extends
		GestureDetector.SimpleOnGestureListener {

	private AbstractView m_view;

	public NavigationGestureListener(AbstractView view,
			NavigationLevelHandler levelHandler) {
		m_view = view;
	}
	
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		m_view.reset();
		return super.onDoubleTap(e);
	}

}