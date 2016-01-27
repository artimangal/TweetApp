package com.intuit.test.tweetapp;

import android.app.Application;

import com.intuit.test.tweetapp.model.Tweet;

/**
 * Created by perk on 27/01/16.
 */
public class TweetApplication extends Application{
    public Tweet tweet;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setCurrentTweet(Tweet tweet){
        this.tweet = tweet;
    }

    public Tweet getCurrentTweet() {
        return tweet;
    }
}
