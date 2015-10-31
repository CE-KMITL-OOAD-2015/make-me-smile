package com.toocomplicated.mademesmile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Win8.1 on 26/10/2558.
 */
public class FeedList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private List<Story> feedList;
    private RecyclerView mRecycler;
    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerAdapter adapter;
    private Button mButtonShare;
    private Button mButtonLogin;
    private ProgressBar bar;
    private String test = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        mRecycler = (RecyclerView)findViewById(R.id.recycler_view);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mSwipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        mSwipeRefresh.setOnRefreshListener(this);
        bar = (ProgressBar)findViewById(R.id.progress_bar);
        bar.setVisibility(View.VISIBLE);
        mButtonShare = (Button)findViewById(R.id.button1_1);
        mButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Share.class));
                finish();
            }
        });
        mButtonLogin = (Button)findViewById(R.id.button2_3);
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
        new AsyncHttpTask().execute();
    }

    @Override
    public void onRefresh() {
        Toast.makeText(FeedList.this, "Refresh Feed...", Toast.LENGTH_SHORT).show();
        new AsyncHttpTask().execute();
    }

    public class AsyncHttpTask extends AsyncTask<String,Void,Integer>{
        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 1;
            final HttpURLConnectionExample  h = new HttpURLConnectionExample();
                    try {
                        test = h.sendPost("feed", "fbid="+ Login.id);
                        feedList = getStoryList(test);
                        System.out.println(feedList);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                    }

            return result;

        }

        @Override
        protected void onPostExecute(Integer integer) {
            bar.setVisibility(View.GONE);
            if(integer == 1){
                adapter = new RecyclerAdapter(FeedList.this, feedList);
                if(mSwipeRefresh.isRefreshing()){
                    mSwipeRefresh.setRefreshing(false);
                }
                mRecycler.setAdapter(adapter);
                System.out.println("Test");
            }
            else{
                Toast.makeText(FeedList.this, "Failed to fetch data!", Toast.LENGTH_LONG).show();
            }
        }
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
