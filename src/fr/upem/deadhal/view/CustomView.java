package fr.upem.deadhal.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import fr.upem.deadhal.graphics.drawable.LevelDrawable;

public abstract class CustomView extends View {

	// private Drawable m_drawable;
	protected LevelDrawable m_levelDrawable;
	protected GestureDetector m_gestureDetector;

	// these matrices will be used to move and zoom image
	protected Matrix m_matrix = new Matrix();
	protected Matrix m_savedMatrix = new Matrix();
	protected Matrix m_savedInverseMatrix = new Matrix();

	// we can be in one of these 3 states
	protected TouchEvent m_mode = TouchEvent.NONE;

	// remember some things for zooming
	protected PointF m_start = new PointF();
	protected PointF m_middle = new PointF();
	protected float m_oldDistance = 1f;
	protected float m_distance = 0f;
	protected float m_newRotation = 0f;
	protected float[] m_lastEvent = null;

	protected boolean m_antiAlias = true;

	public CustomView(Context context) {
		super(context);
	}

	public CustomView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomView(Context context, LevelDrawable drawable) {
		super(context);
		m_levelDrawable = drawable;
	}

	@Override
	public abstract boolean onTouchEvent(MotionEvent event);

	public void saveMatrix(Bundle savedInstanceState) {
		float[] values = new float[9];
		m_matrix.getValues(values);
		savedInstanceState.putFloatArray("matrix", values);
	}

	public void saveMatrix(SharedPreferences preferences) {
		float[] values = new float[9];
		m_matrix.getValues(values);

		SharedPreferences.Editor ed = preferences.edit();
		ed.clear();
		for (int i = 0; i < values.length; i++) {
			ed.putFloat("matrix" + i, values[i]);
		}
		ed.commit();
	}

	protected void restoreMatrix(Bundle savedInstanceState) {
		float[] values = null;
		m_matrix = new Matrix();
		m_savedMatrix = new Matrix();
		values = savedInstanceState.getFloatArray("matrix");
		if (values != null) {
			m_matrix.setValues(values);
			m_savedMatrix.set(m_matrix);
		}
	}

	protected void restoreMatrix(SharedPreferences preferences) {
		float[] values = new float[9];
		m_matrix = new Matrix();
		m_savedMatrix = new Matrix();
		if (preferences != null) {
			for (int i = 0; i < values.length; i++) {
				values[i] = preferences.getFloat("matrix" + i, 0);
			}
			m_matrix.setValues(values);
			m_savedMatrix.set(m_matrix);
		}
	}

	/**
	 * Determine the space between the first two fingers
	 */
	protected float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

	/**
	 * Calculate the mid point of the first two fingers
	 */
	protected void midPoint(PointF point, MotionEvent event) {
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
	protected float rotation(MotionEvent event) {
		double delta_x = (event.getX(0) - event.getX(1));
		double delta_y = (event.getY(0) - event.getY(1));
		double radians = Math.atan2(delta_y, delta_x);
		return (float) Math.toDegrees(radians);
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.save();
		canvas.concat(m_matrix);
		m_levelDrawable.draw(canvas);
		canvas.restore();
	}

	public void reset() {
		m_matrix.reset();
		m_savedMatrix.reset();
	}

	@Override
	public Matrix getMatrix() {
		return m_matrix;
	}

}
