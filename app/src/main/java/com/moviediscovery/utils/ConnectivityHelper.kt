package com.moviediscovery.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.moviediscovery.app.MovieApp

object ConnectivityHelper {

    private val connectivityManager by lazy {
        MovieApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Suppress("DEPRECATION")
    fun hasInternetConnection(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) or
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) or
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI,
                    ConnectivityManager.TYPE_MOBILE,
                    ConnectivityManager.TYPE_ETHERNET,
                    -> true
                    else -> false
                }
            }
        }

        return false
    }
}