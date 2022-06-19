package com.moviediscovery.ui.uistate

sealed interface UIState<out T> {
    object ShowEmptyData : UIState<Nothing>
    object ShowLoading : UIState<Nothing>
    data class ShowData<T>(val data: T) : UIState<T>
    data class ShowError(val errorUIState: ErrorUIState) : UIState<Nothing>
}