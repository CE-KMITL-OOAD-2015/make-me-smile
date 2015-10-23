package com.toocomplicated.mademesmile;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Win8.1 on 19/10/2558.
 */
public class StoryAdapter extends ArrayAdapter<Story> {

    Context context;
    int layoutResourceId;
    ArrayList<Story> resource = null;

    public StoryAdapter(Context context, int layoutResourceId, ArrayList<Story> resource) {
        super(context, layoutResourceId, resource);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        StoryHolder holder = null;


        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new StoryHolder();
            holder.txtView = (TextView)row.findViewById(R.id.listText);
            holder.txtLocate = (TextView)row.findViewById(R.id.listLocate);
            holder.txtName = (TextView)row.findViewById(R.id.name);
            holder.proFile = (ProfilePictureView)row.findViewById(R.id.profile_picturefeed);
          //  holder.image = (ImageView)row.findViewById(R.id.pictest);
            row.setTag(holder);

        }
        else{
            holder = (StoryHolder)row.getTag();
        }
        Story story = resource.get(position);
        holder.txtView.setText(story.getDes());
        holder.txtLocate.setText(story.getPlace().getName());
        Profile profile = Profile.getCurrentProfile();
        holder.txtName.setText(profile.getName());
        //ImageView fbimage = (ImageView)holder.proFile.getChildAt(0);
        //Bitmap bitmap = ((BitmapDrawable)fbimage.getDrawable()).getBitmap();
        holder.proFile.setProfileId(story.getFbid());
        //scaleDownBitmap(bitmap, 100, getContext());
       // holder.image.setImageBitmap(bitmap);


        return row;
    }

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

    static class StoryHolder{
        TextView txtView;
        TextView txtLocate;
        TextView txtName;
        ProfilePictureView proFile;
        //ImageView image;
    }
}
