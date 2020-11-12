package com.thedonbase.nordicdrinking;//
// Created by TheDonBase on 2020-11-11.
// Copyright (c) 2020 CroazStudio. All rights reserved.
//


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchData  extends AsyncTask<Void, Void, String> {

    private OnEventListener<String> mCallBack;
    private Context mContext;
    public Exception mException;

    public FetchData(Context context, OnEventListener callback) {
        mCallBack = callback;
        mContext = context;
    }


    @Override
    protected String doInBackground(Void... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        URL url;
        IOException exception;
        try {
            url = new URL("https://croazstudio.zapto.org/Questions.json");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET"); //Your method here

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null)
                buffer.append(line + "\n");

            if (buffer.length() == 0)
                return null;

            return buffer.toString();
        } catch (IOException e) {
            Log.e("Error", "IO Exception", e);
            exception = e;
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    exception = e;
                    Log.e("Error", "Error closing stream", e);
                }
            }
        }
    }

        @Override
        protected void onPostExecute(String response) {
            if(mCallBack != null) {
                if(mException == null) {
                    try {
                        mCallBack.onSuccess(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    mCallBack.onFailure(mException);
                }
            }
        }
    }