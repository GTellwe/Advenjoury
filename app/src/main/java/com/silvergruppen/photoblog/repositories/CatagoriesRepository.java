package com.silvergruppen.photoblog.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.silvergruppen.photoblog.items.Catagorie;

import java.util.ArrayList;
import java.util.Map;

public class CatagoriesRepository {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public LiveData<ArrayList<Catagorie>> getCatagories() {
        // This isn't an optimal implementation. We'll fix it later.
        final MutableLiveData<ArrayList<Catagorie>> data = new MutableLiveData<>();

        //Get the topics from Firebase and show in nav view
        firebaseFirestore.collection("Topics")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            final ArrayList<Catagorie> tmpArray = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {

                                final String catagorieName = document.getId();
                                firebaseFirestore.collection("Topics").document(document.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        if (task.getResult().exists()) {

                                            Map<String, Object> tmpMap = task.getResult().getData();
                                            int members = Integer.parseInt(tmpMap.get("members").toString());
                                            tmpArray.add(new Catagorie(catagorieName, members));

                                        }

                                    }
                                });

                            }
                            data.setValue(tmpArray);
                        } else {

                        }
                    }
                });

        return data;
    }

}

