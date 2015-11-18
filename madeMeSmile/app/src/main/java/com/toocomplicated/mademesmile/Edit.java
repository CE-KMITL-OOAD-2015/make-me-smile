package com.toocomplicated.mademesmile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Win8.1 on 13/11/2558.
 */
public class Edit extends AppCompatActivity {
    private EditText mEditText;
    private RadioGroup mRadioGroup;
    private TextView mLocateView;
    private Story editStory;
    private Location location;
    private String test = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Bundle b = getIntent().getExtras();
        editStory = b.getParcelable("story");
        mEditText = (EditText)findViewById(R.id.edittext_edit);
        mEditText.setText(editStory.getDes());
        mRadioGroup = (RadioGroup)findViewById(R.id.privacy_radio_edit);
        mLocateView = (TextView)findViewById(R.id.locationview_edit);
        mLocateView.setText("Location : " + editStory.getPlace().getName());
        location = editStory.getPlace();
        System.out.println("Privacy " + editStory.getPrivacy());
        if(editStory.getPrivacy() == 0){
            mRadioGroup.check(R.id.public_radio_edit);
        }else{
            mRadioGroup.check(R.id.private_radio_edit);
        }
        Button mButtonCancle = (Button) findViewById(R.id.buttoncancle_edit); // cancle button
        mButtonCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button mButtonLocation = (Button) findViewById(R.id.buttontag_edit);
        mButtonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LocationList.class));
            }
        });
        Button mButtonConfirm = (Button) findViewById(R.id.buttonconfirm_edit);
        mButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String param = addParam();
                new AsyncEditTask().execute(param);
            }
        });
    }

    private String addParam(){
        int privacy;
        if(mRadioGroup.getCheckedRadioButtonId() == R.id.public_radio_edit){
            privacy = 0;
        }
        else{
            privacy = 1;
        }
        String send = "storyId="+editStory.getStoryId()+"&newPrivacy="+privacy+"&newDes="+mEditText.getText().toString()
                +"&newLocationId="+location.getId()+"&newLocationName="+location.getName()+"&newAddress="+location.getAddress();
        return send;
    }

    public class AsyncEditTask extends AsyncTask<String,Void,Integer>{

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 1;
            final HttpURLConnectionExample  h = new HttpURLConnectionExample();

            try {
                test = h.sendPost("edit",params[0]);
                // System.out.println(feedList);
            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Toast.makeText(Edit.this, "Edit success", Toast.LENGTH_SHORT).show();
            finish();
        }
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences sharePref = PreferenceManager.getDefaultSharedPreferences(this);
        int id = sharePref.getInt("LocationId", 0);
        String name = sharePref.getString("LocationName", "No location");
        String address = sharePref.getString("LocationAddress", "No address");
        location = new Location(id, name, address);
        mLocateView.setText("Location : " + location.getName());
    }
}
