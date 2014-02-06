package fr.upem.deadhal.drawers.models;

import fr.upem.deadhal.components.Corridor;
import fr.upem.deadhal.components.Room;

public class DrawerEditionItem {

	private String m_title;
	private Room m_room;
	private Corridor m_corridor;
	private Type m_type;

	public DrawerEditionItem(Room room) {
		this.m_room = room;
		this.m_title = room.getName();
		this.m_type = Type.ROOM;
	}

	public DrawerEditionItem(String title, Type type) {
		this.m_title = title;
		this.m_type = type;
	}

	public DrawerEditionItem(Corridor corridor, String title) {
		this.m_corridor = corridor;
		this.m_title = title;
		this.m_type = Type.CORRIDOR;
	}

	public String getTitle() {
		return this.m_title;
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
