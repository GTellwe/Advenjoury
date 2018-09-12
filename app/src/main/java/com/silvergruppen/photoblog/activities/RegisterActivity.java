package com.silvergruppen.photoblog.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.silvergruppen.photoblog.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText regEmailText, regPassText, regConfText;
    private Button regBtn, regloginBtn;
    private ProgressBar regProgress;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        regEmailText = (EditText) findViewById(R.id.reg_email);
        regPassText = (EditText) findViewById(R.id.reg_pass);
        regConfText = (EditText) findViewById(R.id.reg_confirm_pass);
        regBtn = (Button )findViewById(R.id.reg_btn);
        regloginBtn = (Button) findViewById(R.id.reg_login_btn);
        regProgress = (ProgressBar) findViewById(R.id.reg_progress);

        regloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = regEmailText.getText().toString();
                String password = regPassText.getText().toString();
                String confPassword = regConfText.getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confPassword)) {

                    if(password.equals(confPassword)){

                        regProgress.setVisibility(View.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){

                                    sendToSetup();

                                } else{

                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, "Error: "+errorMessage, Toast.LENGTH_LONG).show();

                                }

                                regProgress.setVisibility(View.INVISIBLE);
                            }
                        });

                    } else {

                        Toast.makeText(RegisterActivity.this, "Passwords dosent match", Toast.LENGTH_LONG).show();

                    }

                }
            }
        });

    }
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            sendToMain();

        }
    }
    private void sendToMain() {

        Intent MainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(MainIntent);
        finish();
    }
    private void sendToSetup(){
        Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
        startActivity(setupIntent);
        finish();
    }
}
