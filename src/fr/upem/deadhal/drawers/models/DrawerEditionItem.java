package fr.upem.deadhal.drawers.models;

import fr.upem.deadhal.components.Corridor;
import fr.upem.deadhal.components.Room;

public class DrawerEditionItem {

	private String m_title;
	private Room m_room;
	private Corridor m_corridor;
	private Type m_type;

	public DrawerEditionItem(Room room) {
		m_room = room;
		m_title = room.getName();
		m_type = Type.ROOM;
	}

	public DrawerEditionItem(String title, Type type) {
		m_title = title;
		m_type = type;
	}

	public DrawerEditionItem(Corridor corridor, String title) {
		m_corridor = corridor;
		m_title = title;
		m_type = Type.CORRIDOR;
	}

	public String getTitle() {
		return m_title;
	}

	public boolean isSuperTitle() {
		return m_type == Type.ADD_CORRIDOR || m_type == Type.ADD_ROOM;
	}

	public Room getRoom() {
		return m_room;
	}

	public Type getType() {
		return m_type;
	}

	public Corridor getCorridor() {
		return m_corridor;
	}

	public enum Type {
		ADD_ROOM, ROOM, ADD_CORRIDOR, CORRIDOR
	}
}
