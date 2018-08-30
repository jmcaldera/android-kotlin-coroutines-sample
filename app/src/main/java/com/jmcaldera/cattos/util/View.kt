package com.jmcaldera.cattos.util

import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jmcaldera.cattos.GlideApp

fun ImageView.loadUrl(url: String) {
  GlideApp.with(this.context.applicationContext)
      .load(url)
      .fitCenter()
      .transition(DrawableTransitionOptions.withCrossFade())
      .into(this)
}