package com.intuit.test.tweetapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.intuit.test.tweetapp.model.Tweet;

public class DetailActivity extends AppCompatActivity {

    TweetApplication tweetApplication;
    Tweet detailTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tweetApplication = (TweetApplication) getApplicationContext();
        detailTweet = tweetApplication.getCurrentTweet();

        if (detailTweet != null) {
            initUI();
        } else {
            Toast.makeText(this, "Can't show tweet details!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initUI() {
        FrameLayout thumbnail_layout = (FrameLayout) findViewById(R.id.thumbnail_layout);
        ImageView profile_thumbnail = (ImageView) findViewById(R.id.profile_thumbnail);
        if (detailTweet.getProfilePic() != null && !detailTweet.getProfilePic().isEmpty() && !detailTweet.getProfilePic().equals("null")) {
            thumbnail_layout.setVisibility(View.VISIBLE);
            Glide.with(DetailActivity.this).load(detailTweet.getProfilePic())
                    .centerCrop().into(profile_thumbnail);
        } else {
            thumbnail_layout.setVisibility(View.GONE);
        }


        ((TextView)findViewById(R.id.user_name)).setText(detailTweet.getName());
        ((TextView)findViewById(R.id.screen_name)).setText("@" + Html.fromHtml(detailTweet.getScreenName()));
        ((TextView)findViewById(R.id.tweet_desc)).setText(Html.fromHtml(detailTweet.getDescription()));
        ((TextView)findViewById(R.id.tweet_time)).setText(detailTweet.getTwitDate());

        ImageView profile_banner = (ImageView)findViewById(R.id.profile_banner);
        if (detailTweet.getProfileBanner() != null && !detailTweet.getProfileBanner().isEmpty() && !detailTweet.getProfileBanner().equals("null")) {
            profile_banner.setVisibility(View.VISIBLE);
            Glide.with(DetailActivity.this).load(detailTweet.getProfileBanner())
                    .centerCrop().into(profile_banner);
        } else {
            profile_banner.setVisibility(View.GONE);
        }
    }
}
