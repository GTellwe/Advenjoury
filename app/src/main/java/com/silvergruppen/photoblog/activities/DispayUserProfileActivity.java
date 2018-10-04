package com.silvergruppen.photoblog.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.fragments.AccountFragment;
import com.silvergruppen.photoblog.listAdapters.AchievementListAdapter;
import com.silvergruppen.photoblog.listItems.Achievement;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DispayUserProfileActivity extends AppCompatActivity {

    private ArrayList<Achievement> achievementsList;
    private ArrayList<Achievement> achievementsListDoneByUser;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;

    private TextView accountName;
    private CircleImageView imageView;
    private String username;
    private String imageURL;

    private Context mContext;

    private ListView displayAccountListView;
    private AchievementListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispay_user_profile);

        achievementsList = new ArrayList<>();
        achievementsListDoneByUser = new ArrayList<>();

        mContext = this;
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");

        loadAchievmentAndCheckAchievementsDoneByUser();
        loadUserPosts();
        loadImageUrlAndUsername();

        accountName = findViewById(R.id.display_account_name);
        imageView = findViewById(R.id.display_profile_image);

        // Setup the list view

        displayAccountListView = findViewById(R.id.display_account_list_view);
        displayAccountListView.setItemsCanFocus(true);
        listAdapter= new AchievementListAdapter(this, R.layout.achievement_list_item, achievementsListDoneByUser,null, false);
        displayAccountListView.setAdapter(listAdapter);
        displayAccountListView.setItemsCanFocus(true);

    }

    public void loadAchievmentAndCheckAchievementsDoneByUser(){

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Achievements")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                Map<String, Object> tmpMap = document.getData();


                                final String name = tmpMap.get("name").toString();
                                // check if user has done achievement

                                Achievement tmpAchievement = new Achievement(name, tmpMap.get("topic").toString(), tmpMap.get("points").toString(), false);
                                achievementsList.add(tmpAchievement);


                            }

                            // check if achivement is done by user
                            for (final Achievement achievement : achievementsList) {
                                firebaseFirestore.collection("Achievements/" + achievement.getName() + "/Users").document(user_id).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                if (task.getResult().exists())
                                                    achievementsListDoneByUser.add(achievement);
                                                listAdapter.notifyDataSetChanged();

                                            }
                                        });

                            }
                        }
                    }
                });

    }

    public void loadUserPosts(){

        for(final Achievement achievement : achievementsListDoneByUser) {

            firebaseFirestore.collection(user_id + "/" + achievement.getTopic()+"/"+achievement.getName())
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
                            }
                            listAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    private void loadImageUrlAndUsername(){

        /**
         * Load name and profile picture of user
         */

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){
                        Map<String, Object> tmpMap = task.getResult().getData();

                        username = tmpMap.get("name").toString();
                        imageURL = tmpMap.get("image").toString();

                        accountName.setText(username);
                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.drawable.profile_icon);
                        Glide.with(mContext).setDefaultRequestOptions(placeholderRequest).load(imageURL).into(imageView);

                    }
                }


            }
        });


    }
}
