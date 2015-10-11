package com.magic.lesson1;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Kelok on 8/10/2015.
 */
//input - process - output
public class MagicApiRequest<T> extends AsyncTask<String, Object, String> {
    static final String TAG = "MagicApiRequest";

    //Gson parser
    private static final Gson gson = new Gson();

    public final Class<T> type;
    //T can be anything

    Throwable error = null;


    public MagicApiRequest(Class<T> type) {
        this.type = type;
        ArrayList<Employee> employees = new ArrayList<Employee>();
        Employee[] employeeArray = new Employee[10];
    }


    @Override
    protected String doInBackground(String... strings) {
        //Prepare query string
        HttpURLConnection connection = null;
        try {
            String query = strings[0]; //accurve.com/tutorial/create.php
            if (strings.length > 1) {
                query += "?"; //accurve.com/tutorial/create.php?
                for (int c = 1; c < strings.length; c += 2) {
                    if (c > 1) {
                        query += "&"; //accurve.com/tutorial/create.php?ID=888888888801&
                    }
                    //URLEncoder replaces " " with "%20" for URL purpose
                    query += strings[c] + "=" + URLEncoder.encode(strings[c + 1], "UTF-8"); //accurve.com/tutorial/create.php?ID=888888888801
                }
            }

            URL url = new URL("http://accurve.com/tutorial/" + query);
            connection = (HttpURLConnection) url.openConnection();

            //Read response
            //Get bytes                 To speed up reading      Convert into string
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = "";
            String line;
            while ((line = reader.readLine()) != null)  //if reader is not empty
                response += line; //add input line to response string

            return response;


        } catch (Throwable e) {
            //Error happens here
            error = e;
            return null;

        } finally {
            if (connection != null)
                connection.disconnect();
        }

        /*
        API: accurve.com/tutorial/create.php
        Parameter:
        ID = 888888888801
        name = John Smith
         */
    }

    @Override
    protected void onPostExecute(String s) {
        //search.php - Employee[]
        //get.php - Employee
        //create.php - Employee
        //T is replaced by whatever is inside the Api class <>
        if (s == null) {
            onFailed(error);    //No output from server, handle error
        } else {
            //Received json from server, convert to Java Object
            try {
                T parsed = gson.fromJson(s, type);
                onSucess(parsed, s);
            } catch (Throwable e) {
                onFailed(e);
            }
        }
    }

    public void onSucess(T o, String json) {
        Log.d(TAG, "Unhandled onSuccess with: " + o + " json: " + json);
    }

    public void onFailed(Throwable e) {
        Log.d(TAG, "Unhandled onFailed " + e);
    }
}
