package com.multitv.eventbuilder.base

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.multitv.eventbuilder.application.MyApp
import com.multitv.eventbuilder.dialog.CustomDialog

abstract class BaseActivityTwo : AppCompatActivity() {

    /*abstract val rootView: View
    abstract val appBar: View
    abstract val bottomBar: View?*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        internetCallBack()

       /* WindowCompat.setDecorFitsSystemWindows(window, false)
        handleInsets()*/
    }

  /*  private fun handleInsets() {
        val originalTopPadding = appBar.paddingTop
        val originalBottomPadding = bottomBar?.paddingBottom ?: 0

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { _, windowInsets ->
            val insets = windowInsets.getInsets(
                WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars()
            )

            // Apply top inset to AppBar
            appBar.setPadding(
                appBar.paddingLeft,
                originalTopPadding + insets.top,
                appBar.paddingRight,
                appBar.paddingBottom
            )

            // Apply bottom inset to BottomBar (if present)
            bottomBar?.setPadding(
                bottomBar.paddingLeft,
                bottomBar.paddingTop,
                bottomBar.paddingRight,
                originalBottomPadding + insets.bottom
            )

            WindowInsetsCompat.CONSUMED
        }*/

    override fun onResume() {
        super.onResume()
        checkInternetManually()
    }

    fun internetCallBack(){

        MyApp.networkLiveData.observe(this) { isConnected ->
            if (isConnected) {
//                Toast.makeText(this, "Connected to Internet base", Toast.LENGTH_SHORT).show()
                CustomDialog.dismissDialog()
//                onInternetAvailable()
            } else {
//                Toast.makeText(this, "No Internet Connection base", Toast.LENGTH_SHORT).show()
                CustomDialog.showNoInternetDialog(this)
                onInternetLost()
            }
        }
    }

    private fun checkInternetManually() {
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        val isConnected =
            capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        if (isConnected) {
            Log.d("InternetCheck", "Internet Connected (Manual Check) - BaseActivityTwo")
            CustomDialog.dismissDialog()
            onInternetAvailable()
        }
        else{
            onInternetLost()
        }

    }

    open fun onInternetLost(){

    }
    open fun onInternetAvailable(){}

    override fun onDestroy() {
        super.onDestroy()
        CustomDialog.dismissDialog()
    }
}
