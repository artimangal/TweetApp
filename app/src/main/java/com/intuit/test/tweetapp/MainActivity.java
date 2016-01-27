package com.intuit.test.tweetapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    final static String TwitterTokenURL = "https://api.twitter.com/oauth2/token";
    private SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        editor.commit();


        Button btn_authenticate = (Button) findViewById(R.id.btn_authenticate);
        btn_authenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtain a bearer token. Note here it's done a bit cleaner, not doing heavy processing in the UI thread.
                new GetBearerToken().execute("");
            }
        });
    }

    private class GetBearerToken extends AsyncTask<String, Void, String> {
        ProgressDialog pdia;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(MainActivity.this);
            pdia.setMessage("Authenticating...");
            pdia.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // Step 1: Encode consumer key and secret
            String results = null;
            try {
                // URL encode the consumer key and secret
                String urlApiKey = URLEncoder.encode(getResources().getString(R.string.consumer_key), "UTF-8");
                String urlApiSecret = URLEncoder.encode(getResources().getString(R.string.consumer_secret), "UTF-8");

                String combined = urlApiKey + ":" + urlApiSecret;

                // Base64 encode the string
                String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);

                // Step 2: Obtain a bearer token
                HttpPost httpPost = new HttpPost(TwitterTokenURL);
                httpPost.setHeader("Authorization", "Basic " + base64Encoded);
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                httpPost.setEntity(new StringEntity("grant_type=client_credentials"));
                results = getResponseBody(httpPost);

            } catch (UnsupportedEncodingException ex) {
            } catch (IllegalStateException ex1) {
            }
            return results;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(pdia != null){
                pdia.dismiss();
            }
            if(result != null){
                try{
                    JSONObject obj = new JSONObject(result);
                    if(obj.has("token_type") && obj.getString("token_type").equalsIgnoreCase("bearer")){
                        if(obj.has("access_token")){
                            String access_token = obj.getString("access_token");
                            Log.d("xyz", "access_token " + access_token);
                            editor.putString("access_token", access_token);
                            editor.commit();
                            Intent intent = new Intent(MainActivity.this, TweetActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                } catch (JSONException e){

                }
            }

        }
    }

    private String getResponseBody(HttpPost post) {
        StringBuilder sb = new StringBuilder();
        try {

            HttpParams params = new BasicHttpParams();
            params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                    HttpVersion.HTTP_1_1);
            HttpClient httpClient = new DefaultHttpClient(params);
            org.apache.http.HttpResponse response = httpClient.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            String reason = response.getStatusLine().getReasonPhrase();
            Log.d("xyz","status code "+statusCode);
            if (statusCode == 200) {

                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();

                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sb.append(line);
                }
            } else {
                sb.append(reason);
            }
        } catch (UnsupportedEncodingException ex) {
        } catch (ClientProtocolException ex1) {
        } catch (IOException ex2) {
        }
        Log.d("xyz","status  "+sb.toString());
        return sb.toString();
    }
}
