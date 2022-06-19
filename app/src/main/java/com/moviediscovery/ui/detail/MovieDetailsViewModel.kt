package com.moviediscovery.ui.detail

import androidx.lifecycle.*
import com.moviediscovery.api.Resource
import com.moviediscovery.data.repository.MovieDetailsRepository
import com.moviediscovery.model.MovieDetails
import com.moviediscovery.model.uistate.ErrorUIState
import com.moviediscovery.model.uistate.UIState
import com.moviediscovery.utils.Constants
import com.moviediscovery.utils.ErrorUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _movieDetailsLiveData: MutableLiveData<UIState<MovieDetails>> = MutableLiveData()
    val movieDetailsLiveData: LiveData<UIState<MovieDetails>> = _movieDetailsLiveData

    init {
        savedStateHandle.get<Int>(Constants.KEY_MOVIE_ID)?.let {
            getMovieDetails(it)
        }
    }

    private fun getMovieDetails(movieId: Int) {
        _movieDetailsLiveData.value = UIState.ShowLoading
        viewModelScope.launch {
            when (val response = movieDetailsRepository.getMovieDetails(movieId)) {
                is Resource.Success -> {
                    _movieDetailsLiveData.postValue(UIState.ShowData(response.data))
                }
                is Resource.Error -> {
                    _movieDetailsLiveData.postValue(UIState.ShowError(
                        ErrorUIState.FullScreenError(ErrorUtils.propagateError(
                            response.exception
                        ))
                    ))
                }
            }
        }
    }

    fun fetchAgain() {
        savedStateHandle.get<Int>(Constants.KEY_MOVIE_ID)?.let {
            getMovieDetails(it)
        }
    }
}