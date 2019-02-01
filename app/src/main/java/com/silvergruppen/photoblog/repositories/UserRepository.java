package com.silvergruppen.photoblog.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.common.api.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silvergruppen.photoblog.fragments.AccountFragment;
import com.silvergruppen.photoblog.other.User;

import java.util.Map;

public class UserRepository {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public LiveData<User> getUser(String userId) {
        // This isn't an optimal implementation. We'll fix it later.
        final MutableLiveData<User> data = new MutableLiveData<>();

        firebaseFirestore.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){
                        Map<String, Object> tmpMap = task.getResult().getData();

                        String name = tmpMap.get("name").toString();
                        String image = tmpMap.get("image").toString();

                        data.setValue(new User(null,image,name));

                    }
                }


            }
        });
        return data;
    }
}
