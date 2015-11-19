package com.toocomplicated.mademesmile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import com.facebook.FacebookActivity;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import tabs.SlidingTabLayout;

/**
 * Created by Win8.1 on 15/11/2558.
 */
public class FeedTabList extends AppCompatActivity implements MaterialTabListener{
    public static ViewPager mPager;
   // private SlidingTabLayout mTabs;
    private Toolbar toolbar;
    private MaterialTabHost materialTab;
    private ImageButton mImage;
    private Bitmap bitmap;
    private String url;
    private String test = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedtab2);

        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         AdsAlert alert = new AdsAlert();
        alert.show(getFragmentManager(), "Ads");
        getSupportActionBar().setTitle("Make Me Smile");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPager.getCurrentItem()== 1) {
                    mPager.getAdapter().notifyDataSetChanged();
                }
                else if(mPager.getCurrentItem() == 0 ) {
                    mPager.setCurrentItem(1);
                }
                else if(mPager.getCurrentItem() == 2 ) {
                    mPager.setCurrentItem(1);
                }
            }
        });
        materialTab = (MaterialTabHost) findViewById(R.id.materialTabHost);
        mPager = (ViewPager) findViewById(R.id.viewPager);
        FeedPagerAdapter adapter = new FeedPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                materialTab.setSelectedNavigationItem(position);
            }
        });
        for(int i = 0; i < adapter.getCount() ; i++){
            materialTab.addTab(
                    materialTab.newTab()
                            .setIcon(adapter.getIcon(i))
                            .setTabListener(this));
        }

        mPager.setCurrentItem(1);

    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        mPager.setCurrentItem(tab.getPosition());
        View focus = getCurrentFocus();
        if (focus != null) {
            hiddenKeyboard(focus);
        }
    }

    @Override
    public void onTabReselected(MaterialTab tab) {
        View focus = getCurrentFocus();
        if (focus != null) {
            hiddenKeyboard(focus);
        }

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {
        View focus = getCurrentFocus();
        if (focus != null) {
            hiddenKeyboard(focus);
        }

    }
    private void hiddenKeyboard(View v) {
        InputMethodManager keyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    class FeedPagerAdapter extends FragmentPagerAdapter{
        int icons[] = {R.drawable.share,R.drawable.news,R.drawable.star};
        String[] tabText = getResources().getStringArray(R.array.tabs);

        public FeedPagerAdapter(FragmentManager fm){
            super(fm);
            tabText = getResources().getStringArray(R.array.tabs);
        }
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new ShareFragment();
                case 1:
                    return new FeedFragment();
                case 2:
                    return new MostPopularFragment();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable drawable = getResources().getDrawable(icons[position]);
            drawable.setBounds(toPixels(0),toPixels(0),toPixels(36),toPixels(36));
            ImageSpan imageSpan = new ImageSpan(drawable);
            SpannableString spannableString = new SpannableString(" ");
            spannableString.setSpan(imageSpan,0,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }
        private Drawable getIcon(int position){
            return getResources().getDrawable(icons[position]);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
    public int toPixels(float dip){
        Resources r = getResources();
        float densid = r.getDisplayMetrics().density;
        int px = (int) (dip * densid + 0.5f);
        return px;
    }

    public class AdsAlert extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.popupads, null);
            mImage = (ImageButton) view.findViewById(R.id.picads);
            mImage.setImageResource(R.drawable.bg);
            builder.setView(view);

            Dialog dialog = builder.create();

            return dialog;
        }

    }

   /* public class AsyncAdsTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            final HttpURLConnectionExample h = new HttpURLConnectionExample();
            try{
                test = h.sendPost("getAdvertisement","");
                JSONObject jad = new JSONObject(test);
                String url = jad.getString("url");
                String img = jad.getString("img");
                result = url+","+img;
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(!result.equals("")){
                //System.out.println("Pic "+feedItem.getPicList().get(0));
                String[] pic = result.split(",");
                byte[] imageByteArray = decodeImage(pic[1]);
                ByteArrayInputStream bis = new ByteArrayInputStream(imageByteArray);
                bitmap = BitmapFactory.decodeStream(new RecyclerAdapter.PlurkInputStream(bis));
                mImage.setImageBitmap(bitmap);
                url = pic[0];
            }
        }
    }*/
    public byte[] decodeImage(String imageDataString) {
        return Base64.decode(imageDataString, Base64.URL_SAFE);
    }


}
