package com.toocomplicated.mademesmile;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
/**
 * Created by User on 15/10/2558.
 */
public class Story implements Parcelable {
    private int storyId;
    private Location place = null;
    private String des;
    private String fbid;
    private int smile;
    private int sad;
    private String time;
    private int privacy;
    private User user;
    private ArrayList<Comment> commentList;
    private ArrayList<String> picList;
    private int isFeel;
    private int arrive = 0;
    public Story(String des,int privacy,String fbid,Location place) {
        this.des = des;
        this.privacy = privacy;
        this.fbid = fbid;
        this.user = null;
        this.place = place;
    }
    public Story(int storyId,String des,int smile, int sad
            ,String time,int privacy,String fbid,Location place, User user,ArrayList<Comment> commentList
    ,ArrayList<String> picList,int isFeel){
        this.storyId = storyId;
        this.des = des;
        this.smile = smile;
        this.sad = sad;
        this.time = time;
        this.privacy = privacy;
        this.fbid = fbid;
        this.place = place;
        this.user = user;
        this.commentList = commentList;
        this.picList = picList;
        this.isFeel = isFeel;
    }
    public int getStoryId()
    {
        return storyId;
    }
    public String getDes()
    {
        return des;
    }
    public void setDes(String des)
    {
        this.des = des;
    }
    public int getSmile(){
        return smile;
    }
    public String getFbid(){
        return fbid;
    }
    public void setSmile(int check) {
        smile = smile+check;
    }
    public int getSad(){ return sad;}
    public void setSad(int check) {
        sad = sad+check;
    }
    public String getTime() {return time;}
    public int getPrivacy(){return privacy;}
    public void setPrivacy(int privacy){this.privacy = privacy;}
    public Location getPlace()
    {
        return place;
    }
    public  User getUser(){
        return user;
    }
    public ArrayList<Comment> getCommentList()
    {
        return commentList;
    }
    public ArrayList<String> getPicList()
    {
        return picList;
    }
    public int getIsFeel(){
        return isFeel;
    }
    public void setIsFeel(int set){
        this.isFeel = set;
    }

    public Story(Parcel in){
        readFromParcel(in);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in){
        storyId = in.readInt();
        des = in.readString();
        smile = in.readInt();
        sad = in.readInt();
        time = in.readString();
        privacy = in.readInt();
        fbid = in.readString();
        place = in.readParcelable(Location.class.getClassLoader());
        user = (User) in.readValue(User.class.getClassLoader());
        commentList = in.readArrayList(Comment.class.getClassLoader());
        picList = in.readArrayList(java.lang.String.class.getClassLoader());
        isFeel = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.storyId);
        dest.writeString(this.des);
        dest.writeInt(this.smile);
        dest.writeInt(this.sad);
        dest.writeString(this.time);
        dest.writeInt(this.privacy);
        dest.writeString(this.fbid);
        dest.writeParcelable(this.place,flags);
        dest.writeValue(this.user);
        dest.writeList(this.commentList);
        dest.writeList(this.picList);
        dest.writeInt(this.isFeel);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        @Override
        public Story createFromParcel(Parcel source) {
            return new Story(source);
        }

        @Override
        public Story[] newArray(int size) {
            return new Story[size];
        }
    };
}
