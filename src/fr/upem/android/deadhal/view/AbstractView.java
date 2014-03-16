package fr.upem.android.deadhal.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import fr.upem.android.deadhal.components.handlers.AbstractLevelHandler;
import fr.upem.android.deadhal.graphics.Paints;
import fr.upem.android.deadhal.graphics.drawable.AbstractLevelDrawable;

/**
 * This class provides methods for the adaptables views of deadhal.
 * 
 * @author fbousry mremy tmosmant vfricotteau
 * 
 */
public abstract class AbstractView extends View {

	private AbstractLevelDrawable m_levelDrawable;
	protected GestureDetector m_gestureDetector;
	protected AbstractLevelHandler m_levelHandler;

	protected Matrix m_matrix = new Matrix();
	protected Matrix m_savedMatrix = new Matrix();
	protected Matrix m_savedInverseMatrix = new Matrix();
	protected TouchEvent m_mode = TouchEvent.NONE;
	protected PointF m_start = new PointF();
	protected PointF m_middle = new PointF();
	protected float m_oldDistance = 1f;
	protected float m_distance = 0f;
	protected float m_newRotation = 0f;
	protected float[] m_lastEvent = null;

	protected boolean m_antiAlias = true;

	private Vibrator m_vibrator = (Vibrator) getContext().getSystemService(
			Context.VIBRATOR_SERVICE);

	/**
	 * Constructs the view with a context.
	 * 
	 * @param context
	 *            the context
	 */
	protected AbstractView(Context context) {
		super(context);
	}

	/**
	 * Constructs the view with a context, a level handler and a drawable.
	 * 
	 * @param context
	 *            the context
	 * @param levelHandler
	 *            the level handler
	 * @param drawable
	 *            the drawable
	 */
	public AbstractView(Context context, AbstractLevelHandler levelHandler,
			AbstractLevelDrawable drawable) {
		super(context);
		m_levelHandler = levelHandler;
		m_levelDrawable = drawable;
	}

	@Override
	public abstract boolean onTouchEvent(MotionEvent event);

	/**
	 * Build the view.
	 * 
	 * @param gestureDetector
	 *            the gesture detector
	 * @param savedInstanceState
	 *            the saved bundle
	 * @param preferences
	 *            the preferences
	 */
	public void build(GestureDetector gestureDetector,
			Bundle savedInstanceState, SharedPreferences preferences) {
		m_gestureDetector = gestureDetector;

		if (savedInstanceState != null) {
			restoreMatrix(savedInstanceState);
		} else {
			restoreMatrix(preferences);
		}

		setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		View rootView = getRootView();
		rootView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		refresh();
	}

	/**
	 * Refresh the view.
	 */
	public void refresh() {
		bringToFront();
		View rootView = getRootView();
		if (rootView.getLayerType() != View.LAYER_TYPE_SOFTWARE) {
			rootView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		rootView.requestLayout();
		rootView.invalidate();
		invalidate();
	}

	/**
	 * Save the matrix in a bundle.
	 * 
	 * @param savedInstanceState
	 *            the bundle to save in
	 */
	public void saveMatrix(Bundle savedInstanceState) {
		float[] values = new float[9];
		m_matrix.getValues(values);
		savedInstanceState.putFloatArray("matrix", values);
	}

	/**
	 * Save matrix in preferences.
	 * 
	 * @param preferences
	 *            the preferences to save matrix
	 */
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

	/**
	 * Restore a matrix from a bundle.
	 * 
	 * @param savedInstanceState
	 *            the bundle
	 */
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

	/**
	 * Restore a matrix from preferences.
	 * 
	 * @param preferences
	 *            the preferences
	 */
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
	 * @param event
	 *            the event
	 * @return the degrees
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

	/**
	 * Resets the view.
	 */
	public void reset() {
		m_matrix.reset();
		m_savedMatrix.reset();
	}

	@Override
	public Matrix getMatrix() {
		return m_matrix;
	}

	/**
	 * Set a mode for the view.
	 * 
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(TouchEvent mode) {
		m_mode = mode;
	}

	/**
	 * Zoom the view from an event.
	 * 
	 * @param event
	 *            the event
	 */
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

	/**
	 * Drags the view from an event.
	 * 
	 * @param event
	 *            the event
	 */
	protected void drag(MotionEvent event) {
		m_matrix.set(m_savedMatrix);
		float dx = event.getX() - m_start.x;
		float dy = event.getY() - m_start.y;
		m_matrix.postTranslate(dx, dy);
	}

	/**
	 * Initializes the zoom from an event.
	 * 
	 * @param event
	 *            the event
	 */
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

	/**
	 * Cancel the actual process.
	 */
	protected void cancel() {
		m_mode = TouchEvent.NONE;
		m_lastEvent = null;
		m_levelHandler.endProcess();
	}

	/**
	 * Convert the coordinates using the matrix view.
	 * 
	 * @param x
	 *            the x position to convert
	 * @param y
	 *            the y position to convert
	 * @return the converted coordinates
	 */
	public float[] convertCoordinates(float x, float y) {
		Matrix inverse = new Matrix();
		m_matrix.invert(inverse);
		float[] pts = { x, y };
		inverse.mapPoints(pts);
		return pts;
	}

	/**
	 * Convert the coordinates from an event.
	 * 
	 * @param event
	 *            the event
	 * @return
	 */
	public float[] convertCoordinates(MotionEvent event) {
		return convertCoordinates(event.getX(0), event.getY(0));
	}

	/**
	 * Returns the vibrator.
	 * 
	 * @return the vibrator
	 */
	public Vibrator getVibrator() {
		return m_vibrator;
	}

	/**
	 * Returns the rotation of the actual matrix.
	 */
	public float getRotation() {
		float[] values = new float[9];
		m_matrix.getValues(values);
		return Math.round(Math.atan2(values[Matrix.MSKEW_X],
				values[Matrix.MSCALE_X]) * (180 / Math.PI));

	}
}
