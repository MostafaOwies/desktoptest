package com.example.phoneauth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;

public class LoggedIn extends AppCompatActivity {

    VideoView videoView ;
    FirebaseAuth mAuth;
    Button logOut;
    public EditText phoneNumberView, otpView;
    public Button logInViewBTN, verifyViewBTN;
    public CardView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        videoView = findViewById(R.id.videoView);
        logOut=findViewById(R.id.logout);
        phoneNumberView = findViewById(R.id.phoneNumber);
        otpView = findViewById(R.id.OTP);
        logInViewBTN = findViewById(R.id.logIn);
        verifyViewBTN = findViewById(R.id.OTPverifiy);
        cardView=findViewById(R.id.cardView);

        mAuth = FirebaseAuth.getInstance();
        String path = "android.resource://"+getPackageName()+"/"+R.raw.rickroll;
        Uri uri= Uri.parse(path);
        videoView.setVideoURI(uri);
        MediaController controller= new MediaController(this);
        videoView.setMediaController(controller);
        controller.setAnchorView(videoView);
        videoView.start();

    }
    public void Logout(View view){
        mAuth.signOut();
        finish();


    }
}