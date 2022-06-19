package com.moviediscovery.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.moviediscovery.MoviesNavigationDirections
import com.moviediscovery.R
import com.moviediscovery.app.addOnQueryTextChangeListener
import com.moviediscovery.app.makeGone
import com.moviediscovery.app.makeVisible
import com.moviediscovery.databinding.FragmentSearchBinding
import com.moviediscovery.model.Movie
import com.moviediscovery.ui.MovieAdapter
import com.moviediscovery.ui.uistate.ErrorUIState
import com.moviediscovery.ui.uistate.SearchUIState
import com.moviediscovery.ui.uistate.UIState
import com.moviediscovery.utils.ConnectivityHelper
import com.moviediscovery.utils.RecyclerViewEndReachedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchMoviesFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchMoviesViewModel by viewModels<SearchMoviesViewModel>()
    private var movieAdapter: MovieAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.viewSearchMovies.imeOptions =
            binding.viewSearchMovies.imeOptions or EditorInfo.IME_FLAG_NO_EXTRACT_UI

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
                searchMoviesViewModel.loadNext()
            }
        }

        binding.rvMovies.apply {
            adapter = movieAdapter
            setHasFixedSize(true)
            addOnScrollListener(rvEndReachedListener)
        }

        movieAdapter?.addOnDataUpdatedListener {
            binding.rvMovies.scrollToPosition(RecyclerView.SCROLLBAR_POSITION_DEFAULT)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnTryAgain.setOnClickListener {
            if (ConnectivityHelper.hasInternetConnection())
                searchMoviesViewModel.searchMovies(binding.viewSearchMovies.query.toString())
        }

        binding.viewSearchMovies.addOnQueryTextChangeListener { query ->
            searchMoviesViewModel.searchMovies(query)
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchMoviesViewModel.searchUIState.collectLatest {
                    if (it !is UIState.ShowLoading)
                        binding.viewLoading.makeGone()

                    when (it) {
                        is UIState.ShowLoading -> {
                            binding.viewLoading.makeVisible()
                            binding.viewFullScreenError.makeGone()
                            binding.tvEmptySearchResult.makeGone()
                            binding.viewNoSearchResult.makeGone()
                        }
                        is UIState.ShowEmptyData -> {
                            binding.tvEmptySearchResult.makeVisible()
                            binding.viewFullScreenError.makeGone()
                            binding.viewNoSearchResult.makeGone()
                            clearData()
                        }
                        is SearchUIState.ShowNoSearchResult -> {
                            binding.viewNoSearchResult.makeVisible()
                            binding.tvEmptySearchResult.makeGone()
                            binding.viewFullScreenError.makeGone()
                            clearData()
                        }
                        is UIState.ShowData -> {
                            binding.viewNoSearchResult.makeGone()
                            binding.viewFullScreenError.makeGone()
                            binding.tvEmptySearchResult.makeGone()
                            displayMovies(it.data)
                        }
                        is UIState.ShowError -> {
                            binding.tvEmptySearchResult.makeGone()
                            binding.viewNoSearchResult.makeGone()
                            displayError(it.errorUIState)
                        }
                    }
                }
            }
        }
    }

    private fun displayMovies(movieList: List<Movie>) {
        binding.rvMovies.makeVisible()
        movieAdapter?.movies = movieList
    }

    private fun clearData() {
        movieAdapter?.movies = emptyList()
    }

    private fun displayError(errorUIState: ErrorUIState) {
        val errorMessage = getString(errorUIState.errorType.errorMessageResId)
        when (errorUIState) {
            is ErrorUIState.FullScreenError -> {
                binding.viewFullScreenError.makeVisible()
                binding.tvError.text = errorMessage
            }
            is ErrorUIState.ToastError -> {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()

                // If after screen rotation (or other action) the last uiState will be
                // error, we need to update the list with the fetched data manually
                if (movieAdapter?.movies?.isEmpty() == true)
                    displayMovies(searchMoviesViewModel.moviesList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}