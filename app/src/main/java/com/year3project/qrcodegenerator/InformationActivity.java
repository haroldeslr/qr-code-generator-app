package com.year3project.qrcodegenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InformationActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    GeneratorFragment generatorFragment = new GeneratorFragment();
    ScheduleFragment scheduleFragment = new ScheduleFragment();
    AboutFragment aboutFragment = new AboutFragment();

    private String currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        getSupportActionBar().hide();

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,generatorFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.generator:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,generatorFragment).commit();
                        return true;
                    case R.id.schedule:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,scheduleFragment).commit();
                        return true;
                    case R.id.about:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,aboutFragment).commit();
                        return true;
                }

                return false;
            }
        });





    }

}