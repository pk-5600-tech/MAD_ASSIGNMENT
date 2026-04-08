package com.example.my_gallery;
import android.app.AlertDialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.util.Date;
public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        String path = getIntent().getStringExtra("path");
        File file = new File(path);
        ImageView img = findViewById(R.id.detailImage);
        TextView txt = findViewById(R.id.txtDetails);
        Button del = findViewById(R.id.btnDelete);


        img.setImageBitmap(BitmapFactory.decodeFile(path));
        txt.setText("Name: " + file.getName() +
                "\nPath: " + file.getAbsolutePath() +
                "\nSize: " + (file.length() / 1024) + " KB" +
                "\nDate: " + new Date(file.lastModified()));


        del.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Delete")
                    .setMessage("Delete this photo forever?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        if (file.delete()) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}
