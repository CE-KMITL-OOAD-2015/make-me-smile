package com.toocomplicated.mademesmile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.toocomplicated.mademesmile.RecyclerAdapter.*;

/**
 * Created by Win8.1 on 26/10/2558.
 */
public class FeedList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,ClickListener {
    private List<Story> feedList;
    private Story story;
    private RecyclerView mRecycler;
    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerAdapter adapter;
    private LinearLayout linearLayout;
    private Button mButtonShare;
    private Button mButtonLogin;
    private Button mButtonMp;
    private Button mButtonNf;
    private Button mButtonSe;
    private ImageButton mButtonPicSe;
    private EditText txtSearch;
    private ProgressBar bar;
    private String test = "";
    private int page = 0;
    private String mode;
    private String param;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        mRecycler = (RecyclerView)findViewById(R.id.recycler_view);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecycler, new ClickListener() {
            @Override
            public void OnClick(View view, int position) {

            }

            @Override
            public void OnLongClick(View view, final int position) {



            }
        }));
        linearLayout = (LinearLayout)findViewById(R.id.linear_search);
        mButtonPicSe = (ImageButton)findViewById(R.id.search_img_buttonfeed);
        txtSearch = (EditText)findViewById(R.id.txtsearchfeed);
        mSwipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        mSwipeRefresh.setOnRefreshListener(this);
        bar = (ProgressBar)findViewById(R.id.progress_bar);
        bar.setVisibility(View.VISIBLE);
        mButtonShare = (Button)findViewById(R.id.button1_1);
        mButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Share.class);
                startActivity(intent);
                finish();
            }
        });
        mButtonLogin = (Button)findViewById(R.id.button2_3);
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
        mButtonMp = (Button)findViewById(R.id.button2_1);
        mButtonMp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.GONE);
                mode = "orderByPopularity";
                param = "fbid="+Login.id;

                new AsyncHttpTask().execute(mode, param);
            }
        });
        mButtonNf = (Button)findViewById(R.id.button2_2);
        mButtonNf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.GONE);
                mode = "feed";
                param = "fbid="+Login.id;
                new AsyncHttpTask().execute(mode,param);
            }
        });
        mButtonSe = (Button)findViewById(R.id.button1_2);
        mButtonSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
            }
        });
        mButtonPicSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String des = txtSearch.getText().toString();
                if(des.equals("")){
                    Toast.makeText(FeedList.this, "Please put some text to search location" , Toast.LENGTH_SHORT).show();
                }
                else
                    mode = "searchStoryByLocationName";
                param = "locationName="+des;
                new AsyncHttpTask().execute(mode,param);
            }
        });
        mode = "feed";
        param = "fbid="+Login.id;
        new AsyncHttpTask().execute(mode,param);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(FeedList.this, "Refresh Feed...", Toast.LENGTH_SHORT).show();
        new AsyncHttpTask().execute(mode, param);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(FeedList.this, "Refresh Feed...", Toast.LENGTH_SHORT).show();
        //String mode = "feed";
        // String param = "fbid="+Login.id;
        new AsyncHttpTask().execute(mode,param);
    }


    @Override
    public void itemClick(View view, int position) {

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
                test = h.sendPost(params[0], params[1]);
                System.out.println("Story " + test);
                feedList = getStoryList(test);

            } catch (Exception e) {
                // TODO Auto-generated catch block
            }

            System.out.println("result " + result);
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
            //JSONArray jsonArray2 = new JSONArray(response);

            //Log.e("TEST", jsonArray2.getString(0));

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

    public ArrayList<String> stringToArray(String st) {
        if(!st.equals("[]")) {
            String replace = st.replace("[", "");
            String replace1 = replace.replace("]", "");
            ArrayList<String> stList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
            int i = 0;
            for (String it : stList) {
                stList.set(i, it.replace(" ", ""));
                i++;
            }
            return stList;
        }
        else {
            System.out.println("NO PIC");
            return new ArrayList<String>();
        }
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private ClickListener clickListener;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener){
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {

                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
                    if(child != null && clickListener != null){
                        clickListener.OnLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(),e.getY());
            if(child != null && clickListener != null && gestureDetector.onTouchEvent(e) ){
                clickListener.OnClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static interface ClickListener{
        public void OnClick(View view,int position);
        public void OnLongClick(View view,int position);
    }

    public class SettingAlert extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Setting");
            builder.setItems(R.array.setting, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(FeedList.this, "Selected" + which, Toast.LENGTH_SHORT).show();
                    if(which == 1){

                    }

                }
            });
            Dialog dialog = builder.create();
            return  dialog;
        }
    }

   /* class PagerAdapter extends FragmentPagerAdapter{
        int icons[] = {R.drawable.share_icon,R.drawable.feed_icon,R.drawable.star_icon};
        String[] tabText = getResources().getStringArray(R.array.tabs);

        public PagerAdapter(android.support.v4.app.FragmentManager fm){
            super(fm);
            tabText = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }*/
}
