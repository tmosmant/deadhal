package fr.upem.deadhal.view.listeners;

import android.content.Context;
import android.graphics.Matrix;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.MotionEvent;
import fr.upem.deadhal.components.handlers.EditionLevelHandler;
import fr.upem.deadhal.view.AbstractView;
import fr.upem.deadhal.view.TouchEvent;

public class EditionGestureListener extends
		GestureDetector.SimpleOnGestureListener {

	private AbstractView m_view;
	private EditionLevelHandler m_levelHander;

	public EditionGestureListener(AbstractView view,
			EditionLevelHandler levelHandler) {
		m_view = view;
		m_levelHander = levelHandler;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		Matrix inverse = new Matrix();

		m_view.getMatrix().invert(inverse);

		float[] pts = new float[2];

		pts[0] = e.getX(0);
		pts[1] = e.getY(0);

		inverse.mapPoints(pts);
		if (e.getPointerCount() == 1) {
			boolean oldIsRoomSelected = m_levelHander.isRoomSelected();
			m_levelHander.selectRoomFromCoordinates(pts[0], pts[1]);

			if (oldIsRoomSelected != m_levelHander.isRoomSelected()) {
				Vibrator vibratorService = (Vibrator) m_view.getContext()
						.getSystemService(Context.VIBRATOR_SERVICE);
				if (vibratorService != null) {
					vibratorService.vibrate(100);
				}
			}
			m_view.setMode(TouchEvent.NONE);
		}
		super.onLongPress(e);
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		m_view.reset();
		m_view.invalidate();
		return true;
	}

}