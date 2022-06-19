package com.moviediscovery.ui.uistate

sealed interface SearchUIState : UIState<Nothing> {
    object ShowNoSearchResult : SearchUIState
}