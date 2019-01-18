package com.silvergruppen.photoblog.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.activities.SetupActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSettingsFragment extends Fragment {

    private TextView accountName;
    private CircleImageView imageView;

    private FirebaseAuth firebaseAuth;
    private String user_id;
    private FirebaseFirestore firebaseFirestore;

    private ImageView editProfileImage;

    private String userName, imageURL;

    private Uri imageUri;

    private MainActivity mainActivity;

    private Button updateBtn;

    private boolean settupChanged;


    public AccountSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);

        // get the username from Firebase and set the image and headline
        accountName = view.findViewById(R.id.account_settings_account_name);
        imageView = view.findViewById(R.id.account_settings_profile_image);
        editProfileImage = view.findViewById(R.id.account_settings_edit_profile_image);
        updateBtn = view.findViewById(R.id.account_settings_update_btn);

        mainActivity = (MainActivity) getContext();


        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Load setName
        if(userName != null)
            accountName.setText(userName);
        // Load image
        if(imageURL != null) {
            RequestOptions placeholderRequest = new RequestOptions();
            placeholderRequest.placeholder(R.drawable.profile_icon);
            Glide.with(getContext()).setDefaultRequestOptions(placeholderRequest).load(imageURL).into(imageView);
        }


        editProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                        Toast.makeText(getContext(),"Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(mainActivity, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);

                    } else {

                        imageSelect();
                    }

                } else {

                    imageSelect();

                }

            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user_name = accountName.getText().toString();

                mainActivity.updateAccountSetingsToFirestore(user_name);

            }
        });

        return view;
    }



    private void imageSelect(){

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(mainActivity);

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {

        this.userName = userName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public boolean isSettupChanged() {
        return settupChanged;
    }

    public void setSettupChanged(boolean settupChanged) {
        this.settupChanged = settupChanged;
    }
}
