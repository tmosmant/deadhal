package fr.upem.deadhal.components;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Canvas;

public class Level {

	private String title;
	private List<Room> rooms = new LinkedList<Room>();

	public Level(String title) {
		this.title = title;
	}

	public boolean addRoom(Room room) {
		for (Room r : rooms) {
			if (r.getRect().intersect(room.getRect())) {
				return false;
			}
		}
		rooms.add(room);

		return true;
	}

	public String getTitle() {
		return title;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void draw(Canvas canvas) {
		for (Room room : rooms) {
			room.draw(canvas);
		}
	}

}
