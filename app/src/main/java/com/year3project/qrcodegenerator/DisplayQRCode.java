package com.year3project.qrcodegenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class DisplayQRCode extends AppCompatActivity {

    ImageView qrImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_qrcode);

        qrImage = findViewById(R.id.qrcodedisplay);

        Intent intent = getIntent();
        String logData = intent.getStringExtra(GeneratorFragment.EXTRA_MESSAGE);

        QRGEncoder qrgEncoder = new QRGEncoder(logData, null, QRGContents.Type.TEXT, 500);
        try {
            Bitmap qrBits = qrgEncoder.getBitmap();
            qrImage.setImageBitmap(qrBits);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backToForm(View view) {
        finish();
    }


}