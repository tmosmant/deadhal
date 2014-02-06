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
import fr.upem.deadhal.components.handlers.AbstractLevelHandler;
import fr.upem.deadhal.graphics.Paints;
import fr.upem.deadhal.graphics.drawable.AbstractDrawable;

public abstract class AbstractView extends View {

	// private Drawable m_drawable;
	private AbstractDrawable m_levelDrawable;
	protected GestureDetector m_gestureDetector;
	protected AbstractLevelHandler m_levelHandler;

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

	public AbstractView(Context context) {
		super(context);
	}

	public AbstractView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AbstractView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AbstractView(Context context, AbstractLevelHandler levelHandler,
			AbstractDrawable drawable) {
		super(context);
		m_levelHandler = levelHandler;
		m_levelDrawable = drawable;
	}

	@Override
	public abstract boolean onTouchEvent(MotionEvent event);

	public void build(GestureDetector gestureDetector,
			Bundle savedInstanceState, SharedPreferences preferences) {
		m_gestureDetector = gestureDetector;

		if (savedInstanceState != null) {
			restoreMatrix(savedInstanceState);
		} else {
			restoreMatrix(preferences);
		}

		if (android.os.Build.VERSION.SDK_INT >= 11) {
			setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}

		invalidate();
	}

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

	private void restoreMatrix(Bundle savedInstanceState) {
		float[] values;
		m_matrix = new Matrix();
		m_savedMatrix = new Matrix();
		values = savedInstanceState.getFloatArray("matrix");
		if (values != null) {
			m_matrix.setValues(values);
			m_savedMatrix.set(m_matrix);
		}
	}

	private void restoreMatrix(SharedPreferences preferences) {
		float[] values = new float[9];
		m_matrix = new Matrix();
		m_savedMatrix = new Matrix();

		if (preferences.contains("matrix1")) {
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
	 * @param event evenement
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

	public void setMode(TouchEvent mode) {
		m_mode = mode;
	}

	public void refresh() {
		bringToFront();
		View rootView = getRootView();
		rootView.requestLayout();
		rootView.invalidate();
		invalidate();
	}

	public void zoom(MotionEvent event) {
		float newDist = spacing(event);
		if (newDist > 10f) {
			m_matrix.set(m_savedMatrix);
			float scale = (newDist / m_oldDistance);
			m_matrix.postScale(scale, scale, m_middle.x, m_middle.y);

			float[] f = new float[9];
			m_matrix.getValues(f);

			float scaleX = f[Matrix.MSCALE_X];
			float scaleY = f[Matrix.MSCALE_Y];

			if (scaleX >= 40 && scaleY >= 40 && m_antiAlias) {
				Paints.setAntiAlias(false);
				m_antiAlias = false;
			} else if (!m_antiAlias) {
				Paints.setAntiAlias(true);
				m_antiAlias = true;
			}
		}

		if (m_lastEvent != null && event.getPointerCount() >= 2) {
			m_newRotation = rotation(event);
			float r = m_newRotation - m_distance;
			float[] values = new float[9];
			m_matrix.getValues(values);

			float xc = m_middle.x;
			float yc = m_middle.y;
			m_matrix.postRotate(r, xc, yc);
		}
	}

	protected void drag(MotionEvent event) {
		m_matrix.set(m_savedMatrix);
		float dx = event.getX() - m_start.x;
		float dy = event.getY() - m_start.y;
		m_matrix.postTranslate(dx, dy);
	}

	protected void initZoom(MotionEvent event) {
		m_oldDistance = spacing(event);
		if (m_oldDistance > 10f) {
			m_savedMatrix.set(m_matrix);
			midPoint(m_middle, event);
			m_mode = TouchEvent.ZOOM;
		}
		m_lastEvent = new float[4];
		m_lastEvent[0] = event.getX(0);
		m_lastEvent[1] = event.getX(1);
		m_lastEvent[2] = event.getY(0);
		m_lastEvent[3] = event.getY(1);
		m_distance = rotation(event);
	}

	protected void cancel() {
		m_mode = TouchEvent.NONE;
		m_lastEvent = null;
		m_levelHandler.endProcess();
	}
}
