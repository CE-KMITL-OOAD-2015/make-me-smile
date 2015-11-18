package com.toocomplicated.mademesmile;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 22/10/2558.
 */
public class Comment implements Parcelable {
    private int commentId;
    private String detail;
    private User user;
    private String time;

    public Comment(String detail) {
        this.detail = detail;
        this.time = null;
        this.user = null;
        this.commentId = 0;
    }

    public Comment(int commentId,String detail, String time, User user) {
        this.detail = detail;
        this.time = time;
        this.user = user;
        this.commentId = commentId;
    }

    public String getDetail() {
        return detail;
    }
    public User getUser(){
        return user;
    }

    public String getTime() {
        return time;
    }
    public int getCommentId()
    {
        return commentId;
    }

    public Comment(Parcel in){
        readFromParcel(in);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {
        detail = in.readString();
        user = (User) in.readValue(User.class.getClassLoader());
        time = in.readString();
        commentId = in.readInt();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.detail);
        dest.writeValue(this.user);
        dest.writeString(this.time);
        dest.writeInt(this.commentId);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}