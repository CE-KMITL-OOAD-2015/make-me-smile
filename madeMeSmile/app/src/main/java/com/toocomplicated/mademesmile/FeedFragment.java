package com.toocomplicated.mademesmile;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.*;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Win8.1 on 15/11/2558.
 */
public class FeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecycler;
    public static RecyclerAdapter adapter;
    private EditText mTextSearch;
    private ImageButton mSearchButton;
    private ProgressBar bar;
    private SwipeRefreshLayout mSwipeRefresh;
    private String test = "";
    private List<Story> feedList;
    private String mode;
    private String param;
    ShareDialog shareDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout =  inflater.inflate(R.layout.feedfragment,container,false);
        mRecycler = (RecyclerView) layout.findViewById(R.id.recycler_viewflag);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefresh = (SwipeRefreshLayout) layout.findViewById(R.id.swiperefreshfrag);
        mSwipeRefresh.setOnRefreshListener(this);
        mTextSearch = (EditText) layout.findViewById(R.id.searchloc);
        mSearchButton = (ImageButton) layout.findViewById(R.id.buttonsearch);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String des = mTextSearch.getText().toString();
                if(des.equals("")){
                    mode = "feed";
                    param = "fbid="+Login.id;
                    Toast.makeText(getActivity(), "Please put some text to search location" , Toast.LENGTH_SHORT).show();
                }
                else {
                    mode = "searchStoryByLocationName";
                    param = "fbid=" + Login.id + "&locationName=" + des;
                    hiddenKeyboard(getView());
                    new AsyncHttpTask().execute(mode, param);
                }
            }
        });

        mode = "feed";
        param = "fbid="+Login.id;
        new AsyncHttpTask().execute(mode,param);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hiddenKeyboard(getView());
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity(), "Refresh Feed...", Toast.LENGTH_SHORT).show();
        hiddenKeyboard(getView());
        new AsyncHttpTask().execute(mode, param);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getActivity(), "Refresh Feed...", Toast.LENGTH_SHORT).show();
        new AsyncHttpTask().execute(mode, param);

    }

    public class AsyncHttpTask extends AsyncTask<String,Void,Integer> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 1;
            final HttpURLConnectionExample  h = new HttpURLConnectionExample();

            try {
                test = h.sendPost(params[0], params[1]);
                System.out.println("Story " + test);
                feedList = getStoryList(test);
                // System.out.println(feedList);
            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
            System.out.println("result " + result);
            return result;

        }

        @Override
        protected void onPostExecute(Integer integer) {
            if(integer == 1){
                adapter = new RecyclerAdapter(getActivity(), feedList);
                if(mSwipeRefresh.isRefreshing()){
                    mSwipeRefresh.setRefreshing(false);
                }
                mRecycler.setAdapter(adapter);
                System.out.println("Test");
            }
            else{
                Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void hiddenKeyboard(View v) {
        InputMethodManager keyboard = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
    public ArrayList<Story> getStoryList(String response) {
        ArrayList<Story> styList = new ArrayList<Story>();
        try {
            //JSONArray jsonArray2 = new JSONArray(response);


            //JSONObject jObject = new JSONObject("{" + "\"myArray\": " + response + "}");
            //System.out.println(response);
            JSONArray jArray = new JSONArray(response);
            int i = 0;
            while (i < jArray.length()) {
                JSONObject job = jArray.getJSONObject(i);
                JSONObject job2 = new JSONObject(job.get("place").toString());
                JSONObject job3 = new JSONObject(job.get("user").toString());
                Location loc = new Location(job2.getInt("id"), job2.getString("name"), job2.get("address").toString());
                User user = new User(job3.getString("fbid"), job3.getString("name"), job3.getInt("isPost"));
                //JSONObject job4 = new JSONObject("{" + "\"myArray\": " + job.getString("commentList") + "}");
                JSONArray jArray2 = job.getJSONArray("commentList");
                ArrayList<Comment> commentList = new ArrayList<>();
                JSONArray ja = job.getJSONArray("picList");
                ArrayList<String> picList = new ArrayList<>();
                for(int m = 0; m < ja.length(); m++){
                    picList.add(ja.getString(m));
                }

                int j = 0;
                while(j<jArray2.length())
                {
                    JSONObject job5 = jArray2.getJSONObject(j);
                    // System.out.println(job5.toString());
                    JSONObject job6 = new JSONObject(job5.get("user").toString());
                    User usr = new User(job6.getString("fbid"),job6.getString("name"),job6.getInt("isPost"));
                    commentList.add(new Comment(job5.getInt("commentId"),job5.getString("detail"),job5.getString("time"),usr));
                    j++;
                }
                Story story = new Story(job.getInt("storyId")
                        , job.getString("des")
                        , job.getInt("smile")
                        , job.getInt("sad")
                        , job.getString("time")
                        , job.getInt("privacy")
                        , job.getString("fbid")
                        , loc
                        , user
                        , commentList
                        , picList
                        , job.getInt("isFeel")
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
