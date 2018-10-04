package com.silvergruppen.photoblog.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    private TextView accountName, level;
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

    public AccountFragment() {
        achievemensDoneList = new ArrayList<>();
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

        // Set up the list view

        accountListView = view.findViewById(R.id.account_list_view);
        accountListView.setItemsCanFocus(true);
        achievementListAdapter= new AchievementListAdapter(getActivity(), R.layout.achievement_list_item, achievemensDoneList,null, false);
        accountListView.setAdapter(achievementListAdapter);
        accountListView.setItemsCanFocus(true);

        return view;
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
        if(achievementListAdapter != null)
            achievementListAdapter.notifyDataSetChanged();

    }
}
