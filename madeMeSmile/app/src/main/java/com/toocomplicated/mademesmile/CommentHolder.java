package com.toocomplicated.mademesmile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

/**
 * Created by Win8.1 on 4/11/2558.
 */
public class CommentHolder extends RecyclerView.ViewHolder {
    protected ProfilePictureView pictureView;
    protected TextView txtName;
    protected TextView txtComment;
    protected TextView txtTime;
    protected ImageButton setting;
    public CommentHolder(View itemView) {
        super(itemView);
        this.pictureView = (ProfilePictureView) itemView.findViewById(R.id.profile_picturecomment);
        this.txtName = (TextView) itemView.findViewById(R.id.namecomment);
        this.txtTime = (TextView) itemView.findViewById(R.id.time);
        this.txtComment = (TextView) itemView.findViewById(R.id.comment);
        this.setting = (ImageButton) itemView.findViewById(R.id.settingcom);
    }
}
