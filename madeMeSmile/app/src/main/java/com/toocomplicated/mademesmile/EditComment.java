package com.toocomplicated.mademesmile;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Win8.1 on 19/11/2558.
 */
public class EditComment extends AppCompatActivity {
    private EditText mEditText;
    private Comment editComment;
    private String test = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editcom);
        Bundle b = getIntent().getExtras();
        editComment = b.getParcelable("comment");
        mEditText = (EditText) findViewById(R.id.edittext_editcom);
        mEditText.setText(editComment.getDetail());
        Button mButtonCancle = (Button) findViewById(R.id.buttoncancle_editcom); // cancle button
        mButtonCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button mButtonConfirm = (Button) findViewById(R.id.buttonconfirm_editcom);
        mButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String param = addParam();
                new AsyncEditCommentTask().execute(param);
            }
        });
    }

    private String addParam() {
        String send = "detail="+mEditText.getText().toString()+"&fbid="+Login.id+"&commentId="+editComment.getCommentId();
        return send;
    }

    public class AsyncEditCommentTask extends AsyncTask<String,Void,Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 1;
            final HttpURLConnectionExample  h = new HttpURLConnectionExample();

            try {
                test = h.sendPost("editComment",params[0]);
                // System.out.println(feedList);
            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Toast.makeText(EditComment.this, "Edit success", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
