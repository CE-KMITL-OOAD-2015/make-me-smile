package com.toocomplicated.mademesmile;
import java.util.ArrayList;
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
    private ArrayList<Comment> commentList;
    private ArrayList<String> picList;
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
    ,ArrayList<String> picList
                 ){
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
}
