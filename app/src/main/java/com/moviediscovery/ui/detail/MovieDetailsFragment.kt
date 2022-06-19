package com.moviediscovery.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.moviediscovery.R
import com.moviediscovery.app.loadImage
import com.moviediscovery.app.makeGone
import com.moviediscovery.app.makeVisible
import com.moviediscovery.app.setDataIfAvailable
import com.moviediscovery.databinding.FragmentMovieDetailBinding
import com.moviediscovery.model.MovieDetails
import com.moviediscovery.ui.uistate.UIState
import com.moviediscovery.utils.DateUtils
import com.moviediscovery.utils.UrlHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val movieDetailsViewModel by viewModels<MovieDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMovieDetails()

        binding.btnTryAgain.setOnClickListener {
            movieDetailsViewModel.fetchAgain()
        }
    }

    private fun getMovieDetails() {
        lifecycleScope.launchWhenStarted {
            movieDetailsViewModel.movieDetailsLiveData.observe(viewLifecycleOwner) { state ->
                if (state !is UIState.ShowLoading)
                    binding.viewLoading.makeGone()

                when (state) {
                    UIState.ShowLoading -> {
                        binding.viewGroupError.makeGone()
                        binding.viewLoading.makeVisible()
                    }
                    is UIState.ShowData -> {
                        binding.appBar.makeVisible()
                        binding.viewNestedScroll.makeVisible()
                        setData(state.data)
                    }
                    is UIState.ShowError -> {
                        binding.appBar.makeGone()
                        binding.viewNestedScroll.makeGone()
                        binding.viewGroupError.makeVisible()
                        displayError(state.errorUIState.errorType.errorMessageResId)
                    }
                    else -> {
                        /* NO-OP */
                    }
                }
            }
        }
    }

    private fun setData(movieDetails: MovieDetails) {
        binding.apply {
            imgMovie.loadImage(UrlHelper.getBackDropUrl(movieDetails.movieImageUrl))

            binding.toolbarLayout.title = movieDetails.title

            tvPopularity.setDataIfAvailable(isVisible = movieDetails.popularity != null) {
                tvPopularityTitle.makeVisible()
                tvPopularity.text = movieDetails.popularity!!.roundToInt().toString()
            }

            tvVote.setDataIfAvailable(isVisible = movieDetails.voteAverage != null) {
                imgVote.makeVisible()
                tvVote.text = getString(R.string.movie_average_vote_count,
                    movieDetails.voteAverage.toString())
            }

            tvReleaseDate.setDataIfAvailable(isVisible = !movieDetails.releaseDate.isNullOrBlank()) {
                tvReleaseDate.text = DateUtils.formatDate(movieDetails.releaseDate!!)
            }

            tvDetails.setDataIfAvailable(isVisible = !movieDetails.overview.isNullOrBlank()) {
                tvDetails.text = movieDetails.overview
            }

            tvTag.setDataIfAvailable(isVisible = !movieDetails.tagline.isNullOrBlank()) {
                tvTag.text = movieDetails.tagline
            }
        }

        setGenres(movieDetails.genres)
    }

    private fun setGenres(genres: List<String>?) {
        genres?.forEach { genre ->
            val chip = layoutInflater.inflate(
                R.layout.item_genre, binding.chipGroupGenres, false
            ) as Chip
            chip.text = genre
            binding.chipGroupGenres.addView(chip)
        }
    }

    private fun displayError(errorMessageResId: Int) {
        binding.tvError.text = getString(errorMessageResId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}