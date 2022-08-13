package com.moviediscovery.model.uistate

sealed interface UIState<out T> {
    object ShowEmptyData : UIState<Nothing>
    object ShowLoading : UIState<Nothing>
    data class ShowData<T>(val data: T) : UIState<T>
    data class ShowError<T>(
        val errorUIState: ErrorUIState,
        val data: T?,
    ) : UIState<T>
}