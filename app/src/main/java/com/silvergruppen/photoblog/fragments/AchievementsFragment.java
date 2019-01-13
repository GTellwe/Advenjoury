package com.silvergruppen.photoblog.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.adapters.AchievementListAdapter;
import com.silvergruppen.photoblog.items.Achievement;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class AchievementsFragment extends Fragment {

    private final int  COLLAPSED_HEIGHT_2 = 250;

    private final int EXPANDED_HEIGHT_4 = 600;

    public ListView achievementsListView;


    public ArrayList<Achievement> listAchievements;
    public ArrayList<Achievement> listDisplayedAchievements;

    private HashMap<String,Integer> listAchievementsMap;

    private AchievementListAdapter achievementListAdapter;
    private boolean accordion = true;


    public FirebaseFirestore firebaseFirestore;

    private FirebaseAuth mAuth;

    private String current_user_id;

    public TextView title;

    private String currentAchievement;

    public String topic;
    public AchievementsFragment() {
        listAchievements = new ArrayList<>();
        listDisplayedAchievements = new ArrayList<>();
        topic = "Summary";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_achievements, container, false);


        // Set the topic headline
        title = view.findViewById(R.id.topics_headline);
        title.setText(topic);

        // Get the current user
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        // General initilazations
        listAchievementsMap = new HashMap<>();
        firebaseFirestore = FirebaseFirestore.getInstance();


        // Handle the list of achievements
        achievementsListView = view.findViewById(R.id.achievement_list_view);
        achievementsListView.setItemsCanFocus(true);

        achievementListAdapter = new AchievementListAdapter(getActivity(), R.layout.achievement_list_item, listDisplayedAchievements,topic, true);
        achievementsListView.setAdapter(achievementListAdapter);
        achievementsListView.setItemsCanFocus(true);



        return view;
    }



    public void changeTopic(String topic){


        this.topic = topic;
        listDisplayedAchievements.clear();
        for(Achievement achievement : listAchievements) {
            if (achievement.getTopic().equals(topic))
                listDisplayedAchievements.add(achievement);
        }


    }

    public void addAchievement(Achievement achievement){

        listAchievements.add(achievement);

    }

    public ArrayList<Achievement> getListAchievements() {
        return listAchievements;
    }

    public void setListAchievements(ArrayList<Achievement> listAchievements) {
        this.listAchievements = listAchievements;
    }
    public void clearAchievementList(){

        listAchievements.clear();
    }
}
