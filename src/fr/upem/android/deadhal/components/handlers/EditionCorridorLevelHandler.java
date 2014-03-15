package fr.upem.android.deadhal.components.handlers;

import java.util.List;
import java.util.UUID;

import android.graphics.PointF;
import android.graphics.RectF;
import android.widget.Toast;
import fr.upem.android.deadhal.components.Corridor;
import fr.upem.android.deadhal.components.Level;
import fr.upem.android.deadhal.components.Room;
import fr.upem.android.deadhal.view.TouchEvent;

public class EditionCorridorLevelHandler extends AbstractLevelHandler {

	private Room m_start;
	private Room m_end;
	private boolean m_directed = false;
	private PointF m_startPoint;
	private PointF m_endPoint;

	public EditionCorridorLevelHandler(Level level) {
		super(level);
	}

	public Room getStart() {
		return m_start;
	}

	public Room getEnd() {
		return m_end;
	}

	@Override
	public TouchEvent getProcess(float x, float y) {
		// nothing to do
		return TouchEvent.NONE;
	}

	@Override
	public void endProcess() {
		// nothing to do
	}

	public boolean isAllSet() {
		return m_start != null && m_end != null;
	}

	private void setRoom(Room room, PointF point) {
		m_view.getVibrator().vibrate(100);

		if (m_start == null) {
			m_start = room;
			RectF rect = m_start.getRect();
			m_startPoint = new PointF(point.x - rect.left, point.y - rect.top);
		} else if (m_start.equals(room)) {
			Toast.makeText(m_view.getContext(),
					"Corridors are only available between two rooms.",
					Toast.LENGTH_SHORT).show();
			m_start = null;
			m_startPoint = null;
		} else {
			m_end = room;
			RectF rect = m_end.getRect();
			m_endPoint = new PointF(point.x - rect.left, point.y - rect.top);
			Corridor corridor = new Corridor(UUID.randomUUID(),
					m_start.getId(), m_end.getId(), m_directed, m_startPoint,
					m_endPoint);
			m_level.addCorridor(corridor);
			m_start = m_end = null;
			m_startPoint = m_endPoint = null;
		}
		refreshView();
	}

	public void selectRoomFromCoordinates(float x, float y) {
		PointF pointF = new PointF(x, y);
		List<Room> reverseRooms = reverseRooms();
		for (Room room : reverseRooms) {
			if (room.getRect().contains(pointF.x, pointF.y)) {
				setRoom(room, pointF);
				return;
			}
		}
	}

	public boolean isDirected() {
		return m_directed;
	}

	public void setDirected(boolean directed) {
		m_directed = directed;
	}

}
