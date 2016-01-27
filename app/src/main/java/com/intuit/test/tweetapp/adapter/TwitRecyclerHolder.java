package com.intuit.test.tweetapp.adapter;

/**
 * Created by perk on 02/11/15.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.intuit.test.tweetapp.R;


public class TwitRecyclerHolder extends RecyclerView.ViewHolder implements
        OnClickListener {
    // View holder for list recycler view as we used in listview
    public FrameLayout thumbnail_layout;
    TextView user_name;
    TextView screen_name;
    TextView tweet_desc;
    TextView tweet_time;
    ImageView profile_thumbnail, profile_banner;

    private RecyclerView_OnClickListener.OnClickListener onClickListener;

    public TwitRecyclerHolder(View v) {
        super(v);

        // Find all views ids
        this.thumbnail_layout = (FrameLayout) v.findViewById(R.id.thumbnail_layout);
        this.user_name = (TextView) v.findViewById(R.id.user_name);
        this.screen_name = (TextView) v.findViewById(R.id.screen_name);
        this.tweet_desc = (TextView) v.findViewById(R.id.tweet_desc);
        this.tweet_time = (TextView) v.findViewById(R.id.tweet_time);
        this.profile_thumbnail = (ImageView) v.findViewById(R.id.profile_thumbnail);
        this.profile_banner = (ImageView) v.findViewById(R.id.profile_banner);
        // Implement click listener over views that we need

        v.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        // setting custom listener
        if (onClickListener != null) {
            onClickListener.OnItemClick(v, getAdapterPosition());

        }

    }

    // Setter for listener
    public void setClickListener(
            RecyclerView_OnClickListener.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
