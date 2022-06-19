package com.moviediscovery.ui.uistate

sealed class ErrorUIState(val errorType: ErrorType = ErrorType.UNKNOWN_ERROR) {
    class ToastError(errorType: ErrorType) : ErrorUIState(errorType)
    class FullScreenError(errorType: ErrorType) : ErrorUIState(errorType)
}