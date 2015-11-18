package com.toocomplicated.mademesmile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.*;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.ProfilePictureView;

import org.apache.commons.codec.binary.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Win8.1 on 15/11/2558.
 */
public class ShareFragment extends Fragment {
    public String sq = null;
    private ShareHelper helper;
    private ProfilePictureView profilePicture;
    private Story story;
    private String fbid;
    private Location location;
    private TextView mLocationView;
    private static final int SELECTED_PICTURE = 1;
    //private ImageView mImage;
    private Button mButtonPhoto;
    private ArrayList<String> imagesPathList = new ArrayList<>();
    private LinearLayout lnrImages;
    private Bitmap yourbitmap;
    private RadioGroup mPrivacyRadio;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.activity_share2,container,false);
        FacebookSdk.sdkInitialize(getActivity());
        helper = new ShareHelper(getActivity());
        location = new Location(0, "", "");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
        fbid = Login.id;

        Button mButtonLocation = (Button) layout.findViewById(R.id.buttontag2);
        mButtonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LocationList.class));
            }
        });
        mLocationView = (TextView) layout.findViewById(R.id.locationview2);
        mLocationView.setText("Location : " + location.getName());
        final EditText mEditText = (EditText) layout.findViewById(R.id.edittext2);
        lnrImages = (LinearLayout) layout.findViewById(R.id.lnrImages2);
        mPrivacyRadio = (RadioGroup) layout.findViewById(R.id.privacy_radio2);
        mPrivacyRadio.check(R.id.public_radio2);
        mButtonPhoto = (Button) layout.findViewById(R.id.buttonphoto2);

        Button mButtonShare = (Button) layout.findViewById(R.id.buttonshare2); // share button
        mButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location.getName().equals("") || location.getName().equals("No location")) {
                    Toast.makeText(getActivity(), "You must tag location!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LocationList.class));
                } else {
                    addStory(mEditText.getText().toString());
                    sendStory();
                   // sendPic(imagesPathList.get(0));
                    mEditText.setText(null);
                    location = new Location(0, "", "");
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.commit();
                    mLocationView.setText("Location : " + location.getName());
                    FeedTabList.mPager.setCurrentItem(1);
                }
            }
        });
        mButtonPhoto.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),CustomPhotoGalleryActivity.class);
            startActivityForResult(intent, SELECTED_PICTURE);
        }
    });

        profilePicture = (ProfilePictureView) layout.findViewById(R.id.profile_picturetest2);
        profilePicture.setProfileId(Login.id);
        return layout;
    }

    private void addStory(String str) {
        //  HttpURLConnectionExample httpyk = new HttpURLConnectionExample();
        int privacy;
        if(mPrivacyRadio.getCheckedRadioButtonId() == R.id.public_radio2){
            privacy = 0;
        }
        else{
            privacy = 1;
        }
        String pic;
        if(imagesPathList.size() != 0){
            pic = imageToString(imagesPathList.get(0));
        }
        else{
            pic = "";
        }
        sq = "privacy="+ privacy + "&des=" + str + "&fbid=" + fbid + "&locationId=" + location.getId() + "&locationName=" +
                location.getName() + "&address=" + location.getAddress() + "&img=" + pic;
        Log.e("TEST2", sq);
//        System.out.println("check pic " + imageToString(imagesPathList).toString());
    }

    private String imageToString(String pic) {
        File file = new File(pic);
        String imageDataString = "";
        try {

            FileInputStream imageInFile = new FileInputStream(file);
            byte imageData[] = new byte[(int) file.length()];
            imageInFile.read(imageData);
           ByteArrayInputStream bis = new ByteArrayInputStream(imageData);

            imageDataString = encodeImage(imageData);
            imageInFile.close();
            return imageDataString;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String encodeImage(byte[] imageByteArray){
        return  Base64.encodeToString(imageByteArray, Base64.URL_SAFE);
        //return new String(imageByteArray);
    }


        @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECTED_PICTURE && resultCode == CustomPhotoGalleryActivity.RESULT_OK && data != null) {
            imagesPathList = new ArrayList<String>();
            String[] imagesPath = data.getStringExtra("data").split("\\|");
            try {
                lnrImages.removeAllViews();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            for (int i = 0; i < imagesPath.length; i++) {
                System.out.println("picpath " + imagesPath[i]);
                imagesPathList.add(imagesPath[i]);
                System.out.println("check" + imagesPath[i]);
                System.out.println("checklist" + imagesPathList);
                yourbitmap = BitmapFactory.decodeFile(imagesPath[i]);


                ImageView imageView = new ImageView(getActivity());
                imageView.setImageBitmap(yourbitmap);
                imageView.setAdjustViewBounds(true);
                lnrImages.addView(imageView);
            }
        }
    }

    private void sendStory() {
        final HttpURLConnectionExample h2 = new HttpURLConnectionExample();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    h2.sendPost("writeStory", sq);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                }
            }
        }).start();
    }

    private void sendPic(final String path){
        final HttpURLConnectionAdd h = new HttpURLConnectionAdd();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    h.sendPost(path);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                }
            }
        }).start();
    }




    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharePref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int id = sharePref.getInt("LocationId", 0);
        String name = sharePref.getString("LocationName", "No location");
        String address = sharePref.getString("LocationAddress", "No address");
        location = new Location(id, name, address);
        mLocationView.setText("Location : " + location.getName());
    }

}
