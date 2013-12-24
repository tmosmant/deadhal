package fr.upem.deadhal.multitouch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class TouchView extends View {

	private Drawable m_drawable;
	private GestureDetector m_gestureDetector;

	// these matrices will be used to move and zoom image
	private Matrix m_matrix = new Matrix();
	private Matrix m_savedMatrix = new Matrix();

	// we can be in one of these 3 states
	private static final int ms_none = 0;
	private static final int ms_drag = 1;
	private static final int ms_zoom = 2;
	private int m_mode = ms_none;

	// remember some things for zooming
	private PointF m_start = new PointF();
	private PointF m_middle = new PointF();
	private float m_oldDistance = 1f;
	private float m_distance = 0f;
	private float m_newRotation = 0f;
	private float[] m_lastEvent = null;

	public TouchView(Context context) {
		super(context);
	}

	public TouchView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TouchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TouchView(Context context, Drawable drawable) {
		super(context);
		m_drawable = drawable;
	}

	public void build(GestureDetector gestureDetector) {
		this.m_gestureDetector = gestureDetector;
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN:
			m_savedMatrix.set(m_matrix);
			m_start.set(event.getX(), event.getY());
			m_mode = ms_drag;
			m_lastEvent = null;
			break;

		case MotionEvent.ACTION_POINTER_DOWN:
			m_oldDistance = spacing(event);
			if (m_oldDistance > 10f) {
				m_savedMatrix.set(m_matrix);
				midPoint(m_middle, event);
				m_mode = ms_zoom;
			}
			m_lastEvent = new float[4];
			m_lastEvent[0] = event.getX(0);
			m_lastEvent[1] = event.getX(1);
			m_lastEvent[2] = event.getY(0);
			m_lastEvent[3] = event.getY(1);
			m_distance = rotation(event);
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			m_mode = ms_none;
			m_lastEvent = null;
			break;

		case MotionEvent.ACTION_MOVE:
			if (m_mode == ms_drag) {
				m_matrix.set(m_savedMatrix);
				float dx = event.getX() - m_start.x;
				float dy = event.getY() - m_start.y;
				m_matrix.postTranslate(dx, dy);
			}

			else if (m_mode == ms_zoom) {
				float newDist = spacing(event);

				if (newDist > 10f) {
					m_matrix.set(m_savedMatrix);
					float scale = (newDist / m_oldDistance);
					m_matrix.postScale(scale, scale, m_middle.x, m_middle.y);
				}

				if (m_lastEvent != null && event.getPointerCount() >= 2) {
					m_newRotation = rotation(event);
					float r = m_newRotation - m_distance;
					float[] values = new float[9];
					m_matrix.getValues(values);
					float tx = values[2];
					float ty = values[5];
					float sx = values[0];
					float xc = (m_drawable.getMinimumWidth() / 2) * sx;
					float yc = (m_drawable.getMinimumHeight() / 2) * sx;
					m_matrix.postRotate(r, tx + xc, ty + yc);
				}
			}
			break;
		}

		invalidate();

		return m_gestureDetector.onTouchEvent(event);
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
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.i("ondraw: ", "on");

		canvas.save();
		canvas.concat(m_matrix);
		m_drawable.draw(canvas);
		canvas.restore();
	}

	public void reset() {
		m_matrix.reset();
		m_savedMatrix.reset();
	}

}
