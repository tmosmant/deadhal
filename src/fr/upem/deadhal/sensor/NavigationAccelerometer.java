package fr.upem.deadhal.sensor;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.widget.Toast;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.handlers.NavigationLevelHandler;

public class NavigationAccelerometer implements SensorEventListener {

	private Activity m_activity;
	private NavigationLevelHandler m_levelHandler;

	private SensorManager m_sensorManager = null;

	public NavigationAccelerometer(Activity activity,
			NavigationLevelHandler levelHandler) {
		m_activity = activity;
		m_levelHandler = levelHandler;
		m_sensorManager = (SensorManager) activity
				.getSystemService(Context.SENSOR_SERVICE);
	}

	public void activate() {
		Sensor m_sensor = m_sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		m_sensorManager.registerListener(this, m_sensor,
				SensorManager.SENSOR_DELAY_GAME);
		lockScreenOrientation();
	}

	public void desactivate() {
		m_sensorManager.unregisterListener(this);
		unlockScreenOrientation();
	}

	private void lockScreenOrientation() {
		Display display = m_activity.getWindowManager().getDefaultDisplay();
		int rotation = display.getRotation();
		int height;
		int width;

		Point size = new Point();
		display.getSize(size);
		height = size.y;
		width = size.x;

		switch (rotation) {
		case Surface.ROTATION_90:
			if (width > height) {
				m_activity
						.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			} else {
				m_activity
						.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
			}
			break;
		case Surface.ROTATION_180:
			if (height > width) {
				m_activity
						.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
			} else {
				m_activity
						.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
			}
			break;
		case Surface.ROTATION_270:
			if (width > height) {
				m_activity
						.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
			} else {
				m_activity
						.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
			break;
		default:
			if (height > width) {
				m_activity
						.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			} else {
				m_activity
						.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		}
		Toast.makeText(m_activity, R.string.screen_lock, Toast.LENGTH_SHORT)
				.show();
	}

	private void unlockScreenOrientation() {
		m_activity
				.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		Toast.makeText(m_activity, R.string.screen_unlock, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (m_levelHandler.getSelectedRoom() != null) {
			float x = 0, y = 0;

			Matrix matrix = new Matrix();
			matrix.postRotate(m_levelHandler.getView().getRotation());
			Matrix inverse = new Matrix();
			matrix.invert(inverse);

			float[] convertCoordinates = { event.values[0], event.values[1] };

			inverse.mapPoints(convertCoordinates);

			switch (m_activity.getWindowManager().getDefaultDisplay()
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

			x += m_levelHandler.getPawn().getPosition().x;
			y += m_levelHandler.getPawn().getPosition().y;

			m_levelHandler.moveWithSensor(x, y);
		}
	}

}
