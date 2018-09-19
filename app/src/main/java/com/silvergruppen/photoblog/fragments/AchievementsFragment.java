package com.silvergruppen.photoblog.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.animations.ResizeAnimation;
import com.silvergruppen.photoblog.listAdapters.AchievementListAdapter;
import com.silvergruppen.photoblog.listItems.Achievement;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AchievementsFragment extends Fragment {

    private final int  COLLAPSED_HEIGHT_2 = 250;

    private final int EXPANDED_HEIGHT_4 = 600;

    public ListView achievementsListView;


    public ArrayList<Achievement> listAchievements;
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
        // Required empty public constructor
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
        listAchievements = new ArrayList<>();
        achievementListAdapter = new AchievementListAdapter(getActivity(), R.layout.achievement_list_item, listAchievements);
        achievementsListView.setAdapter(achievementListAdapter);
        achievementsListView.setItemsCanFocus(true);

        // Get the Achievements from firestore

        firebaseFirestore.collection("Achievements")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Map<String,Object> tmpMap = document.getData();

                                if(tmpMap.get("topic").equals(topic)) {
                                    String name = tmpMap.get("name").toString();
                                    listAchievements.add(new Achievement(name,topic, tmpMap.get("points").toString(), COLLAPSED_HEIGHT_2
                                            , EXPANDED_HEIGHT_4, COLLAPSED_HEIGHT_2));
                                    listAchievementsMap.put(name,listAchievements.size());
                                }

                                achievementListAdapter.notifyDataSetChanged();
                            }

                            // Get all the current users posts
                            Toast.makeText(getContext(), "here", Toast.LENGTH_LONG).show();

                            for(final Achievement achievement : listAchievements) {

                                firebaseFirestore.collection(current_user_id + "/" + topic+"/"+achievement.getName())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (DocumentSnapshot document : task.getResult()) {

                                                        Map<String, Object> tmpMap = document.getData();
                                                        String postText = tmpMap.get("desc").toString();
                                                        String postImageUrl = tmpMap.get("image_url").toString();
                                                        Date timestamp = (Date) tmpMap.get("timestamp");
                                                        achievement.addJournalItem(postText, postImageUrl,timestamp);

                                                    }
                                                } else {

                                                }
                                            }
                                        });
                            }

                        }
                    }
                });


        return view;
    }


    public void changeTopic(String topic){


        this.topic = topic;


    }

}
