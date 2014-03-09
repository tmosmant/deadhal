package fr.upem.deadhal.components.handlers;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.UUID;

import fr.upem.deadhal.MainActivity;
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
		MainActivity activity = (MainActivity) m_view.getContext();
		activity.getVibratorService().vibrate(100);

		if (m_start == null) {
			m_start = room;
		} else {
			m_end = room;
			Corridor corridor = new Corridor(UUID.randomUUID(),
					m_start.getId(), m_end.getId(), m_directed);
			m_level.addCorridor(corridor);
			m_view.invalidate();
			m_start = m_end = null;
		}
	}

	public void selectRoomFromCoordinates(float x, float y) {
		Collection<Room> rooms = m_level.getRooms().values();
		LinkedList<Room> reverseRooms = new LinkedList<Room>(rooms);
		Collections.reverse(reverseRooms);
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
