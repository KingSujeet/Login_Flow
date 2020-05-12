package com.fikkarnot.loginflow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.internal.SignInButtonImpl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    // creating instances or variables

    private SignInButtonImpl signInButton;
    private GoogleSignInClient googleSignInClient;
    private String TAG="mainTag";
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private int RESULT_CODE_SINGIN=999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initializing the instances or connecting instances to activity_main.xml Views
        progressBar = findViewById(R.id.progressBar);
        signInButton = findViewById(R.id.sign_in_button);

        // getting instances of FirebaseAuth class
        mAuth = FirebaseAuth.getInstance();

        //checking the users are already logged In or not.
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            // if the users are already logged in then directly move to HomeActivity
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();
        }

        // Configuring Google Sign IN
        GoogleSignInOptions gso = new
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //changing the progress bar color
                progressBar.setIndeterminateTintList(ColorStateList.valueOf(Color.parseColor("#673AB7")));
                //setting progressbar visibility to visible
                progressBar.setVisibility(View.VISIBLE);
                signInM();
            }
        });
    }

    private void signInM() {
        //this method will open the intent where users can select their account
        // for login process
        Intent singInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(singInIntent,RESULT_CODE_SINGIN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {


            if (requestCode == RESULT_CODE_SINGIN) {        //just to verify the code
                //create a Task object and use GoogleSignInAccount from Intent and write a separate method to handle singIn Result.

                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        }catch (Exception e){

            progressBar.setVisibility(View.GONE);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {

        //we use try catch block because of Exception.
        try {

            GoogleSignInAccount account = task.getResult(ApiException.class);
            //SignIn successful now show authentication
            FirebaseGoogleAuth(account);

        } catch (ApiException e) {
            e.printStackTrace();
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(MainActivity.this,"SignIn Failed!!!",Toast.LENGTH_LONG).show();
           FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        //here we are checking the Authentication Credential and checking the task is successful or not and display the message
        //based on that.
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //if everything is allright then move to PhoneVerification activity
                    Toast.makeText(MainActivity.this,"Signed In successfully",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),PhoneVerificatonActivity1.class));
                    finish();

                }
                else {
                    //if it is failed to login then Toast will appear
                    Toast.makeText(MainActivity.this,"Sign In Failed!",Toast.LENGTH_LONG).show();

                }
            }
        });


    }





}
