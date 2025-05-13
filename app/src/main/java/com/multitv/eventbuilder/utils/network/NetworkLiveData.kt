package com.multitv.ott.resource.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData

class NetworkLiveData private constructor(context: Context) : LiveData<Boolean>() {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : NetworkCallback() {
        override fun onAvailable(network: Network) {
            postValue(true) // Internet is available
        }

        override fun onLost(network: Network) {
            postValue(false) // Internet is lost
        }

        override fun onUnavailable() {
            super.onUnavailable()
            postValue(false)
        }
    }

    init {
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        val activeNetwork = connectivityManager.activeNetworkInfo
        postValue(activeNetwork != null && activeNetwork.isConnected)
    }

    override fun onActive() {
        super.onActive()
        val activeNetwork = connectivityManager.activeNetworkInfo
        postValue(activeNetwork != null && activeNetwork.isConnected)
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    fun isNetworkAvailable(): Boolean {
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
    fun checkManually() {
        val activeNetwork = connectivityManager.activeNetworkInfo
        postValue(activeNetwork != null && activeNetwork.isConnected)
    }

    companion object {
        @Volatile
        private var instance: NetworkLiveData? = null

        fun getInstance(context: Context): NetworkLiveData {
            return instance ?: synchronized(this) {
                instance ?: NetworkLiveData(context.applicationContext).also { instance = it }
            }
        }
    }
}
