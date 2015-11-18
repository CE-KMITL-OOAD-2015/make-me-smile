package com.toocomplicated.mademesmile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
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
    private Button mButtonCanclePop;
    private Button mButtonCreatePop;
    private Button mButtonCreateFinish;
    private EditText mEditText;
    private EditText nameEditText;
    private EditText addressEditText;
    private String test = "";
    private Location location;
    private Location addLoc;
    private String locationName;
    private PopupWindow popupWindow;
    private PopupWindow popupWindow2;
    private LayoutInflater layoutInflater;
    private LayoutInflater layoutInflater2;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);
        location = new Location(0,"","");
        linearLayout = (LinearLayout)findViewById(R.id.linear_locate);
        mRecycler = (RecyclerView)findViewById(R.id.recycler_viewlocate);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecycler, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                location = adapter.getLocation(position);
                locationName = location.getName();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("LocationId",location.getId());
                editor.putString("LocationName",location.getName());
                editor.putString("LocationAddress",location.getAddress());
                editor.apply();

                Toast.makeText(LocationList.this, "Choose location "+locationName , Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        mButtonCancle = (Button)findViewById(R.id.buttoncanclelocate);
        mButtonCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
               /* editor.putInt("LocationId",location.getId());
                editor.putString("LocationName",location.getName());
                editor.putString("LocationAddress",location.getAddress());
                editor.apply();*/
                finish();
            }
        });
        mEditText = (EditText)findViewById(R.id.textlocation);
        mButtonSearch = (ImageButton)findViewById(R.id.search_img_button);
        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEditText.getText().toString();
                if(name.equals("")){
                    Toast.makeText(LocationList.this, "Please put some text to search location" , Toast.LENGTH_SHORT).show();
                }
                else
                new AsyncLocateTask().execute(name);
            }
        });
        //new AsyncLocateTask().execute();
    }


    public class AsyncLocateTask extends AsyncTask<String,Void,Integer>{

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 1;
            final HttpURLConnectionExample  h = new HttpURLConnectionExample();
            try {
                test = h.sendPost("searchLocation", "name=" + params[0]);
                locationList = getLocationList(test);
                System.out.println(locationList);
                if(test.equals("[]")){
                    result = 0;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
            System.out.println("result "+ result);
            return result;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if(integer == 1){
                System.out.println("check");
                adapter = new LocationAdapter(LocationList.this, locationList);
                mRecycler.setAdapter(adapter);
            }
            else{
                Toast.makeText(LocationList.this, "No location found!", Toast.LENGTH_LONG).show();
                adapter = new LocationAdapter(LocationList.this, locationList);
                mRecycler.setAdapter(adapter);
                LocationAlert alert = new LocationAlert();
                alert.show(getFragmentManager(), "Location Alert");
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

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private  GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, RecyclerView recyclerView, ClickListener clickListener){
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if(child != null && clickListener != null && gestureDetector.onTouchEvent(e)){
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
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
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }

    public class LocationAlert extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            setCancelable(false);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.popuplocation, null);
            TextView mTextView = (TextView)view.findViewById(R.id.viewalert);
            mTextView.setText(mEditText.getText() +" Not found !\n Do you want to create new location?");
            builder.setView(view);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MakeLocationAlert alert = new MakeLocationAlert();
                    alert.show(getFragmentManager(), "Location Alert");
                    Toast.makeText(getActivity(), "Please put name and address" , Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "Return to search location" , Toast.LENGTH_SHORT).show();
                }
            });
            Dialog dialog = builder.create();

            return dialog;
        }

    }

    public class MakeLocationAlert extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            setCancelable(false);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.popupcreate, null);
            final EditText mNameView = (EditText)view.findViewById(R.id.textname);
            mNameView.setText(mEditText.getText().toString());
            final EditText mAddressView = (EditText) view.findViewById(R.id.textaddress);
            builder.setView(view);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    addLoc = new Location(0, mNameView.getText().toString(), mAddressView.getText().toString());
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("LocationId", addLoc.getId());
                    editor.putString("LocationName", addLoc.getName());
                    editor.putString("LocationAddress", addLoc.getAddress());
                    editor.apply();
                    finish();
                    Toast.makeText(LocationList.this, "Create " + mNameView.getText().toString(), Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "Return to search location", Toast.LENGTH_SHORT).show();
                }
            });

            Dialog dialog = builder.create();
            return dialog;
        }
    }
}
