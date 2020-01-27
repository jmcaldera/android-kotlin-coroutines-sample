package com.jmcaldera.cattos.features

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jmcaldera.cattos.R
import com.jmcaldera.cattos.databinding.ItemCattoImageBinding
import com.jmcaldera.cattos.domain.model.Cat
import com.jmcaldera.cattos.features.CatsAdapter.ViewHolder
import javax.inject.Inject

class CatsAdapter
@Inject constructor() : ListAdapter<Cat, ViewHolder>(
    AsyncDifferConfig.Builder(CatDiffCallback()).build()
) {

  internal var clickListener: (Cat) -> Unit = {}

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = createBinding(parent)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItem(position), clickListener)
  }

  private fun createBinding(parent: ViewGroup): ItemCattoImageBinding {
    return DataBindingUtil.inflate<ItemCattoImageBinding>(
        LayoutInflater.from(parent.context),
        R.layout.item_catto_image,
        parent,
        false
    )
  }

  class ViewHolder(val binding: ItemCattoImageBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cat: Cat, clickListener: (Cat) -> Unit) {
      binding.root.setOnClickListener { clickListener.invoke(cat) }
      binding.cat = cat
      binding.executePendingBindings()
    }
  }
}

class CatDiffCallback : DiffUtil.ItemCallback<Cat>() {
  override fun areItemsTheSame(old: Cat, new: Cat): Boolean = old.id == new.id

  override fun areContentsTheSame(old: Cat, new: Cat): Boolean = old == new

}