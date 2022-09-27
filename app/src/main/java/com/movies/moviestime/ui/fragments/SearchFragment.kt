package com.movies.moviestime.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.movies.moviestime.R
import com.movies.moviestime.databinding.FragmentSearchBinding
import com.movies.moviestime.network.ApiStatus
import com.movies.moviestime.ui.adapters.SearchMoviesAdapter
import com.movies.moviestime.ui.viewmodel.AllMovieViewModel
import com.movies.moviestime.utils.Constant.Companion.QUERY_PAGE_SIZE
import com.movies.moviestime.utils.Utility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchMoviesAdapter: SearchMoviesAdapter
    private val allMoviesViewModel: AllMovieViewModel by viewModels()
    var job: Job? = null

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etSearchMovie.apply {
            this.requestFocus()
            val input: InputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            input.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
        setUpRecyclerView()
        observeData()
        setUpSearch()
        handleClicks()
    }

    private fun setUpSearch() {
        binding.etSearchMovie.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
            }
            editable?.let {
                if (editable.toString().isNotEmpty()) {
                    allMoviesViewModel.doSearchForMovie(editable.toString())
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rcvSearch.apply {
            searchMoviesAdapter = SearchMoviesAdapter()
            adapter = searchMoviesAdapter
            addOnScrollListener(this@SearchFragment.scrollListener)
        }
    }

    private fun observeData() {
        allMoviesViewModel.searchAllMovies.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiStatus.Success -> {
                    hideProgressBar()
                    if (result.data?.Response == "True") {
                        val totalPages = result.data.totalResults
                        isLastPage = allMoviesViewModel.searchMoviePage.toString() == totalPages
                        if (isLastPage) {
                            binding.rcvSearch.setPadding(0, 0, 0, 0)
                        }
                        searchMoviesAdapter.submitList(result.data.Search.toList())
                    } else {
                        displayErrorSnackBar()
                    }
                    binding.loading.visibility = View.GONE
                }
                is ApiStatus.Error -> {
                    displayErrorSnackBar()
                }
                is ApiStatus.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun displayErrorSnackBar() {
        Utility.displayErrorSnackBar(
            binding.root,
            requireContext().getString(R.string.unknown_error_occurred),
            requireContext()
        )
    }

    private fun handleClicks() {
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val isNoErrors = !isError
            val isLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNoErrors && isLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                        isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                allMoviesViewModel.doSearchForMovie(binding.etSearchMovie.text.toString())
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

}