package com.example.my_gallery;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    GridView grid;
    ArrayList<File> list = new ArrayList<>();
    File myDir;
    ImageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        grid = findViewById(R.id.gridView);
        Button btnCap = findViewById(R.id.btnCapture);
        Button btnOpen = findViewById(R.id.btnOpenFolder);


        adapter = new ImageAdapter(this, list);
        grid.setAdapter(adapter);


        // Standard directory for this app
        myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyPhotos");
        if (!myDir.exists()) myDir.mkdirs();


        // 1. Capture Photo Logic
        btnCap.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            } else {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 101);
            }
        });


        // 2. Open Folder (File Explorer Style)
        btnOpen.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 102);
        });


        // 3. Open Detail Page (For Delete Option)
        grid.setOnItemClickListener((p, view, pos, id) -> {
            Intent i = new Intent(this, DetailActivity.class);
            i.putExtra("path", list.get(pos).getAbsolutePath());
            startActivityForResult(i, 200); // 200 is our request code for details
        });


        refreshGrid();
    }


    private void refreshGrid() {
        File[] files = myDir.listFiles();
        list.clear();
        if (files != null) {
            for (File f : files) {
                if (f.getName().endsWith(".jpg") || f.getName().endsWith(".png")) list.add(f);
            }
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Case A: Captured from Camera
            if (requestCode == 101 && data != null) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                saveToFolder(bmp);
            }
            // Case B: Picked from File Explorer
            else if (requestCode == 102 && data != null) {
                importFromUri(data.getData());
            }
            // Case C: Returned from Delete Screen
            else if (requestCode == 200) {
                refreshGrid(); // Refresh to remove the deleted item from view
            }
        }
    }


    private void saveToFolder(Bitmap bmp) {
        File file = new File(myDir, "CAP_" + System.currentTimeMillis() + ".jpg");
        try (FileOutputStream out = new FileOutputStream(file)) {
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            refreshGrid();
        } catch (Exception e) { e.printStackTrace(); }
    }


    private void importFromUri(Uri uri) {
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            File file = new File(myDir, "EXP_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) != -1) fos.write(buffer, 0, read);
            fos.close();
            is.close();
            refreshGrid();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
