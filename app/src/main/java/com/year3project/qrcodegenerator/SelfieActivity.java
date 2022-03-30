package com.year3project.qrcodegenerator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelfieActivity extends AppCompatActivity {

    private ImageView imageView;

    private Button openBtn;
    private Button fillUpFormBtn;

    private String currentPhotoPath;
    private Uri photoURI;

    public static final String userDataPreferences = "UserDataPreferences";
    public static final String currentPhotoPathKey = "CurrentPhotoPathKey";
    public static final String photoURIKey = "PhotoURIKey";
    public static final String tempImageNameKey = "TempImageNameKey";

    // new codes
    Bitmap bitmap;
    String tempImageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie);

        getSupportActionBar().hide();

        imageView = findViewById(R.id.image_view);
        openBtn = findViewById(R.id.open_btn);
        fillUpFormBtn = findViewById(R.id.fillupform_btn);

        if (ContextCompat.checkSelfPermission(SelfieActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SelfieActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    }, 100);
        }

        openBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    photoURI = FileProvider.getUriForFile(getApplicationContext(),
                            "com.example.android.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, 100);
                }
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode ==  100 ) {
            setPic();
            enableFillInformationButton();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "JPEG_" + "SELFIE" + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void enableFillInformationButton() {
        fillUpFormBtn.setVisibility(View.VISIBLE);
    }

    private void setPic() {
        float imageRotation = getCameraPhotoOrientation(getApplicationContext(), photoURI, currentPhotoPath);
        imageView.setRotation(imageRotation);

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void fillUpFormButton(View view) {
        tempImageName = currentPhotoPath.substring(currentPhotoPath.length() - 35);

        // save info to shared pref
        SharedPreferences sharedPreferences = getSharedPreferences(userDataPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(currentPhotoPathKey, currentPhotoPath);
        editor.putString(photoURIKey, photoURI.toString());
        editor.putString(tempImageNameKey, tempImageName);
        editor.commit();

        uploadImage();
    }


    // new codes
    private void uploadImage() {
        // show progress dialog
        ProgressDialog pd = new ProgressDialog(SelfieActivity.this);
        pd.setTitle("Saving Image");
        pd.setMessage("Please wait.");
        pd.show();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,75, byteArrayOutputStream);
        byte[] imageInByte = byteArrayOutputStream.toByteArray();
        String encodedImage =  Base64.encodeToString(imageInByte,Base64.DEFAULT);

        Call<ResponsePOJO> call = RetroClient.getInstance().getApi().uploadImage(encodedImage, tempImageName);
        call.enqueue(new Callback<ResponsePOJO>() {
            @Override
            public void onResponse(Call<ResponsePOJO> call, Response<ResponsePOJO> response) {
                Toast.makeText(SelfieActivity.this, response.body().getRemarks(), Toast.LENGTH_LONG).show();

                if(response.body().isStatus()){
                    Intent intent = new Intent(getApplicationContext(), InformationActivity.class);
                    startActivity(intent);
                }else{

                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                Toast.makeText(SelfieActivity.this, "Network Failed", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }
}