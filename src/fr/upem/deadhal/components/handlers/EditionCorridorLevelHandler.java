package fr.upem.deadhal.components.handlers;

import java.util.List;
import java.util.UUID;

import android.widget.Toast;
import fr.upem.deadhal.components.Corridor;
import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.view.TouchEvent;

public class EditionCorridorLevelHandler extends AbstractLevelHandler {

	private Room m_start;
	private Room m_end;
	private boolean m_directed = false;

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

	private void setRoom(Room room) {
		m_view.getVibrator().vibrate(100);

		if (m_start == null) {
			m_start = room;
		} else if (m_start.equals(room)) {
			Toast.makeText(m_view.getContext(),
					"Corridors are only available between two rooms.",
					Toast.LENGTH_SHORT).show();
			m_start = null;
		} else if (corridorBetween(m_start, room)) {
			Toast.makeText(m_view.getContext(),
					"There is already a corridor between those rooms.",
					Toast.LENGTH_SHORT).show();
			m_start = null;
		} else {
			m_end = room;
			Corridor corridor = new Corridor(UUID.randomUUID(),
					m_start.getId(), m_end.getId(), m_directed);
			m_level.addCorridor(corridor);
			m_start = m_end = null;
		}
		refreshView();
	}

	private boolean corridorBetween(Room start, Room end) {
		for (Corridor corridor : m_level.getCorridors().values()) {
			if (corridor.getSrc().equals(start.getId())
					&& corridor.getDst().equals(end.getId())) {
				return true;
			}
			if (corridor.getDst().equals(start.getId())
					&& corridor.getSrc().equals(end.getId())) {
				return true;
			}
		}
		return false;
	}

	public void selectRoomFromCoordinates(float x, float y) {
		List<Room> reverseRooms = reverseRooms();
		for (Room room : reverseRooms) {
			if (room.getRect().contains(x, y)) {
				setRoom(room);
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
