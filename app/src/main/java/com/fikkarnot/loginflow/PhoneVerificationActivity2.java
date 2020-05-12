package com.fikkarnot.loginflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneVerificationActivity2 extends AppCompatActivity {

    private String verificationid;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText;
    private String phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification2);
        // creating instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Initializing the instances or connecting instances to activity_main.xml Views
        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.editTextCode);

        // getting data from previous activity
        phonenumber = getIntent().getStringExtra("phonenumber");
        sendVerificationCode(phonenumber);
        // adding OnclickListener to button Anonymously
        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString().trim();

                // Adding some rules for users when entering OTP
                if ((code.isEmpty() || code.length() < 6)){

                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
                // called verifyCode method
                verifyCode(code);

            }
        });
    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);

    }


    private void sendVerificationCode(String number){
        // this method will send sms to users
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationid = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //getting the code send by sms
            String code = phoneAuthCredential.getSmsCode();
            // Sometimes the code is not detect automatically
            // then users has to set code manually
            if (code != null){
                // setting progressbar visibility to Visible
                progressBar.setVisibility(View.VISIBLE);
                // moving to UserDetailActivity
                Intent intent = new  Intent(getApplicationContext(),UserDetailActivity.class);
                // putting phonenumber data to next activity with intent
                intent.putExtra("phone",phonenumber);
                startActivity(intent);
                finish();
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            // if it is failed then showing error toast to users
            Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG).show();

        }
    };
}