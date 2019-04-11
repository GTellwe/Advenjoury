package com.silvergruppen.photoblog.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    // Views
    public RecyclerView achievementsRecyclerView;
    private CircleImageView profileImage;
    private TextView titleTextView;

    // Lists
    public ArrayList<RecycleListItem> listAchievements;

    // firebase
    public FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;

    // strings
    private String current_user_id;
    private  final String headline = "ACIEVEMENTS";
    private String imageURL;

    //other
    private Catagorie currentCatagorie;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecycleViewAdapter catagoriesListAdapter;
    private MainActivity mainActivity;
    private AchievmentsViewModel achievmentsViewModel;




    public AchievementsFragment2() {

        listAchievements = new ArrayList<>();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_achievements_2, container, false);


        mainActivity = (MainActivity) getActivity();
        mainActivity.setHeadline(headline);

        // Get the current user
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

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
        achievmentsViewModel.init();
        final Observer<ArrayList<RecycleListItem>> achievementsObserver = new Observer<ArrayList<RecycleListItem>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<RecycleListItem> newAchievements) {
                listAchievements =newAchievements;
                catagoriesListAdapter.updateList(getListOfAchievementsInCatagorie(currentCatagorie));

            }
        };
        achievmentsViewModel.getAchievementsList().observe(this,achievementsObserver);


        //if(currentCatagorie != null)
            //progressTextView.setText(Integer.toString(mainActivity.getAchievementsDoneByCurrentUser(currentCatagorie))+"/"+Integer.toString(currentCatagorie.getNumberOfAchievments()));
        return view;
    }




    public void addAchievement(Achievement achievement){

        listAchievements.add(achievement);

        //catagoriesListAdapter.notifyDataSetChanged();

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
    public void notifyDatasetChanged(){

        catagoriesListAdapter.notifyDataSetChanged();
    }
    public ArrayList<RecycleListItem> getListAchievements() {
        return listAchievements;
    }

    public void setListAchievements(ArrayList<RecycleListItem> listAchievements) {
        this.listAchievements = listAchievements;
    }
    public void clearAchievementList(){

        listAchievements.clear();
    }

    public Catagorie getCurrentCatagorie() {
        return currentCatagorie;
    }

    public void setCurrentCatagorie(Catagorie currentCatagorie) {
        this.currentCatagorie = currentCatagorie;
    }
}
