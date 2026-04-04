package com.example.snap_shot;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<Uri> imageUris;

    public ImageAdapter(List<Uri> imageUris) {
        this.imageUris = imageUris;
    }

    // ViewHolder (represents ONE item)
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(ImageView itemView) {
            super(itemView);
            imageView = itemView;
        }
    }

    // Create new item view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());



        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return new ViewHolder(imageView);
    }

    // Bind data to view
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setImageURI(imageUris.get(position));


        // Make it square
        holder.imageView.post(() -> {
            int width = holder.imageView.getWidth();
            holder.imageView.getLayoutParams().height = width;
            holder.imageView.requestLayout();
        });

        holder.imageView.setOnClickListener(v -> {
            Context context = holder.imageView.getContext();
            Intent intent = new Intent(context, PhotoActivity.class);
            intent.putExtra("imgUri", imageUris.get(position).toString());
            context.startActivity(intent);
        });
    }

    // Total items
    @Override
    public int getItemCount() {
        return imageUris.size();
    }
}