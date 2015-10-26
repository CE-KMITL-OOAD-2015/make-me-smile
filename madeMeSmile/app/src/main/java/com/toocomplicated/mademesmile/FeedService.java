package com.toocomplicated.mademesmile;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.Provider;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Win8.1 on 24/10/2558.
 */
public class FeedService extends Service {
    private static final long UPDATE_INTERVAL = 1000;
    private final IBinder mBinder = new MyBinder();
    private Timer timer = new Timer();
    private ArrayList<Story> list = new ArrayList<>();
    private String test = "";
    private int i = 0;

    @Override
    public void onCreate() {
        pollForUpdates();
    }

    private void pollForUpdates() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                final HttpURLConnectionExample h = new HttpURLConnectionExample();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            list.clear();
                            test = h.sendPost("feed", "d");
                            list = getStoryList(test);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                }).start();
            }
        }, 0, UPDATE_INTERVAL);
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;  // ออบเจ็ค MyBinder
    }

    /* คลาส MyBinder สืบทอดจากคลาส Binder และมีเมธอด getService ที่จะส่งคืนตัวเซอร์วิซ
     * (ออบเจ็ค MyService) ออกไปเพื่อให้ client อ้างอิงถึงเซอร์วิซได้ */
    public class MyBinder extends Binder {
        FeedService getService() {
            return FeedService.this;
        }
    }

    // เมธอดที่เตรียมไว้ให้ client มาเรียกใช้ความสามารถของเซอร์วิซ
    public ArrayList<Story> getWordList() {
        return list;
    }
    public ArrayList<Story> getStoryList(String response) {
        ArrayList<Story> styList = new ArrayList<Story>();
        try {
            JSONArray jarr = new JSONArray();
            JSONObject jObject = new JSONObject("{" + "\"myArray\": " + response + "}");
            System.out.println(response);
            JSONArray jArray = jObject.getJSONArray("myArray");
            int i = 0;
            while (i < jArray.length()) {
                JSONObject job = jArray.getJSONObject(i);
                JSONObject job2 = new JSONObject(job.get("place").toString());
                JSONObject job3 = new JSONObject(job.get("user").toString());
                Location loc = new Location(job2.getInt("id"), job2.getString("name"), job2.get("address").toString());
                User user = new User(job3.getString("fbid"), job3.getString("name"), job3.getInt("isPost"));
                Story story = new Story(job.getInt("storyId")
                        , job.getString("des")
                        , job.getInt("smile")
                        , job.getInt("sad")
                        , job.getString("time")
                        , job.getInt("privacy")
                        , job.getString("fbid")
                        , loc
                        , user
                );
                styList.add(story);
                i++;

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return styList;
    }


}



