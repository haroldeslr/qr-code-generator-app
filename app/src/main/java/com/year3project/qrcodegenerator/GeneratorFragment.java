package com.year3project.qrcodegenerator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class GeneratorFragment extends Fragment {

    public static final String EXTRA_MESSAGE = "com.year3project.qrcodegenerator.MESSAGE";

    public static final String userDataPreferences = "UserDataPreferences";
    public static final String fullNameKey = "FullNameKey";
    public static final String ageKey = "AgeKey";
    public static final String addressKey = "AddressKey";
    public static final String contactNumberKey = "ContactNumberKey";
    public static final String purposeKey = "PurposeKey";
    public static final String temperatureKey = "temperatureKey";
    public static final String genderKey = "GenderKey";
    public static final String tempImageNameKey = "TempImageNameKey";

    private EditText fullName;
    private EditText contactNumber;
    private EditText address;
    private EditText age;
    private EditText temperature;
    private EditText gender;
    private EditText reason;
    private TextView buildings;

    boolean[] selectedLanguage;
    ArrayList<Integer> langList = new ArrayList<>();
    String[] langArray = {"PTS", "CSDL/ITS", "OP (OFFICE OF THE PRESIDENT)", "FVR", "CMA", "BASIC ED", "CHS", "GYM", "STUDENT PLAZA", "PHINMA GARDEN", "NORTH HALL", "MBA HALL", "FACULTY CENTER"};

    private String selectedBuilding;
    private String tempImageName;

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
        buildings = rootView.findViewById(R.id.building_selector);

        setUserDataForm();

        // initialize selected language array
        selectedLanguage = new boolean[langArray.length];

        buildings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

                // set title
                builder.setTitle("Select Buildings");

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(langArray, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            // when checkbox selected
                            // Add position  in lang list
                            langList.add(i);
                            // Sort array list
                            Collections.sort(langList);
                        } else {
                            // when checkbox unselected
                            // Remove position from langList
                            langList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < langList.size(); j++) {
                            // concat array value
                            stringBuilder.append(langArray[langList.get(j)]);
                            // check condition
                            if (j != langList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        // set text on textView
                        buildings.setText(stringBuilder.toString());
                        selectedBuilding = stringBuilder.toString();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        for (int j = 0; j < selectedLanguage.length; j++) {
                            // remove all selection
                            selectedLanguage[j] = false;
                            // clear language list
                            langList.clear();
                            // clear text view value
                            buildings.setText("");
                        }
                    }
                });
                // show dialog
                builder.show();
            }
        });


        return rootView;
    }

    private void setUserDataForm() {
        SharedPreferences userData = getActivity().getSharedPreferences(userDataPreferences, Context.MODE_PRIVATE);

        String fullNameValue = userData.getString(fullNameKey, "");
        String contactNumberValue = userData.getString(contactNumberKey, "");
        String addressValue = userData.getString(addressKey, "");
        String ageValue = userData.getString(ageKey, "");
        String temperatureValue = userData.getString(temperatureKey, "");
        String genderValue = userData.getString(genderKey, "");
        String purposeValue = userData.getString(purposeKey, "");
        tempImageName = userData.getString(tempImageNameKey, "");

        fullName.setText(fullNameValue);
        contactNumber.setText(contactNumberValue);
        address.setText(addressValue);
        age.setText(ageValue);
        temperature.setText(temperatureValue);
        gender.setText(genderValue);
        reason.setText(purposeValue);
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
                !TextUtils.isEmpty(reason_text) && fullName_text.length() >= 5 &&  fullName_text.length() <= 70 &&
                contactNumber_text.length() >= 9 && contactNumber_text.length() <= 12 &&
                address_text.length() >= 3 && address_text.length() <= 70 && age_text.length() <= 2 && temperature_text.length() >= 2 &&
                temperature_text.length() <= 5 && gender_text.length() >= 4 && gender_text.length() <= 6 &&
                reason_text.length() >= 4 && reason_text.length() <= 255 && selectedBuilding != null) {
            String logData = getFormData();

            // save user data to sharedpreferences
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(userDataPreferences, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(fullNameKey, fullName_text);
            editor.putString(contactNumberKey, contactNumber_text);
            editor.putString(addressKey, address_text);
            editor.putString(ageKey, age_text);
            editor.putString(temperatureKey, temperature_text);
            editor.putString(genderKey, gender_text);
            editor.putString(purposeKey, reason_text);
            editor.commit();

            Intent intent = new Intent(getActivity(), DisplayQRCode.class);
            intent.putExtra(EXTRA_MESSAGE, logData);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "Please fill up form properly", Toast.LENGTH_LONG).show();
        }

    }

    private String getFormData() {
        String qrCodeIdentifier = "UPANG_QR_CODE";
        String separator = ",,,,";
        String logData = qrCodeIdentifier + fullName.getText().toString() + separator + contactNumber.getText().toString() + separator +
                address.getText().toString() + separator + age.getText().toString() + separator + temperature.getText().toString() + separator +
                gender.getText().toString() + separator + reason.getText().toString() + separator + selectedBuilding + separator + tempImageName;
        Log.i("Test", logData);
        return logData;
    }
}