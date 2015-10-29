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
    private int presssmile = 0;
    private int presssad = 0;

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
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        final Story feedItem = feedItemList.get(position);

        holder.txtView.setText(feedItem.getDes());
        holder.txtLocate.setText(feedItem.getPlace().getName());
        holder.txtName.setText(feedItem.getUser().getName());
        holder.proFile.setProfileId(feedItem.getFbid());
        holder.smileCount.setText("" + feedItem.getSmile());
        holder.sadCount.setText("" + feedItem.getSad());
        holder.smile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HttpURLConnectionExample h = new HttpURLConnectionExample();
                try {
                    h.sendPost("addFeeling", "storyId=" + feedItem.getStoryId() + "&fbid=" + Login.id + "&mode=0");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                }
                Story item = feedItemList.get(position);
                if(presssad != 1){
                    presssmile = item.setSmile(presssmile);
                }
                feedItemList.set(position, item);
                notifyItemChanged(position);

            }
        });
        holder.sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HttpURLConnectionExample h = new HttpURLConnectionExample();
                try {
                    h.sendPost("addFeeling", "storyId=" + feedItem.getStoryId() + "&fbid=" + Login.id + "&mode=1");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                }
                Story item = feedItemList.get(position);
                if(presssmile != 1){
                      presssad = item.setSad(presssad);
                }
                feedItemList.set(position, item);
                notifyItemChanged(position);


            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }
}
