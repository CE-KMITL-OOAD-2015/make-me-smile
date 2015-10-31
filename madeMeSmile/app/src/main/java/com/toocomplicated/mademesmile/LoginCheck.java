package com.toocomplicated.mademesmile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

/**
 * Created by Win8.1 on 30/10/2558.
 */
public class LoginCheck extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME, 0);
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);
        // data = getIntent().getExtras().getInt("check");
        if(hasLoggedIn)
        {
            startActivity(new Intent(getApplicationContext(), FeedList.class));
            finish();
        }
        else{
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }
    }
}
