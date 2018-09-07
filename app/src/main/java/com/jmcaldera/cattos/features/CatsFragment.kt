package com.jmcaldera.cattos.features

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.jmcaldera.cattos.databinding.FragmentCatsBinding
import com.jmcaldera.cattos.domain.model.RetryCallback
import com.jmcaldera.cattos.domain.model.Status.LOADING
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

  var isLoading: Boolean = false

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

    binding.callback = object : RetryCallback {
      override fun retry() {
        catsViewModel.getCats()
      }
    }

    setupObservers()
    initRecyclerView()
    catsViewModel.getCats()
  }

  private fun initRecyclerView() {
    binding.catList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      val threshold = 2

      override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager

        val totalItemCount = layoutManager.itemCount
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

        if (threshold + lastVisibleItem > totalItemCount && !isLoading) {
          catsViewModel.loadNextPage()
        }
      }
    })
    catsAdapter.clickListener = {
      Toast.makeText(context, it.id, Toast.LENGTH_SHORT).show()
    }
    binding.catList.adapter = catsAdapter
    binding.catList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
  }

  private fun setupObservers() {

    catsViewModel.cats.observe(this, Observer { cats ->
      catsAdapter.submitList(cats)
    })

    catsViewModel.loadMoreState.observe(this, Observer { loadingMore ->
      if (loadingMore == null) {
        binding.loadingMore = false
        isLoading = false
      } else {
        binding.loadingMore = loadingMore.isRunning
        isLoading = loadingMore.isRunning
        val error = loadingMore.errorMessageIfNotHandled
        if (error != null) {
          Snackbar.make(binding.loadingMoreBar, error, Snackbar.LENGTH_LONG).show()
        }
      }
    })

    catsViewModel.loadingState.observe(this, Observer { loadingState ->
      binding.loadingState = loadingState
      isLoading = loadingState?.status == LOADING
    })

  }

  companion object {
    @JvmStatic fun newInstance() = CatsFragment()
  }
}
