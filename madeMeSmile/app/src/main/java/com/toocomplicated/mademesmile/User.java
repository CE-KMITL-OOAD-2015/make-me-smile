package com.toocomplicated.mademesmile;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 15/10/2558.
 */
public class User implements Parcelable {
    private String fbid;
    private String name;
    private int isPost;
    public User(String fbid, String name)
    {
        this.fbid = fbid;
        this.name = name;
        this.isPost = 0;
    }
    public User(String fbid,String name,int isPost)
    {
        this.fbid = fbid;
        this.name = name;
        this.isPost = isPost;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getId()
    {
        return fbid;
    }
    public int getIsPost()
    {
        return isPost;
    }
    public void setPost()
    {
        isPost = 1;
    }
    public void notPost() { isPost = 0;}

    public User(Parcel in){
        readFromParcel(in);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        @Override
        public Object createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size){
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fbid);
        dest.writeString(this.name);
        dest.writeInt(this.isPost);
    }

    private void readFromParcel(Parcel in ) {

        fbid = in .readString();
        name  = in .readString();
        isPost  = in .readInt();
    }
}
