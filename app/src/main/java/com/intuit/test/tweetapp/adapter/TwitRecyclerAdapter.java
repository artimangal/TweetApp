package com.intuit.test.tweetapp.adapter;

/**
 * Created by perk on 02/11/15.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.intuit.test.tweetapp.DetailActivity;
import com.intuit.test.tweetapp.R;
import com.intuit.test.tweetapp.TweetApplication;
import com.intuit.test.tweetapp.model.Tweet;

import java.util.ArrayList;

/*This class represents the Adapter for the Tweet List*/

public class TwitRecyclerAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {// Recyclerview will extend to
    // recyclerview adapter
    private ArrayList<Tweet> arrayList;
    private Context context;
    private TweetApplication tweetApplication;


    public TwitRecyclerAdapter(Context context,
                               ArrayList<Tweet> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        tweetApplication = (TweetApplication)context.getApplicationContext();
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    /*public void removeItem(int index) {
        try {
            arrayList.remove(index);
            notifyItemRemoved(index);
            notifyItemRangeRemoved(index, 1);
            notifyItemRangeChanged(index, arrayList.size());
            notifyDataSetChanged();
        } catch (IndexOutOfBoundsException e) {
            notifyDataSetChanged();
            e.printStackTrace();
        }
    }*/

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Tweet tweet = arrayList.get(position);

        final TwitRecyclerHolder mainHolder = (TwitRecyclerHolder) holder;// holder

        if (tweet.getProfilePic() != null && !tweet.getProfilePic().isEmpty() && !tweet.getProfilePic().equals("null")) {
            mainHolder.thumbnail_layout.setVisibility(View.VISIBLE);
            Glide.with(context).load(tweet.getProfilePic())
                    .centerCrop().into(mainHolder.profile_thumbnail);
        } else {
            mainHolder.thumbnail_layout.setVisibility(View.GONE);
        }

        mainHolder.user_name.setText(tweet.getName());
        mainHolder.screen_name.setText("@"+Html.fromHtml(tweet.getScreenName()));
        mainHolder.tweet_desc.setText(Html.fromHtml(tweet.getDescription()));
        mainHolder.tweet_time.setText(tweet.getTwitDate());

        if (tweet.getProfileBanner() != null && !tweet.getProfileBanner().isEmpty() && !tweet.getProfileBanner().equals("null")) {
            mainHolder.profile_banner.setVisibility(View.VISIBLE);
            Glide.with(context).load(tweet.getProfileBanner())
                    .centerCrop().into(mainHolder.profile_banner);
        } else {
            mainHolder.profile_banner.setVisibility(View.GONE);
        }

        // Implement click listener over layout
        mainHolder.setClickListener(new RecyclerView_OnClickListener.OnClickListener() {

            @Override
            public void OnItemClick(View view, int position) {
                tweetApplication.setCurrentTweet(tweet);
                Intent intent = new Intent(context, DetailActivity.class);
                context.startActivity(intent);
            }

        });

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.twit_page_item, viewGroup, false);
        RecyclerView.ViewHolder listHolder = new TwitRecyclerHolder(mainGroup);
        return listHolder;

    }

    /*public static String findDiff(String dt2) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.US);
        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = (Date) cal.getTime();
        String dt1 = format.format(currentLocalTime);

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = (Date) format.parse(dt1);
            d2 = (Date) format.parse(dt2);

            // in milliseconds
            long diff = d1.getTime() - d2.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            long diffWeeks = diff / (24 * 60 * 60 * 1000 * 7);
            //long diffMonths = (long) (diff / (24 * 60 * 60 * 1000 * 30.41666666));
            long diffYears = diff / (24 * 60 * 60 * 1000) / 365;

//            Log.d("xyz", diff+" milliseconds "+diffYears+" Years "+diffWeeks+" Weeks "+diffDays + " Days " + diffHours + " Hours " + diffMinutes
//                    + " Minutes ");

            if(diffYears > 0) {
                return diffYears+"y ago";
            }else if(diffWeeks > 0) {
                return diffWeeks+"w ago";
            }else if(diffDays > 0){
                return diffDays+"d ago";
            } else if (diffHours >0){
                return diffHours+"h ago";
            } else if (diffMinutes >0){
                return diffMinutes+"m ago";
            } else if(diffSeconds > 0) {
                return diffSeconds+"s ago";
            } else if (diffSeconds == 0) {
                return "Just Now";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }*/

}
