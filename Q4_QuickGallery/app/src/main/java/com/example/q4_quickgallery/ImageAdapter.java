package com.example.q4_quickgallery;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private final ArrayList<Uri> images;
    private final Context context;

    public ImageAdapter(ArrayList<Uri> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // item_image.xml ko inflate karna
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri uri = images.get(position);

        // Image set karna
        holder.imageView.setImageURI(uri);

        // Photo par click karne par Detail Activity kholna
        holder.itemView.setOnClickListener(v -> {
            // Dhyan dein: ImagedetailActivity (small 'd') hi rakhein agar file ka naam wahi hai
            Intent intent = new Intent(context, ImagedetailActivity.class);
            intent.putExtra("imageUri", uri.toString());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            // item_image.xml ki ID match honi chahiye
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}