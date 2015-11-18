package com.toocomplicated.mademesmile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
    private boolean smilePress;
    private boolean sadPress;
    private String test = "";

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
       // LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,true);
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
        holder.commentCount.setText("" + feedItem.getCommentList().size());
          if (feedItem.getPicList().size() != 0) {
                String res = feedItem.getPicList().get(0);
                //System.out.println("Pic "+feedItem.getPicList().get(0));

                byte[] imageByteArray = decodeImage(res);
                ByteArrayInputStream bis = new ByteArrayInputStream(imageByteArray);
               // bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                bitmap = BitmapFactory.decodeStream(new PlurkInputStream(bis));
                holder.pic.setAdjustViewBounds(true);
                holder.pic.setImageBitmap(bitmap);
               // holder.pic.setAdjustViewBounds(true);
            }
        if(Login.id.equals(feedItem.getFbid())){
            holder.setting.setVisibility(View.VISIBLE);
        }
        else{
            holder.setting.setVisibility(View.GONE);
        }
        holder.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Setting");
                builder.setItems(R.array.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            Story edit_story = feedItemList.get(position);
                            Intent intent = new Intent(mContext,Edit.class);
                            intent.putExtra("story",edit_story);
                            mContext.startActivity(intent);
                        }
                        else if(which == 1){
                            delete(position);
                            Toast.makeText(mContext, "Deleted " + position, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        holder.smile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  final HttpURLConnectionExample h = new HttpURLConnectionExample();
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            check = Integer.parseInt(h.sendPost("addFeeling", "storyId=" + feedItem.getStoryId() + "&fbid=" + Login.id + "&mode=0"));
                            System.out.println("check " + check);
                            Story item = feedItemList.get(position);
                            item.setSmile(check);
                            feedItemList.set(position, item);
                            if(check == 1){
                                holder.smile.setImageResource(R.drawable.smile_press);
                            }
                            else if(check == -1){
                                holder.smile.setImageResource(R.drawable.smile_unpress);
                            }

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
                notifyDataSetChanged();*/
                String storyId = ""+feedItem.getStoryId();
                String fbid = ""+feedItem.getFbid();
                String mode = ""+0;
                String pos = ""+position;
                new AsyncFeelTask().execute(storyId, fbid, mode, pos);
            }
        });
        holder.sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* final HttpURLConnectionExample h = new HttpURLConnectionExample();
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
                notifyItemChanged(position);*/
                    String storyId = "" + feedItem.getStoryId();
                    String fbid = "" + feedItem.getFbid();
                    String mode = "" + 1;
                    String pos = "" + position;
                    new AsyncFeelTask().execute(storyId, fbid, mode, pos);
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
                    holder.comment.setImageResource(R.drawable.comment_down);
                    holder.commentView.startAnimation(out);
                    holder.commentView.setVisibility(View.GONE);
                    System.out.println("test hide");
                } else {
                   // TranslateAnimation animate = new TranslateAnimation(0,0,0,holder.commentView.getHeight());
                    Animation in = AnimationUtils.loadAnimation(mContext,android.R.anim.fade_in);
                   // animate.setDuration(500);
                    //animate.setFillAfter(true);
                    holder.comment.setImageResource(R.drawable.comment_up);
                    holder.commentView.startAnimation(in);
                    holder.commentView.setVisibility(View.VISIBLE);
                    System.out.println("test show");
                }
                //notifyItemChanged(position);
                notifyDataSetChanged();
            }

        });

        adapterComment = new CommentAdapter(mContext, feedItem.getCommentList());
        holder.commentView.setAdapter(adapterComment);
        holder.proFileCom.setProfileId(Login.id);
        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comment cm = null;
                //String input = setParam(holder.txtComment.getText().toString(), feedItem.getFbid(), feedItem.getStoryId());
                String detail = holder.txtComment.getText().toString();
                String fbid = Login.id;
                String storyId = "" + feedItem.getStoryId();
                try {
                    new AsyncCommentTask().execute(detail, fbid, storyId).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                System.out.println("print time " + test);
                try {
                    JSONObject jObject = new JSONObject(test);
                    JSONObject jObject2 = new JSONObject(jObject.get("user").toString());
                    User usr = new User(jObject2.getString("fbid"), jObject2.getString("name"), jObject2.getInt("isPost"));
                    cm = new Comment(jObject.getInt("commentId"), jObject.getString("detail"), jObject.getString("time"), usr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Comment com = new Comment(0,holder.txtComment.getText().toString(),, new User(Login.id,Login.name));
                feedItem.getCommentList().add(0, cm);
                holder.commentCount.setText("" + feedItem.getCommentList().size());
                holder.txtComment.setText(null);
                notifyItemChanged(position);
            }
        });
        if(feedItem.getIsFeel() == 0){
            holder.smile.setImageResource(R.drawable.smile_unpress);
            holder.sad.setImageResource(R.drawable.sad_unpress);
        }
        else if(feedItem.getIsFeel() == 1){
            holder.smile.setImageResource(R.drawable.smile_press);
            holder.sad.setImageResource(R.drawable.sad_unpress);
        }
        else{
            holder.smile.setImageResource(R.drawable.smile_unpress);
            holder.sad.setImageResource(R.drawable.sad_press);
        }
    }



    private String setParam(String detail,String fbid,int storyId) {
        String param = "detail=" + detail + "&fbid=" + fbid + "&storyId=" + storyId;
        System.out.println("param" + param);
        return param;
    }

    public void delete(int position){
        Story delStory = feedItemList.get(position);
        new AsyncDeleteTask().execute("" + delStory.getStoryId(), delStory.getFbid(), "" + position);
    }
    public byte[] decodeImage(String imageDataString) {
        return Base64.decode(imageDataString.replace('\n','\0'), Base64.URL_SAFE);
    }

    public class AsyncIsFeelTask extends AsyncTask<String,Void,Integer>{

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            final HttpURLConnectionExample h = new HttpURLConnectionExample();
            try{
                test = h.sendPost("isFeel", "storyId=" + params[0] + "&fbid=" + params[1]);
                result = Integer.parseInt(test);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if(integer == 1){
                smilePress = true;
                sadPress = false;
            }
            else if(integer == -1){
                smilePress = false;
                sadPress = true;
            }else{
                System.out.println("Nothing change");
            }
        }
    }

    public class AsyncFeelTask extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... params) {
            String result = "";
            final HttpURLConnectionExample h = new HttpURLConnectionExample();
            try{
                test = h.sendPost("addFeeling", "storyId=" + params[0] + "&fbid=" + params[1] + "&mode=" + params[2]);
               result = test + "," + params[3] + "," + params[2];
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String input) {
            String[] variable = input.split(",");
            int change = Integer.parseInt(variable[0]);
            int position = Integer.parseInt(variable[1]);
            int mode = Integer.parseInt(variable[2]);
            Story item = feedItemList.get(position);
            if(mode == 0){
                item.setSmile(change);
                if(change == 1){
                    item.setIsFeel(1);
                }
                else if(change == -1){
                    item.setIsFeel(0);
                }
            }
            else{
                item.setSad(change);
                if(change == 1){
                    item.setIsFeel(2);
                }
                else if(change == -1){
                    item.setIsFeel(0);
                }
            }
          //  notifyItemChanged(position);
            notifyDataSetChanged();
        }
    }

    public class AsyncCommentTask extends AsyncTask<String,Void,Integer>{

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnectionExample h = new HttpURLConnectionExample();

            try{
                test = h.sendPost("comment","detail=" + params[0] + "&fbid=" + params[1] + "&storyId=" + params[2]);
                System.out.println("time " +test);
                result = 1;
            }
            catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("result " + result);
            return result;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if(integer == 1){
                Toast.makeText(mContext, "Comment success!", Toast.LENGTH_LONG).show();

            }
            else{
                Toast.makeText(mContext, "Error!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class AsyncDeleteTask extends  AsyncTask<String,Void,Integer>{

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnectionExample h = new HttpURLConnectionExample();
            try{
                test = h.sendPost("delete", "&storyId=" + params[0] + "&fbid=" + params[1]);
                result = Integer.parseInt(params[2]);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("result " + result);
            return result;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            feedItemList.remove(integer);
            //notifyItemRemoved(integer);
            notifyItemRemoved(integer);
            notifyItemRangeChanged(integer,feedItemList.size());
        }
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
