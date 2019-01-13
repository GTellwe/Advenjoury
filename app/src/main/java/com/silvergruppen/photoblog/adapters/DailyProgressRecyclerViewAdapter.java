package com.silvergruppen.photoblog.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.animations.ResizeAnimation;
import com.silvergruppen.photoblog.holders.MyRecyclerViewHolder;
import com.silvergruppen.photoblog.items.Achievement;
import com.silvergruppen.photoblog.items.Catagorie;
import com.silvergruppen.photoblog.items.RecycleListItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DailyProgressRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {

    private ArrayList<Achievement> dailyItems;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private MainActivity mainActivity;
    private Calendar calendar;



    public DailyProgressRecyclerViewAdapter(Context context,
                              ArrayList<Achievement> dailyItems) {
        this.dailyItems = dailyItems;
        this.context = context;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        if(context.getClass() == MainActivity.class)
            mainActivity = (MainActivity) context;

    }

    @Override
    public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        CardView
                v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_progress_list_item, parent, false);
        MyRecyclerViewHolder vh = new MyRecyclerViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyRecyclerViewHolder holder, final int position) {

        final Achievement tmpItem = dailyItems.get(position);
        tmpItem.setRecyclerHolder(holder);



        //holder.parentLayout.setTranslationZ(cardView.getElevation()+10*position);
        final ConstraintLayout constraintLayout = (ConstraintLayout) holder.parentLayout.getChildAt(0);

        holder.setaSwitch((Switch) constraintLayout.getChildAt(3));
        holder.setDescriptionTextView((TextView)constraintLayout.getChildAt(4));
        holder.setDownArrowImageView((ImageView) constraintLayout.getChildAt(1));
        holder.setUpArrowImageView((ImageView) constraintLayout.getChildAt(2));

        ArrayList<Integer> tmpList = mainActivity.getDailyProgressHashMap().get(calendar);
        if(tmpList != null && tmpList.get(position) == 1 ) {
            constraintLayout.getChildAt(5).setVisibility(View.VISIBLE);
            holder.getaSwitch().setChecked(true);
        } else {
            constraintLayout.getChildAt(5).setVisibility(View.INVISIBLE);
            holder.getaSwitch().setChecked(false);
        }


        holder.getaSwitch().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.getaSwitch().isChecked()) {
                    updateDailyProgressHashMap(position,1);
                }
                else {
                    updateDailyProgressHashMap(position,0);

                }
                notifyDataSetChanged();
            }
        });

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle(view, position, tmpItem);
            }
        });
        TextView textView = (TextView) constraintLayout.getChildAt(0);
        textView.setText(tmpItem.getName());

    }

    private void updateDailyProgressHashMap(int position, int done){

        HashMap<Calendar, ArrayList<Integer>> dailyProgressHashMap = mainActivity.getDailyProgressHashMap();
        ArrayList<Integer> dailyProgressList = dailyProgressHashMap.get(calendar);

        if(dailyProgressList ==  null) {
            ArrayList<Integer> tmpDailyProgressList = new ArrayList<>();
            tmpDailyProgressList.add(0);
            tmpDailyProgressList.add(0);
            tmpDailyProgressList.add(0);
            tmpDailyProgressList.add(position,done);
            dailyProgressHashMap.put(calendar,tmpDailyProgressList);
        } else {

            dailyProgressList.set(position,done);
        }

    }

    @Override
    public int getItemCount() {
        return dailyItems.size();

    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    private void toggle(View view, final int position, Achievement achievementItem) {

        int fromHeight = 0;
        int toHeight = 0;

        if (achievementItem.isOpen()) {

            fromHeight = achievementItem.getExpandedHeight();
            toHeight = achievementItem.getCollapsedHeight();
        } else {
            fromHeight = achievementItem.getCollapsedHeight();
            toHeight = achievementItem.getExpandedHeight();

        }

        toggleAnimation(achievementItem, position, fromHeight, toHeight, true);
    }
    /*
    private void closeAll() {
        int i = 0;
        for (Achievement achievementItem : dailyItems) {
            if (achievementItem.isOpen()) {
                toggleAnimation(achievementItem, i, achievementItem.getExpandedHeight(),
                        achievementItem.getCollapsedHeight(), false);
            }
            i++;
        }
    }
*/
    private void toggleAnimation(final Achievement listItem, final int position,
                                 final int fromHeight, final int toHeight, final boolean goToItem) {

        ResizeAnimation resizeAnimation = new ResizeAnimation(this, listItem,  fromHeight, toHeight);
        resizeAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                listItem.setOpen(!listItem.isOpen(), false);
                listItem.setCurrentHeight(toHeight);
                if(!listItem.isOpen()){
                    listItem.setSwitchVisibility(View.VISIBLE);
                    listItem.setUppArrowVisibility(View.VISIBLE);
                    listItem.setDownArrowVisibility(View.INVISIBLE);
                    listItem.setDescriptionTextViewVisibility(View.VISIBLE);
                }
                else {
                    listItem.setSwitchVisibility(View.INVISIBLE);
                    listItem.setUppArrowVisibility(View.INVISIBLE);
                    listItem.setDownArrowVisibility(View.VISIBLE);
                    listItem.setDescriptionTextViewVisibility(View.INVISIBLE);

                }
                notifyDataSetChanged();
            }
        });

       listItem.getRecyclerHolder().parentLayout.startAnimation(resizeAnimation);
    }






}