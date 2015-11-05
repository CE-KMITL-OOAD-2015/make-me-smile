package com.toocomplicated.mademesmile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import org.json.JSONObject;

import android.util.Base64;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Win8.1 on 26/10/2558.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private List<Story> feedItemList;
    private Context mContext;
    private Bitmap bitmap;
    private ClickListener clickListener;
    private CommentAdapter adapterComment;
    private int presssmile = 0;
    private int presssad = 0;
    private int check = 0;

    public RecyclerAdapter(Context context, List<Story> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listtest, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        viewHolder.commentView.setHasFixedSize(false);
        viewHolder.commentView.setVerticalFadingEdgeEnabled(true);
        com.toocomplicated.mademesmile.LinearLayoutManager mLayoutManager = new com.toocomplicated.mademesmile.LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,true);
        viewHolder.commentView.setLayoutManager(mLayoutManager);

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
        try {
            if (feedItem.getPicList().size() != 0) {
                String res = feedItem.getPicList().get(0);
                //  System.out.println(feedItem.getPicList().get(0));

                byte[] imageByteArray = com.toocomplicated.mademesmile.Base64.decode(res);
                ByteArrayInputStream bis = new ByteArrayInputStream(imageByteArray);
               // bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                bitmap = BitmapFactory.decodeStream(new PlurkInputStream(bis));

                holder.pic.setImageBitmap(bitmap);
               // holder.pic.setAdjustViewBounds(true);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        holder.smile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HttpURLConnectionExample h = new HttpURLConnectionExample();
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            check = Integer.parseInt(h.sendPost("addFeeling", "storyId=" + feedItem.getStoryId() + "&fbid=" + Login.id + "&mode=0"));
                            System.out.println("check " + check);
                            Story item = feedItemList.get(position);
                            item.setSmile(check);
                            feedItemList.set(position, item);

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                notifyItemChanged(position);
            }
        });
        holder.sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HttpURLConnectionExample h = new HttpURLConnectionExample();
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            check = Integer.parseInt(h.sendPost("addFeeling", "storyId=" + feedItem.getStoryId() + "&fbid=" + Login.id + "&mode=1"));
                            System.out.println("check " + check);
                            Story item = feedItemList.get(position);
                            item.setSad(check);
                            feedItemList.set(position, item);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                notifyItemChanged(position);
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.commentView.getVisibility() == View.VISIBLE) {
                   // TranslateAnimation animate = new TranslateAnimation(0,0,0,-holder.commentView.getHeight());
                    Animation out = AnimationUtils.loadAnimation(mContext,android.R.anim.fade_out);
                   // out.setDuration(500);
                    //animate.setFillAfter(true);
                    holder.commentView.startAnimation(out);
                    holder.commentView.setVisibility(View.GONE);
                    System.out.println("test hide");
                } else {
                   // TranslateAnimation animate = new TranslateAnimation(0,0,0,holder.commentView.getHeight());
                    Animation in = AnimationUtils.loadAnimation(mContext,android.R.anim.fade_in);
                   // animate.setDuration(500);
                    //animate.setFillAfter(true);
                    holder.commentView.startAnimation(in);
                    holder.commentView.setVisibility(View.VISIBLE);
                    System.out.println("test show");
                }
                //notifyItemChanged(position);
            }

        });

        adapterComment = new CommentAdapter(mContext, feedItem.getCommentList());
        holder.commentView.setAdapter(adapterComment);
        holder.proFileCom.setProfileId(Login.id);
        holder.txtComment.setText(null);
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    public interface ClickListener{
        public void itemClick(View view, int position);
    }

    public static class PlurkInputStream extends FilterInputStream {

        protected PlurkInputStream(InputStream in) {
            super(in);
        }

        @Override
        public int read(byte[] buffer, int offset, int count)
                throws IOException {
            int ret = super.read(buffer, offset, count);
            for ( int i = 6; i < buffer.length - 4; i++ ) {
                if ( buffer[i] == 0x2c ) {
                    if ( buffer[i + 2] == 0 && buffer[i + 1] > 0
                            && buffer[i + 1] <= 48 ) {
                        buffer[i + 1] = 0;
                    }
                    if ( buffer[i + 4] == 0 && buffer[i + 3] > 0
                            && buffer[i + 3] <= 48 ) {
                        buffer[i + 3] = 0;
                    }
                }
            }
            return ret;
        }

    }
}
