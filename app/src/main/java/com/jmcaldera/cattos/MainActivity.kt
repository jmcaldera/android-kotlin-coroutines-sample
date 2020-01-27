package com.jmcaldera.cattos

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.jmcaldera.cattos.features.CatsFragment
import com.jmcaldera.cattos.util.inTransaction
import dagger.android.AndroidInjection

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    addFragment(savedInstanceState)
  }

  private fun addFragment(savedInstanceState: Bundle?) =
    savedInstanceState ?: supportFragmentManager.inTransaction {
      add(
          container(), CatsFragment.newInstance()
      )
    }

  @IdRes
  fun container(): Int = R.id.fragmentContainer
}
