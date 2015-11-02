package com.toocomplicated.mademesmile;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Location implements Parcelable{
    private int id;
    private String name;
    private String address;
    public Location(int id, String name, String address)
    {
        this.id = id;
        this.name = name;
        this.address = address;
    }
    public int getId()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }
    public String getAddress() { return address;}

    public Location(Parcel in){
            readFromParcel(in);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        @Override
        public Object createFromParcel(Parcel source) {
            return new Location(source);
        }

        public Location[] newArray(int size){
            return new Location[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getName());
        dest.writeString(getAddress());
    }

    private void readFromParcel(Parcel in ) {

        id = in .readInt();
        name  = in .readString();
        address  = in .readString();
    }
}
