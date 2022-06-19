package com.moviediscovery.ui.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.moviediscovery.MoviesNavigationDirections
import com.moviediscovery.R
import com.moviediscovery.app.makeGone
import com.moviediscovery.app.makeVisible
import com.moviediscovery.databinding.FragmentMoviesBinding
import com.moviediscovery.model.Movie
import com.moviediscovery.ui.MovieAdapter
import com.moviediscovery.ui.uistate.ErrorUIState
import com.moviediscovery.ui.uistate.UIState
import com.moviediscovery.utils.ConnectivityHelper
import com.moviediscovery.utils.RecyclerViewEndReachedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularMoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private val popularMoviesViewModel by viewModels<PopularMoviesViewModel>()
    private var movieAdapter: MovieAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)

        movieAdapter = MovieAdapter { movieId ->
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
            binding.rvMovies.layoutManager
        ) {
            override fun onEndReached() {
                popularMoviesViewModel.loadMore()
            }
        }

        binding.rvMovies.apply {
            adapter = movieAdapter
            setHasFixedSize(true)
            addOnScrollListener(rvEndReachedListener)
        }

        binding.viewSwipeRefresh.setOnRefreshListener {
            popularMoviesViewModel.getPopularMovies(isRefreshing = true)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnTryAgain.setOnClickListener {
            if (ConnectivityHelper.hasInternetConnection())
                popularMoviesViewModel.getPopularMovies()
        }

        popularMoviesViewModel.uiStateLiveData.observe(viewLifecycleOwner) { state ->
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
                    displayError(state.errorUIState)
                }
                else -> {
                    /* NO-OP */
                }
            }
        }
    }

    private fun displayMovies(movieList: List<Movie>) {
        binding.rvMovies.makeVisible()
        movieAdapter?.movies = movieList
    }

    private fun displayError(errorUIState: ErrorUIState) {
        val errorMessage = getString(errorUIState.errorType.errorMessageResId)
        when (errorUIState) {
            is ErrorUIState.FullScreenError -> {
                binding.viewFullScreenError.makeVisible()
                binding.tvError.text = errorMessage
            }
            is ErrorUIState.ToastError -> {
                if (!popularMoviesViewModel.isToastErrorShown())
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()

                // After showing the toast error update the state in ViewModel
                popularMoviesViewModel.setToastErrorShown(true)

                // If after screen rotation (or other action) the last uiState will be
                // error, we need to update the list with the fetched data manually
                if (movieAdapter?.movies?.isEmpty() == true)
                    displayMovies(popularMoviesViewModel.moviesList)
            }
        }
    }
}