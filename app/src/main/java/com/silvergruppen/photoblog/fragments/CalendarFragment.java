package com.silvergruppen.photoblog.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.adapters.CalendarAcievementsRecyclerViewAdapter;
import com.silvergruppen.photoblog.adapters.DailyProgressRecyclerViewAdapter;
import com.silvergruppen.photoblog.adapters.WeekleyGridViewAdapter;
import com.silvergruppen.photoblog.items.Achievement;
import com.silvergruppen.photoblog.items.CalendarItem;
import com.silvergruppen.photoblog.adapters.CalendarGridViewAdapter;
import com.silvergruppen.photoblog.items.WeekleyItem;
import com.silvergruppen.photoblog.other.MonthlyProgress;
import com.silvergruppen.photoblog.other.User;
import com.silvergruppen.photoblog.viewmodels.CalendarViewModel;
import com.silvergruppen.photoblog.viewmodels.ProgressViewModel;
import com.silvergruppen.photoblog.viewmodels.UserProfileViewModel;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class CalendarFragment extends Fragment {

    // Constants
    private final static int dailyId=1, weekleyId = 2, monthlyId = 3;
    private static final String UID_KEY = "uid";
    private final String DAILY_KEY = "DailyAchievements";
    private final String WEEKLY_KEY = "WeekleyAchievements";
    private final String MONTHLY_KEY = "MonthlyAchievements";

    // Views
    private GridView calendarGridView, weekleyGridView;
    private TextView monthTextView, monthProgressTextView;
    private TextView dailyProgressFractionTextView,weekleyProgressFractionTextView,monthlyProgressFractionTextView;
    private LinearLayout monthTextLayout;
    private ProgressBar dailyProgressbar, weekleyProgressbar, monthlyProgressbar;
    private ConstraintLayout expandedImage;
    private RelativeLayout mainContainer;
    private RecyclerView achievementsRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;


    // Adapters
    private CalendarGridViewAdapter calendarGridViewAdapter;
    private WeekleyGridViewAdapter weekleyGridViewAdapter;

    //Lists
    private ArrayList<CalendarItem> calendarItems, weekleyItems;
    private String[] monthNames =  {"January", "February",
            "March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December"};
    private ArrayList<Achievement> dailyList, weeklyList, monthlyList;

    // other
    private CalendarViewModel calendarViewModel;
    private Calendar calendar;
    private int shortAnimationDuration = 200;
    private Animator currentAnimator;
    private CalendarAcievementsRecyclerViewAdapter recycleViewAdapter;
    private String currentVisibleAchievementList;

    // Strings
    private String userId;



    public CalendarFragment(){

        // add the Calendar Items to be visible in current month
        calendarItems = computeCalendarItemsForDaysInMonth(Calendar.getInstance());

        // set up the weekley items list
        weekleyItems  = computeCalendarITemsForWeeks(calendarItems);


        // set up the achievementsLists
        dailyList = new ArrayList<>();

        weeklyList = new ArrayList<>();

        monthlyList = new ArrayList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_progress, container, false);

        // get arguments
        userId = getArguments().getString(UID_KEY);

        // initialize view model
        calendarViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        calendarViewModel.init(userId);

        // update current calendar
        calendar = Calendar.getInstance();

        // setup the progressbars
        dailyProgressbar = view.findViewById(R.id.daily_progressbar);
        weekleyProgressbar = view.findViewById(R.id.weekly_progressbar);
        monthlyProgressbar = view.findViewById(R.id.monthly_progressbar);

        dailyProgressFractionTextView = view.findViewById(R.id.daily_progress_fraction_text_view);
        weekleyProgressFractionTextView = view.findViewById(R.id.weekly_progress_fraction_text_view);
        monthlyProgressFractionTextView = view.findViewById(R.id.monthly_progress_fraction_text_view);

        // load data
        final Observer<HashMap<String, ArrayList<Achievement>>> userObserver = new Observer<HashMap<String, ArrayList<Achievement>>>() {
            @Override
            public void onChanged(@Nullable final HashMap<String, ArrayList<Achievement>> newDoneMatrix) {
                updateCalendarItems(newDoneMatrix, DAILY_KEY);
                if(newDoneMatrix.get(Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_YEAR))) != null)
                    dailyList = newDoneMatrix.get(Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_YEAR)));

                recycleViewAdapter.notifyDataSetChanged();
            }
        };

        calendarViewModel.getDoneAchievementsMatrix().observe(this,userObserver);

        final Observer<HashMap<String, ArrayList<Achievement>>> weekleyObserver = new Observer<HashMap<String, ArrayList<Achievement>>>() {
            @Override
            public void onChanged(@Nullable final HashMap<String, ArrayList<Achievement>> newDoneMatrix) {
                updateCalendarItems(newDoneMatrix, WEEKLY_KEY);
                if(newDoneMatrix.get(Integer.toString(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR))) != null)
                    weeklyList = newDoneMatrix.get(Integer.toString(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)));
            }
        };

        calendarViewModel.getDoneWeekleyAchievementsMatrix().observe(this,weekleyObserver);

        final Observer<HashMap<String, ArrayList<Achievement>>> monthlyObserver = new Observer<HashMap<String, ArrayList<Achievement>>>() {
            @Override
            public void onChanged(@Nullable final HashMap<String, ArrayList<Achievement>> newDoneMatrix) {
                /*int[] currentMonthMonthlyAchievementsDone = newDoneMatrix[calendar.get(Calendar.MONTH)-1];
                int monthlyProgress =0;
                for( int i=0; i<currentMonthMonthlyAchievementsDone.length; i++)
                    if(currentMonthMonthlyAchievementsDone[i] == 1) {
                        monthlyList.get(i).setDone(true);
                        monthlyProgress++;
                    }
                monthProgressTextView.setText(monthlyProgress+"/1");
                if(monthlyProgress == 1) {
                    //monthTextLayout.setBackgroundResource(R.drawable.monthbackground);
                    monthProgressTextView.setTextColor(Color.WHITE);
                    monthProgressTextView.setBackgroundResource(R.drawable.calendar_item_background_drawable);
                    //monthTextView.setTextColor(Color.WHITE);
                }

                // set the monthly progressbar progress
                monthlyProgressbar.setProgress(monthlyProgress);
                monthlyProgressFractionTextView.setText(monthlyProgress+"/1");
                */
            }
        };

        calendarViewModel.getDoneMonthlyAchievementsMatrix().observe(this, monthlyObserver);

        // set up the grid views
        calendarGridView = view.findViewById(R.id.daily_progress_calendar_grid_view);
        weekleyGridView = view.findViewById(R.id.weekley_list_view);

        calendarGridViewAdapter = new CalendarGridViewAdapter(getContext(), calendarItems, this);
        calendarGridView.setAdapter(calendarGridViewAdapter);

        weekleyGridViewAdapter = new WeekleyGridViewAdapter(getContext(), weekleyItems);
        weekleyGridView.setAdapter(weekleyGridViewAdapter);

        // set the month name
        monthTextView = view.findViewById(R.id.daily_progress_month_text);
        calendar = Calendar.getInstance();
        String month  = monthNames[calendar.get(Calendar.MONTH)];
        monthTextView.setText(month);

        // set month progress
        monthTextLayout = view.findViewById(R.id.daily_progress_month_text_layout);
        monthProgressTextView = view.findViewById(R.id.calendar_monthly_progress_text);
        monthProgressTextView.setText("0/1");


        // set the on click listener to the monthly progress
        monthTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getContext();
                mainActivity.startDailyProgressFragment(Calendar.getInstance(),monthlyId);
            }
        });

        // setup the pop up views and lists
        expandedImage = view.findViewById(R.id.achievements_list_container);
        mainContainer = view.findViewById(R.id.container);

        achievementsRecyclerView = (RecyclerView) view.findViewById(R.id.calendar_achievement_recycler_view);
        achievementsRecyclerView.setHasFixedSize(true);

        ((SimpleItemAnimator) achievementsRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        mLayoutManager = new LinearLayoutManager(getActivity());
        achievementsRecyclerView.setLayoutManager(mLayoutManager);
        recycleViewAdapter = new CalendarAcievementsRecyclerViewAdapter(getActivity(), this);
        recycleViewAdapter.setItems(dailyList);
        achievementsRecyclerView.setAdapter(recycleViewAdapter);
        recycleViewAdapter.notifyDataSetChanged();

        dailyProgressbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recycleViewAdapter.setItems(dailyList);
                recycleViewAdapter.notifyDataSetChanged();
                currentVisibleAchievementList = DAILY_KEY;
                zoomImageFromThumb(view, R.drawable.icon);
            }
        });

        weekleyProgressbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recycleViewAdapter.setItems(weeklyList);
                recycleViewAdapter.notifyDataSetChanged();
                currentVisibleAchievementList = WEEKLY_KEY;
                zoomImageFromThumb(view, R.drawable.icon);
            }
        });

        monthlyProgressbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recycleViewAdapter.setItems(monthlyList);
                recycleViewAdapter.notifyDataSetChanged();
                currentVisibleAchievementList = MONTHLY_KEY;
                zoomImageFromThumb(view, R.drawable.icon);
            }
        });

        return view;
    }

    private void updateCalendarItems(HashMap<String, ArrayList<Achievement>> newDoneMap, String achievementType){



        ArrayList<CalendarItem> newCalendarItems;

        if(DAILY_KEY.equals(achievementType))
             newCalendarItems = calendarGridViewAdapter.getCalendarItems();

        else
            newCalendarItems = weekleyGridViewAdapter.getWeekleyItems();

        for(int i =0; i<newCalendarItems.size();i++){

            CalendarItem item = newCalendarItems.get(i);

                int progress = 0;
                int nmbOfAchievements =0;
                int tmpAchievementKey;
               if(achievementType.equals(DAILY_KEY))
                    tmpAchievementKey = Calendar.DAY_OF_YEAR;
                else
                  tmpAchievementKey = Calendar.WEEK_OF_YEAR;

                if(newDoneMap.get(Integer.toString(item.getCalendar().get(tmpAchievementKey))) != null) {
                    ArrayList<Achievement> dailyAchievements = newDoneMap.get(Integer.toString(item.getCalendar().get(tmpAchievementKey)));
                    for(Achievement achievement : dailyAchievements) {
                        if(achievement.isDone())
                            progress++;
                    }
                    nmbOfAchievements = dailyAchievements.size();

                }

                item.setProgress(progress);

                // update the progress of the
                if(item.getCalendar().get(tmpAchievementKey) == Calendar.getInstance().get(tmpAchievementKey)) {
                    if(DAILY_KEY.equals(achievementType)) {
                        dailyProgressbar.setProgress(progress);
                        dailyProgressFractionTextView.setText(progress + "/" + nmbOfAchievements);
                    }else{
                        weekleyProgressbar.setProgress(progress);
                        weekleyProgressFractionTextView.setText(progress + "/" + nmbOfAchievements);
                    }

                }



        }
        if(achievementType.equals(DAILY_KEY)) {
            //calendarGridViewAdapter.setCalendarItems(newCalendarItems);
            //calendarGridViewAdapter.notifyDataSetChanged();
        }else{
            weekleyGridViewAdapter.setWeekleyItems(newCalendarItems);
            weekleyGridViewAdapter.notifyDataSetChanged();
        }
    }

    public void zoomImageFromThumb(final View thumbView, int imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ConstraintLayout expandedImageView = expandedImage;
        //expandedImageView.setImageResource(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        mainContainer
                .getGlobalVisibleRect(finalBounds, globalOffset);
        //expandedImageView.getGlobalVisibleRect(finalBounds);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        int offsetX = (mainContainer.getWidth()-expandedImageView.getWidth())/2;
        int offsetY = (mainContainer.getHeight()-expandedImageView.getHeight())/2-140;

        finalBounds.offset(-globalOffset.x+offsetX, -globalOffset.y+offsetY);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(shortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }
                });
                set.start();
                currentAnimator = set;
            }
        });
    }
    private ArrayList<CalendarItem> computeCalendarItemsForDaysInMonth(Calendar calendar){

        ArrayList<CalendarItem> tmpList = new ArrayList<>();
        // get number of days in month
        int numberOfDaysInCurrentMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int numberOfDaysInPreviousMonth = new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)-1,1).getActualMaximum(Calendar.DAY_OF_MONTH);
        int numberOfDayInFirstWeek = new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),0).get(Calendar.DAY_OF_WEEK)-2;


        for (int i =0; i<= numberOfDayInFirstWeek; i++){

            tmpList.add(new CalendarItem(new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)-1,i+numberOfDaysInPreviousMonth-numberOfDayInFirstWeek)));

        }

        for(int i = 1; i <= numberOfDaysInCurrentMonth; i++) {

            tmpList.add(new CalendarItem(new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), i)));

        }

        for(int i =1; i< 7*6-numberOfDayInFirstWeek-numberOfDaysInCurrentMonth; i++){

            tmpList.add(new CalendarItem(new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, i)));
        }

        return tmpList;
    }

    private ArrayList<CalendarItem> computeCalendarITemsForWeeks(ArrayList<CalendarItem> calItems){
        ArrayList<CalendarItem> tmpList = new ArrayList<>();
        for(int i =0; i < 6; i++){

            tmpList.add(new CalendarItem(calItems.get(i*7).getCalendar()));
        }
        return tmpList;

    }

    public void removeAchievement(String achievementName){
        calendarViewModel.removeAchievement(userId,achievementName, currentVisibleAchievementList);
    }

    public CalendarViewModel getCalendarViewModel(){

        return calendarViewModel;
    }
}
