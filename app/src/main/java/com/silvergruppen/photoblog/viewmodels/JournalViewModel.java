package com.silvergruppen.photoblog.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.silvergruppen.photoblog.items.Achievement;
import com.silvergruppen.photoblog.items.PostItem;
import com.silvergruppen.photoblog.repositories.JournalRepository;

import java.util.ArrayList;

public class JournalViewModel extends ViewModel {

    private LiveData<ArrayList<PostItem>> journalItems;
    private JournalRepository repo;


    public JournalViewModel() {
        repo = new JournalRepository();
    }

    public void init(String userId, Achievement achievement) {
        if (journalItems != null){
            //ViewModel is created on a per-Fragment basis, so the userId
            // doesn't change.
            return;
        }
        journalItems = repo.getJournalItems(userId, achievement);
    }
    public LiveData<ArrayList<PostItem>> getJournalItems() {
        return this.journalItems;
    }

}