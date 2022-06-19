package com.moviediscovery.utils

import com.moviediscovery.ui.uistate.ErrorType
import java.net.UnknownHostException

object ErrorUtils {
    fun propagateError(exception: Exception?): ErrorType {
        return when (exception) {
            is UnknownHostException -> {
                if (ConnectivityHelper.hasInternetConnection()) {
                    ErrorType.INTERNAL_SERVER_ERROR
                } else ErrorType.NETWORK_ERROR
            }
            else -> {
                ErrorType.UNKNOWN_ERROR
            }
        }
    }
}