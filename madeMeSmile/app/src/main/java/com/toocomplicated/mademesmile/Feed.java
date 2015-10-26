package com.toocomplicated.mademesmile;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Handler;

public class Feed extends ListActivity implements StoryAdapter.customButtonListener {
    private Button mButtonShare;
    private Button mButtonLogin;
    private Button mButtomSmile;
    private ShareHelper helper;
    private ArrayList<Story> styList2;
    private String test = "";
    private StoryAdapter adapter;
    private ListView listViews;
   // private ProfilePictureView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final HttpURLConnectionExample  h = new HttpURLConnectionExample();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    test = h.sendPost("feed", "fbid="+ Login.id);
                    // JSONObject jsonObject = new jsonObject("{"test)
                    //styList = getStoryList(test);
                    styList2 = getStoryList(test);
                    System.out.println("Check");
                    //showStory(styList2);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showStory(styList2);
                        }
                    });
                    System.out.println("Check2");
                    for (Story it : styList2) {
                        System.out.println(it.getDes());
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                }
            }
        }).start();



        //bindWidget();
        //setWidgetEventListener();
        //helper = new ShareHelper(this);
        /*try {
           Cursor cursor = getAllStory();
            showStory(cursor);
        }
        catch (Exception e){
        }
        finally {
            helper.close();
        }*/

            //handlePendingAction();
       /* final HttpURLConnectionExample  h = new HttpURLConnectionExample();
        new Thread( new Runnable() {
            @Override
            public void run() {
                try {

                    h.sendPost("feed", null);
                   // test =  h.callStr();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                }
               test =  h.callStr();
            }
        }).start();

      //  test = getAllStory();
        showStory(test);*/
        setContentView(R.layout.activity_feed);
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

    private void setWidgetEventListener() {

        mButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Share.class));
            }
        });
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }

    private void bindWidget() {

        mButtonShare = (Button)findViewById(R.id.button1_1);
        mButtonLogin = (Button)findViewById(R.id.button2_3);
    }

   // private static  int[] VIEWS = {R.id.rowid, R.id.time, R.id.content};
    private void showStory(ArrayList<Story> listValues){
        /*ArrayList<String> str = new ArrayList<>();
        int i = 0;
        while(i < listValues.size()) {
            str.add(listValues.get(i).getDes());
            System.out.println(str.get(i)+"str");
            i++;
        }*/
        System.out.println("Check3");
        adapter = new StoryAdapter(this,R.layout.listtest, listValues);
        //ArrayAdapter<String> myAdapter = new ArrayAdapter <String>(this,
         //       R.layout.listtest, R.id.listText, listValues);
        adapter.setCustomButtonListner(Feed.this);
        setListAdapter(adapter);
        //adapter.notifyDataSetChanged();
        //setListAdapter(adapter);

       // SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item, cursor, COLUMNS, VIEWS);
      //  setListAdapter(adapter);
        /*StringBuilder builder = new StringBuilder("Your story:\n\n");
        Profile profile = Profile.getCurrentProfile();

        while(cursor.moveToNext()){
            long id = cursor.getLong(0);
            long time = cursor.getLong(1);
            String content = cursor.getString(2);

            builder.append("Story ").append(id).append(": ");
            String strDate = (String) DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date(time));
            builder.append(strDate).append("\n");
            builder.append("\t").append(content).append("\n");
        }
        TextView mTextView = (TextView)findViewById(R.id.all_story2);
        mTextView.setText("Test");*/
       /* profilePicture = (ProfilePictureView) findViewById(R.id.profile_picturefeed);
        profilePicture.setProfileId(profile.getId());*/

    }

   /* @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        styList2.clear();
        final HttpURLConnectionExample  h = new HttpURLConnectionExample();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showStory(styList2);
                        }
                    });
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                }
            }
        }).start();
        adapter.notifyDataSetChanged();
        adapter.setNotifyOnChange(true);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        final HttpURLConnectionExample  h = new HttpURLConnectionExample();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    test = h.sendPost("feed", "d");
                    // JSONObject jsonObject = new jsonObject("{"test)
                    //styList = getStoryList(test);
                    styList2 = getStoryList(test);
                    //showStory(styList2);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showStory(styList2);
                        }
                    });
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                }
            }
        }).start();
        setContentView(R.layout.activity_feed);
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
    }


    @Override
    public void onButtonClickListener(int position, final Story value) {
        final HttpURLConnectionExample  h = new HttpURLConnectionExample();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    test = h.sendPost("feed", "fbid="+ Login.id);
                    styList2 = getStoryList(test);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showStory(styList2);
                        }
                    });

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                }
            }
        }).start();
        System.out.println("On click");
    }
}
