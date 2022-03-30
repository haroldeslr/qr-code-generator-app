package com.year3project.qrcodegenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.WriterException;

import java.io.File;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class DisplayQRCode extends AppCompatActivity {

    ImageView qrImage, imageView;

    private String currentPhotoPath;
    private Uri photoURI;

    public static final String userDataPreferences = "UserDataPreferences";
    public static final String currentPhotoPathKey = "CurrentPhotoPathKey";
    public static final String photoURIKey = "PhotoURIKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_qrcode);

        qrImage = findViewById(R.id.qrcodedisplay);
        imageView = findViewById(R.id.selfie_img);

        Intent intent = getIntent();
        String logData = intent.getStringExtra(GeneratorFragment.EXTRA_MESSAGE);

        SharedPreferences userData = getSharedPreferences(userDataPreferences, Context.MODE_PRIVATE);
        currentPhotoPath = userData.getString(currentPhotoPathKey, "");
        photoURI = Uri.parse(userData.getString(photoURIKey, ""));

        QRGEncoder qrgEncoder = new QRGEncoder(logData, null, QRGContents.Type.TEXT, 500);
        try {
            Bitmap qrBits = qrgEncoder.getBitmap();
            qrImage.setImageBitmap(qrBits);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setPic();
    }

    private void setPic() {
        float imageRotation = getCameraPhotoOrientation(getApplicationContext(), photoURI, currentPhotoPath);
        imageView.setRotation(imageRotation);

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        imageView.setImageBitmap(bitmap);
    }

    public int getCameraPhotoOrientation(Context context, Uri imageUri,
                                         String imagePath) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }


}