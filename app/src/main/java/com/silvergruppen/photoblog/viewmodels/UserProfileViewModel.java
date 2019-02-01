package com.silvergruppen.photoblog.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.silvergruppen.photoblog.other.User;
import com.silvergruppen.photoblog.repositories.UserRepository;

import javax.inject.Inject;

public class UserProfileViewModel extends ViewModel {



    private LiveData<User> user;
    private UserRepository userRepo;


    public UserProfileViewModel() {
        userRepo = new UserRepository();
    }

    public void init(String userId) {
        if (this.user != null) {
            // ViewModel is created on a per-Fragment basis, so the userId
            // doesn't change.
            return;
        }
        user = userRepo.getUser(userId);
    }
    public LiveData<User> getUser() {
        return this.user;
    }
}
