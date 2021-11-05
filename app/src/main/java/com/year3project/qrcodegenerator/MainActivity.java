package com.year3project.qrcodegenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.year3project.qrcodegenerator.MESSAGE";

    EditText fullName;
    EditText contactNumber;
    EditText address;
    EditText age;
    EditText temperature;
    EditText gender;
    EditText reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fullName = findViewById(R.id.full_name);
        contactNumber = findViewById(R.id.contact_number);
        address = findViewById(R.id.address);
        age = findViewById(R.id.age);
        temperature = findViewById(R.id.temperature);
        gender = findViewById(R.id.gender);
        reason = findViewById(R.id.reason);
    }

    public void displayQRCodeActivity(View view) {
        String fullName_text = fullName.getText().toString().trim();
        String contactNumber_text = contactNumber.getText().toString().trim();
        String address_text = address.getText().toString().trim();
        String age_text = age.getText().toString().trim();
        String temperature_text = temperature.getText().toString().trim();
        String gender_text = gender .getText().toString().trim();
        String reason_text = reason.getText().toString().trim();

        if (!TextUtils.isEmpty(fullName_text) && !TextUtils.isEmpty(contactNumber_text) && !TextUtils.isEmpty(address_text) &&
                !TextUtils.isEmpty(age_text) && !TextUtils.isEmpty(temperature_text) && !TextUtils.isEmpty(gender_text) &&
                !TextUtils.isEmpty(reason_text)) {
            String logData = getFormData();

            Intent intent = new Intent(this, DisplayQRCode.class);
            intent.putExtra(EXTRA_MESSAGE, logData);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please fill up the form", Toast.LENGTH_LONG).show();
        }

    }

    private String getFormData() {
        String separator = ",,,,";
        String logData = fullName.getText().toString() + separator + contactNumber.getText().toString() + separator + address.getText().toString() +
                separator + age.getText().toString() + separator + temperature.getText().toString() + separator + gender.getText().toString() +
                separator + reason.getText().toString();

        return logData;
    }
}