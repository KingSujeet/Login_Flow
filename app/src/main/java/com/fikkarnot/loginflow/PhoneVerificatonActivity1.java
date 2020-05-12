package com.fikkarnot.loginflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class PhoneVerificatonActivity1 extends AppCompatActivity {


    private Spinner spinner;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verificaton1);


        // Initializing the instances or connecting instances to activity_main.xml Views
        spinner = findViewById(R.id.spinnerCountries);
        // setting an adapter to spinner for displaying country name and code
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));

        editText = findViewById(R.id.editTextPhone);

        // adding OncliClickListener to button anonymously
        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
                // getting text from EditText and store it to String number
                String number = editText.getText().toString().trim();
                //adding some rule for the users such as EditText cannot be empty etc.
                if (number.isEmpty() || number.length() < 10) {
                    editText.setError("Valid number is required");
                    editText.requestFocus();
                    return;
                }

                String phonenumber = "+" + code + number;

                //if everything is all right then move to Otp activity (PhoneVerificationActivity2)
                Intent intent = new Intent(getApplicationContext(), PhoneVerificationActivity2.class);
                //putting phone number data to following activity with intent
                intent.putExtra("phonenumber", phonenumber);
                startActivity(intent);
                finish();

            }
        });

    }
}
