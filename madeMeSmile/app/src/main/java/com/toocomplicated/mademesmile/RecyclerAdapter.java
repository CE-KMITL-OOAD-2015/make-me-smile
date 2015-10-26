package com.toocomplicated.mademesmile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Win8.1 on 26/10/2558.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private List<Story> feedItemList;
    private Context mContext;

    public RecyclerAdapter(Context context, List<Story> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listtest, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Story feedItem = feedItemList.get(position);

        holder.txtView.setText(feedItem.getDes());
        holder.txtLocate.setText(feedItem.getPlace().getName());
        holder.txtName.setText(feedItem.getUser().getName());
        holder.proFile.setProfileId(feedItem.getFbid());
        holder.smileCount.setText("" + feedItem.getSmile());
        holder.sadCount.setText("" + feedItem.getSad());

    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }
}
