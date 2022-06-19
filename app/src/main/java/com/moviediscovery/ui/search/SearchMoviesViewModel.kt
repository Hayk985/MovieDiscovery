package com.moviediscovery.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviediscovery.api.Resource
import com.moviediscovery.common.Pagination
import com.moviediscovery.data.repository.MovieRepository
import com.moviediscovery.model.Movie
import com.moviediscovery.model.MovieData
import com.moviediscovery.ui.uistate.ErrorType
import com.moviediscovery.ui.uistate.ErrorUIState
import com.moviediscovery.ui.uistate.SearchUIState
import com.moviediscovery.ui.uistate.UIState
import com.moviediscovery.utils.ConnectivityHelper
import com.moviediscovery.utils.Constants
import com.moviediscovery.utils.ErrorUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchMoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val pagination: Pagination,
) : ViewModel() {

    private val searchQueryState: MutableStateFlow<String> = MutableStateFlow("")
    private val _searchUIState = MutableStateFlow<UIState<List<Movie>>>(UIState.ShowEmptyData)
    val searchUIState: StateFlow<UIState<List<Movie>>> = _searchUIState.asStateFlow()

    private val _moviesList: MutableList<Movie> = mutableListOf()
    val moviesList: List<Movie> get() = _moviesList.toList()

    private var ongoingRequestAvailable: Boolean = false

    init {
        initSearchFlow()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun initSearchFlow() {
        viewModelScope.launch(Dispatchers.IO) {
            searchQueryState
                .debounce(Constants.SEARCH_DEBOUNCE_TIME)
                .filter {
                    if (it.isBlank() || it.length <= 1) {
                        _searchUIState.emit(UIState.ShowEmptyData)
                        _moviesList.clear()
                        return@filter false
                    }

                    if (!ConnectivityHelper.hasInternetConnection()) {
                        sendErrorMessage(ErrorType.NETWORK_ERROR)
                        return@filter false
                    }

                    it.isNotEmpty()
                }.flatMapLatest {
                    performSearch(it)
                }.collectLatest {
                    handleResponse(it)
                }
        }
    }

    fun searchMovies(query: String) {
        searchQueryState.value = query
    }

    private suspend fun performSearch(
        query: String, pageNumber: Int = Pagination.FIRST_PAGE,
    ): Flow<Resource<MovieData>> {
        ongoingRequestAvailable = true
        _searchUIState.emit(UIState.ShowLoading)
        return movieRepository.searchMovies(query, pageNumber)
    }

    fun loadNext() {
        if (pagination.hasNextPage() && !ongoingRequestAvailable) {
            if (!ConnectivityHelper.hasInternetConnection()) {
                sendErrorMessage(ErrorType.NETWORK_ERROR)
                return
            }

            viewModelScope.launch {
                performSearch(searchQueryState.value, pagination.getNextPage()).collectLatest {
                    handleResponse(it)
                }
            }
        }
    }

    private suspend fun handleResponse(response: Resource<MovieData>) {
        when (response) {
            is Resource.Success -> {
                handleSuccess(response.data)
            }
            is Resource.Error -> {
                val errorType = ErrorUtils.propagateError(response.exception)
                sendErrorMessage(errorType)
            }
        }
        ongoingRequestAvailable = false
    }

    private suspend fun handleSuccess(movieData: MovieData) {
        if (movieData.currentPage == Pagination.FIRST_PAGE)
            _moviesList.clear()
        _moviesList.addAll(movieData.movies)

        if (moviesList.isEmpty()) {
            _searchUIState.emit(SearchUIState.ShowNoSearchResult)
            return
        }

        updatePagingData(movieData.currentPage, movieData.totalPages)
        _searchUIState.emit(UIState.ShowData(moviesList))
    }

    private fun sendErrorMessage(errorType: ErrorType) {
        val errorUIState = if (_moviesList.isNotEmpty()) {
            ErrorUIState.ToastError(errorType)
        } else {
            ErrorUIState.FullScreenError(errorType)
        }

        _searchUIState.value = UIState.ShowError(errorUIState)
    }

    private fun updatePagingData(currentPage: Int, totalPages: Int) {
        pagination.setCurrentPage(currentPage)
        pagination.setTotalPageCount(totalPages)
    }
}