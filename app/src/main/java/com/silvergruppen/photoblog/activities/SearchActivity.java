package com.silvergruppen.photoblog.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

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
    private TextView mainToolbarHeadline;
    private BottomNavigationView mainBottomNav;
    private MainActivity mainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mContext = this;


        // Set up the toolbar
        mainToolbar = (Toolbar) findViewById(R.id.search_activity_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle(null);


        // setup the listview
        searchListItems = new ArrayList<>();
        searchListView =(ListView) findViewById(R.id.search_list);
        searchListAdapter = new SearchListAdapter(mContext,searchListItems);
        searchListView.setAdapter(searchListAdapter);

        mainToolbarHeadline = findViewById(R.id.search_activity_toolbar_headline);
        mainToolbarHeadline.setText("SEARCH");

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


        SearchView searchView = (SearchView) findViewById(R.id.search_view);
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

        changeBottomMenuIconSize();

        mainBottomNav = findViewById(R.id.search_activity_bottom_nav);
        mainBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.bottom_action_home:

                        //MainActivity.replaceFragment(MainActivity.homefragmentId);
                        MainActivity.displayfragmentOnStart(MainActivity.homefragmentId);
                        finish();
                        return true;
                    case R.id.bottom_action_achievement:
                        MainActivity.displayfragmentOnStart(MainActivity.catogiresFragmentId);
                        finish();
                        return true;
                    case R.id.bottom_action_account:
                        MainActivity.displayfragmentOnStart(MainActivity.accountFragmentId);
                        finish();
                        return true;
                    case R.id.bottom_action_daily_progress:
                        MainActivity.displayfragmentOnStart(MainActivity.calendarFragmentId);
                        finish();
                        return true;

                    default:
                        return false;

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
/*
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

*/
        return true;
    }

    private void doMySearch(String query) {
    }

    private void changeBottomMenuIconSize() {

        BottomNavigationView bottomNavigation = (BottomNavigationView) findViewById(R.id.search_activity_bottom_nav);

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigation.getChildAt(0);

        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, displayMetrics);
            iconView.setLayoutParams(layoutParams);

        }
    }
}
