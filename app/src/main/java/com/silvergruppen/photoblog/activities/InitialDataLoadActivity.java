package com.silvergruppen.photoblog.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.fragments.AccountFragment;
import com.silvergruppen.photoblog.fragments.HomeFragment;
import com.silvergruppen.photoblog.listItems.Achievement;
import com.silvergruppen.photoblog.listItems.PostItem;
import com.silvergruppen.photoblog.other.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InitialDataLoadActivity extends AppCompatActivity {


    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_data_load);



        mContext = this;
    }




    private void sendToLogin(){
        Intent intent = new Intent(InitialDataLoadActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
