package com.toocomplicated.mademesmile;
import java.util.Date;
/**
 * Created by User on 15/10/2558.
 */
public class Story {
    private int storyId;
    private Location place = null;
    private String des;
    private String fbid;
    private int smile;
    private int sad;
    private String time;
    private int privacy;
    private User user;
    public Story(String des,int privacy,String fbid,Location place) {
        this.des = des;
        this.privacy = privacy;
        this.fbid = fbid;
        this.place = place;
    }
    public Story(int storyId,String des,int smile, int sad
            ,String time,int privacy,String fbid,Location place, User user){
        this.storyId = storyId;
        this.des = des;
        this.smile = smile;
        this.sad = sad;
        this.time = time;
        this.privacy = privacy;
        this.fbid = fbid;
        this.place = place;
        this.user = user;
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
    public void setSmile() { smile++;}
    public int getSad(){ return sad;}
    public void setSad() { sad++;}
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
}