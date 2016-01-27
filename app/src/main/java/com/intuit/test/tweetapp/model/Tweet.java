package com.intuit.test.tweetapp.model;

/**
 * Created by perk on 26/01/16.
 */
public class Tweet {
    String name, screen_name;
    String description;
    String profile_pic, profile_banner;
    String twitter_date;

    public Tweet(String name, String screen_name, String description, String profile_pic, String profile_banner, String twitter_date){
        this.name = name;
        this.screen_name = screen_name;
        this.description = description;
        this.profile_pic = profile_pic;
        this.profile_banner = profile_banner;
        this.twitter_date = twitter_date;
    }

    public String getName() {
        return name;
    }

    public String getScreenName () {
        return screen_name;
    }

    public String getDescription() {
        return description;
    }

    public String getProfilePic() {
        return profile_pic;
    }

    public String getProfileBanner() {
        return profile_banner;
    }

    public String getTwitDate() {
        return twitter_date;
    }
}
