package com.example.my_gallery;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.*;
import android.widget.*;
import java.io.File;
import java.util.ArrayList;


public class ImageAdapter extends BaseAdapter {
    Context ct;
    ArrayList<File> files;


    public ImageAdapter(Context c, ArrayList<File> f) {
        this.ct = c;
        this.files = f;
    }


    public int getCount() { return files.size(); }
    public Object getItem(int i) { return files.get(i); }
    public long getItemId(int i) { return i; }


    public View getView(int i, View v, ViewGroup parent) {
        ImageView img = (v == null) ? new ImageView(ct) : (ImageView) v;
        if (v == null) {
            img.setLayoutParams(new GridView.LayoutParams(350, 350));
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setPadding(4, 4, 4, 4);
        }
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = 4;
        img.setImageBitmap(BitmapFactory.decodeFile(files.get(i).getAbsolutePath(), opt));
        return img;
    }
}
