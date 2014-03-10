package fr.upem.deadhal.components.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.widget.Toast;
import fr.upem.deadhal.R;
import fr.upem.deadhal.components.Corridor;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.view.TouchEvent;

public class NavigationLevelHandler extends AbstractLevelHandler {

	private Room m_roomStart;
	private Room m_roomEnd;
	private Room m_localisationRoom;
	private List<UUID> m_shortestPath = new ArrayList<UUID>();

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
		if (m_localisationRoom != null
				&& m_localisationRoom.getRect().contains(x, y)) {
			m_localisationRoom = null;
			refreshView();
			return;
		}
		List<Room> reverseRooms = reverseRooms();
		for (Room room : reverseRooms) {
			if (!room.equals(m_localisationRoom)
					&& room.getRect().contains(x, y)) {
				m_localisationRoom = room;
				refreshView();
				return;
			}
		}
	}

	public Room getLocalisationRoom() {
		return m_localisationRoom;
	}

	public void setLocalisationRoom(Room localisationRoom) {
		m_localisationRoom = localisationRoom;
	}

	public void moveMinotaur(float x, float y) {
		Room room = getRoomFromCoordinates(x, y);
		if (room != null && m_localisationRoom != null
				&& !room.equals(m_localisationRoom)) {
			for (Corridor corridor : m_level.getCorridors().values()) {
				if (corridor.getSrc().equals(m_localisationRoom.getId())
						&& corridor.getDst().equals(room.getId())) {
					m_localisationRoom = room;
					m_view.refresh();
					return;
				}
				if (!corridor.isDirected()) {
					if (corridor.getDst().equals(m_localisationRoom.getId())
							&& corridor.getSrc().equals(room.getId())) {
						m_localisationRoom = room;
						m_view.refresh();
						return;
					}
				}
			}
			Toast.makeText(m_view.getContext(),
					R.string.this_move_isn_t_possible, Toast.LENGTH_SHORT)
					.show();
		}
	}
}
