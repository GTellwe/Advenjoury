package com.silvergruppen.photoblog.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.adapters.SearchListAdapter;
import com.silvergruppen.photoblog.items.SearchItem;

import java.util.ArrayList;
import java.util.Map;


public class SearchFragment extends Fragment {


    private ListView searchListView;
    private ArrayList<SearchItem> searchListItems;
    private SearchListAdapter searchListAdapter;
    private FirebaseFirestore firebaseFirestore;
    private Context mContext;

    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_search, container, false);

        // setup the listview
        searchListItems = new ArrayList<>();
        searchListView =(ListView) view.findViewById(R.id.search_list);
        searchListAdapter = new SearchListAdapter(mContext,searchListItems);
        searchListView.setAdapter(searchListAdapter);

        //Load all the profiles into searchListItems
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    for (DocumentSnapshot document : task.getResult()){

                        Map<String,Object> tmpMap = document.getData();
                        String name = (String) tmpMap.get("name").toString();
                        String image = (String) tmpMap.get("image").toString();
                        String user_id = document.getId();
                        SearchItem searchItem = new SearchItem(image, name, user_id);
                        searchListItems.add(searchItem);

                    }
                    // Set the list adapter
                    searchListAdapter.setArraylist(searchListItems);
                    searchListAdapter.notifyDataSetChanged();
                }
            }
        });

        SearchView searchView = (SearchView) view.findViewById(R.id.search_widget);
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchListAdapter.filter(s);
                return false;
            }
        });


        return view;
    }



}
