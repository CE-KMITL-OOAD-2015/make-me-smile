package com.toocomplicated.mademesmile;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.*;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.widget.ShareButton;

/**
 * Created by Win8.1 on 26/10/2558.
 */
public class CustomViewHolder extends RecyclerView.ViewHolder {
    protected TextView txtView;
    protected TextView txtLocate;
    protected TextView txtName;
    protected ProfilePictureView proFile;
    protected ProfilePictureView proFileCom;
    protected Button send;
    protected EditText txtComment;
    protected ImageButton smile;
    protected ImageButton sad;
    protected ImageButton comment;
    protected ImageButton setting;
    protected TextView smileCount;
    protected TextView sadCount;
    protected TextView commentCount;
    protected ImageView pic;
    protected TextView time;
    protected RecyclerView commentView;
    protected RelativeLayout rl;
    public CustomViewHolder(View itemView) {
        super(itemView);
        this.txtView = (TextView) itemView.findViewById(R.id.listText);
        this.txtLocate = (TextView) itemView.findViewById(R.id.listLocate);
        this.txtName = (TextView) itemView.findViewById(R.id.name);
        this.proFile = (ProfilePictureView) itemView.findViewById(R.id.profile_picturefeed);
        this.smile = (ImageButton) itemView.findViewById(R.id.smile);
        this.sad = (ImageButton) itemView.findViewById(R.id.sad);
        this.comment = (ImageButton) itemView.findViewById(R.id.commentbtn);
        this.smileCount = (TextView) itemView.findViewById(R.id.smilecount);
        this.sadCount = (TextView) itemView.findViewById(R.id.sadcount);
        this.pic = (ImageView) itemView.findViewById(R.id.imagetest);
        this.commentView = (RecyclerView) itemView.findViewById(R.id.recycler_viewcomment);
        this.proFileCom = (ProfilePictureView) itemView.findViewById(R.id.profile_picturecommentself);
        this.send = (Button) itemView.findViewById(R.id.commentsendbtn);
        this.txtComment = (EditText) itemView.findViewById(R.id.commenttext);
        this.rl = (RelativeLayout) itemView.findViewById(R.id.relative);
        this.commentCount = (TextView) itemView.findViewById(R.id.commentcount);
        this.setting = (ImageButton) itemView.findViewById(R.id.setting);
        this.time = (TextView) itemView.findViewById(R.id.time);
    }


}
