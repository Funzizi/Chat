package com.example.appchat.base;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.appchat.R;

public class Utillity {

    public static <T> void loadAvatar(ImageView imageView, T data) {
        if (data.equals("default")) {
            imageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(imageView)
                    .load(data)
                    .into(imageView);
        }
    }
}
