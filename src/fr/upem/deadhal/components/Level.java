package fr.upem.deadhal.components;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.widget.Toast;

public class Level extends View {

	private String title;
	private List<Room> rooms = new LinkedList<Room>();

	public Level(Context context) {
		super(context);
	}

	public Level(Context context, String title) {
		super(context);
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

	public Room selectRoom(float x, float y) {
		for (Room room : rooms) {
			room.setSelected(false);
		}
		for (Room room : rooms) {
			if (room.getRect().contains((int) x, (int) y)) {
				Toast.makeText(getContext(), room.getTitle() + " selected.",
						Toast.LENGTH_SHORT).show();
				return room;
			}
		}
		return null;
	}

}
