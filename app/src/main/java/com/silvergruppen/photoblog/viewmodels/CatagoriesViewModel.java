package com.silvergruppen.photoblog.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.silvergruppen.photoblog.items.Catagorie;
import com.silvergruppen.photoblog.repositories.CalendarRepository;
import com.silvergruppen.photoblog.repositories.CatagoriesRepository;

import java.util.ArrayList;

public class CatagoriesViewModel extends ViewModel {


    private LiveData<ArrayList<Catagorie>> catagories;
    private CatagoriesRepository catagorieRepo;


    public CatagoriesViewModel() {
        catagorieRepo = new CatagoriesRepository();
    }

    public void init() {
        if (this.catagories != null) {
            // ViewModel is created on a per-Fragment basis, so the userId
            // doesn't change.
            return;
        }
        catagories = catagorieRepo.getCatagories();
    }
    public LiveData<ArrayList<Catagorie>> getCatagories() {
        return this.catagories;
    }


}
