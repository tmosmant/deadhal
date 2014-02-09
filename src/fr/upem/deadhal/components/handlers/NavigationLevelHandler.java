package fr.upem.deadhal.components.handlers;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import fr.upem.deadhal.components.Level;
import fr.upem.deadhal.components.Room;
import fr.upem.deadhal.components.listeners.SelectionRoomListener;
import fr.upem.deadhal.view.TouchEvent;

public class NavigationLevelHandler extends AbstractLevelHandler {

	private Room m_roomStart;
	private Room m_roomEnd;
	private Room m_selectedRoom;

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

	public void selectRoomFromCoordinates(float x, float y) {
		Collection<Room> rooms = m_level.getRooms().values();
		LinkedList<Room> reverseRooms = new LinkedList<Room>(rooms);
		Collections.reverse(reverseRooms);

		for (Room room : reverseRooms) {
			if (room.getRect().contains(x, y)) {
				for (SelectionRoomListener listener : selectionRoomListeners) {
					listener.onSelectRoom(room);
				}
				m_selectedRoom = room;
				invalidateView();
				return;
			}
		}
	}

	@Override
	public TouchEvent getProcess(float x, float y) {
		return null;
	}

	@Override
	public void endProcess() {
	}
}
