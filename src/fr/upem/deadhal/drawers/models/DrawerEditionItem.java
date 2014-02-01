package fr.upem.deadhal.drawers.models;

import fr.upem.deadhal.components.Corridor;
import fr.upem.deadhal.components.Room;

public class DrawerEditionItem {

	private String m_title;
	private boolean m_isSuperTitle;
	private Room m_room;
	private Corridor m_corridor;

	public DrawerEditionItem(Room room) {
		this.m_room = room;
		this.m_title = room.getTitle();
	}

	public DrawerEditionItem(String title, boolean isSuperTitle) {
		this.m_title = title;
		this.m_isSuperTitle = isSuperTitle;
	}

	public DrawerEditionItem(Corridor corridor, String title) {
		this.m_corridor = corridor;
		this.m_title = title;
	}

	public String getTitle() {
		return this.m_title;
	}

	public boolean isSuperTitle() {
		return m_isSuperTitle;
	}

	public Room getRoom() {
		return m_room;
	}

	public Corridor getCorridor() {
		return m_corridor;
	}
}
