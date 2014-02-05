package fr.upem.deadhal.components;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.os.Parcel;
import android.os.Parcelable;

public class Level implements Parcelable {

	private String m_title = new String();
	private Map<UUID, Room> m_rooms = new HashMap<UUID, Room>();
	private Map<UUID, Corridor> m_corridors = new HashMap<UUID, Corridor>();

	public Level() {
	}

	public Level(String title) {
		this.m_title = title;
	}

	public Level(Parcel source) {
		this.m_title = source.readString();
		source.readMap(m_rooms, HashMap.class.getClass().getClassLoader());
		source.readMap(m_corridors, HashMap.class.getClass().getClassLoader());
	}

	public void addRoom(Room room) {
		m_rooms.put(room.getId(), room);
	}

	public void addCorridor(Corridor corridor) {
		m_corridors.put(corridor.getId(), corridor);
	}

	public void removeRoom(Room room) {
		m_rooms.remove(room.getId());
	}

	public void removeCorridor(Corridor corridor) {
		m_corridors.remove(corridor.getId());
	}

	public String getTitle() {
		return m_title;
	}

	public void setTitle(String title) {
		this.m_title = title;
	}

	public Map<UUID, Room> getRooms() {
		return m_rooms;
	}

	public Map<UUID, Corridor> getCorridors() {
		return m_corridors;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(m_title);
		dest.writeMap(m_rooms);
		dest.writeMap(m_corridors);
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
