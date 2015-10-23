package com.toocomplicated.mademesmile;
/**
 * Created by User on 15/10/2558.
 */
public class User {
    private int id;
    private String name;
    private int isPost;
    public User(int id, String name)
    {
        this.id = id;
        this.name = name;
        this.isPost = 0;
    }
    public User(int id,String name,int isPost)
    {
        this.id = id;
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
    public int getId()
    {
        return id;
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
