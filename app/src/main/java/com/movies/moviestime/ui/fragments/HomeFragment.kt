package com.movies.moviestime.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import com.movies.moviestime.R
import com.movies.moviestime.databinding.FragmentHomeBinding
import com.movies.moviestime.network.ApiStatus
import com.movies.moviestime.ui.adapters.AllMoviesAdapter
import com.movies.moviestime.ui.viewmodel.AllMovieViewModel
import com.movies.moviestime.ui.viewmodel.StorageViewModel
import com.movies.moviestime.utils.Utility
import com.movies.moviestime.utils.Utility.isConnectedToInternet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val storageViewModel: StorageViewModel by viewModels()
    private val movieViewModel: AllMovieViewModel by viewModels()
    private var isUsersFirstTimeVisit: Boolean = true
    lateinit var allMoviesAdapter: AllMoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        storageViewModel.changeSelectedTheme(requireContext().getString(R.string.dark_mode))
        allMoviesAdapter = AllMoviesAdapter()
        binding.popularMovieRcv.adapter = allMoviesAdapter
        checkIfUsersFirstTimeWithoutInternet()
        handleSwipeToRefresh()
        movieViewModel.getLatestPopularMovie()
        binding.layoutSearch.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            findNavController().navigate(action)
        }
        binding.btnRetryInternet.setOnClickListener {
            checkIfUsersFirstTimeWithoutInternet()
        }
    }

    private fun checkIfUsersFirstTimeWithoutInternet() {
        storageViewModel.isUsersFirstTime.observe(viewLifecycleOwner) { usersFirstTime ->
            isUsersFirstTimeVisit = usersFirstTime
            if (usersFirstTime == true && !isConnectedToInternet(requireContext())) {
                binding.lytNotConnected.visibility = View.VISIBLE
                binding.lytMain.visibility = View.GONE
            } else {
                getData()
                binding.lytNotConnected.visibility = View.GONE
                binding.lytMain.visibility = View.VISIBLE
            }
        }
    }

    private fun getData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieViewModel.getRecentMovie.collect { result ->
                    val movie = result ?: return@collect
                    allMoviesAdapter.submitList(movie.data)
                    when (result) {
                        is ApiStatus.Success -> {
                            storageViewModel.changeUsersFirstTime(false)
                            binding.shimmerLayout.visibility = View.GONE
                        }
                        is ApiStatus.Error -> {
                            binding.shimmerLayout.visibility = View.GONE
                        }
                        is ApiStatus.Loading -> {
                            if (isUsersFirstTimeVisit && isConnectedToInternet(requireContext())) {
                                binding.shimmerLayout.visibility = View.VISIBLE
                                delay(2000)
                            }
                            binding.root.isRefreshing = true
                        }
                        null -> {
                            if (isUsersFirstTimeVisit && isConnectedToInternet(requireContext())) {
                                binding.shimmerLayout.visibility = View.VISIBLE
                            }
                        }
                    }
                    //handle refresh
                    binding.root.isRefreshing = result is ApiStatus.Loading
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieViewModel.snackErrorMessage.collect { error ->
                    Utility.displayErrorSnackBar(
                        binding.root,
                        getString(R.string.not_connected_internet_error),
                        requireContext()
                    )
                }
            }
        }

    }

    private fun handleSwipeToRefresh() {
        binding.root.setOnRefreshListener {
            movieViewModel.onRefreshSwiped()
        }
    }

}