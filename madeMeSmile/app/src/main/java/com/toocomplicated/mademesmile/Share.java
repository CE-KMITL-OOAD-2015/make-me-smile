package com.toocomplicated.mademesmile;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private Location location;
    private TextView mLocationView;
    private static final int SELECTED_PICTURE = 1;
    private ImageView mImage;
    private Button mButtonPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        helper = new ShareHelper(this);
        location = new Location(0,"","");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
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
                startActivity(new Intent(getApplicationContext(),FeedList.class));
                finish();
            }
        });
        Button mButtonLocation = (Button)findViewById(R.id.buttontag);
        mButtonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LocationList.class));
            }
        });
        mLocationView = (TextView)findViewById(R.id.locationview);
        mLocationView.setText("Location : "+location.getName());
        mImage = (ImageView)findViewById(R.id.pictureview);
        final EditText mEditText = (EditText)findViewById(R.id.edittext);
        mButtonPhoto = (Button)findViewById(R.id.buttonphoto);
        mButtonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECTED_PICTURE);
            }
        });
        Button mButtonShare = (Button) findViewById(R.id.buttonshare); // share button
        mButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location.getName().equals("")){
                    Toast.makeText(Share.this, "You must tag location!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LocationList.class));
                }
                else {
                    try {
                        addStory(mEditText.getText().toString());
                        sendStory();
                    /*Cursor cursor = getAllStory();
                    showStory(cursor);*/
                        mEditText.setText(null);
                    } catch (Exception e) {
                    } finally {
                        startActivity(new Intent(getApplicationContext(), FeedList.class));
                        finish();
                    }
                }
            }
        });

        profilePicture = (ProfilePictureView)findViewById(R.id.profile_picturetest);
        profilePicture.setProfileId("1021181027942407");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECTED_PICTURE && resultCode == RESULT_OK && data != null){
            Uri pickedImage = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();

            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            mImage.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        }
    }

    private void addStory(String str) {
        //  HttpURLConnectionExample httpyk = new HttpURLConnectionExample();
        sq = "privacy=0&des="+ str +"&fbid="+ fbid +"&locationId="+ location.getId() +"&locationName="+
                location.getName() +"&address="+ location.getAddress() +"&img=1234";
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

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences sharePref = PreferenceManager.getDefaultSharedPreferences(this);
        int id = sharePref.getInt("LocationId", 0);
        String name = sharePref.getString("LocationName", "No location");
        String address = sharePref.getString("LocationAddress","No address");
        location = new Location(id,name,address);
        mLocationView.setText("Location : "+location.getName());
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
