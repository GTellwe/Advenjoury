package com.silvergruppen.photoblog.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.adapters.RecycleViewAdapter;
import com.silvergruppen.photoblog.items.Catagorie;
import com.silvergruppen.photoblog.items.RecycleListItem;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CatagoriesFragment extends Fragment {


    public RecyclerView acatagoriesRecyclerView;


    public ArrayList<Catagorie> listCatagories;


    public FirebaseFirestore firebaseFirestore;
    private RecyclerView.LayoutManager mLayoutManager;

    private FirebaseAuth mAuth;

    private String current_user_id;
    private RecycleViewAdapter catagoriesListAdapter;
    private MainActivity mainActivity;
    private final String headline = "ACHIEVEMENT";

    private CircleImageView profileImage;
    private String imageURL;

    public CatagoriesFragment() {

        listCatagories = new ArrayList<>();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_catagories, container, false);


        mainActivity = (MainActivity) getActivity();
        mainActivity.setHeadline(headline);
        // Get the current user
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        // General initilazations
        firebaseFirestore = FirebaseFirestore.getInstance();

        // set the profile image
        profileImage = view.findViewById(R.id.catagories_profile_image);
        imageURL = mainActivity.getCurrentUserImageUrl();
        if(imageURL != null) {
            RequestOptions placeholderRequest = new RequestOptions();
            placeholderRequest.placeholder(R.drawable.profile_icon);
            Glide.with(getContext()).setDefaultRequestOptions(placeholderRequest).load(imageURL).into(profileImage);
        }

        // Handle the list of achievements
        acatagoriesRecyclerView = (RecyclerView) view.findViewById(R.id.catagories_list_view);
        acatagoriesRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        acatagoriesRecyclerView.setLayoutManager(mLayoutManager);
        catagoriesListAdapter = new RecycleViewAdapter(getActivity(), listCatagories);
        acatagoriesRecyclerView.setAdapter(catagoriesListAdapter);
        catagoriesListAdapter.notifyDataSetChanged();


        return view;
    }




    public void addCatagorie(Catagorie catagorie){

        listCatagories.add(catagorie);

        //catagoriesListAdapter.notifyDataSetChanged();

    }
    public void notifyDatasetChanged(){

        catagoriesListAdapter.notifyDataSetChanged();
    }
    public ArrayList<Catagorie> getListAchievements() {
        return listCatagories;
    }

    public void setListAchievements(ArrayList<Catagorie> listAchievements) {
        this.listCatagories = listAchievements;
    }
    public void clearAchievementList(){

        listCatagories.clear();
    }
}
