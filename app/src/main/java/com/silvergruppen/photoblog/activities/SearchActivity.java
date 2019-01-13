package com.silvergruppen.photoblog.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class SearchActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    private ListView searchListView;
    private ArrayList<SearchItem> searchListItems;
    private SearchListAdapter searchListAdapter;
    private FirebaseFirestore firebaseFirestore;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mContext = this;

        // Set up the toolbar
        mainToolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle(null);

        // setup the listview
        searchListItems = new ArrayList<>();
        searchListView =(ListView) findViewById(R.id.search_list);
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




        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchViewItem.getActionView();
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


        return true;
    }

    private void doMySearch(String query) {
    }
}
