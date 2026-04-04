package com.example.q4_quickgallery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1;
    private static final int FOLDER_REQUEST = 2;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCamera = findViewById(R.id.btnCamera);
        Button btnGallery = findViewById(R.id.btnGallery);

        checkPermissions();

        btnCamera.setOnClickListener(v -> openCamera());
        btnGallery.setOnClickListener(v -> openFolder());
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(getExternalFilesDir("Pictures"), "IMG_" + System.currentTimeMillis() + ".jpg");

        // FIX: Authority matched with Manifest
        imageUri = FileProvider.getUriForFile(this, "com.example.galleryq4.provider", file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    private void openFolder() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, FOLDER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                Toast.makeText(this, "Photo saved!", Toast.LENGTH_SHORT).show();
            }
            else if (requestCode == FOLDER_REQUEST && data != null) {
                Uri folderUri = data.getData();

                // FIX: Taking persistent permission so GalleryActivity can read files
                getContentResolver().takePersistableUriPermission(folderUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION);

                Intent intent = new Intent(this, GalleryActivity.class);
                intent.putExtra("folderUri", folderUri.toString());
                startActivity(intent);
            }
        }
    }
}