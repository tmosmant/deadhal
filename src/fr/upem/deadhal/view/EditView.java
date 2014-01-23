package fr.upem.deadhal.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.listeners.SelectionRoomListener;
import fr.upem.deadhal.graphics.Paints;
import fr.upem.deadhal.graphics.drawable.LevelDrawable;

public class EditView extends CustomView {
	
	public EditView(Context context) {
		super(context);
	}

	public EditView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EditView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public EditView(Context context, LevelDrawable drawable) {
		super(context, drawable);
	}
	
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
	
	public void build(GestureDetector gestureDetector, Bundle savedInstanceState) {
		m_gestureDetector = gestureDetector;
		restoreMatrix(savedInstanceState);
		m_levelDrawable.addSelectionRoomListener(m_selectionRoomListener);

		if (android.os.Build.VERSION.SDK_INT >= 11) {
			setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		invalidate();		
	}
	
	public void build(GestureDetector gestureDetector, SharedPreferences preferences) {
		m_gestureDetector = gestureDetector;
		restoreMatrix(preferences);
		m_levelDrawable.addSelectionRoomListener(m_selectionRoomListener);
		
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		invalidate();
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
			}
			break;
		}

		bringToFront();
		View rootView = getRootView();
		rootView.requestLayout();
		rootView.invalidate();
		invalidate();

		return true;
	}

}
