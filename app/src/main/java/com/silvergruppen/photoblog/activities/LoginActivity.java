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

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmailText, loginPassText;
    private Button loginRegBtn, loginBtn;
    private ProgressBar loginProgress;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginEmailText = (EditText) findViewById(R.id.login_email);
        loginPassText = (EditText) findViewById(R.id.login_password);
        loginRegBtn = (Button )findViewById(R.id.login_reg_btn);
        loginBtn = (Button) findViewById(R.id.login_btn);
        loginProgress = (ProgressBar) findViewById(R.id.login_progress);

        loginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regIntent);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String loginEmail = loginEmailText.getText().toString();
                String loginPassword = loginPassText.getText().toString();

                if(!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPassword)){

                    loginProgress.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(loginEmail,loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                sendToMain();

                            } else{

                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error: "+errorMessage, Toast.LENGTH_LONG).show();

                            }

                            loginProgress.setVisibility(View.INVISIBLE);
                        }
                    });


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

        Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(MainIntent);
        finish();
    }
}
