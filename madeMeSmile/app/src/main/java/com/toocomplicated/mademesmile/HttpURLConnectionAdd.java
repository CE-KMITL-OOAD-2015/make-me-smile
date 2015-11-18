package com.toocomplicated.mademesmile;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;


public class HttpURLConnectionAdd {

    private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {

        HttpURLConnectionAdd http = new HttpURLConnectionAdd();

        System.out.println("Testing - Send Http POST request");
        http.sendPost("");

    }

    public void sendPost(String path) throws Exception {
        File file = new File(path);
        String imageDataString = "";
        try {
			/*
			 * Reading a Image file from file system
			 */
            FileInputStream imageInFile = new FileInputStream(file);
            byte imageData[] = new byte[(int)file.length()];
            imageInFile.read(imageData);

			/*
			 * Converting Image byte array into Base64 String
			 */

            imageDataString = encodeImage(imageData);
            imageInFile.close();

        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        }
        //"privacy=0&des=kuyy&fbid=10204855639980330&locationId=1&locationName=KMITL&address=Address1";
       // System.out.println(imageDataString);
        String urlParameters  = "storyId=98&img="+imageDataString; //change only storyId & fbid
        HttpURLConnectionExample he = new HttpURLConnectionExample();
        he.sendPost("postPicture",urlParameters);

    }

    public static String encodeImage(byte[] imageByteArray){
        //return Base64.encodeBase64URLSafeString(imageByteArray);
        //System.out.println("123" + android.util.Base64.encodeToString(imageByteArray, android.util.Base64.URL_SAFE));
        return android.util.Base64.encodeToString(imageByteArray, android.util.Base64.URL_SAFE);
        //return new String(imageByteArray);
    }


}