package fr.upem.deadhal.components.handlers;

import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;

public class NavigationLevelHandler extends AbstractLevelHandler {

	private Room m_roomStart;
	private Room m_roomEnd;

	public NavigationLevelHandler(Level level) {
		super(level);
	}

	public Room getRoomStart() {
		return m_roomStart;
	}

	public Room getRoomEnd() {
		return m_roomEnd;
	}

	public void setRoomStart(Room room) {
		m_roomStart = room;
	}

	public void setRoomEnd(Room room) {
		m_roomEnd = room;
	}
}
