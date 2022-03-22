package com.year3project.qrcodegenerator;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GeneratorFragment extends Fragment {

    public static final String EXTRA_MESSAGE = "com.year3project.qrcodegenerator.MESSAGE";

    EditText fullName;
    EditText contactNumber;
    EditText address;
    EditText age;
    EditText temperature;
    EditText gender;
    EditText reason;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_generator_fragment, container, false);

        Button mGenerate = (Button) rootView.findViewById(R.id.btn_generate);
        mGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayQRCodeActivity();
            }
        });

        fullName = rootView.findViewById(R.id.tf_fullname);
        contactNumber = rootView.findViewById(R.id.tf_contact_number);
        address = rootView.findViewById(R.id.tf_address);
        age = rootView.findViewById(R.id.tf_age);
        temperature = rootView.findViewById(R.id.tf_temp);
        gender = rootView.findViewById(R.id.tf_gender);
        reason = rootView.findViewById(R.id.tf_purpose);

        return rootView;


        //return inflater.inflate(R.layout.fragment_generator, container, false);


    }

    public void displayQRCodeActivity() {
        String fullName_text = fullName.getText().toString().trim();
        String contactNumber_text = contactNumber.getText().toString().trim();
        String address_text = address.getText().toString().trim();
        String age_text = age.getText().toString().trim();
        String temperature_text = temperature.getText().toString().trim();
        String gender_text = gender .getText().toString().trim();
        String reason_text = reason.getText().toString().trim();

        if (!TextUtils.isEmpty(fullName_text) && !TextUtils.isEmpty(contactNumber_text) && !TextUtils.isEmpty(address_text) &&
                !TextUtils.isEmpty(age_text) && !TextUtils.isEmpty(temperature_text) && !TextUtils.isEmpty(gender_text) &&
                !TextUtils.isEmpty(reason_text) && fullName_text.length() <= 255 && contactNumber_text.length() <= 12 &&
                address_text.length() <= 255 && age_text.length() <= 2 && temperature_text.length() <= 5 && gender_text.length() <= 6 &&
                reason_text.length() <= 255) {
            String logData = getFormData();

            Intent intent = new Intent(getActivity(), DisplayQRCode.class);
            intent.putExtra(EXTRA_MESSAGE, logData);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "Please fill up form properly", Toast.LENGTH_LONG).show();
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