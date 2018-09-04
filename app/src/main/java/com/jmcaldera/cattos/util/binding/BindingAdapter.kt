package com.jmcaldera.cattos.util.binding

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.jmcaldera.cattos.util.loadUrl

object BindingAdapters {

  @JvmStatic
  @BindingAdapter("imageUrl")
  fun loadImageUrl(imageView: ImageView, url: String) {
    imageView.loadUrl(url)
  }
}