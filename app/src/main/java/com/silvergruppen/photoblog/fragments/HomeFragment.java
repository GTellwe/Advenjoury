package com.silvergruppen.photoblog.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.silvergruppen.photoblog.adapters.PostListAdapter;
import com.silvergruppen.photoblog.items.PostItem;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.other.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static ArrayList<PostItem> postItems;
    private static ArrayList<User> allUserID;

    private ListView homeListView;
    private static SortbyDate comparator;

    private static PostListAdapter postListAdapter;

    FirebaseFirestore firebaseFirestore;
    public HomeFragment() {

        allUserID = new ArrayList<>();
        postItems = new ArrayList<>();
        comparator = new SortbyDate();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container,false);

        // setup the list view
        homeListView = view.findViewById(R.id.home_list_view);
        homeListView.setItemsCanFocus(true);
        postListAdapter = new PostListAdapter(getActivity(), postItems);

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
        // Post items choudl be added in the correct order
        postItems.add(postItem);
        Collections.sort(postItems, comparator);
        postListAdapter.clonePostList(postItems);
        postListAdapter.notifyDataSetChanged();
    }

    public void notifyDataSetChanged(){

        postListAdapter.notifyDataSetChanged();
    }
    public static void clearPostItemsList(){

        if(postItems != null)
            postItems.clear();
    }

    // comparator for sorting the post items after date
    private class SortbyDate implements Comparator<PostItem>
    {

        public int compare(PostItem a, PostItem b)
        {
            return b.getTimeStamp().compareTo(a.getTimeStamp());
        }
    }
}
