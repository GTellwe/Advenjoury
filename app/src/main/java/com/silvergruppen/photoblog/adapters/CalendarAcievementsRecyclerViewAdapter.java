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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.animations.ResizeAnimation;
import com.silvergruppen.photoblog.fragments.CalendarFragment;
import com.silvergruppen.photoblog.holders.CalendarRecyclerViewHolder;
import com.silvergruppen.photoblog.holders.MyRecyclerViewHolder;
import com.silvergruppen.photoblog.items.Achievement;
import com.silvergruppen.photoblog.other.DailyProgress;
import com.silvergruppen.photoblog.other.MonthlyProgress;
import com.silvergruppen.photoblog.other.WeekleyProgress;
import com.silvergruppen.photoblog.viewmodels.ProgressViewModel;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarAcievementsRecyclerViewAdapter extends RecyclerView.Adapter<CalendarRecyclerViewHolder> {


    // lists
    private ArrayList<Achievement> items;

    // firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    // strings
    private String user_id;

    // keys
    private final String DAILY_KEY = "DailyAchievements";
    private final String WEEKLY_KEY = "WeekleyAchievements";
    private final String MONTHLY_KEY = "MonthlyAchievements";

    // other
    private Context context;
    private MainActivity mainActivity;
    private Calendar calendar;
    private DailyProgress dailyProgress;
    private ProgressViewModel viewModel;
    private WeekleyProgress weekleyProgress;
    private MonthlyProgress monthlyProgress;
    private final static int dailyId=1, weekleyId = 2, monthlyId = 3;
    private CalendarFragment fragment;
    private String achivementKey;


    public CalendarAcievementsRecyclerViewAdapter(Context context, CalendarFragment fragment) {

        items = new ArrayList<>();
        this.context = context;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();

        if(context.getClass() == MainActivity.class)
            mainActivity = (MainActivity) context;

        this.fragment = fragment;


    }

    @Override
    public CalendarRecyclerViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        ConstraintLayout
                v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_achievement_list_item, parent, false);


        CalendarRecyclerViewHolder vh = new CalendarRecyclerViewHolder(v);
        return vh;
    }



    @Override
    public void onBindViewHolder(final CalendarRecyclerViewHolder holder, final int position) {

        final Achievement tmpItem = items.get(position);

        // set the checkbox
        holder.getaSwitch().setChecked(tmpItem.isDone());

        holder.bind(tmpItem);

        // handle the remove achievement button
        holder.getRemoveAchievementButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // remove the achievement from firebase and update the list
                fragment.removeAchievement(holder.getNameTextView().getText().toString());
            }
        });

        // update firebase when swich is checked or unchcecked
        holder.getaSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                // update firebase
                // get wich day month or week it is
                String dayMonth;
                switch (achivementKey){

                    case DAILY_KEY:
                        dayMonth = Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
                        break;
                    case WEEKLY_KEY:
                        dayMonth = Integer.toString(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
                        break;
                    case MONTHLY_KEY:
                        dayMonth = Integer.toString(Calendar.getInstance().get(Calendar.MONTH));
                        break;

                    default:
                        dayMonth = "0";

                            break;
                }
                fragment.getCalendarViewModel().updateAchievementDone(achivementKey, tmpItem,b,dayMonth);

                // update the UI progress
                fragment.updateProgress(achivementKey, b);


            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();

    }

    public ArrayList<Achievement> getItems() {
        return items;
    }

    public void setItems(ArrayList<Achievement> items) {
        this.items = items;
    }

    public void setAchivementKey(String achivementKey){
        this.achivementKey = achivementKey;

    }



}