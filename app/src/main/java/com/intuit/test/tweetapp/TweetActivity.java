package com.intuit.test.tweetapp;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import com.intuit.test.tweetapp.adapter.TwitRecyclerAdapter;
import com.intuit.test.tweetapp.model.Tweet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class TweetActivity extends AppCompatActivity {

    ProgressBar progressBar;
    final static String TwitterStreamURL = "https://api.twitter.com/1.1/search/tweets.json?q=";
    SharedPreferences preferences;
    ArrayList<Tweet> twits;
    private RecyclerView listRecyclerView;
    private TwitRecyclerAdapter adapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        preferences = PreferenceManager.getDefaultSharedPreferences(TweetActivity.this);

        twits = new ArrayList<>();
        twits.clear();

        init();

        new GetTweets().execute("");
    }

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mLayoutManager = new LinearLayoutManager(TweetActivity.this);
        listRecyclerView = (RecyclerView) findViewById(R.id.recycleview);
        listRecyclerView.setHasFixedSize(true);
        listRecyclerView.setLayoutManager(mLayoutManager);
        implementScrollListener();

        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresh.setColorSchemeResources(R.color.material_green,R.color.material_red, R.color.material_blue);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
    }
    SwipeRefreshLayout refresh;
    private void refreshContent() {
        progressBar.setVisibility(View.VISIBLE);
        twits.clear();
        new GetTweets().execute("");
    }

    public void updateUI() {

        if(adapter == null) {
            progressBar.setVisibility(View.GONE);
            adapter = new TwitRecyclerAdapter(this, twits);
            listRecyclerView.setAdapter(adapter);// set adapter on recyclerview
            adapter.notifyDataSetChanged();
        } else {
            progressBar.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
            refresh.setRefreshing(false);
        }

    }

    boolean userScrolled = false;

    // Implement scroll listener
    private void implementScrollListener() {
        listRecyclerView
                .addOnScrollListener(new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView,
                                                     int newState) {

                        super.onScrollStateChanged(recyclerView, newState);

                        // If scroll state is touch scroll then set userScrolled
                        // true
                        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                            userScrolled = true;

                        }

                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx,
                                           int dy) {

                        super.onScrolled(recyclerView, dx, dy);
                        // Here get the child count, item count and visibleitems
                        // from layout manager

                        int visibleItemCount = mLayoutManager.getChildCount();
                        int totalItemCount = mLayoutManager.getItemCount();
                        int pastVisiblesItems = mLayoutManager
                                .findFirstVisibleItemPosition();

                        // Now check if userScrolled is true and also check if
                        // the item is end then update recycler view and set
                        // userScrolled to false
                        if (userScrolled
                                && (visibleItemCount + pastVisiblesItems) == totalItemCount) {
                            userScrolled = false;

                            //updateRecyclerView();
                        }

                    }

                });

    }

    private class GetTweets extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String results = null;
            try {
                HttpGet httpGet = new HttpGet(TwitterStreamURL + "indiGo6E&count=40");

                // construct a normal HTTPS request and include an Authorization
                // header with the value of Bearer <>
                httpGet.setHeader("Authorization", "Bearer " + preferences.getString("access_token", ""));
                httpGet.setHeader("Content-Type", "application/json");
                // update the results with the body of the response
                results = getResponseBody(httpGet);
            } catch (Exception e) {

            }
            return results;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.has("statuses")) {
                        JSONArray array = object.getJSONArray("statuses");
                        Log.d("xyz", "length " + array.length());
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            if (obj.has("user")) {
                                JSONObject user_obj = obj.getJSONObject("user");
                                twits.add(new Tweet(user_obj.has("name") ? user_obj.getString("name") : "", user_obj.has("screen_name") ? user_obj.getString("screen_name") : "", user_obj.has("description") ? user_obj.getString("description") : "", user_obj.has("profile_image_url") ? user_obj.getString("profile_image_url") : "", user_obj.has("profile_banner_url") ? user_obj.getString("profile_banner_url") : "", user_obj.has("created_at") ? user_obj.getString("created_at") : ""));
                            }
                        }
                        updateUI();
                    }
                } catch (JSONException e) {

                }
            }
        }
    }

    private String getResponseBody(HttpGet get) {
        StringBuilder sb = new StringBuilder();
        try {

            HttpParams params = new BasicHttpParams();
            params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                    HttpVersion.HTTP_1_1);
            HttpClient httpClient = new DefaultHttpClient(params);
            org.apache.http.HttpResponse response = httpClient.execute(get);
            int statusCode = response.getStatusLine().getStatusCode();
            String reason = response.getStatusLine().getReasonPhrase();
            Log.d("xyz", "status code " + statusCode);
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
        Log.d("xyz", "status  " + sb.toString());
        return sb.toString();
    }

}
