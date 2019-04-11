package com.silvergruppen.photoblog.activities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.fragments.AccountFragment;
import com.silvergruppen.photoblog.fragments.AccountSettingsFragment;
import com.silvergruppen.photoblog.fragments.AchievementFragment;
import com.silvergruppen.photoblog.fragments.AchievementsFragment2;
import com.silvergruppen.photoblog.fragments.CatagoriesFragment;
import com.silvergruppen.photoblog.fragments.CalendarFragment;
import com.silvergruppen.photoblog.fragments.DailyProgressFragment;
import com.silvergruppen.photoblog.fragments.HomeFragment;
import com.silvergruppen.photoblog.fragments.SearchFragment;
import com.silvergruppen.photoblog.items.Achievement;
import com.silvergruppen.photoblog.items.Catagorie;
import com.silvergruppen.photoblog.items.PostItem;
import com.silvergruppen.photoblog.other.NotificationPublisher;
import com.silvergruppen.photoblog.other.User;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    // constants
    private static final String DAY_KEY = "day";
    private static final String UID_KEY = "uid";
    private static final String TYPE_KEY = "type";
    private final static int dailyId=1, weekleyId = 2;
    public static final int accountFragmentId = 0,calendarFragmentId=2, catogiresFragmentId = 1, homefragmentId=3;
    public static int displayFramentOnResume = calendarFragmentId;

    // fragments
    private CatagoriesFragment catagoriesFragment;
    private CalendarFragment calendarFragment;
    private DailyProgressFragment dailyProgressFragment;
    private AccountSettingsFragment accountSettingsFragment;
    private AchievementsFragment2 achievementsFragment;
    private AchievementFragment achievementFragment;

    // strings
    private String current_user_id;
    private String topic, currentAchievement;
    private String currentUserImageUrl;

    // uri
    private Uri accountImageUri;

    // views
    private Toolbar mainToolbar;
    private ProgressBar progress;
    private BottomNavigationView mainBottomNav;
    private DrawerLayout mainDrawerLayout;
    private TextView navigationProfileText;
    private CircleImageView navigationProfileImage;
    private ImageView toolbarIcon;
    private TextView mainToolbarHeadline;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;

    // lists
    private ArrayList<Achievement> achievementsList;
    private ArrayList<User> allUsersID;
    private ArrayList<PostItem> allPostItems;

    // hashmaps
    private HashMap<String, Object> achievementHashmap;
    private HashMap<Calendar,ArrayList<Integer>> dailyProgressHashMap = new HashMap<Calendar, ArrayList<Integer>>();


    // other
    private static FragmentManager supportFrameManager;
    private Semaphore semaphore;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // setup firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();



        if(currentUser == null){

            sendToLogin();

        } else {

            // initialize views
            progress = findViewById(R.id.progressBar_main);
            mainDrawerLayout = findViewById(R.id.main_drawer_layout);
            mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
            mainToolbarHeadline = findViewById(R.id.main_toolbar_headline);

            // initialize lists
            allPostItems = new ArrayList<>();
            allUsersID = new ArrayList<>();

            // Initialize fragments
            calendarFragment = new CalendarFragment();
            dailyProgressFragment = new DailyProgressFragment();
            accountSettingsFragment = new AccountSettingsFragment();
            catagoriesFragment = new CatagoriesFragment();
            achievementsFragment = new AchievementsFragment2();
            achievementFragment = new AchievementFragment();

            // other initializations
            mContext = this;
            achievementHashmap = new HashMap<>();

            changeBottomMenuIconSize();

            // setup action bar
            setSupportActionBar(mainToolbar);
            getSupportActionBar().setTitle(null);

            // get user id
            current_user_id = mAuth.getCurrentUser().getUid();

            // change the headline
            mainToolbarHeadline.setText("CALENDAR");

            // setup support frame manager
            supportFrameManager = getSupportFragmentManager();

            // setup notifications
            scheduleNotification(getNotification("Don't forget your daily achievements"));

            // show calendar fragment
            Bundle bundle2 = new Bundle();
            bundle2.putString("uid",current_user_id);
            calendarFragment.setArguments(bundle2);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_container, calendarFragment);
            fragmentTransaction.commit();

            firebaseFirestore.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                        if(!task.getResult().exists()){


                            //replaceFragment(accountSettingsFragment);

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

            // set up the bottom nativation view
            mainBottomNav = findViewById(R.id.main_bottom_nav);
            mainBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {

                        case R.id.bottom_action_achievement:
                            replaceFragment(catagoriesFragment);
                            return true;
                        case R.id.bottom_action_daily_progress:
                            Log.d("Action daily","\n \n ");
                            replaceFragment(calendarFragmentId);
                            return true;
                        default:
                            return false;

                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        //replaceFragment(displayFramentOnResume);
        //Toast.makeText(this,"start", Toast.LENGTH_LONG).show();
        super.onStart();


    }



    private void changeBottomMenuIconSize() {
        BottomNavigationView bottomNavigation = (BottomNavigationView) findViewById(R.id.main_bottom_nav);

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigation.getChildAt(0);

        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, displayMetrics);
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
                // set alarm for notification

                //scheduleNotification(getNotification("Don't forget your daily achievements"), 3000);

                //replaceFragment(accountSettingsFragment);
                /*
                Intent settingsIntent = new Intent(MainActivity.this, SetupActivity.class);
                startActivity(settingsIntent);
                */
                return true;

                default:
                    return false;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                accountImageUri = result.getUri();
                accountSettingsFragment.setImageUri(accountImageUri);
                //setupImage.setImageURI(mainImageUri);

                accountSettingsFragment.setSettupChanged(true);
                //isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }

    private void scheduleNotification(Notification notification) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the alarm to start at approximately 2:00 p.m.
        /*Calendar tmpCal = Calendar.getInstance();
        tmpCal.setTimeInMillis(System.currentTimeMillis());
        tmpCal.set(Calendar.HOUR_OF_DAY, 18);
        */long futureInMillis = SystemClock.elapsedRealtime() + 5000;

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, tmpCal.getTimeInMillis(),
         //       1000 * 60 * 20, pendingIntent);
    }
    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Reminder");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.logotransparent);

        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "YOUR_CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }


        return builder.build();
    }


    public int getAchievementsDoneByCurrentUser(Catagorie catagorie){

        int achievementsDone = 0;
        for(Achievement achievement : achievementsList){

            if(achievement.isDone() && achievement.getCatagorie().equals(catagorie.getName())){
                achievementsDone ++;
            }
        }

        return achievementsDone;
    }

    public String getCurrentUserImageUrl(){

        return currentUserImageUrl;

    }


    public HashMap<Calendar, ArrayList<Integer>> getDailyProgressHashMap() {
        return dailyProgressHashMap;
    }

    public void setDailyProgressHashMap(HashMap<Calendar, ArrayList<Integer>> dailyProgressHashMap) {
        this.dailyProgressHashMap = dailyProgressHashMap;
    }

    public void startDailyProgressFragment(Calendar calendar, int type){

        dailyProgressFragment.setCalendar(calendar);

        Bundle bundle = new Bundle();
        bundle.putString(UID_KEY,current_user_id);
        bundle.putInt(TYPE_KEY,type);
        dailyProgressFragment.setArguments(bundle);
        replaceFragment(dailyProgressFragment);
    }

    public void startAchievementFragment(Catagorie catagorie){


        mainToolbarHeadline.setText(catagorie.getName().toUpperCase());
        achievementsFragment.setCurrentCatagorie(catagorie);
        replaceFragment(achievementsFragment);

    }

    public void setHeadline(String headline){

        mainToolbarHeadline.setText(headline);
    }

    public void startAchievementActivity(Achievement achievement){


        // clear all journal items

        setHeadline(achievement.getName());

        /*
        // Load all post items into the jorunal
        for(PostItem tmpPostItem : allPostItems){

            if(tmpPostItem.achievementName.equals(achievement.getName()) && tmpPostItem.getUser_id().equals(current_user_id)){

                achievementFragment.addJournalItem(tmpPostItem);
            }

        }
        */
        Bundle bundle2 = new Bundle();
        bundle2.putString("uid",current_user_id);
        achievementFragment.setArguments(bundle2);
        achievementFragment.setAchievement(achievement);
        replaceFragment(achievementFragment);
        // Intent achievementIntent = new Intent(MainActivity.this, AchievementActivity.class);
        //startActivity(achievementIntent);
        //startActivity(achievementIntent);

    }
    public void loadAllUserData(){

        //loadAchievements();
        //loadNameAndProfilePicture();
        //loadAllusers();
        //loadNUmberOfPosts();
       // loadNumberOfFollowers();

        //loadCatagories();

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


    public void replaceFragment(Fragment fragment){
/*
        if(!fragment.equals(achievementsFragment))
            mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        else {

            mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mainDrawerLayout.openDrawer(Gravity.LEFT);
        }
*/

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void replaceFragment(int fragmentId){

        switch (fragmentId){
            /*
            case accountFragmentId:
                Bundle bundle = new Bundle();
                bundle.putString("uid",current_user_id);
                mainToolbarHeadline.setText("ACCOUNT");
                accountFragment.setArguments(bundle);
                replaceFragment(accountFragment);
                break;

            case homefragmentId:
                replaceFragment(homeFragment);
            case catogiresFragmentId:
                replaceFragment(catagoriesFragment);
                */
            case calendarFragmentId:
                mainToolbarHeadline.setText("CALENDAR");
                Bundle bundle2 = new Bundle();
                bundle2.putString("uid",current_user_id);
                calendarFragment.setArguments(bundle2);
                Log.d("replace calendar \n \n \n"," ");
                replaceFragment(calendarFragment);
                break;
                default:
                    break;

        }



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
        /*
        if(threads ==0) {
            progress.setVisibility(View.INVISIBLE);

        }

/
    }

    public void loadAchievements(){

        /**
         *  Loads all the achievements into achievmentlist. Loads all the posts into the achievments.
         *  Checks if the achievment is done
         *
         */
/*
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
                                final String catagorie = tmpMap.get("topic").toString();
                                // check if user has done achievement

                                Achievement tmpAchievement = new Achievement(name, tmpMap.get("topic").toString(), tmpMap.get("points").toString(),false, catagorie);
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
                                                            String postImageUrl = null;
                                                            if(tmpMap.get("image_url") != null)
                                                                postImageUrl = tmpMap.get("image_url").toString();
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
                */

    }

    public void loadNameAndProfilePicture(){

        /**
         * Load name and profile picture of user
         */
        /*
        //threads++;
        progress.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){
                        Map<String, Object> tmpMap = task.getResult().getData();

                        String name = tmpMap.get("name").toString();
                        String image = tmpMap.get("image").toString();

                        //AccountFragment.setUserName(name);
                        //AccountFragment.setImageURL(image);

                        accountSettingsFragment.setImageURL(image);
                        accountSettingsFragment.setUserName(name);
                        currentUserImageUrl = image;

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
                //threads--;
                loadingTaskDone();


            }
        });
*/

    }
    public  void loadAchievmentsDone(){

        // Load all the achievements done by user
        //accountFragment.getAchievemensDoneList().clear();
        firebaseFirestore.collection("Users/"+current_user_id+"/Achievements").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    for(DocumentSnapshot doc : task.getResult().getDocuments()){


                        String achievementName = doc.getId();
                        //accountFragment.addAchievementDone((Achievement) achievementHashmap.get(achievementName));

                    }


                }

            }
        });
    }

    private void loadAllusers(){
/*
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
        */
    }

    public void loadPostItems(){
        /*

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

                            String image_thumb = null;
                            if(tmpMap.get("image_thumb") != null)
                                image_thumb = tmpMap.get("image_thumb").toString();
                            //Log.d("\n \n \n \n \n", tmpMap.get("image_thumb").toString());

                            String postImageUrl = null;
                            if(tmpMap.get("image_url") != null)
                                postImageUrl = tmpMap.get("image_url").toString();

                            Date timestamp = (Date) tmpMap.get("timestamp");

                            String topic = null;
                            if(tmpMap.get("topic") != null)
                                topic = tmpMap.get("topic").toString();

                            String achievement_name = null;
                            if(tmpMap.get("achievement_name") != null)
                                topic = tmpMap.get("achievement_name").toString();

                            String headline = "Added new post on achievement \""+ achievement_name+"\"";
                            PostItem tmpPostItem = new PostItem(user.getName(),postImageUrl,desc, image_thumb, timestamp, user.getImageUrl(), user.getId(), topic, achievement_name, headline);
                            allPostItems.add(tmpPostItem);
                            HomeFragment.addPostItem(tmpPostItem);

                        }



                    }

                 threads--;
                    loadingTaskDone();
                }
            });

        }
        */
    }
    public void loadNUmberOfPosts(){
/*
            threads++;
            progress.setVisibility(View.VISIBLE);

            firebaseFirestore.collection(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful())
                       // accountFragment.setNumberOFPosts(task.getResult().size());

                    threads--;
                    loadingTaskDone();
                }
            });
            */

    }

    public void loadNumberOfFollowers(){
        /*
        threads++;
        progress.setVisibility(View.VISIBLE);

        firebaseFirestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful())
                    //accountFragment.setNumberOfFollowers(task.getResult().size());

                threads--;
                loadingTaskDone();

            }
        });
        */

    }

    private void loadCatagories(){


        //Get the topics from Firebase and show in nav view
        firebaseFirestore.collection("Topics")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                final String catagorieName = document.getId();
                                firebaseFirestore.collection("Topics").document(document.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        if(task.getResult().exists()){

                                            Map<String, Object> tmpMap = task.getResult().getData();
                                            int members = Integer.parseInt(tmpMap.get("members").toString());
                                            catagoriesFragment.addCatagorie(new Catagorie(catagorieName, members));

                                        }

                                    }
                                });

                            }
                        } else {

                        }
                    }
                });

    }

    public void setAchievementDoneInfireBase(Boolean done, final Achievement achievementItem){
/*

        final String currentUserID= mAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        // if the achievement is not done by the current user then add the user name to the achievments list of
        // users otherwise remove it
        threads++;
        progress.setVisibility(View.VISIBLE);
        if(done){

            final HashMap<String, Object> tmpHashMap = new HashMap<>();
            tmpHashMap.put("timestamp", FieldValue.serverTimestamp());

            firebaseFirestore.collection("Achievements/"+achievementItem.getName()+"/Users").document(currentUserID).set(tmpHashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // add to Users/achievments as well
                            firebaseFirestore.collection("Users/"+currentUserID+"/Achievements").document(achievementItem.getName()).set(tmpHashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(mContext,"Achievement marked as done",Toast.LENGTH_LONG).show();
                                            achievementItem.setDone(true);
                                        }
                                    });
                            threads--;
                            loadingTaskDone();
                        }

                    });

        } else {

            firebaseFirestore.collection("Achievements/"+achievementItem.getName()+"/Users").document(currentUserID).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            firebaseFirestore.collection("Users/"+currentUserID+"/Achievements").document(achievementItem.getName()).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(mContext,"Achievement marked as not done",Toast.LENGTH_LONG).show();
                                            achievementItem.setDone(false);
                                        }
                                    });
                            threads--;
                            loadingTaskDone();

                        }
                    });


        }*/

    }

    public void updateAccountSetingsToFirestore(final String user_name){

        if (!TextUtils.isEmpty(user_name) && accountImageUri != null) {

            progress.setVisibility(View.VISIBLE);



                String user_id = mAuth.getCurrentUser().getUid();

                StorageReference img_path = storageReference.child("profile_images").child(user_id + ".jpg");
                img_path.putFile(accountImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {

                            storeAccountUsernameFirestore(task, user_name);

                        } else {

                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(MainActivity.this, "Image Error: " + errorMessage, Toast.LENGTH_LONG).show();

                            progress.setVisibility(View.INVISIBLE);

                        }


                    }
                });

        }

    }

    private void storeAccountUsernameFirestore(@NonNull Task<UploadTask.TaskSnapshot> task, String user_name){

        Uri downloadUri;
        if(task != null) {

            downloadUri = task.getResult().getDownloadUrl();

        } else {

            downloadUri = accountImageUri;
        }

        String user_id = mAuth.getCurrentUser().getUid();
        Map<String,String> userMap = new HashMap<>();
        userMap.put("name",user_name);
        userMap.put("image",downloadUri.toString());
        firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(MainActivity.this, "Updating settings",Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();

                } else {

                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(MainActivity.this, "FIRESTORE Error: "+errorMessage,Toast.LENGTH_LONG).show();

                }

                progress.setVisibility(View.INVISIBLE);
            }
        });

    }

    public static void displayfragmentOnStart(int fragmentId){

        displayFramentOnResume = fragmentId;



    }





}
