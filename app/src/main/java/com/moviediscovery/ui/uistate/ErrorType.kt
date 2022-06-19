package com.moviediscovery.ui.uistate

import androidx.annotation.StringRes
import com.moviediscovery.R

enum class ErrorType(@StringRes val errorMessageResId: Int) {
    NETWORK_ERROR(R.string.error_no_connection),
    INTERNAL_SERVER_ERROR(R.string.error_internal),
    UNKNOWN_ERROR(R.string.error_unknown)
}