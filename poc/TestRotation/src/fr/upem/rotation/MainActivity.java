package fr.upem.rotation;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {

	// private PointF start = new PointF();
	// we can be in one of these 3 states
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private int mode = NONE;

	// remember some things for zooming
	private PointF start = new PointF();
	private PointF mid = new PointF();
	private float oldDist = 1f;
	private float d = 0f;
	private float newRot = 0f;
	private float[] lastEvent = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.rLayout);
		rLayout.setOnTouchListener(onTouchListener());

		final List<View> views = new ArrayList<View>();
		for (int i = 0; i < rLayout.getChildCount(); i++) {
			views.add(rLayout.getChildAt(i));
		}

		SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setMax(360);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			float rotation = 0;

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				rotation = progress;
				for (View v : views) {
					v.setRotation(rotation);
				}
			}
		});
	}

	private OnTouchListener onTouchListener() {
		return new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// switch (event.getAction() & MotionEvent.ACTION_MASK) {
				//
				// case MotionEvent.ACTION_DOWN:
				// start.set(event.getX(), event.getY());
				// break;
				//
				// case MotionEvent.ACTION_MOVE:
				// dx = event.getX();
				// dy = event.getY();
				// v.setTranslationX(dx);
				// v.setTranslationY(dy);
				// break;
				// }
				//
				// return true;
				switch (event.getAction() & MotionEvent.ACTION_MASK) {

				case MotionEvent.ACTION_DOWN:
					start.set(event.getX(), event.getY());
					mode = DRAG;
					lastEvent = null;
					break;

				case MotionEvent.ACTION_POINTER_DOWN:
					oldDist = spacing(event);
					if (oldDist > 10f) {
						midPoint(mid, event);
						mode = ZOOM;
					}
					lastEvent = new float[4];
					lastEvent[0] = event.getX(0);
					lastEvent[1] = event.getX(1);
					lastEvent[2] = event.getY(0);
					lastEvent[3] = event.getY(1);
					d = rotation(event);
					break;

				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
					mode = NONE;
					lastEvent = null;
					break;

				case MotionEvent.ACTION_MOVE:
					if (mode == DRAG) {
						float dx = event.getX() - start.x;
						float dy = event.getY() - start.y;
						v.setTranslationX(dx);
						v.setTranslationY(dy);
					}

					else if (mode == ZOOM) {
//						float newDist = spacing(event);

						if (lastEvent != null && event.getPointerCount() == 3) {
							newRot = rotation(event);
							v.setRotation(newRot);
						}
					}
					break;
				}

				return true;

			}

		};

	}

	/**
	 * Determine the space between the first two fingers
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

	/**
	 * Calculate the mid point of the first two fingers
	 */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	/**
	 * Calculate the degree to be rotated by.
	 * 
	 * @param event
	 * @return Degrees
	 */
	private float rotation(MotionEvent event) {
		double delta_x = (event.getX(0) - event.getX(1));
		double delta_y = (event.getY(0) - event.getY(1));
		double radians = Math.atan2(delta_y, delta_x);
		return (float) Math.toDegrees(radians);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
