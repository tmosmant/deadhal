package fr.upem.deadhal.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.handlers.EditionLevelHandler;
import fr.upem.deadhal.graphics.Paints;
import fr.upem.deadhal.graphics.drawable.EditionLevelDrawable;

public class EditionView extends AbstractView {

	private EditionLevelHandler m_levelHandler;

	private EditionView(Context context) {
		super(context);
	}

	public EditionView(Context context, EditionLevelHandler levelHandler,
			EditionLevelDrawable drawable) {
		super(context, levelHandler, drawable);
		m_levelHandler = levelHandler;
	}

	@Override
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

	@Override
	protected float rotation(MotionEvent event) {
		double delta_x = (event.getX(0) - event.getX(1));
		double delta_y = (event.getY(0) - event.getY(1));
		double radians = Math.atan2(delta_y, delta_x);
		return (float) Math.toDegrees(radians);
	}

	@Override
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

	private void rotateRoom(MotionEvent event) {
		if (m_lastEvent != null && event.getPointerCount() >= 2) {
			m_newRotation = rotation(event);
			float r = (m_newRotation - m_distance);
			float[] values = new float[9];
			m_levelHandler.getSelectedRoom().getMatrix().getValues(values);
			RectF rect = m_levelHandler.getSelectedRoom().getRect();
			m_levelHandler.getSelectedRoom().getMatrix()
					.postRotate(r, rect.centerX(), rect.centerY());
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		m_gestureDetector.onTouchEvent(event);

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			getProcess(event);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:

			if (!initRoomRotate(event)) {
				initZoom(event);
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			cancel();
			break;
		case MotionEvent.ACTION_MOVE:
			switch (m_mode) {
			case DRAG_ROOM:
				dragRoom(event);
				break;
			case DRAG:
				drag(event);
				break;
			case NONE:
				getProcess(event);
				break;
			case RESIZE_ROOM:
				resizeRoom(event);
				break;
			case ZOOM:
				zoom(event);
				break;
			case ROTATE_ROOM:
				rotateRoom(event);
				break;
			default:
				break;
			}
			break;
		}
		refresh();
		return true;
	}

	private boolean initRoomRotate(MotionEvent event) {
		Room selectedRoom = m_levelHandler.getSelectedRoom();

		if (selectedRoom != null) {
			System.out.println("ok1");
			PointerCoords outPointerCoords = new PointerCoords();
			event.getPointerCoords(0, outPointerCoords);
			float pts[] = convertCoordinates(outPointerCoords.x,
					outPointerCoords.y);
			Room roomFromCoordinates = m_levelHandler.getRoomFromCoordinates(
					pts[0], pts[1]);
			boolean t = roomFromCoordinates != null;
			boolean u = selectedRoom.equals(roomFromCoordinates);
			System.out.println(t + " " + u);
			if (roomFromCoordinates != null
					&& selectedRoom.equals(roomFromCoordinates)) {
				System.out.println("ok2");
				m_mode = TouchEvent.ROTATE_ROOM;
				Log.v("deadhal", roomFromCoordinates.getName());
				m_lastEvent = new float[4];
				m_lastEvent[0] = event.getX(0);
				m_lastEvent[1] = event.getX(1);
				m_lastEvent[2] = event.getY(0);
				m_lastEvent[3] = event.getY(1);
				m_distance = rotation(event);
				return true;
			}
		}
		return false;
	}

	private void resizeRoom(MotionEvent event) {
		final float x = event.getX();
		final float y = event.getY();

		final float[] pts = { x, y };
		m_savedInverseMatrix.mapPoints(pts);

		// Calculate the distance moved
//		final float dx = pts[0] - m_start.x;
//		final float dy = pts[1] - m_start.y;

		Matrix inverse = new Matrix();
		m_levelHandler.getSelectedRoom().getMatrix().invert(inverse);
		
		float[] pts2 = { pts[0] , pts[1] };
		inverse.mapPoints(pts2);
		float[] pts3 = { m_start.x , m_start.y };
		inverse.mapPoints(pts3);

		float dx = pts2[0] - pts3[0];
		float dy = pts2[1] - pts3[1];
		
		
		
		final Point point = m_levelHandler.resizeSelectedRoom(dx, dy);

		
		// Avoid to store coordinates when the resize reach the min
		// size
		if (point.x != 0) {
			m_start.x = pts[0];
		}
		if (point.y != 0) {
			m_start.y = pts[1];
		}
	}

	private void dragRoom(MotionEvent event) {
		final float x = event.getX();
		final float y = event.getY();

		float[] pts = { x, y };
		m_savedInverseMatrix.mapPoints(pts);

		// Calculate the distance moved
		final float dx = pts[0] - m_start.x;
		final float dy = pts[1] - m_start.y;

		m_levelHandler.getSelectedRoom().getMatrix().postTranslate(dx, dy);

//		 m_levelHandler.translateSelectedRoom(dx, dy);

		// Remember this touch position for the next move
		// event
		m_start.set(pts[0], pts[1]);
	}

	private void getProcess(MotionEvent event) {

		boolean isRoomSelected = m_levelHandler.isRoomSelected();

		m_mode = TouchEvent.DRAG;
		m_matrix.invert(m_savedInverseMatrix);
		float[] pts = { event.getX(), event.getY() };
		m_savedInverseMatrix.mapPoints(pts);

		if (isRoomSelected) {
			m_mode = m_levelHandler.getProcess(pts[0], pts[1]);
			if (m_mode != TouchEvent.DRAG) {
				m_start.set(pts[0], pts[1]);
			}
		}
		if (m_mode == TouchEvent.DRAG) {
			m_savedMatrix.set(m_matrix);
			m_start.set(event.getX(), event.getY());
		}
		m_lastEvent = null;
	}
}
