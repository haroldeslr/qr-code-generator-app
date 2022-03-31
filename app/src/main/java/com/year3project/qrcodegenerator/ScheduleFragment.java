package com.year3project.qrcodegenerator;

import static com.year3project.qrcodegenerator.GeneratorFragment.fullNameKey;
import static com.year3project.qrcodegenerator.GeneratorFragment.userDataPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;

public class ScheduleFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "ScheduleFragment";

    public static final String selectedDepartmentKey = "SelectedDepartmentKey";

    private Context context;
    private RecyclerView rv;

    private String selectedDepartment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = container.getContext();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_schedule_fragment, container, false);

        // get save selected department
        SharedPreferences userData = getActivity().getSharedPreferences(userDataPreferences, Context.MODE_PRIVATE);
        String selectedDepartmentValue = userData.getString(selectedDepartmentKey, "");


        // setup department spinner
        Spinner spinner = (Spinner) view.findViewById(R.id.department_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.buildings_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // set selected department
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        if (selectedDepartmentValue != "") {
            int spinnerPosition = adapter.getPosition(selectedDepartmentValue);
            spinner.setSelection(spinnerPosition);
        }
        spinner.setOnItemSelectedListener(this);

        rv = view.findViewById(R.id.announcementRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        selectedDepartment = parent.getItemAtPosition(pos).toString();
        saveSelectedDepartment();
        new GetAnnouncement(context, rv, selectedDepartment).execute();
    }

    private void saveSelectedDepartment() {
        // save user data to sharedpreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(userDataPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(selectedDepartmentKey, selectedDepartment);
        editor.commit();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}