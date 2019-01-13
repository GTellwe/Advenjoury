package com.silvergruppen.photoblog.animations;


import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

import com.silvergruppen.photoblog.adapters.AchievementListAdapter;
import com.silvergruppen.photoblog.adapters.DailyProgressRecyclerViewAdapter;
import com.silvergruppen.photoblog.items.Achievement;

public class ResizeAnimation extends Animation {
    private CardView mView;
    private float mToHeight;
    private float mFromHeight;


    private DailyProgressRecyclerViewAdapter mListAdapter;
    private Achievement mListItem;

    public ResizeAnimation(DailyProgressRecyclerViewAdapter listAdapter, Achievement listItem, float fromHeight, float toHeight) {
        mToHeight = toHeight;
        mFromHeight = fromHeight;
        mView = listItem.getRecyclerHolder().parentLayout;
        mListAdapter = listAdapter;
        mListItem = listItem;
        setDuration(200);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float height = (mToHeight - mFromHeight) * interpolatedTime
                + mFromHeight;
        ViewGroup.LayoutParams p = (ViewGroup.LayoutParams) mView.getLayoutParams();
        p.height = (int) height;
        mListItem.setCurrentHeight(p.height);
        mListAdapter.notifyDataSetChanged();
    }
}
