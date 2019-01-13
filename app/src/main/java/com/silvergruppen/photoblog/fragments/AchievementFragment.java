package com.silvergruppen.photoblog.fragments;

import android.app.Activity;
import android.location.SettingInjectorService;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.items.Achievement;
import com.silvergruppen.photoblog.items.PostItem;

import java.util.ArrayList;
import java.util.List;

public class AchievementFragment extends Fragment {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ArrayList<Fragment> fragmentsList;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private FragmentActivity myContext;
    private HowtoFragment howtoFragment;
    private JournalFragment journalFragment;
    private AchievementSettingsFragment settingsFragment;
    private Achievement achievement;

    public AchievementFragment(){

        howtoFragment = new HowtoFragment();
        journalFragment  = new JournalFragment();
        settingsFragment = new AchievementSettingsFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_achievement);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);

        //  setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_achievement, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.achievement_view_pager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    public void addJournalItem(PostItem postItem){

        journalFragment.addPostItem(postItem);

    }
    public void clearAllJournalItems(){

        journalFragment.clearPostItemsList();
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(myContext.getSupportFragmentManager());
        adapter.addFragment(howtoFragment, "How to");
        adapter.addFragment(journalFragment, "Journal");
        adapter.addFragment(settingsFragment, "Settings");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public Achievement getAchievement() {
        return achievement;
    }

    public void setAchievement(Achievement achievement) {
        settingsFragment.setAchievement(achievement);
        this.achievement = achievement;
    }
}