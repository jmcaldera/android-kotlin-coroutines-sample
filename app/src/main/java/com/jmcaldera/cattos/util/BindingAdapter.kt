package com.jmcaldera.cattos.util

import android.databinding.BindingAdapter
import android.widget.ImageView

object BindingAdapters {

  @JvmStatic
  @BindingAdapter("imageUrl")
  fun loadImageUrl(imageView: ImageView, url: String) {
    imageView.loadUrl(url)
  }
}