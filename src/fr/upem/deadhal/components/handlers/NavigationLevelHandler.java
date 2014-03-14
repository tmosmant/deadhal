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
import fr.upem.deadhal.components.listeners.SelectionRoomListener;
import fr.upem.deadhal.view.TouchEvent;

public class NavigationLevelHandler extends AbstractLevelHandler {

	private static final int SHORT_DELAY = 2000;

	private Room m_roomStart;
	private Room m_roomEnd;
	private Room m_selectedRoom;
	private List<UUID> m_shortestPath = new ArrayList<UUID>();
	private PointF m_localisation = new PointF();
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

	private void handleMove(float x, float y) {
		m_start.set(x, y);
		m_localisation.set(x, y);
		refreshView();
	}

	public Room getSelectedRoom() {
		return m_selectedRoom;
	}

	public void selectRoom(Room room) {
		if (room != null) {
			m_selectedRoom = room;
			for (SelectionRoomListener listener : m_selectionRoomListeners) {
				listener.onSelectRoom(room);
			}
		} else {
			m_selectedRoom = null;
			for (SelectionRoomListener listener : m_selectionRoomListeners) {
				listener.onUnselectRoom(room);
			}
		}
	}

	public void moveWithSensor(float x, float y) {
		if (m_selectedRoom != null) {
			RectF rect = m_selectedRoom.getRect();
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
		if (room != null && m_selectedRoom != null) {
			if (room.equals(m_selectedRoom)) {
				handleMove(x, y);
				return true;
			} else {
				for (Corridor corridor : m_level.getCorridors().values()) {
					if (corridor.getSrc().equals(m_selectedRoom.getId())
							&& corridor.getDst().equals(room.getId())) {
						selectRoom(room);
						handleMove(x, y);
						m_view.getVibrator().vibrate(100);
						return true;
					}
					if (!corridor.isDirected()) {
						if (corridor.getDst()
								.equals(m_selectedRoom.getId())
								&& corridor.getSrc().equals(room.getId())) {
							selectRoom(room);
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

	public PointF getLocalisation() {
		return m_localisation;
	}

}
