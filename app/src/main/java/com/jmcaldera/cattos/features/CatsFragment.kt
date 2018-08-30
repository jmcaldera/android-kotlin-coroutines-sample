package com.jmcaldera.cattos.features

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.jmcaldera.cattos.databinding.FragmentCatsBinding
import com.jmcaldera.cattos.util.autoCleared
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [CatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CatsFragment : Fragment() {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  @Inject
  lateinit var catsAdapter: CatsAdapter

  lateinit var catsViewModel: CatsViewModel

  var binding by autoCleared<FragmentCatsBinding>()

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidSupportInjection.inject(this)
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val dataBinding = FragmentCatsBinding.inflate(inflater)
    binding = dataBinding
    return dataBinding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    catsViewModel = ViewModelProviders.of(this, viewModelFactory)
        .get(CatsViewModel::class.java)

    setupObservers()
    initRecyclerView()
    catsViewModel.getCats()
  }

  private fun initRecyclerView() {
    catsAdapter.clickListener = {
      Toast.makeText(context, it.id, Toast.LENGTH_SHORT).show()
    }
    binding.catList.adapter = catsAdapter
    binding.catList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
  }

  private fun setupObservers() {
    catsViewModel.error.observe(this, Observer {
      // show error
      it?.let {
        if (it) binding.textIds.text = "Error"
      }
    })

    catsViewModel.cats.observe(this, Observer { cats ->
      catsAdapter.submitList(cats)
    })

  }

  companion object {
    @JvmStatic fun newInstance() = CatsFragment()
  }
}
