package com.toocomplicated.mademesmile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Win8.1 on 4/11/2558.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {
    private Context mContext;
    private List<Comment> commentList;

    public CommentAdapter (Context context, List<Comment> commentList){
        mContext = context;
        this.commentList = commentList;
    }
    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.commentlist,null);
        CommentHolder viewHolder = new CommentHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        Comment commentItem = commentList.get(position);

        holder.txtName.setText(commentItem.getUser().getName());
        holder.txtComment.setText(commentItem.getDetail());
        holder.txtTime.setText(commentItem.getTime());
        holder.pictureView.setProfileId(commentItem.getUser().getId());

    }

    @Override
    public int getItemCount() {
        return (null != commentList ? commentList.size() : 0);
    }
}
