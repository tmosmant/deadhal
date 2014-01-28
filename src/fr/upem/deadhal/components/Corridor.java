package fr.upem.deadhal.components;

import java.util.UUID;

import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;

public class Corridor implements Parcelable {

	private UUID id;
	private UUID src;
	private UUID dst;
	private boolean directed;

	public Corridor(UUID id, UUID src, UUID dst, boolean directed) {
		this.id = id;
		this.src = src;
		this.dst = dst;
		this.directed = directed;
	}

	public Corridor(Parcel source) {
		ParcelUuid src = source.readParcelable(ParcelUuid.class
				.getClassLoader());
		this.src = src.getUuid();

		ParcelUuid dst = source.readParcelable(ParcelUuid.class
				.getClassLoader());
		this.dst = dst.getUuid();

		boolean[] val = new boolean[1];
		source.readBooleanArray(val);
		directed = val[0];
	}

	public UUID getId() {
		return id;
	}

	public UUID getSrc() {
		return src;
	}

	public void setSrc(UUID src) {
		this.src = src;
	}

	public UUID getDst() {
		return dst;
	}

	public void setDst(UUID dst) {
		this.dst = dst;
	}

	public boolean isDirected() {
		return directed;
	}

	public void setDirected(boolean directed) {
		this.directed = directed;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		ParcelUuid srcParcelUuid = new ParcelUuid(src);
		srcParcelUuid.writeToParcel(dest, flags);
		ParcelUuid dstParcelUuid = new ParcelUuid(src);
		dstParcelUuid.writeToParcel(dest, flags);
		dest.writeBooleanArray(new boolean[] { directed });
	}

	public static final Parcelable.Creator<Corridor> CREATOR = new Parcelable.Creator<Corridor>() {
		@Override
		public Corridor createFromParcel(Parcel source) {
			return new Corridor(source);
		}

		@Override
		public Corridor[] newArray(int size) {
			return new Corridor[size];
		}
	};

}
