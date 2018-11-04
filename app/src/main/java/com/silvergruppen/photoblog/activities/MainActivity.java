package com.silvergruppen.photoblog.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.fragments.AccountFragment;
import com.silvergruppen.photoblog.fragments.HomeFragment;
import com.silvergruppen.photoblog.fragments.AchievementsFragment;
import com.silvergruppen.photoblog.listItems.Achievement;
import com.silvergruppen.photoblog.listItems.PostItem;
import com.silvergruppen.photoblog.other.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    private ProgressBar progress;
    private NavigationView navView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private String current_user_id;

    private BottomNavigationView mainBottomNav;

    private HomeFragment homeFragment;
    private AchievementsFragment achievementsFragment;
    private AccountFragment accountFragment;
    private String topic, currentAchievement;

    private DrawerLayout mainDrawerLayout;

    private Semaphore semaphore;


    private ArrayList<Achievement> achievementsList;
    private HashMap<String, Object> achievementHashmap;
    private ArrayList<User> allUsersID;
    private ArrayList<PostItem> allPostItems;

    private TextView navigationProfileText;
    private CircleImageView navigationProfileImage;

    public static int threads;

    private Context context;



    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = findViewById(R.id.progressBar_main);
        mContext = this;
        threads = 0;
        changeBottomMenuIconSize();
        achievementHashmap = new HashMap<>();
        context = this;

        allPostItems = new ArrayList<>();
        allUsersID = new ArrayList<>();
        // Initialize fragments
        homeFragment = new HomeFragment();
        achievementsFragment = new AchievementsFragment();
        accountFragment = new AccountFragment();

        accountFragment.setAchievemensDoneList(new ArrayList<Achievement>());
        achievementsFragment.setListAchievements(new ArrayList<Achievement>());



        mainDrawerLayout = findViewById(R.id.main_drawer_layout);
        navView = mainDrawerLayout.findViewById(R.id.nav_view);
        View navHead = navView.getHeaderView(0);
        navigationProfileImage = navHead.findViewById(R.id.nav_profile_image);
        navigationProfileText = navHead.findViewById(R.id.nav_profile_text);

        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle(null);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();



        navView = findViewById(R.id.nav_view);

        if(mAuth.getCurrentUser() != null) {

            //Get the topics from Firebase and show in nav view
            final Menu navMenu = navView.getMenu();
            firebaseFirestore.collection("Topics")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    navMenu.add(document.getId());
                                }
                            } else {

                            }
                        }
                    });



            mainBottomNav = findViewById(R.id.main_bottom_nav);

            //fragments

            replaceFragment(homeFragment);

            // Create the on navigationView Item listener
            navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    achievementsFragment.changeTopic(menuItem.getTitle().toString());
                    topic = menuItem.getTitle().toString();
                    refreshFragment(achievementsFragment);
                    return true;
                }
            });



            mainBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {

                        case R.id.bottom_action_home:
                            replaceFragment(homeFragment);
                            return true;
                        case R.id.bottom_action_achievement:
                            replaceFragment(achievementsFragment);
                            return true;
                        case R.id.bottom_action_account:
                            replaceFragment(accountFragment);
                            return true;
                        case R.id.bottom_action_search:
                            Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                            startActivity(searchIntent);
                        default:
                            return false;

                    }
                }
            });




        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){

            sendToLogin();

        } else {

            current_user_id = mAuth.getCurrentUser().getUid();


            firebaseFirestore.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                        if(!task.getResult().exists()){

                            Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                            startActivity(setupIntent);
                            finish();

                        } else{

                            loadAllUserData();

                        }

                    } else {

                        String errorMessage = task.getException().toString();
                        Toast.makeText(MainActivity.this,"Error  . " + errorMessage,Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void changeBottomMenuIconSize() {

        BottomNavigationView bottomNavigation = (BottomNavigationView) findViewById(R.id.main_bottom_nav);

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigation.getChildAt(0);

        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.action_logout_btn:
                logOut();
                return true;
            case R.id.action_setting_btn:

                Intent settingsIntent = new Intent(MainActivity.this, SetupActivity.class);
                startActivity(settingsIntent);

                return true;

                default:
                    return false;
        }

    }

    public void loadAllUserData(){

        loadAchievements();
        loadNameAndProfilePicture();
        loadAllusers();
        loadNUmberOfPosts();
        loadNumberOfFollowers();

    }
    private void logOut(){

        mAuth.signOut();
        sendToLogin();

    }
    private void sendToLogin(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void replaceFragment(Fragment fragment){

        if(!fragment.equals(achievementsFragment))
            mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        else {

            mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mainDrawerLayout.openDrawer(Gravity.LEFT);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();

    }
    private void refreshFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.detach(fragment);
        fragmentTransaction.attach(fragment);
        fragmentTransaction.commit();
    }
    public String getTopic(){
        return topic;
    }

    public String getCurrentAchievement() {
        return currentAchievement;
    }

    public void setCurrentAchievement(String currentAchievement) {
        this.currentAchievement = currentAchievement;
    }
    private void loadingTaskDone(){

        if(threads ==0) {
            progress.setVisibility(View.INVISIBLE);

        }


    }

    public void loadAchievements(){

        /**
         *  Loads all the achievements into achievmentlist. Loads all the posts into the achievments.
         *  Checks if the achievment is done
         *
         */

        firebaseFirestore = FirebaseFirestore.getInstance();
        achievementsList = new ArrayList<>();
        achievementsList.clear();
        achievementsFragment.clearAchievementList();
        accountFragment.getAchievemensDoneList().clear();

        progress.setVisibility(View.VISIBLE);
        threads++;
        firebaseFirestore.collection("Achievements")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                Map<String,Object> tmpMap = document.getData();


                                final String name = tmpMap.get("name").toString();
                                // check if user has done achievement

                                Achievement tmpAchievement =new Achievement(name, tmpMap.get("topic").toString(), tmpMap.get("points").toString(),false);
                                achievementsList.add(tmpAchievement);
                                achievementsFragment.addAchievement(tmpAchievement);
                                achievementHashmap.put(name,tmpAchievement);



                            }
                            // Check if the achievements are done by the current user
                            for(final Achievement achievement : achievementsList){
                                threads++;
                                progress.setVisibility(View.VISIBLE);
                                firebaseFirestore.collection("Achievements/"+achievement.getName()+"/Users").document(current_user_id).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                if(task.getResult().exists()) {

                                                    achievement.setDone(true);
                                                    accountFragment.addAchievementDone(achievement);
                                                    accountFragment.setNumberOfPoints(accountFragment.getNumberOfPoints()+ Integer.parseInt(achievement.getPoints()));

                                                }
                                                else
                                                    achievement.setDone(false);

                                                threads--;
                                                loadingTaskDone();
                                            }

                                        });

                            }

                            // Get all the current users posts


                            for(final Achievement achievement : achievementsList) {
                                threads++;
                                progress.setVisibility(View.VISIBLE);
                                firebaseFirestore.collection(current_user_id)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (DocumentSnapshot document : task.getResult()) {

                                                        Map<String, Object> tmpMap = document.getData();
                                                        String tmpTopic = tmpMap.get("topic").toString();
                                                        String achievementName = tmpMap.get("achievement_name").toString();

                                                        if(tmpTopic.equals(achievement.getTopic()) && achievementName.equals(achievement.getName())) {
                                                            String postText = tmpMap.get("desc").toString();
                                                            String postImageUrl = tmpMap.get("image_url").toString();
                                                            Date timestamp = (Date) tmpMap.get("timestamp");
                                                            achievement.addJournalItem(postText, postImageUrl, timestamp);

                                                        }
                                                    }
                                                } else {

                                                }
                                                threads--;
                                                loadingTaskDone();
                                            }
                                        });
                            }

                        }

                        threads--;
                        loadingTaskDone();
                    }
                });
    }

    public void loadNameAndProfilePicture(){

        /**
         * Load name and profile picture of user
         */
        threads++;
        progress.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){
                        Map<String, Object> tmpMap = task.getResult().getData();

                        String name = tmpMap.get("name").toString();
                        String image = tmpMap.get("image").toString();

                        AccountFragment.setUserName(name);
                        AccountFragment.setImageURL(image);

                        // loaf the image and name into the drawer layout
                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.drawable.profile_icon);
                        Glide.with(context).setDefaultRequestOptions(placeholderRequest).load(image).into(navigationProfileImage);

                        navigationProfileText.setText(name);


                    }
                } else {

                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(mContext, "FIRESTORE retrive Error: "+errorMessage,Toast.LENGTH_LONG).show();

                }
                threads--;
                loadingTaskDone();


            }
        });

    }
    public  void loadAchievmentsDone(){

        // Load all the achievements done by user
        accountFragment.getAchievemensDoneList().clear();
        firebaseFirestore.collection("Users/"+current_user_id+"/Achievements").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    for(DocumentSnapshot doc : task.getResult().getDocuments()){


                        String achievementName = doc.getId();
                        accountFragment.addAchievementDone((Achievement) achievementHashmap.get(achievementName));

                    }


                }

            }
        });
    }

    private void loadAllusers(){

        threads++;
        progress.setVisibility(View.VISIBLE);
        allUsersID.clear();
        firebaseFirestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                        Map<String, Object> tmpMap = document.getData();
                        String username = tmpMap.get("name").toString();
                        String image = tmpMap.get("image").toString();

                        User tmpUser = new User(document.getId(),image,username);
                        allUsersID.add(tmpUser);

                    }

                    HomeFragment.setAllUserID(allUsersID);
                    loadPostItems();
                }
                threads--;
                loadingTaskDone();
            }
        });
    }

    public void loadPostItems(){

        // clear all post items before loading all the postitems.
        allPostItems.clear();
        HomeFragment.clearPostItemsList();

        // loop through all users and get their respective posts
        for(final User user : allUsersID) {

            threads++;
            progress.setVisibility(View.VISIBLE);

            firebaseFirestore.collection(user.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()) {

                        for (DocumentSnapshot document : task.getResult()) {


                            Map<String, Object> tmpMap = document.getData();
                            String desc = tmpMap.get("desc").toString();
                            String image_thumb = tmpMap.get("image_thumb").toString();
                            String postImageUrl = tmpMap.get("image_url").toString();
                            Date timestamp = (Date) tmpMap.get("timestamp");
                            String topic = tmpMap.get("topic").toString();
                            String achievement_name = tmpMap.get("achievement_name").toString();
                            PostItem tmpPostItem = new PostItem(user.getName(),postImageUrl,desc, image_thumb, timestamp, user.getImageUrl(), user.getId(), topic, achievement_name);
                            allPostItems.add(tmpPostItem);
                            HomeFragment.addPostItem(tmpPostItem);
                            homeFragment.notifyDataSetChanged();

                        }



                    }

                 threads--;
                    loadingTaskDone();
                }
            });

        }
    }
    public void loadNUmberOfPosts(){

            threads++;
            progress.setVisibility(View.VISIBLE);

            firebaseFirestore.collection(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful())
                        accountFragment.setNumberOFPosts(task.getResult().size());

                    threads--;
                    loadingTaskDone();
                }
            });

    }

    public void loadNumberOfFollowers(){

        threads++;
        progress.setVisibility(View.VISIBLE);

        firebaseFirestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful())
                    accountFragment.setNumberOfFollowers(task.getResult().size());

                threads--;
                loadingTaskDone();

            }
        });

    }





}
