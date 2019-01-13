package com.silvergruppen.photoblog.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.silvergruppen.photoblog.R;

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
