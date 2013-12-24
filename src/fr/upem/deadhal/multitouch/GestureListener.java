package fr.upem.deadhal.multitouch;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureListener extends GestureDetector.SimpleOnGestureListener {
	
	private TouchView view;

	public GestureListener(TouchView view) {
		this.view = view;
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		view.reset();
		view.invalidate();
		return true;
	}
}