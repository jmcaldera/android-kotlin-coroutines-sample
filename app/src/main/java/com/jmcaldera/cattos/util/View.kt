package com.jmcaldera.cattos.util

import android.support.v4.widget.CircularProgressDrawable
import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jmcaldera.cattos.GlideApp

fun ImageView.loadUrl(url: String) {
  GlideApp.with(this.context.applicationContext)
      .load(url)
      .centerCrop()
      .placeholder(CircularProgressDrawable(this.context).apply {
        centerRadius = 15f
        setStyle(CircularProgressDrawable.DEFAULT)
        start()
      })
      .transition(DrawableTransitionOptions.withCrossFade())
      .into(this)
}