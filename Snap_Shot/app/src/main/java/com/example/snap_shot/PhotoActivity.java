package com.example.snap_shot;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
import android.net.Uri;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class PhotoActivity extends AppCompatActivity {
    ImageButton prevBtn, deleteBtn;
    ImageView img;
    TextView imgName, imgPath, imgSize, imgCreatedAt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_photo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        prevBtn = findViewById(R.id.prevBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        img = findViewById(R.id.imageView);
        imgName = findViewById(R.id.imgName);
        imgPath = findViewById(R.id.imgPath);
        imgSize = findViewById(R.id.imgSize);
        imgCreatedAt = findViewById(R.id.imgCreatedAt);


        Intent intent = getIntent();
        Uri imgUri = Uri.parse(intent.getStringExtra("imgUri"));
        img.setImageURI(imgUri);


        prevBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent1);
        });

        deleteBtn.setOnClickListener(v -> {
            showDeleteDialog(imgUri);
        });

        //need to convert Uri to usable file object to extract data
        DocumentFile file = DocumentFile.fromSingleUri(this, imgUri);

        if (file != null) {
            imgName.setText(String.format(Locale.US, "Name: %s", file.getName()));

            long size = file.length()/1024;  // size in bytes converted to kib
            imgSize.setText(String.format(Locale.US, "Size: %d kib", size));

            /* format timeStamp in ms */
            long createdAt = file.lastModified(); // timestamp in ms
            Date date = new Date(createdAt);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US);
            String formattedDate = sdf.format(date);
            imgCreatedAt.setText(String.format("Created At: %s", formattedDate));

            imgPath.setText(String.format(Locale.US, "Path: %s", imgUri.getPath()));

        }
    }

    private void showDeleteDialog(Uri uri) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Image")
                .setMessage("Do you really want to delete this image?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    boolean deleted = DocumentFile.fromSingleUri(this, uri).delete();

                    if (deleted) {
                        Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();

                        // go back to MainActivity
                        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent2);
                        finish(); // close current activity
                    } else {
                        Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}