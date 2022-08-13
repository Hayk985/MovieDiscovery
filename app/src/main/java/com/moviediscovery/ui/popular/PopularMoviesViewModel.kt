package com.moviediscovery.ui.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviediscovery.api.Resource
import com.moviediscovery.common.Pagination
import com.moviediscovery.data.repository.MovieRepository
import com.moviediscovery.model.Movie
import com.moviediscovery.model.MovieData
import com.moviediscovery.model.MovieType
import com.moviediscovery.model.uistate.ErrorType
import com.moviediscovery.model.uistate.ErrorUIState
import com.moviediscovery.model.uistate.UIState
import com.moviediscovery.utils.ConnectivityHelper
import com.moviediscovery.utils.ErrorUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val pagination: Pagination,
) : ViewModel() {

    private val _moviesList: MutableList<Movie> = mutableListOf()
    private val moviesList: List<Movie> get() = _moviesList.toList()

    private val _uiStateLiveData = MutableLiveData<UIState<List<Movie>>>()
    val uiStateLiveData: LiveData<UIState<List<Movie>>> get() = _uiStateLiveData

    private var ongoingRequestAvailable: Boolean = false
    private var isToastErrorShown: Boolean = false

    init {
        getPopularMovies()
    }

    fun getPopularMovies(
        pageNumber: Int = Pagination.FIRST_PAGE,
        isRefreshing: Boolean = false,
    ) {
        _uiStateLiveData.value = UIState.ShowLoading
        viewModelScope.launch {
            ongoingRequestAvailable = true
            movieRepository.getMovies(
                movieType = MovieType.POPULAR,
                pageNumber = pageNumber,
                shouldFetchFromCache = !isRefreshing
            ).collect { response ->
                ongoingRequestAvailable = false
                when (response) {
                    is Resource.Success -> {
                        if (pageNumber == Pagination.FIRST_PAGE)
                            _moviesList.clear()
                        handleSuccessResponse(response.data)
                    }
                    is Resource.Error -> {
                        if (isRefreshing)
                            setToastErrorShown(false)
                        handleErrorResponse(response.exception)
                    }
                }
            }
        }
    }

    fun loadMore() {
        if (pagination.hasNextPage() && !ongoingRequestAvailable) {
            if (!ConnectivityHelper.hasInternetConnection()) {
                _uiStateLiveData.postValue(UIState.ShowError(
                    ErrorUIState.ToastError(ErrorType.NETWORK_ERROR),
                    moviesList
                ))
                return
            }

            getPopularMovies(pagination.getNextPage())
        }
    }

    private fun handleSuccessResponse(data: MovieData) {
        _moviesList.addAll(data.movies)
        _uiStateLiveData.postValue(UIState.ShowData(moviesList))
        updatePagingData(data.currentPage, data.totalPages)

        // Reset toast error state
        setToastErrorShown(false)
    }

    private fun handleErrorResponse(exception: Exception?) {
        val errorType: ErrorType = ErrorUtils.propagateError(exception)

        val errorUIState = if (moviesList.isNotEmpty()) {
            ErrorUIState.ToastError(errorType)
        } else {
            ErrorUIState.FullScreenError(errorType)
        }

        _uiStateLiveData.postValue(UIState.ShowError(errorUIState, moviesList))
    }

    fun setToastErrorShown(toastErrorShown: Boolean) {
        isToastErrorShown = toastErrorShown
    }

    fun isToastErrorShown(): Boolean = isToastErrorShown

    private fun updatePagingData(currentPage: Int, totalPages: Int) {
        pagination.setCurrentPage(currentPage)
        pagination.setTotalPageCount(totalPages)
    }
}