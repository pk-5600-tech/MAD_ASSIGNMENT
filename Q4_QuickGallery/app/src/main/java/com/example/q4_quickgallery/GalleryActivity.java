package com.example.q4_quickgallery;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Uri> imageList = new ArrayList<>();
    ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        adapter = new ImageAdapter(imageList, this);
        recyclerView.setAdapter(adapter);

        String folderUri = getIntent().getStringExtra("folderUri");
        loadImages(Uri.parse(folderUri));
    }

    //  Load images from selected folder
    private void loadImages(Uri uri) {

        DocumentFile folder = DocumentFile.fromTreeUri(this, uri);

        for (DocumentFile file : folder.listFiles()) {

            if (file.getType() != null && file.getType().startsWith("image")) {
                imageList.add(file.getUri());
            }
        }

        adapter.notifyDataSetChanged();
    }
}