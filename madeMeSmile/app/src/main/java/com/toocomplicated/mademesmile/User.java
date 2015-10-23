package com.toocomplicated.mademesmile;
/**
 * Created by User on 15/10/2558.
 */
public class User {
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

}
