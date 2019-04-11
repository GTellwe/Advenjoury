package com.silvergruppen.photoblog.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.activities.NewPostActivity;
import com.silvergruppen.photoblog.adapters.PostListAdapter;
import com.silvergruppen.photoblog.items.Achievement;
import com.silvergruppen.photoblog.items.PostItem;
import com.silvergruppen.photoblog.viewmodels.JournalViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class JournalFragment extends Fragment {

    // Constants
    private static final String UID_KEY = "uid";
    private static final String ACHIEVMENT_KEY = "achi";
    // views
    private FloatingActionButton addPostButton;

    // lists
    private static ArrayList<PostItem> postItems;

    // strings
    private String userId;

    // views
    private ListView journalListView;

    //other
    private static PostListAdapter postListAdapter;
    private static SortbyDate comparator;
    private JournalViewModel viewModel;
    private Achievement achievement;


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

        // Handle the post button
        addPostButton = view.findViewById(R.id.add_post_button);
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newPostIntent = new Intent(getActivity(), NewPostActivity.class);
                Bundle b = new Bundle();
                b.putString("topic",achievement.getTopic());
                b.putString("achievement", achievement.getName());
                newPostIntent.putExtras(b);
                getActivity().startActivity(newPostIntent);
            }
        });

        // set up the list view
        journalListView = view.findViewById(R.id.journal_list_view);
        journalListView.setItemsCanFocus(true);
        postListAdapter = new PostListAdapter(getActivity(), postItems);

        journalListView.setAdapter(postListAdapter);
        journalListView.setItemsCanFocus(true);

        // get the data for the list view
        viewModel = ViewModelProviders.of(this).get(JournalViewModel.class);
        viewModel.init(userId, achievement);

        final Observer<ArrayList<PostItem>> listObserver = new Observer<ArrayList<PostItem>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<PostItem> newPostItems) {

                postItems = newPostItems;
                notifyDataSetChanged();

                Toast.makeText(getContext(), Integer.toString(postItems.size()), Toast.LENGTH_LONG).show();
            }
        };

        viewModel.getJournalItems().observe(this, listObserver);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void notifyDataSetChanged(){

        postListAdapter.notifyDataSetChanged();
    }


    // comparator for sorting the post items after date
    private class SortbyDate implements Comparator<PostItem>
    {

        public int compare(PostItem a, PostItem b)
        {
            return b.getTimeStamp().compareTo(a.getTimeStamp());
        }
    }

    public void setAchievement(Achievement achievement){
        this.achievement = achievement;

    }

    public void setUserId(String userId){
        this.userId = userId;

    }


}
