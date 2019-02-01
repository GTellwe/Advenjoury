package com.silvergruppen.photoblog.adapters;

import android.content.Context;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.CompoundButton;
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
import com.silvergruppen.photoblog.fragments.DailyProgressFragment;
import com.silvergruppen.photoblog.holders.MyRecyclerViewHolder;
import com.silvergruppen.photoblog.items.Achievement;
import com.silvergruppen.photoblog.items.Catagorie;
import com.silvergruppen.photoblog.items.RecycleListItem;
import com.silvergruppen.photoblog.other.DailyProgress;
import com.silvergruppen.photoblog.viewmodels.DailyProgressViewModel;

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
    private DailyProgress dailyProgress;
    private DailyProgressViewModel viewModel;



    public DailyProgressRecyclerViewAdapter(Context context,
                                            DailyProgressViewModel viewModel) {


        if(viewModel.getDailyProgress().getValue() != null)
            dailyItems = viewModel.getDailyProgress().getValue().getDailyAchievementsList();
        else
            dailyItems = new ArrayList<>();
/*
        dailyItems = new ArrayList<>();
        dailyItems.add(new Achievement("Meditate","Daily","10",false,"daily",
                "Go to a quite and comfortable place and breathe in a rytmic pattern for 10 minutes"));
        dailyItems.add(new Achievement("Selfie","Daily","10",false,"daily",
                "Take a selfie and save it in an album"));
        dailyItems.add(new Achievement("Writing","Daily","10",false,"daily",
                "Write a small text and reflect on your day"));
        dailyItems.add(new Achievement("Get emotional","Daily","10",false,"daily",
                "Take a look at a cute animal"));
        dailyItems.add(new Achievement("Gratitude","Daily","10",false,"daily",
                "Look your self in the mirror and be thankful for all the opportunities and people you have around you"));
        dailyItems.add(new Achievement("Fresh air","Daily","10",false,"daily",
                " Go outside for at least 10 minutes to get som fresh air"));
        dailyItems.add(new Achievement("No sugar","Daily","10",false,"daily",
                "Dont eat any refined sugars"));
        dailyItems.add(new Achievement("Exercise","Daily","10",false,"daily",
                "Do at least 15 minutes of cardio"));
*/

        this.context = context;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        if(context.getClass() == MainActivity.class)
            mainActivity = (MainActivity) context;
        this.viewModel = viewModel;

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
    public void onBindViewHolder( MyRecyclerViewHolder holder,  final int position) {

        final Achievement tmpItem = dailyItems.get(position);

        holder.bind(tmpItem);


/*
        //ArrayList<Integer> tmpList = mainActivity.getDailyProgressHashMap().get(calendar);
        if(viewModel != null && viewModel.getDailyProgress().getValue().getDailyAchievementsList().get(position).isDone() ) {
            constraintLayout.getChildAt(5).setVisibility(View.VISIBLE);
            holder.getaSwitch().setChecked(true);
        } else {
            constraintLayout.getChildAt(5).setVisibility(View.INVISIBLE);
            holder.getaSwitch().setChecked(false);
        }
*/




        holder.getaSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {
                    updateDailyProgressHashMap(position,1);
                }
                else {
                    updateDailyProgressHashMap(position,0);

                }
                //notifyDataSetChanged();
            }
        });

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean open = tmpItem.isOpen();
                tmpItem.setOpen(!open, false);
                notifyItemChanged(position);
                /*
                Log.d("toggle "+tmpItem.getName()+" "+position,"\n \n \n \n");
                toggle(view, position, tmpItem);
                */
            }
        });
        /*
        ImageView downArraowImageView = holder.getDownArrowImageView();
        downArraowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle(view, position, tmpItem);
            }
        });
        /*
        ImageView upArrowImageView = holder.getUpArrowImageView();
        upArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle(view, position, tmpItem);
            }
        });
        */


    }

    private void updateDailyProgressHashMap(int position, int done){
/*
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
*/
        viewModel.updateDailyProgress(position, done);

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

        toggleAnimation( view,achievementItem, position, fromHeight, toHeight, true);
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
    private void toggleAnimation(final View view, final Achievement listItem, final int position,
                                 final int fromHeight, final int toHeight, final boolean goToItem) {

        ResizeAnimation resizeAnimation = new ResizeAnimation(view,this, listItem,  fromHeight, toHeight);
        resizeAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                listItem.setOpen(!listItem.isOpen(),false);
                //listItem.setCurrentHeight(toHeight);
                /*
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
                */
                //notifyDataSetChanged();
            }
        });

       listItem.getRecyclerHolder().parentLayout.startAnimation(resizeAnimation);
    }


    public DailyProgress getDailyProgress() {
        return dailyProgress;
    }

    public void setDailyProgress(DailyProgress dailyProgress) {
        this.dailyProgress = dailyProgress;
        dailyItems = dailyProgress.getDailyAchievementsList();
        notifyDataSetChanged();

    }


}