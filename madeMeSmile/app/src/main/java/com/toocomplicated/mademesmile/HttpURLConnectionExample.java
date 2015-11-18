package com.toocomplicated.mademesmile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.String;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.lang.Object.*;
import java.util.LinkedList;
import java.util.List;

public class HttpURLConnectionExample {

    private  final String USER_AGENT = "Mozilla/5.0";
    public String str1;
    public static void main(String[] args) throws Exception{

        HttpURLConnectionExample http = new HttpURLConnectionExample();

        // System.out.println("Testing 1 - Send Http GET request");
        //http.sendGet();

        System.out.println("\nTesting 2 - Send Http POST request");
       //http.sendPost(null, "kkkk");

    }

    // HTTP GET request
    public void sendGet() throws Exception {

        String url = "http://www.google.com/search?q=mkyong";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());



    }

    // HTTP POST request
    public  String sendPost(String mode,String param) throws Exception {
        String urlParameters = param;
        byte[] postData = urlParameters.getBytes("UTF-8");
        int postDataLength = postData.length;
        URL url;
        HttpURLConnection urlConn;
        DataOutputStream printout;
        DataInputStream input;
        url = new URL("http://161.246.6.31:8080/"+mode);
        urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setInstanceFollowRedirects(false);
        urlConn.setRequestMethod("POST");
        urlConn.setRequestProperty("charset", "utf-8");
        urlConn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        urlConn.setRequestProperty("User-Agent", USER_AGENT);
        urlConn.connect();
        DataOutputStream wr = new DataOutputStream(urlConn.getOutputStream());
        wr.write(postData);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(urlConn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        urlConn.disconnect();
        return response.toString();



    }

    public ArrayList<String> callStr(){

        ArrayList<String> strlist = new ArrayList<>() ;

            int i = 0, j = 0;

            while(i<str1.length())
            {

                    for (i = i + 1; i<str1.length() && str1.charAt(i) != '{'; i++) ;
                    for (j = i + 1; j<str1.length() && str1.charAt(j) != '}'; j++) ;
                if(j>=str1.length()||i>=str1.length())
                    break;
                strlist.add(str1.substring(i + 1, j));
                    System.out.println(str1.substring(i + 1, j));
            }
        for(int k = 0 ; k < strlist.size() ; k++)
            System.out.println(strlist.get(k));
        return strlist;

    }
//JSONObject jsonParam = new JSONObject();
//jsonParam.put("ID", "25");
//jsonParam.put("description", "Real");
//jsonParam.put("enable", "true");
  //      printout = new DataOutputStream(urlConn.getOutputStream ());
       // printout.write(URLEncoder.encode(jsonParam.toString(),"UTF-8"));
       // printout.flush ();
      //  printout.close ();



}