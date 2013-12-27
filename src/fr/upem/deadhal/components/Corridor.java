package fr.upem.deadhal.components;

import android.os.Parcel;
import android.os.Parcelable;

public class Corridor implements Parcelable {

	public Corridor(Parcel source) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
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
