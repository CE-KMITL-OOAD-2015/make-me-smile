package com.toocomplicated.mademesmile;

import android.media.Image;

/**
 * Created by Win8.1 on 19/11/2558.
 */
public class Advertisement {
    private String pic;
    private String url;

    public Advertisement(String pic,String url){
        this.pic = pic;
        this.url = url;
    }

    public String getPic(){
        return this.pic;
    }

    public String getUrl(){
        return this.url;
    }
}
