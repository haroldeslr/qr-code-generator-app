package com.year3project.qrcodegenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SelfieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie);

        getSupportActionBar().hide();
    }
}