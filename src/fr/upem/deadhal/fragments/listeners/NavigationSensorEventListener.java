package fr.upem.deadhal.fragments.listeners;

import android.app.Activity;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.Surface;
import fr.upem.deadhal.components.handlers.NavigationLevelHandler;

public class NavigationSensorEventListener implements SensorEventListener {

	private Activity activity;
	private NavigationLevelHandler m_levelHandler;

	public NavigationSensorEventListener(Activity activity,
			NavigationLevelHandler m_levelHandler) {
		this.activity = activity;
		this.m_levelHandler = m_levelHandler;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (m_levelHandler.getLocalisationRoom() != null) {
			float x = 0, y = 0;

			Matrix matrix = new Matrix();
			matrix.postRotate(m_levelHandler.getView().getRotation());
			Matrix inverse = new Matrix();
			matrix.invert(inverse);

			float[] convertCoordinates = { event.values[0], event.values[1] };

			inverse.mapPoints(convertCoordinates);

			switch (activity.getWindowManager().getDefaultDisplay()
					.getRotation()) {
			case Surface.ROTATION_0:
				x = -convertCoordinates[0];
				y = convertCoordinates[1];
				break;
			case Surface.ROTATION_90:
				x = convertCoordinates[0];
				y = convertCoordinates[1];
				break;
			case Surface.ROTATION_180:
				x = convertCoordinates[0];
				y = -convertCoordinates[1];
				break;
			case Surface.ROTATION_270:
				x = -convertCoordinates[1];
				y = -convertCoordinates[0];
				break;
			}

			x += m_levelHandler.getLocalisationX();
			y += m_levelHandler.getLocalisationY();

			m_levelHandler.moveWithSensor(x, y);
		}
	}

}
