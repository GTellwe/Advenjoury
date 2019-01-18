package com.silvergruppen.photoblog.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.activities.NewPostActivity;
import com.silvergruppen.photoblog.adapters.PostListAdapter;
import com.silvergruppen.photoblog.items.PostItem;
import com.silvergruppen.photoblog.other.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class JournalFragment extends Fragment {

    private static ArrayList<PostItem> postItems;
    private ListView journalListView;
    private static PostListAdapter postListAdapter;
    private static SortbyDate comparator;
    private FloatingActionButton addPostButton;


    public JournalFragment() {
        comparator = new SortbyDate();
        postItems = new ArrayList<>();

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_journal, container, false);

        addPostButton = view.findViewById(R.id.add_post_button);
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newPostIntent = new Intent(getActivity(), NewPostActivity.class);
                Bundle b = new Bundle();
                MainActivity tmpActivity = (MainActivity) getActivity();
                b.putString("topic",tmpActivity.getTopic());
                b.putString("achievement", tmpActivity.getCurrentAchievement());
                newPostIntent.putExtras(b);
                getActivity().startActivity(newPostIntent);
            }
        });

        journalListView = view.findViewById(R.id.journal_list_view);
        journalListView.setItemsCanFocus(true);
        postListAdapter = new PostListAdapter(getActivity(), postItems);

        journalListView.setAdapter(postListAdapter);
        journalListView.setItemsCanFocus(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
        //postListAdapter.clonePostList(postItems);
        //postListAdapter.notifyDataSetChanged();
    }

    public void notifyDataSetChanged(){

        postListAdapter.notifyDataSetChanged();
    }
    public static void clearPostItemsList(){

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
