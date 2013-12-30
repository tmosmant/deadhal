package fr.upem.deadhal.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.listeners.SelectionRoomListener;
import fr.upem.deadhal.graphics.drawable.LevelDrawable;

public class TouchView extends View {

	// private Drawable m_drawable;
	private GestureDetector m_gestureDetector;
	private LevelDrawable m_levelDrawable;

	// these matrices will be used to move and zoom image
	private Matrix m_matrix = new Matrix();
	private Matrix m_savedMatrix = new Matrix();
	private Matrix m_savedInverseMatrix = new Matrix();

	// we can be in one of these 3 states
	private TouchEvent m_mode = TouchEvent.NONE;

	// remember some things for zooming
	private PointF m_start = new PointF();
	private PointF m_middle = new PointF();
	private float m_oldDistance = 1f;
	private float m_distance = 0f;
	private float m_newRotation = 0f;
	private float[] m_lastEvent = null;

	private SelectionRoomListener m_selectionRoomListener = new SelectionRoomListener() {

		@Override
		public void onUnselectRoom(Room room) {
			Toast.makeText(getContext(), room.getTitle() + " unselected.",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onSelectRoom(Room room) {
			Toast.makeText(getContext(), room.getTitle() + " selected.",
					Toast.LENGTH_SHORT).show();
		}
	};

	public TouchView(Context context) {
		super(context);
	}

	public TouchView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TouchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TouchView(Context context, LevelDrawable drawable) {
		super(context);
		m_levelDrawable = drawable;
	}

	public void build(GestureDetector gestureDetector, Bundle savedInstanceState) {
		m_gestureDetector = gestureDetector;
		restoreMatrix(savedInstanceState);
		m_levelDrawable.addSelectionRoomListener(m_selectionRoomListener);
		invalidate();
	}

	public void saveMatrix(Bundle savedInstanceState) {
		float[] values = new float[9];
		m_matrix.getValues(values);
		savedInstanceState.putFloatArray("matrix", values);
	}

	private void restoreMatrix(Bundle savedInstanceState) {
		float[] values = null;
		m_matrix = new Matrix();
		m_savedMatrix = new Matrix();
		if (savedInstanceState != null) {
			values = savedInstanceState.getFloatArray("matrix");
			if (values != null) {
				m_matrix.setValues(values);
				m_savedMatrix.set(m_matrix);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		m_gestureDetector.onTouchEvent(event);

		boolean isRoomSelected = m_levelDrawable.isRoomSelected();

		// Room selectedRoom = m_drawable.getSelectedRoom();
		switch (event.getAction() & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN:
			m_mode = TouchEvent.DRAG;
			if (isRoomSelected == true) {
				m_matrix.invert(m_savedInverseMatrix);
				float[] pts = { event.getX(), event.getY() };
				m_savedInverseMatrix.mapPoints(pts);
				m_start.set(pts[0], pts[1]);
				m_mode = m_levelDrawable.getProcess(pts[0], pts[1]);
			} else {
				m_savedMatrix.set(m_matrix);
				m_start.set(event.getX(), event.getY());
			}
			m_lastEvent = null;
			break;

		case MotionEvent.ACTION_POINTER_DOWN:
			if (isRoomSelected == false) {
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
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			m_mode = TouchEvent.NONE;
			m_lastEvent = null;
			m_levelDrawable.endProcess();
			break;

		case MotionEvent.ACTION_MOVE:
			if (m_mode == TouchEvent.DRAG) {
				if (isRoomSelected == false) {
					m_matrix.set(m_savedMatrix);
					float dx = event.getX() - m_start.x;
					float dy = event.getY() - m_start.y;
					m_matrix.postTranslate(dx, dy);
				} else {
					final float x = event.getX();
					final float y = event.getY();

					float[] pts = { x, y };
					m_savedInverseMatrix.mapPoints(pts);

					// Calculate the distance moved
					final float dx = pts[0] - m_start.x;
					final float dy = pts[1] - m_start.y;

					m_levelDrawable.translateSelectedRoom(dx, dy);

					// Remember this touch position for the next move
					// event
					m_start.set(pts[0], pts[1]);
				}
			} else if (m_mode == TouchEvent.RESIZE_ROOM) {
				final float x = event.getX();
				final float y = event.getY();

				float[] pts = { x, y };
				m_savedInverseMatrix.mapPoints(pts);

				// Calculate the distance moved
				final float dx = pts[0] - m_start.x;
				final float dy = pts[1] - m_start.y;

				m_levelDrawable.resizeSelectedRoom(dx, dy);

				m_start.set(pts[0], pts[1]);
			}

			else if (m_mode == TouchEvent.ZOOM) {
				if (isRoomSelected == false) {
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

						float xc = m_middle.x;
						float yc = m_middle.y;
						m_matrix.postRotate(r, xc, yc);
					}
				}
			}
			break;
		}

		bringToFront();
		invalidate();

		return true;
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
