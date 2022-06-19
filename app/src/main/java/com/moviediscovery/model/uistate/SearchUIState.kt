package com.moviediscovery.model.uistate

sealed interface SearchUIState : UIState<Nothing> {
    object ShowNoSearchResult : SearchUIState
}