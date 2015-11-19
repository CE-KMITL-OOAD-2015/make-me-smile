package com.toocomplicated.mademesmile;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.internal.Utility;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private static final String PERMISSION = "publish_actions";

    CallbackManager callbackManager;
    ProfileTracker profileTracker;
    public static final String PREFS_NAME = "MyPrefsFile";
    private TextView userName;
    private ProfilePictureView profilePicture;
    private int data = 0;
    public static boolean login = false;
    public static String name;
    public static String id;
    public static User user;
    private final String USER_AGENT = "Mozilla/5.0";
   // private Button postLinkButton;
  //  private Button postPictureButton;

   /* private enum PendingAction {
        NONE,
        POST_LINK,
        POST_PICTURE
    }*/
   /* private PendingAction pendingAction = PendingAction.NONE;

    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException error) {
            String title = "เกิดข้อผิดพลาดในการโพสต์";
            String msg = error.getMessage();
            showResult(title, msg);
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            if (result.getPostId() != null) {
                String title = "โพสต์ลง Facebook สำเร็จ";
                String id = result.getPostId();
                String msg = String.format("Post ID: %s", id);
                showResult(title, msg);
            }
        }

        private void showResult(String title, String alertMessage) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(title)
                    .setMessage(alertMessage)
                    .setPositiveButton("OK", null)
                    .show();
        }
    };*/


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

       /* SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();*/
        System.out.println("Create");
            FacebookSdk.sdkInitialize(this.getApplicationContext());
        if(AccessToken.getCurrentAccessToken() == null) {
            LoginManager.getInstance().logOut();
        }
        else
        {
            startActivity(new Intent(getApplicationContext(), FeedTabList.class));
            finish();
        }
            callbackManager = CallbackManager.Factory.create();

            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            //handlePendingAction();
                            updateUI();
                            //editor.putString("nameKey",name);
                            final HttpURLConnectionExample h = new HttpURLConnectionExample();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        JSONObject jUser  =  new JSONObject(h.sendPost("user", "fbid=" + id + "&name=" + name));
                                        user = new User(jUser.getString("fbid"),jUser.getString("name"),jUser.getInt("isPost"));
                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                    }
                                }
                            }).start();
                            SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0); // 0 - for private mode
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean("hasLoggedIn", true);
                            editor.commit();
                            startActivity(new Intent(getApplicationContext(), FeedTabList.class));
                            finish();
                        }

                        @Override
                        public void onCancel() {
                            updateUI();
                        }

                        @Override
                        public void onError(FacebookException e) {
                            updateUI();
                            Toast.makeText(getApplicationContext(), "Error! Login fail.Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });

            setContentView(R.layout.activity_login);

            userName = (TextView) findViewById(R.id.user_name);
            profilePicture = (ProfilePictureView) findViewById(R.id.profile_picture);
            // postLinkButton = (Button) findViewById(R.id.post_link_button);
            //postPictureButton = (Button) findViewById(R.id.post_picture_button);




        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile profile1) {
                updateUI();
            }
        };

       /* postLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performPublish(PendingAction.POST_LINK);
            }
        });

        postPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickPictureDialog();
            }
        });*/
    }

    private void updateUI()  {
        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
        Profile profile = Profile.getCurrentProfile();

        if (loggedIn && (profile != null)) {
            profilePicture.setProfileId(profile.getId());
            userName.setText(profile.getName());
            name = profile.getName();
            id = profile.getId();





           // postLinkButton.setEnabled(true);
          //  postPictureButton.setEnabled(true);
        } else {
            profilePicture.setProfileId(null);
            userName.setText(null);
           // postLinkButton.setEnabled(false);
          //  postPictureButton.setEnabled(false);
        }
    }

    public String getID() {
        return id;
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Resume");
        updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
}
