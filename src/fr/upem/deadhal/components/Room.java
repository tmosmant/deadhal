package fr.upem.deadhal.components;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.graphics.RectF;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;

public class Room implements Parcelable {

	private UUID id;
	private String title;
	private RectF rect;
	private Map<UUID, UUID> neighbors = new HashMap<UUID, UUID>();

	public Room(UUID id, String title, RectF rect) {
		this.id = id;
		this.title = title;
		this.rect = rect;
	}

	public Room(Parcel source) {
		ParcelUuid id = source
				.readParcelable(ParcelUuid.class.getClassLoader());
		this.id = id.getUuid();
		this.title = source.readString();
		float left = source.readFloat();
		float right = source.readFloat();
		float top = source.readFloat();
		float bottom = source.readFloat();
		rect = new RectF(left, top, right, bottom);
		source.readMap(neighbors, HashMap.class.getClass().getClassLoader());
	}

	public String getTitle() {
		return title;
	}

	public RectF getRect() {
		return rect;
	}

	public UUID getId() {
		return id;
	}

	public Map<UUID, UUID> getNeighbors() {
		return neighbors;
	}

	public void addNeighbor(UUID corridor, UUID room) {
		neighbors.put(corridor, room);
	}

	public boolean isAccessible(Room dst) {
		return neighbors.containsValue(dst);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		ParcelUuid parcelUuid = new ParcelUuid(id);
		parcelUuid.writeToParcel(dest, flags);
		dest.writeString(title);
		dest.writeFloat(rect.left);
		dest.writeFloat(rect.right);
		dest.writeFloat(rect.top);
		dest.writeFloat(rect.bottom);
		dest.writeMap(neighbors);
	}

	public static final Parcelable.Creator<Room> CREATOR = new Parcelable.Creator<Room>() {
		@Override
		public Room createFromParcel(Parcel source) {
			return new Room(source);
		}

		@Override
		public Room[] newArray(int size) {
			return new Room[size];
		}
	};

}
