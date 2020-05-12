package com.fikkarnot.loginflow;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class UserDetailActivity extends AppCompatActivity {

    String phonenumber;
    TextInputEditText name,prof, dob;
    ProgressBar progressBar;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    DatabaseReference mDatabaseReference;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_details);

        // Initializing the instances or connecting instances to activity_main.xml Views
        name = findViewById(R.id.editT_name);
        prof = findViewById(R.id.editT_prof);
        dob = findViewById(R.id.dob);
        progressBar = findViewById(R.id.progressBar2);

        // getting data from previous activity
        phonenumber = getIntent().getStringExtra("phone");

        // getting current user information
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // getting calender instance
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        // getting Database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        Button button = findViewById(R.id.button);
        // setting OnClickListener to button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // adding rules for users
                if (TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(prof.getText().toString()) || TextUtils.isEmpty(dob.getText().toString()) )
                {
                    Toast.makeText(getApplicationContext(),"Please fill all the fields",Toast.LENGTH_SHORT).show();
                    if (TextUtils.isEmpty(name.getText().toString())){

                        name.setError("Required!!");
                        name.requestFocus();
                    }
                    if (TextUtils.isEmpty(name.getText().toString())){

                        prof.setError("Required!!");
                        prof.requestFocus();
                    }

                    return;
                }

                try {

                                progressBar.setVisibility(View.VISIBLE);
                            // creating object of MyAppUser class
                            MyAppUser user = new MyAppUser();
                            //setting users data to MyAPPUser class using Setters method
                            user.setName(name.getText().toString());
                            user.setProfession(prof.getText().toString());
                            user.setDob(dob.getText().toString());
                            user.setPhone(phonenumber);
                            // creating Users database field in firebase database with Unique users UID
                            mDatabaseReference.child("Users").child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(UserDetailActivity.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        // Go to HomeActivity if users data succesfully send to firebase
                                        Toast.makeText(getApplicationContext(), "Successfully Registered ", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(UserDetailActivity.this, HomeActivity.class));
                                        finish();
                                    } else {
                                        // if any error occur then show error mesaage to the user using Toast
                                        Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });



                }catch (Exception e){

                    // if any error occur then show error mesaage to the user using Toast
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        // setting data text to EditText
        dob.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

}
