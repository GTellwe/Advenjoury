package com.silvergruppen.photoblog.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.silvergruppen.photoblog.listAdapters.AchievementListAdapter;
import com.silvergruppen.photoblog.listAdapters.PostListAdapter;
import com.silvergruppen.photoblog.listItems.PostItem;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.other.User;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static ArrayList<PostItem> postItems;
    private static ArrayList<User> allUserID;

    private ListView homeListView;

    private PostListAdapter postListAdapter;

    FirebaseFirestore firebaseFirestore;
    public HomeFragment() {

        allUserID = new ArrayList<>();
        postItems = new ArrayList<>();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container,false);

        // setup the list view
        homeListView = view.findViewById(R.id.home_list_view);
        homeListView.setItemsCanFocus(true);
        postListAdapter = new PostListAdapter(getActivity(), R.layout.post_list_item, postItems);
        homeListView.setAdapter(postListAdapter);
        homeListView.setItemsCanFocus(true);

        return view;

    }



    public ArrayList<User> getAllUserID() {
        return allUserID;
    }

    public static void setAllUserID(ArrayList<User> newNllUserID) {

        allUserID = newNllUserID;

    }

    public ArrayList<PostItem> getPostItems() {
        return postItems;
    }

    public static void setPostItems(ArrayList<PostItem> newPostItems) {

        postItems.clear();
        postItems.addAll(newPostItems);

    }
    public static void addPostItem(PostItem postItem){

        postItems.add(postItem);
    }

    public void notifyDataSetChanged(){

        postListAdapter.notifyDataSetChanged();
    }
    public static void clearPostItemsList(){

        postItems.clear();
    }
}
