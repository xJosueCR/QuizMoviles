package com.example.quiz;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncTaskManager  extends AsyncTask<String, String, String> {
    public final static String GET = "GET";
    public final static String POST = "POST";
    public final static String DELETE = "DELETE";
    public final static String PUT = "PUT";
    public interface AsyncResponse {
        void processFinish(String output);
    }
    public AsyncResponse delegate = null;
    ProgressDialog progressDialog;
    String ip = "10.0.2.2";
    String puerto = "8080";
    String dominio = "GestionAcademica";
    private String apiUrl;

    public AsyncTaskManager(String ip, AsyncResponse delegate) {
        this.ip = ip;
        this.delegate = delegate;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... params) {
        String response = "";
        try {
            URL url;
            HttpURLConnection urlConnection = null;


            try {
                url = new URL(this.ip);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");


                switch (params[0]) {
                    case GET: {
                        urlConnection.setRequestMethod("GET");

                    }
                    break;

                    case POST: {
                        urlConnection.setRequestMethod("POST");
                        urlConnection.setRequestProperty("Accept", "application/json");
                        urlConnection.setDoOutput(true);
                        String jsonString = params[1];
                        try (OutputStream os = urlConnection.getOutputStream()) {
                            byte[] input = jsonString.getBytes("utf-8");
                            os.write(input, 0, input.length);
                        }
                    }
                    break;
                    case PUT: {
                        urlConnection.setRequestMethod("PUT");
                        urlConnection.setRequestProperty("Accept", "application/json");
                        urlConnection.setDoOutput(true);
                        String jsonString = params[1];
                        try (OutputStream os = urlConnection.getOutputStream()) {
                            byte[] input = jsonString.getBytes("utf-8");
                            os.write(input, 0, input.length);
                        }
                    }
                    break;

                    case DELETE: {
                        urlConnection.setRequestMethod("DELETE");
                    }
                    break;
                }


                InputStream in = urlConnection.getInputStream();

                InputStreamReader isw = new InputStreamReader(in);

                int data = isw.read();
                while (data != -1) {
                    response += (char) data;
                    data = isw.read();
                }
                return response;

            } catch (Exception e) {

                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        delegate.processFinish(s);

    }
}
