package com.toocomplicated.mademesmile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.provider.ContactsContract;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Win8.1 on 19/10/2558.
 */
public class StoryAdapter extends ArrayAdapter<Story> {

    customButtonListener custom;

    public interface customButtonListener{
        public void onButtonClickListener(int position,Story value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.custom = listener;
    }

    Context context;
    int layoutResourceId;
    ArrayList<Story> resource = null;
    private String test = "";

    public StoryAdapter(Context context, int layoutResourceId, ArrayList<Story> resource) {
        super(context, layoutResourceId, resource);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.resource = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        StoryHolder holder = null;
        final HttpURLConnectionExample h = new HttpURLConnectionExample();

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new StoryHolder();
            holder.txtView = (TextView) row.findViewById(R.id.listText);
            holder.txtLocate = (TextView) row.findViewById(R.id.listLocate);
            holder.txtName = (TextView) row.findViewById(R.id.name);
            holder.proFile = (ProfilePictureView) row.findViewById(R.id.profile_picturefeed);
            holder.smile = (Button) row.findViewById(R.id.smile);
            holder.sad = (Button) row.findViewById(R.id.sad);
            holder.smileCount = (TextView) row.findViewById(R.id.smilecount);
            holder.sadCount = (TextView) row.findViewById(R.id.sadcount);

            //  holder.image = (ImageView)row.findViewById(R.id.pictest);
            row.setTag(holder);

        } else {
            holder = (StoryHolder) row.getTag();
        }
        final Story story = resource.get(position);
        holder.smile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HttpURLConnectionExample h = new HttpURLConnectionExample();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            test = h.sendPost("addFeeling", "storyId=" + story.getStoryId() + "&fbid=" + Login.id + "&mode=0");
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                }).start();
                if(custom != null) {
                    custom.onButtonClickListener(position, story);
                }
            }
        });
        holder.sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HttpURLConnectionExample h = new HttpURLConnectionExample();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("b4");
                            test = h.sendPost("addFeeling", "storyId=" + story.getStoryId() + "&fbid=" + Login.id + "&mode=1");
                            System.out.println("after");
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                }).start();
                if(custom != null) {
                    custom.onButtonClickListener(position, story);
                }
            }
        });
        holder.txtView.setText(story.getDes());
        holder.txtLocate.setText(story.getPlace().getName());
        holder.txtName.setText(story.getUser().getName());
        //ImageView fbimage = (ImageView)holder.proFile.getChildAt(0);
        //Bitmap bitmap = ((BitmapDrawable)fbimage.getDrawable()).getBitmap();
        holder.proFile.setProfileId(story.getFbid());
        holder.smileCount.setText("" + story.getSmile());
        holder.sadCount.setText("" + story.getSad());
        //scaleDownBitmap(bitmap, 100, getContext());
        // holder.image.setImageBitmap(bitmap);


        return row;
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
                        , job.getInt("placeId")
                        , job.getString("time")
                        , job.getInt("privacy")
                        , job.getString("fbid")
                        , loc
                        , user
                );
                styList.add(story);
                i++;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return styList;
    }

    public ArrayList<Story> getData() {
        return resource;
    }

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h = (int) (newHeight * densityMultiplier);
        int w = (int) (h * photo.getWidth() / ((double) photo.getHeight()));

        photo = Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }


    static class StoryHolder {
        TextView txtView;
        TextView txtLocate;
        TextView txtName;
        ProfilePictureView proFile;
        Button smile;
        Button sad;
        TextView smileCount;
        TextView sadCount;
        //ImageView image;
    }

}
