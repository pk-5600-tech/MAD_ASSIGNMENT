package com.example.q4_quickgallery;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import java.util.Date;

public class ImagedetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView details;
    Button btnDelete;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        // UI Initialization
        imageView = findViewById(R.id.imageView);
        details = findViewById(R.id.details);
        btnDelete = findViewById(R.id.btnDelete);

        // URI lena
        String uriString = getIntent().getStringExtra("imageUri");
        if (uriString != null) {
            imageUri = Uri.parse(uriString);
            imageView.setImageURI(imageUri);
            showDetails();
        }

        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    private void showDetails() {
        try {
            DocumentFile docFile = DocumentFile.fromSingleUri(this, imageUri);
            if (docFile != null && docFile.exists()) {
                String name = docFile.getName();
                long sizeInBytes = docFile.length();
                long lastModified = docFile.lastModified();

                String info = "📌 Name: " + name +
                        "\n⚖️ Size: " + (sizeInBytes / 1024) + " KB" +
                        "\n📅 Date: " + new Date(lastModified).toString();

                details.setText(info);
            }
        } catch (Exception e) {
            details.setText("Details load nahi ho saki.");
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Image")
                .setMessage("Kya aap pakka is photo ko delete karna chahte hain?")
                .setPositiveButton("Yes, Delete", (dialog, which) -> deleteImage())
                .setNegativeButton("No, Cancel", null)
                .show();
    }

    private void deleteImage() {
        DocumentFile fileToDelete = DocumentFile.fromSingleUri(this, imageUri);

        if (fileToDelete != null && fileToDelete.delete()) {
            Toast.makeText(this, "Photo Deleted!", Toast.LENGTH_SHORT).show();

            // Result set karna taaki GalleryActivity ko pata chale kuch change hua hai
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Delete failed! Check folder permissions.", Toast.LENGTH_SHORT).show();
        }
    }
}