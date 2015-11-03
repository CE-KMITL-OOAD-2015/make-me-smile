package com.toocomplicated.mademesmile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

/**
 * Created by Win8.1 on 26/10/2558.
 */
public class CustomViewHolder extends RecyclerView.ViewHolder {
    protected TextView txtView;
    protected TextView txtLocate;
    protected TextView txtName;
    protected ProfilePictureView proFile;
    protected Button smile;
    protected Button sad;
    protected TextView smileCount;
    protected TextView sadCount;
    protected ImageView pic;
    public CustomViewHolder(View itemView) {
        super(itemView);
        this.txtView = (TextView) itemView.findViewById(R.id.listText);
        this.txtLocate = (TextView) itemView.findViewById(R.id.listLocate);
        this.txtName = (TextView) itemView.findViewById(R.id.name);
        this.proFile = (ProfilePictureView) itemView.findViewById(R.id.profile_picturefeed);
        this.smile = (Button) itemView.findViewById(R.id.smile);
        this.sad = (Button) itemView.findViewById(R.id.sad);
        this.smileCount = (TextView) itemView.findViewById(R.id.smilecount);
        this.sadCount = (TextView) itemView.findViewById(R.id.sadcount);
        this.pic = (ImageView) itemView.findViewById(R.id.imagetest);
    }
}
