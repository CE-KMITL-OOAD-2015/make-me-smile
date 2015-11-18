package com.toocomplicated.mademesmile;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedtab2);

        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*System.out.println("Click");
                mPager.setCurrentItem(1);
                FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.viewPager,new FeedFragment()).commit();*/
            }
        });
        materialTab = (MaterialTabHost) findViewById(R.id.materialTabHost);
        mPager = (ViewPager) findViewById(R.id.viewPager);
        FeedPagerAdapter adapter = new FeedPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){

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

        /*mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setCustomTabView(R.layout.tab_view,R.id.tabstext);
        mTabs.setDistributeEvenly(true);
        mTabs.setViewPager(mPager);*/
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

}
