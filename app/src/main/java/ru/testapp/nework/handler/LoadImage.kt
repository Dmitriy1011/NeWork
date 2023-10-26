package ru.testapp.nework.handler

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ru.testapp.nework.R

fun ImageView.loadImage(url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.baseline_crop_original_24)
        .transition(DrawableTransitionOptions.withCrossFade())
        .error(R.drawable.baseline_error_outline_24)
        .into(this)
}