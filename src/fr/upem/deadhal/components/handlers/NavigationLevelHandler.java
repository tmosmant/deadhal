package fr.upem.deadhal.components.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.graphics.PointF;
import android.graphics.RectF;
import android.widget.Toast;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.Corridor;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.view.TouchEvent;

public class NavigationLevelHandler extends AbstractLevelHandler {

	private static final int SHORT_DELAY = 2000;

	private Room m_roomStart;
	private Room m_roomEnd;
	private Room m_localisationRoom;
	private List<UUID> m_shortestPath = new ArrayList<UUID>();
	private float m_localisationX;
	private float m_localisationY;
	private long m_timestampError = 0;

	private PointF m_start = new PointF();

	public NavigationLevelHandler(Level level) {
		super(level);
	}

	public Room getRoomStart() {
		return m_roomStart;
	}

	public void setRoomStart(Room room) {
		m_roomStart = room;
	}

	public Room getRoomEnd() {
		return m_roomEnd;
	}

	public void setRoomEnd(Room room) {
		m_roomEnd = room;
	}

	@Override
	public TouchEvent getProcess(float x, float y) {
		return null;
	}

	@Override
	public void endProcess() {
	}

	public List<UUID> getShortestPath() {
		return m_shortestPath;
	}

	public void setShortestPath(List<UUID> m_path) {
		this.m_shortestPath = m_path;
	}

	public void selectRoomFromCoordinates(float x, float y) {
		Room room = getRoomFromCoordinates(x, y);
		if (room != null && room.equals(m_localisationRoom)) {
			m_localisationRoom = null;
			m_view.getVibrator().vibrate(100);
		} else if (room != null) {
			handleMove(x, y);
			m_localisationRoom = room;
			m_view.getVibrator().vibrate(100);
		}
	}

	private void handleMove(float x, float y) {
		m_start.set(x, y);
		m_localisationX = x;
		m_localisationY = y;
		refreshView();
	}

	public Room getLocalisationRoom() {
		return m_localisationRoom;
	}

	public void setLocalisationRoom(Room localisationRoom) {
		m_localisationRoom = localisationRoom;
	}

	public void moveWithSensor(float x, float y) {
		if (m_localisationRoom != null) {
			RectF rect = m_localisationRoom.getRect();
			if (rect.contains(x, y)) {
				handleMove(x, y);
			} else {
				if (rect.contains(m_start.x, y)) {
					handleMove(m_start.x, y);
				} else if (rect.contains(x, m_start.y)) {
					handleMove(x, m_start.y);
				}
			}
		}
	}

	public boolean move(float x, float y) {
		Room room = getRoomFromCoordinates(x, y);
		if (room != null && m_localisationRoom != null) {
			if (room.equals(m_localisationRoom)) {
				handleMove(x, y);
				return true;
			} else {
				for (Corridor corridor : m_level.getCorridors().values()) {
					if (corridor.getSrc().equals(m_localisationRoom.getId())
							&& corridor.getDst().equals(room.getId())) {
						m_localisationRoom = room;
						handleMove(x, y);
						m_view.getVibrator().vibrate(100);
						return true;
					}
					if (!corridor.isDirected()) {
						if (corridor.getDst()
								.equals(m_localisationRoom.getId())
								&& corridor.getSrc().equals(room.getId())) {
							m_localisationRoom = room;
							handleMove(x, y);
							m_view.getVibrator().vibrate(100);
							return true;
						}
					}
				}
				if (mustPrintError()) {
					Toast.makeText(m_view.getContext(),
							R.string.this_move_isn_t_possible,
							Toast.LENGTH_SHORT).show();
				}
			}
		}
		return false;
	}

	private boolean mustPrintError() {
		long currentTimeMillis = System.currentTimeMillis();
		if (m_timestampError == 0
				|| m_timestampError + SHORT_DELAY < currentTimeMillis) {
			m_timestampError = currentTimeMillis;
			return true;
		}
		return false;
	}

	public float getLocalisationX() {
		return m_localisationX;
	}

	public float getLocalisationY() {
		return m_localisationY;
	}
}
