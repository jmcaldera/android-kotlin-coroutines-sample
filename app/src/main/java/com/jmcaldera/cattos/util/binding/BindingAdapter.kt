package com.jmcaldera.cattos.util.binding

import android.databinding.BindingAdapter
import android.view.View
import android.widget.ImageView
import com.jmcaldera.cattos.util.loadUrl

object BindingAdapters {

  @JvmStatic
  @BindingAdapter("imageUrl")
  fun loadImageUrl(imageView: ImageView, url: String) {
    imageView.loadUrl(url)
  }

  @JvmStatic
  @BindingAdapter("visibleOrGone")
  fun setVisibility(view: View, show: Boolean) {
    view.visibility = if (show) View.VISIBLE else View.GONE
  }
}