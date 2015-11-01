package com.toocomplicated.mademesmile;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
        linearLayout = (LinearLayout)findViewById(R.id.linear_locate);
        mRecycler = (RecyclerView)findViewById(R.id.recycler_viewlocate);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecycler, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                location = adapter.getLocation(position);
                locationName = location.getName();
                Toast.makeText(LocationList.this, "Choose location "+locationName , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
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

                layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup)layoutInflater.inflate(R.layout.popuplocation,null);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                popupWindow = new PopupWindow(container,400,150,true);
                popupWindow.showAtLocation(linearLayout, Gravity.CENTER,0,0);
                mButtonCanclePop = (Button)container.findViewById(R.id.buttoncanclelocatepop);
                mButtonCanclePop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                mButtonCreatePop = (Button)container.findViewById(R.id.buttoncreatepop);
                mButtonCreatePop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        layoutInflater2 = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        ViewGroup container2 = (ViewGroup)layoutInflater2.inflate(R.layout.popupcreate,null);
                        popupWindow2 = new PopupWindow(container2,400,300,true);
                        popupWindow2.showAtLocation(linearLayout, Gravity.CENTER,0,0);
                        nameEditText = (EditText)container2.findViewById(R.id.textname);
                        nameEditText.setText(mEditText.getText().toString());
                        addressEditText = (EditText)container2.findViewById(R.id.textaddress);
                        mButtonCreateFinish = (Button)container2.findViewById(R.id.buttoncreatepopfinish);
                        mButtonCreateFinish.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addLoc = new Location(0,nameEditText.getText().toString(),addressEditText.getText().toString());
                                popupWindow2.dismiss();
                                Toast.makeText(LocationList.this, "Create " + nameEditText.getText().toString(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });
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
}
