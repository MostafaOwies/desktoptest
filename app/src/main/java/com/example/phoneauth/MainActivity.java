package com.example.phoneauth;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public FirebaseAuth mAuth ;
    public EditText phoneNumberView, otpView;
    public Button logInViewBTN, verifyViewBTN;
    public String verificationID;
    public String phoneNumber;
    public String otp;
    public CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        mAuth.useAppLanguage();
        phoneNumberView = findViewById(R.id.phoneNumber);
        otpView = findViewById(R.id.OTP);
        logInViewBTN = findViewById(R.id.logIn);
        verifyViewBTN = findViewById(R.id.OTPverifiy);
        phoneNumberView.setVisibility(View.VISIBLE);
        logInViewBTN.setVisibility(View.VISIBLE);
        cardView=findViewById(R.id.cardView);
        cardView.setVisibility(View.INVISIBLE);
    }
    @Override
    protected  void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), LoggedIn.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected  void onRestart() {
        super.onRestart();
        phoneNumberView.setVisibility(View.VISIBLE);
        logInViewBTN.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.INVISIBLE);
        verifyViewBTN.setVisibility(View.INVISIBLE);
        otpView.setVisibility(View.INVISIBLE);
        phoneNumberView.setText("");
        otpView.setText("");
    }

    //Login button onClick Method
    public void Login(View view){
        phoneNumber=phoneNumberView.getText().toString();
        if (phoneNumber.isEmpty() ) {
            Toast.makeText(MainActivity.this, "Enter phone number", Toast.LENGTH_SHORT).show();
        } else {
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(phoneNumber)       // Phone number to verify
                            .setTimeout(5L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);

        }
        cardView.setVisibility(View.VISIBLE);
        logInViewBTN.setVisibility(View.INVISIBLE);
    }

    public void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
        signInByCredential(credential);
    }

    public void signInByCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoggedIn.class);
                        startActivity(intent);
                    }
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                verifyCode(code);
                Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(MainActivity.this, "Invalid Credential", Toast.LENGTH_SHORT).show();
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Toast.makeText(MainActivity.this, "Too Many Requests", Toast.LENGTH_SHORT).show();
            } else {
                Log.w(TAG, "onVerificationFailed", e);
            }
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(verificationId, token);
            Toast.makeText(MainActivity.this, "Code sent", Toast.LENGTH_SHORT).show();
            verificationID = verificationId;
            phoneNumberView.setVisibility(View.INVISIBLE);
            otpView.setVisibility(View.VISIBLE);
            verifyViewBTN.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.INVISIBLE);
        }
    };

    public void verifyOTP(View view) {
        otp=otpView.getText().toString();
        if (otp.isEmpty()){
            Toast.makeText(MainActivity.this, "Wrong OTP", Toast.LENGTH_SHORT).show();
        }
        else {
            verifyCode(otp);
        }
        cardView.setVisibility(View.VISIBLE);
        verifyViewBTN.setVisibility(View.INVISIBLE);
    }
}