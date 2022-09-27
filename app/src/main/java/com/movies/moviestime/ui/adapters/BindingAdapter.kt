package com.movies.moviestime.ui.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.movies.moviestime.database.entity.model.Search

@BindingAdapter("displayImage")
fun ImageView.setDisplayImage(url: String){
    Glide.with(this)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)

}
