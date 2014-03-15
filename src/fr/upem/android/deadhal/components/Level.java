package fr.upem.android.deadhal.components;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class Level implements Parcelable {

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
	private String m_title = "";
	private ConcurrentHashMap<UUID, Room> m_rooms = new ConcurrentHashMap<UUID, Room>();
	private ConcurrentHashMap<UUID, Corridor> m_corridors = new ConcurrentHashMap<UUID, Corridor>();

	public Level() {

	}

	public Level(String title) {
		m_title = title;
	}

	@SuppressWarnings("unchecked")
	public Level(Parcel source) {
		m_title = source.readString();

		m_rooms = new ConcurrentHashMap<UUID, Room>();
		m_corridors = new ConcurrentHashMap<UUID, Corridor>();

		Bundle bundle = source.readBundle(ConcurrentHashMap.class
				.getClassLoader());
		try {
			m_rooms = (ConcurrentHashMap<UUID, Room>) bundle
					.getSerializable("rooms");
			m_corridors = (ConcurrentHashMap<UUID, Corridor>) bundle
					.getSerializable("corridors");
		} catch (Exception e) {
			// handle an exception thrown on samsung devices
			m_title = null;
		}

	}

	public int nbRooms() {
		return m_rooms.size();
	}

	public void addRoom(Room room) {
		m_rooms.put(room.getId(), room);
	}

	public void addCorridor(Corridor corridor) {
		m_corridors.put(corridor.getId(), corridor);
		Room roomSrc = m_rooms.get(corridor.getSrc());
		Room roomDst = m_rooms.get(corridor.getDst());
		if (corridor.isDirected()) {
			roomSrc.addNeighbor(corridor.getId(), roomDst.getId());
		} else {
			roomSrc.addNeighbor(corridor.getId(), roomDst.getId());
			roomDst.addNeighbor(corridor.getId(), roomSrc.getId());
		}
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
		Bundle bundle = new Bundle(ConcurrentHashMap.class.getClassLoader());
		bundle.putSerializable("rooms", m_rooms);
		bundle.putSerializable("corridors", m_corridors);
		dest.writeBundle(bundle);
	}

}
