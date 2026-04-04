package com.example.snap_shot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Uri photoUri;
    public static Uri selectedFolderUri;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> filePicker;
    private ImageButton openCameraBtn, selectFolderBtn;
    private List<Uri> imgUris;
    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        openCameraBtn = findViewById(R.id.openCameraBtn);
        selectFolderBtn = findViewById(R.id.selectFolderBtn);
        imgUris = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        int columnWidth = 300;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int spanCount = Math.max(3, metrics.widthPixels / columnWidth);

        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        adapter = new ImageAdapter(imgUris);
        recyclerView.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);
        String uriStr = prefs.getString("folder_uri", null);
        if (uriStr != null) {
            selectedFolderUri = Uri.parse(uriStr);
            loadImagesFromFolder();
        }

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                imgUris.add(photoUri);
                adapter.notifyItemInserted(imgUris.size() - 1);
            }
        });

        filePicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri treeUri = result.getData().getData();
                getContentResolver().takePersistableUriPermission(treeUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                getSharedPreferences("app", MODE_PRIVATE).edit().putString("folder_uri", treeUri.toString()).apply();
                selectedFolderUri = treeUri;
                loadImagesFromFolder();
                Toast.makeText(this, "Folder Selected", Toast.LENGTH_SHORT).show();
            }
        });

        selectFolderBtn.setOnClickListener(v -> filePicker.launch(new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)));

        openCameraBtn.setOnClickListener(v -> {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 100);
                return;
            }
            try {
                photoUri = createImageUri();
                if (photoUri == null) return;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                cameraLauncher.launch(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Camera Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadImagesFromFolder() {
        DocumentFile pickedDir = DocumentFile.fromTreeUri(this, selectedFolderUri);
        if (pickedDir == null || !pickedDir.isDirectory()) return;
        imgUris.clear();
        for (DocumentFile file : pickedDir.listFiles()) {
            if (file.isFile() && file.getType() != null && file.getType().startsWith("image/")) {
                imgUris.add(file.getUri());
            }
        }
        adapter.notifyDataSetChanged();
    }

    private Uri createImageUri() {
        DocumentFile pickedDir = DocumentFile.fromTreeUri(this, selectedFolderUri);
        if (pickedDir == null) return null;
        DocumentFile newFile = pickedDir.createFile("image/jpeg", "IMG_" + System.currentTimeMillis());
        return newFile != null ? newFile.getUri() : null;
    }
}