package fr.upem.deadhal.sensor;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Surface;
import android.widget.Toast;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.handlers.NavigationLevelHandler;

public class Gyroscope implements SensorEventListener {

	private Activity activity;
	private NavigationLevelHandler m_levelHandler;

	private int rotation;
	private Sensor m_sensor = null;
	private SensorManager m_sensorManager = null;

	public Gyroscope(Activity activity, NavigationLevelHandler levelHandler) {
		this.activity = activity;
		this.m_levelHandler = levelHandler;
		m_sensorManager = (SensorManager) activity
				.getSystemService(Context.SENSOR_SERVICE);
	}

	public void activate() {
		m_sensor = m_sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		m_sensorManager.registerListener(this, m_sensor,
				SensorManager.SENSOR_DELAY_GAME);
		lockScreenOrientation();
		rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
	}

	public void desactivate() {
		m_sensorManager.unregisterListener(this);
		unlockScreenOrientation();
	}

	private void lockScreenOrientation() {
		activity.setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		Toast.makeText(activity, R.string.screen_lock, Toast.LENGTH_SHORT).show();
	}

	private void unlockScreenOrientation() {
		activity.setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		Toast.makeText(activity, R.string.screen_unlock, Toast.LENGTH_SHORT).show();
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {
		float x = 0, y = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			x = -event.values[0];
			y = event.values[1];
			break;
		case Surface.ROTATION_90:
			x = event.values[1];
			y = event.values[0];
			break;
		case Surface.ROTATION_180:
			x = event.values[0];
			y = -event.values[1];
			break;
		case Surface.ROTATION_270:
			x = -event.values[1];
			y = -event.values[0];
			break;
		}
		x += m_levelHandler.getLocalisationX();
		y += m_levelHandler.getLocalisationY();
		m_levelHandler.move(x, y);
	}

}
