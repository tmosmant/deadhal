package fr.upem.deadhal.components;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.os.Parcel;
import android.os.Parcelable;

public class Level implements Parcelable {

	private String title;
	private Map<UUID, Room> rooms = new HashMap<UUID, Room>();

	public Level(String title) {
		this.title = title;
	}

	public Level(Parcel source) {
		this.title = source.readString();
		source.readMap(rooms, HashMap.class.getClass().getClassLoader());
		// source.readList(rooms, (Object.class.getClassLoader()));
	}
	public boolean addRoom(Room room) {

		for (Room r : rooms.values()) {
			if (r.getRect().intersect(room.getRect())) {
				return false;
			}
		}
		rooms.put(room.getId(), room);

		return true;
	}

	public String getTitle() {
		return title;
	}

	public Map<UUID, Room> getRooms() {
		return rooms;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeMap(rooms);
		// dest.writeList(rooms);
	}

	public static final Parcelable.Creator<Level> CREATOR = new Parcelable.Creator<Level>() {
		@Override
		public Level createFromParcel(Parcel source) {
			return new Level(source);
		}

		@Override
		public Level[] newArray(int size) {
			return new Level[size];
		}
	};

}
