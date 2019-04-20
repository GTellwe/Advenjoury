package com.silvergruppen.photoblog.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.adapters.RecycleViewAdapter;
import com.silvergruppen.photoblog.items.Achievement;
import com.silvergruppen.photoblog.items.Catagorie;
import com.silvergruppen.photoblog.items.RecycleListItem;
import com.silvergruppen.photoblog.viewmodels.AchievmentsViewModel;
import com.silvergruppen.photoblog.viewmodels.CalendarViewModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AchievementsFragment2 extends Fragment {

    // Constants
    private static final String UID_KEY = "uid";
    private static final String TOPIC_KEY = "top";

    // Views
    public RecyclerView achievementsRecyclerView;
    private CircleImageView profileImage;
    private TextView titleTextView;
    private FloatingActionButton showAddachievementsViewButton;
    private ConstraintLayout expandedImage;
    private FrameLayout mainContainer;
    private Button addAchievementButton;
    private EditText achievementNameEditTextView;

    // Lists
    public ArrayList<RecycleListItem> listAchievements;

    // firebase
    public FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;

    // strings
    private String userId;
    private  final String headline = "ACIEVEMENTS";
    private String imageURL;
    private String topic;

    //other
    private Catagorie currentCatagorie;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecycleViewAdapter catagoriesListAdapter;
    private MainActivity mainActivity;
    private AchievmentsViewModel achievmentsViewModel;
    private Animator currentAnimator;
    private int shortAnimationDuration = 200;




    public AchievementsFragment2() {

        listAchievements = new ArrayList<>();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_achievements_2, container, false);

        // get the currnt user id and topic
        userId = getArguments().getString(UID_KEY);
        topic = getArguments().getString(TOPIC_KEY);

        mainActivity = (MainActivity) getActivity();
        mainActivity.setHeadline(headline);

        // Get the current user
        mAuth = FirebaseAuth.getInstance();


        // General initilazations
        firebaseFirestore = FirebaseFirestore.getInstance();

        // set the profile image
        profileImage = view.findViewById(R.id.achievements_profile_image);
        imageURL = mainActivity.getCurrentUserImageUrl();
        if(imageURL != null) {
            RequestOptions placeholderRequest = new RequestOptions();
            placeholderRequest.placeholder(R.drawable.profile_icon);
            Glide.with(getContext()).setDefaultRequestOptions(placeholderRequest).load(imageURL).into(profileImage);
        }

        // set the title
        titleTextView= view.findViewById(R.id.achievements_title);
        if(currentCatagorie!= null) {
            titleTextView.setText(currentCatagorie.getName());
        }

        // Handle the list of achievements
        achievementsRecyclerView = (RecyclerView) view.findViewById(R.id.catagories_list_view);
        achievementsRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        achievementsRecyclerView.setLayoutManager(mLayoutManager);
        catagoriesListAdapter = new RecycleViewAdapter(getActivity(), getListOfAchievementsInCatagorie(currentCatagorie));
        achievementsRecyclerView.setAdapter(catagoriesListAdapter);
        catagoriesListAdapter.notifyDataSetChanged();

        // load the achievmeents
        achievmentsViewModel = ViewModelProviders.of(this).get(AchievmentsViewModel.class);
        achievmentsViewModel.init(userId);
        final Observer<ArrayList<RecycleListItem>> achievementsObserver = new Observer<ArrayList<RecycleListItem>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<RecycleListItem> newAchievements) {
                listAchievements =newAchievements;
                catagoriesListAdapter.updateList(getListOfAchievementsInCatagorie(currentCatagorie));

            }
        };
        achievmentsViewModel.getAchievementsList().observe(this,achievementsObserver);


        showAddachievementsViewButton = view.findViewById(R.id.show_add_achievement_view_button);
        showAddachievementsViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(view, R.drawable.icon);
            }
        });

        // initialize animations views
        expandedImage = view.findViewById(R.id.add_achievement_expanded_view);
        mainContainer = view.findViewById(R.id.container);
        achievementNameEditTextView = view.findViewById(R.id.new_achievement_text);

        // when the add achievement button is pressed, add the achievement to firebase and reload the fragment
        addAchievementButton = view.findViewById(R.id.add_new_achievement_button);
        addAchievementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the name of the achievement
                if(achievementNameEditTextView.getText() == null) {
                    Toast.makeText(getContext(), "Plaese name the achievement", Toast.LENGTH_LONG).show();
                    return;
                }

                // tell the view model to update the lists of achievements
                achievmentsViewModel.addAchievementToList(userId, achievementNameEditTextView.getText().toString(), currentCatagorie.getName());
            }
        });

        return view;

    }

    private ArrayList<RecycleListItem> getListOfAchievementsInCatagorie(Catagorie catagorie){
        ArrayList<RecycleListItem> achievementsInCatagorie = new ArrayList<>();

        for(RecycleListItem tmpRecycklerListItem: listAchievements){

            Achievement tmpAchievement = (Achievement) tmpRecycklerListItem;

            if(tmpAchievement.getTopic().equals(catagorie.getName()))
                achievementsInCatagorie.add(tmpAchievement);
        }
        return achievementsInCatagorie;

    }

    public void setCurrentCatagorie(Catagorie currentCatagorie) {
        this.currentCatagorie = currentCatagorie;
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
        int offsetY = (mainContainer.getHeight()-expandedImageView.getHeight())-60;

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

}
