package com.toocomplicated.mademesmile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Win8.1 on 4/11/2558.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {
    private Context mContext;
    private List<Comment> commentList;
    private String test = "";

    public CommentAdapter(Context context, List<Comment> commentList) {
        mContext = context;
        this.commentList = commentList;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commentlist, null);
        CommentHolder viewHolder = new CommentHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CommentHolder holder, final int position) {
        final Comment commentItem = commentList.get(position);

        holder.txtName.setText(commentItem.getUser().getName());
        holder.txtComment.setText(commentItem.getDetail());
        holder.txtTime.setText(commentItem.getTime());
        holder.pictureView.setProfileId(commentItem.getUser().getId());
        if(Login.id.equals(commentItem.getUser().getId())){
            holder.setting.setVisibility(View.VISIBLE);
        }
        else{
            holder.setting.setVisibility(View.GONE);
        }
        holder.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("SettingComment");
                builder.setItems(R.array.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Comment edit_comment = commentList.get(position);
                            Intent intent = new Intent(mContext,EditComment.class);
                            intent.putExtra("comment",edit_comment);
                            mContext.startActivity(intent);
                        } else if (which == 1) {
                            delete(position);
                            Toast.makeText(mContext, "Deleted comment " + position, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }

        });


    }

    public void delete(int position) {
        Comment delComment = commentList.get(position);
        new AsyncDeleteCommentTask().execute(Login.id, "" + delComment.getCommentId(), "" + position);
    }


    @Override
    public int getItemCount() {
        return (null != commentList ? commentList.size() : 0);
    }

    public class AsyncDeleteCommentTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnectionExample h = new HttpURLConnectionExample();
            try {
                test = h.sendPost("deleteComment", "&fbid=" + params[0] + "&commentId=" + params[1]);
                result = Integer.parseInt(params[2]);

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("result " + result);
            return result;
        }

        @Override
        protected void onPostExecute(Integer integer) {
          /* if(commentList.size() == 1){
                commentList.clear();
                //CustomViewHolder.commentCount.setText("0");
            }
            else{*/
            System.out.println("integer" + integer);
            commentList.remove(integer);
            // CustomViewHolder.commentCount.setText(""+commentList.size());
            //}
            System.out.println("pass");
            try {
                commentList.clear();
                JSONArray jarr = new JSONArray(test);
                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject jsonComment = jarr.getJSONObject(i);
                    JSONObject jsonUsr = new JSONObject(jsonComment.get("user").toString());
                    User usr = new User(jsonUsr.getString("fbid"), jsonUsr.getString("name"), jsonUsr.getInt("isPost"));
                    commentList.add(new Comment(jsonComment.getInt("commentId"), jsonComment.getString("detail")
                            , jsonComment.getString("time"), usr));
                }

                //notifyItemChanged(integer);
                // notifyItemRangeChanged(integer, commentList.size());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            FeedFragment.adapter.notifyDataSetChanged();
            notifyDataSetChanged();
        }

    }
}

