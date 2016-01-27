package com.intuit.test.tweetapp.adapter;

import android.view.View;

/**
 * Created by perk on 02/11/15.
 */
public class RecyclerView_OnClickListener {
    /** Interface for Item Click over Recycler View Items **/
    public interface OnClickListener {
        public void OnItemClick(View view, int position);
    }
}
