package com.toocomplicated.mademesmile;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.ProfilePictureView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.Date;

import static com.toocomplicated.mademesmile.Constant.CONTENT;
import static com.toocomplicated.mademesmile.Constant.TABLE_NAME;
import static com.toocomplicated.mademesmile.Constant.TIME;
import static com.toocomplicated.mademesmile.Constant._ID;

public class Share extends AppCompatActivity {
    public String sq = null;
    private ShareHelper helper;
    private ProfilePictureView profilePicture;
    private Story story;
    private String fbid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        helper = new ShareHelper(this);
        fbid = Login.id;
        /*try {
           Cursor cursor = getAllStory();
            showStory(cursor);
        }
        catch (Exception e){
        }
        finally {
            helper.close();
        }*/

        Button mButtonCancle = (Button)findViewById(R.id.buttoncancle); // cancle button
        mButtonCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Feed.class));
                finish();
            }
        });
        final EditText mEditText = (EditText)findViewById(R.id.edittext);
        Button mButtonShare = (Button) findViewById(R.id.buttonshare); // share button
        mButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addStory(mEditText.getText().toString());
                    sendStory();
                    /*Cursor cursor = getAllStory();
                    showStory(cursor);*/
                    mEditText.setText(null);
                } catch (Exception e) {
                } finally {
                    helper.close();
                    startActivity(new Intent(getApplicationContext(), Feed.class));
                    finish();
                }
            }
        });

        profilePicture = (ProfilePictureView)findViewById(R.id.profile_picturetest);
        profilePicture.setProfileId("1021181027942407");
    }




    private void addStory(String str) {
        //  HttpURLConnectionExample http = new HttpURLConnectionExample();
        sq = "privacy=0&des="+ str +"&fbid="+ fbid +"&locationId=1&locationName=ECC&address=LADKRABANG";
    }

    private void sendStory() {
        final   HttpURLConnectionExample  h2 = new HttpURLConnectionExample();
        new Thread( new Runnable() {
            @Override
            public void run() {
                try {
                    h2.sendPost("writeStory",sq);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                }
            }
        }).start();
    }





        /*SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIME, System.currentTimeMillis());
        values.put(CONTENT, str);
        db.insertOrThrow(TABLE_NAME, null, values);*/


    private static String[] COLUMNS = {_ID, TIME, CONTENT };
    private static String ORDER_BY = TIME + " DESC";

    private Cursor getAllStory(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, COLUMNS, null, null, null, null, ORDER_BY);
        return cursor;
    }

    private void showStory(Cursor cursor){
        StringBuilder builder = new StringBuilder("Your story:\n\n");

        while(cursor.moveToNext()){
            long id = cursor.getLong(0);
            long time = cursor.getLong(1);
            String content = cursor.getString(2);

            builder.append("Story ").append(id).append(": ");
            String strDate = (String) DateFormat.format("yyyy-MM-dd hh:mm:ss" , new Date(time));
            builder.append(strDate).append("\n");
            builder.append("\t").append(content).append("\n");
        }
       // TextView mTextView = (TextView)findViewById(R.id.all_story);
      //  mTextView.setText(builder);
    }

   /* private void bindWidget() {
        mButtonCancle = (Button)findViewById(R.id.buttoncancle);
        mButtonCancle.setOnClickListener(this);
        mButtonShare = (Button)findViewById(R.id.buttonshare);
        mButtonShare.setOnClickListener(this);
        mTextView = (TextView)findViewById(R.id.all_story);
        mEditText = (EditText)findViewById(R.id.edittext);
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share, menu);
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
}
