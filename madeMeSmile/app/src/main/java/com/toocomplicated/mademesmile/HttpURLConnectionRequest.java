package com.toocomplicated.mademesmile;


import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class HttpURLConnectionRequest {

    private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {

        HttpURLConnectionRequest http = new HttpURLConnectionRequest();

        System.out.println("Testing - Send Http POST request");
        http.sendPost();

    }

    private void sendPost() throws Exception {

        //"privacy=0&des=kuyy&fbid=10204855639980330&locationId=1&locationName=KMITL&address=Address1";

        String urlParameters  = "storyId=1"; //theory : it return only one.
        byte[] postData       = urlParameters.getBytes( "UTF-8" );
        int    postDataLength = postData.length;
        String request        = "http://203.151.92.173:8080/requestPicture";
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

        try {
			/*
			 * Converting a Base64 String into Image byte array
			 */
            byte[] imageByteArray = decodeImage(response.toString());

			/*
			 * Write a image byte array into file system
			 */
            FileOutputStream imageOutFile = new FileOutputStream("/Users/L/Desktop/demo/src/main/java/testhttp/mini3.png");
            imageOutFile.write(imageByteArray);

            imageOutFile.close();

            System.out.println("Image Successfully Manipulated!");
        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        }


    }

    public static String encodeImage(byte[] imageByteArray){
        return Base64.encodeBase64URLSafeString(imageByteArray);
    }

    public static byte[] decodeImage(String imageDataString) {
        return Base64.decodeBase64(imageDataString);
    }

}