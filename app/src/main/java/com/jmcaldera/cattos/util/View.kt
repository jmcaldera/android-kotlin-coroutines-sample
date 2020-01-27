package com.jmcaldera.cattos.util

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jmcaldera.cattos.GlideApp
import com.jmcaldera.cattos.R

fun ImageView.loadUrl(url: String) {
  GlideApp.with(this.context)
      .load(url)
      .centerCrop()
      .placeholder(CircularProgressDrawable(
          this.context).apply {
        setStyle(CircularProgressDrawable.LARGE)
        setColorSchemeColors(ContextCompat.getColor(context, R.color.colorAccent))
        start()
      })
      .transition(DrawableTransitionOptions.withCrossFade())
      .into(this)
}