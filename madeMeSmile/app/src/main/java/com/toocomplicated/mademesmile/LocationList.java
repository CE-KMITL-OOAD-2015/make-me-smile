package com.toocomplicated.mademesmile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Win8.1 on 31/10/2558.
 */
public class LocationList extends AppCompatActivity {
    private List<Location> locationList;
    private RecyclerView mRecycler;
    private LocationAdapter adapter;
    private ImageButton mButtonSearch;
    private Button mButtonCancle;
    private EditText mEditText;
    private String test = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);
        mRecycler = (RecyclerView)findViewById(R.id.recycler_viewlocate);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mButtonCancle = (Button)findViewById(R.id.buttoncanclelocate);
        mButtonCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Share.class));
                finish();
            }
        });
        mEditText = (EditText)findViewById(R.id.textlocation);
        mButtonSearch = (ImageButton)findViewById(R.id.search_img_button);
        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEditText.getText().toString();
                new AsyncLocateTask().execute(name);
            }
        });
        new AsyncLocateTask().execute();
    }

    public class AsyncLocateTask extends AsyncTask<String,Void,Integer>{

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 1;
            final HttpURLConnectionExample  h = new HttpURLConnectionExample();
            try {
                test = h.sendPost("searchLocation", "name="+ params[0] );
                locationList = getLocationList(test);
                System.out.println(locationList);
            } catch (Exception e) {
                // TODO Auto-generated catch block
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if(integer == 1){
                adapter = new LocationAdapter(LocationList.this, locationList);
                mRecycler.setAdapter(adapter);
            }
            else{
                Toast.makeText(LocationList.this, "Failed to fetch data!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public ArrayList<Location> getLocationList(String response) {
        ArrayList<Location> styList = new ArrayList<>();
        try {
            JSONArray jarr = new JSONArray();
            JSONObject jObject = new JSONObject("{" + "\"myArray\": " + response + "}");
            System.out.println(response);
            JSONArray jArray = jObject.getJSONArray("myArray");
            int i = 0;
            while (i < jArray.length()) {
                JSONObject job = jArray.getJSONObject(i);
                Location loc = new Location(job.getInt("id")
                        , job.getString("name")
                        , job.getString("address")
                );
                styList.add(loc);
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
