package com.silvergruppen.photoblog.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.listAdapters.AchievementRecyclerAdapter;
import com.silvergruppen.photoblog.listItems.Achievement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AchievementsFragment extends Fragment {

    public RecyclerView topicsListView;
    public List<Achievement> listAchievements;

    private AchievementRecyclerAdapter achievementRecyclerAdapter;

    public FirebaseFirestore firebaseFirestore;

    public TextView title;

    public String topic;
    public AchievementsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_achievements, container, false);
        title = view.findViewById(R.id.topics_headline);
        title.setText(topic);

        topicsListView = view.findViewById(R.id.achievement_list_view);
        listAchievements = new ArrayList<>();
        achievementRecyclerAdapter = new AchievementRecyclerAdapter(listAchievements);
        topicsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        topicsListView.setAdapter(achievementRecyclerAdapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        Toast.makeText(getActivity(),"start",Toast.LENGTH_LONG).show();
        firebaseFirestore.collection("Achievements")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Map<String,Object> tmpMap = document.getData();

                                if(tmpMap.get("topic").equals(topic))
                                    listAchievements.add(new Achievement(tmpMap.get("name").toString(), tmpMap.get("points").toString()));
                                Toast.makeText(getActivity(),"topic"+topic+" "+tmpMap.get("topic"),Toast.LENGTH_LONG).show();
                            }
                        } else {

                        }
                    }
                });
        /*
        firebaseFirestore.collection("Achievements").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                Toast.makeText(getActivity(),"Here",Toast.LENGTH_LONG).show();
                for(DocumentChange doc: documentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED) {

                        Map<String,Object> tmpMap = doc.getDocument().getData();

                        if(tmpMap.get("topic").equals(topic))
                            listAchievements.add(new Achievement(tmpMap.get("name").toString(), tmpMap.get("points").toString()));
                        Toast.makeText(getActivity(),"topic"+topic+" "+tmpMap.get("topic"),Toast.LENGTH_LONG).show();

                        achievementRecyclerAdapter.notifyDataSetChanged();

                    }
                }
            }
        });
*/

        return view;
    }
    public void changeTopic(String topic){


        this.topic = topic;


    }

}
