package com.silvergruppen.photoblog.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.listAdapters.AchievementListAdapter;
import com.silvergruppen.photoblog.listItems.Achievement;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private TextView accountName, levelTextView, achievementsDoneTextView, postsTextView, followersTextView;
    private CircleImageView imageView;
    private ProgressBar levelProgress;

    private String user_id;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private ArrayList<Achievement> achievemensDoneList;

    private ListView accountListView;
    private AchievementListAdapter achievementListAdapter;

    private static String userName;
    private static String imageURL;
    private int achievementsDone, numberOFPosts, numberOfFollowers, numberOfPoints;


    public AccountFragment() {

        achievemensDoneList = new ArrayList<>();
        achievementsDone = 0;
        numberOFPosts = 0;
        numberOfFollowers = 0;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // initializations


        // get the username from Firebase and set the image and headline
        accountName = view.findViewById(R.id.account_name);
        imageView = view.findViewById(R.id.profile_image);


        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Load setName
        if(userName != null)
            accountName.setText(userName);
        // Load image
        if(imageURL != null) {
            RequestOptions placeholderRequest = new RequestOptions();
            placeholderRequest.placeholder(R.drawable.profile_icon);
            Glide.with(getContext()).setDefaultRequestOptions(placeholderRequest).load(imageURL).into(imageView);
        }

        // set the achievements done text
        achievementsDoneTextView = view.findViewById(R.id.account_achivement_number_text_view);
        achievementsDoneTextView.setText(Integer.toString(achievementsDone));

        // set the posts done text
        postsTextView = view.findViewById(R.id.account_post_number_text_view);
        postsTextView.setText(Integer.toString(numberOFPosts));

        // set the number of followers text
        followersTextView = view.findViewById(R.id.account_followers_text_view);
        followersTextView.setText(Integer.toString(numberOfFollowers));

        // set the level
        levelTextView =  view.findViewById(R.id.level_text);
        levelTextView.setText("Level "+Integer.toString(convertPointsToLevel()));

        // set the progress
        levelProgress = view.findViewById(R.id.account_progress);
        levelProgress.setProgress(getLevelProgress());

        // Set up the list view

        accountListView = view.findViewById(R.id.account_list_view);
        accountListView.setItemsCanFocus(true);
        achievementListAdapter= new AchievementListAdapter(getActivity(), R.layout.achievement_list_item, achievemensDoneList,null, false);
        accountListView.setAdapter(achievementListAdapter);
        accountListView.setItemsCanFocus(true);

        return view;
    }


    private int convertPointsToLevel(){

        int level =0;
        int pointsPerLevel = 30;
        int pointsLeft = numberOfPoints-pointsPerLevel;

        while(pointsLeft>0){

            level++;
            pointsPerLevel = pointsPerLevel + 10;
            pointsLeft = pointsLeft-pointsPerLevel;
        }
        return level;
    }
    private int getLevelProgress(){

        int progress =0;
        int pointsPerLevel = 30;
        int pointsLeft = numberOfPoints-pointsPerLevel;

        while(pointsLeft>0){

            progress = pointsLeft;
            pointsPerLevel = pointsPerLevel + 10;
            pointsLeft = pointsLeft-pointsPerLevel;

        }
        return progress;

    }
    public String getUserName() {
        return userName;
    }

    public static void setUserName(String newUserName) {
        userName = newUserName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public static void setImageURL(String newImageURL) {
        imageURL = newImageURL;
    }

    public ArrayList<Achievement> getAchievemensDoneList() {
        return achievemensDoneList;
    }

    public void setAchievemensDoneList(ArrayList<Achievement> achievemensDoneList) {
        this.achievemensDoneList = achievemensDoneList;
    }
    public void addAchievementDone(Achievement achievement){

        achievemensDoneList.add(achievement);
        achievementsDone = achievemensDoneList.size();


        if(achievementListAdapter != null)
            achievementListAdapter.notifyDataSetChanged();

    }
    public void setNumberOFPosts(int numberOFPosts){

        this.numberOFPosts = numberOFPosts;
    }
    public void setNumberOfFollowers(int numberOfFollowers){

        this.numberOfFollowers = numberOfFollowers;
    }

    public void setNumberOfPoints(int numberOfPoints) {
        Log.d("points"+numberOfPoints, "\n \n \n \n \n");
        this.numberOfPoints = numberOfPoints;
    }

    public int getNumberOfPoints() {
        return numberOfPoints;
    }
}
