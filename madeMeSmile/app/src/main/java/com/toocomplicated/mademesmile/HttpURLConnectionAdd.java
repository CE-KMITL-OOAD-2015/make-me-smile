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
        http.sendPost();

    }

    private void sendPost() throws Exception {
        File file = new File("/Users/L/Desktop/demo/src/main/java/testhttp/img.png");
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
        System.out.println(imageDataString);
        String urlParameters  = "storyId=1&fbid=2&img="+imageDataString; //change only storyId & fbid
        byte[] postData       = urlParameters.getBytes( "UTF-8");
        int    postDataLength = postData.length;
        String request        = "http://203.151.92.173:8080/postPicture";
        URL    url            = new URL( request );
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
        conn.setDoOutput( true );
        conn.setInstanceFollowRedirects( false );
        conn.setRequestMethod( "POST" );
        conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty( "charset", "utf-8");
        conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
        conn.setUseCaches( false );
         DataOutputStream wr = new DataOutputStream( conn.getOutputStream());
            wr.write( postData );


        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());


    }

    public static String encodeImage(byte[] imageByteArray){
        return Base64.encodeBase64URLSafeString(imageByteArray);
    }

    public static byte[] decodeImage(String imageDataString) {
        return Base64.decodeBase64(imageDataString);
    }

}