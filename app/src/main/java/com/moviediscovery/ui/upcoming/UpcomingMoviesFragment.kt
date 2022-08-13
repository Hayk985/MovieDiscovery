package com.moviediscovery.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.moviediscovery.MoviesNavigationDirections
import com.moviediscovery.R
import com.moviediscovery.app.makeGone
import com.moviediscovery.app.makeVisible
import com.moviediscovery.databinding.FragmentMoviesBinding
import com.moviediscovery.model.Movie
import com.moviediscovery.ui.MovieAdapter
import com.moviediscovery.model.uistate.ErrorUIState
import com.moviediscovery.model.uistate.UIState
import com.moviediscovery.utils.ConnectivityHelper
import com.moviediscovery.utils.RecyclerViewEndReachedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingMoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private val upcomingMoviesViewModel by viewModels<UpcomingMoviesViewModel>()
    private var movieAdapter: MovieAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)

        movieAdapter = MovieAdapter(requireContext()) { movieId ->
            if (!ConnectivityHelper.hasInternetConnection()) {
                Toast.makeText(
                    context, getString(R.string.error_no_connection), Toast.LENGTH_SHORT
                ).show()
                return@MovieAdapter
            }

            val action = MoviesNavigationDirections.actionGlobalMovieDetails(movieId)
            findNavController().navigate(action)
        }

        val rvEndReachedListener = object : RecyclerViewEndReachedListener(
            binding.rvMovies.layoutManager as GridLayoutManager
        ) {
            override fun onEndReached() {
                upcomingMoviesViewModel.loadMore()
            }
        }

        binding.rvMovies.apply {
            adapter = movieAdapter
            setHasFixedSize(true)
            addOnScrollListener(rvEndReachedListener)
        }

        binding.viewSwipeRefresh.setOnRefreshListener {
            upcomingMoviesViewModel.getUpcomingMovies(isRefreshing = true)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnTryAgain.setOnClickListener {
            if (ConnectivityHelper.hasInternetConnection())
                upcomingMoviesViewModel.getUpcomingMovies()
        }

        upcomingMoviesViewModel.uiStateLiveData.observe(viewLifecycleOwner) { state ->
            // Hide loading after getting response
            if (state !is UIState.ShowLoading) {
                binding.viewLoading.makeGone()
                if (binding.viewSwipeRefresh.isRefreshing) {
                    binding.viewSwipeRefresh.isRefreshing = false
                }
            }

            when (state) {
                is UIState.ShowLoading -> {
                    binding.viewLoading.makeVisible()
                    binding.viewFullScreenError.makeGone()
                }
                is UIState.ShowData -> {
                    binding.viewFullScreenError.makeGone()
                    displayMovies(state.data)
                }
                is UIState.ShowError -> {
                    displayError(state.errorUIState, state.data)
                }
                else -> Unit
            }
        }
    }

    private fun displayMovies(movieList: List<Movie>) {
        binding.rvMovies.makeVisible()
        movieAdapter?.movies = movieList
    }

    private fun displayError(errorUIState: ErrorUIState, movieList: List<Movie>?) {
        val errorMessage = getString(errorUIState.errorType.errorMessageResId)
        when (errorUIState) {
            is ErrorUIState.FullScreenError -> {
                binding.viewFullScreenError.makeVisible()
                binding.tvError.text = errorMessage
            }
            is ErrorUIState.ToastError -> {
                if (!upcomingMoviesViewModel.isToastErrorShown())
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()

                // After showing the toast error update the state in ViewModel
                upcomingMoviesViewModel.setToastErrorShown(true)

                // In case of configuration change
                if (movieAdapter?.movies?.isEmpty() == true) {
                    movieList?.let {
                        displayMovies(it)
                    }
                }
            }
        }
    }
}