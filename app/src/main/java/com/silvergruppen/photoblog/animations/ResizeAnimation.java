package com.silvergruppen.photoblog.animations;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Toast;

import com.silvergruppen.photoblog.listAdapters.AchievementListAdapter;
import com.silvergruppen.photoblog.listItems.Achievement;

public class ResizeAnimation extends Animation {
    private View mView;
    private float mToHeight;
    private float mFromHeight;

    private float mToWidth;
    private float mFromWidth;

    private AchievementListAdapter mListAdapter;
    private Achievement mListItem;

    public ResizeAnimation(AchievementListAdapter listAdapter, Achievement listItem,
                           float fromWidth, float fromHeight, float toWidth, float toHeight) {
        mToHeight = toHeight;
        mToWidth = toWidth;
        mFromHeight = fromHeight;
        mFromWidth = fromWidth;
        mView = listItem.getHolder().getTextViewWrap();
        mListAdapter = listAdapter;
        mListItem = listItem;
        setDuration(200);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float height = (mToHeight - mFromHeight) * interpolatedTime
                + mFromHeight;
        float width = (mToWidth - mFromWidth) * interpolatedTime + mFromWidth;
        ViewGroup.LayoutParams p = (ViewGroup.LayoutParams) mView.getLayoutParams();
        p.height = (int) height;
        p.width = (int) width;
        mListItem.setCurrentHeight(p.height);
        mListAdapter.notifyDataSetChanged();
    }
}
