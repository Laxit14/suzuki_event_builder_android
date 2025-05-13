package com.multitv.eventbuilder.ui.splash.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.base.BaseActivityTwo
import com.multitv.eventbuilder.constant.Preference
import com.multitv.eventbuilder.ui.login.activity.LoginActivity
import com.multitv.eventbuilder.ui.main.activity.MainActivity


import android.widget.Toast
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability


@SuppressLint("CustomSplashScreen")
class SplashScreen : BaseActivityTwo() {

    private val handler = Handler(Looper.getMainLooper())
    private val REQUEST_CODE_UPDATE = 1001
    private var isNavigate = false

    private lateinit var appUpdateManager: AppUpdateManager
    private val updateListener = InstallStateUpdatedListener { state ->
        when (state.installStatus()) {
            InstallStatus.DOWNLOADED -> {
                // Optional: You can prompt the user to restart the app
                Toast.makeText(this, "Update downloaded", Toast.LENGTH_SHORT).show()
            }
            InstallStatus.INSTALLED -> {
                moveToNextActivity()
            }
            InstallStatus.CANCELED, InstallStatus.FAILED -> {
                moveToNextActivity()
            }
            else -> {
                // Other states: downloading, pending, etc.
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Preference.init(this)

        // Initialize the update manager
        appUpdateManager = AppUpdateManagerFactory.create(this)

        checkForUpdate()

        internetCallBack()
    }

    private fun checkForUpdate() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // Start the immediate update flow
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    REQUEST_CODE_UPDATE
                )
            } else {
                // No update required, proceed with splash
                proceedWithSplash()
            }
        }.addOnFailureListener {
            // In case of failure, continue as normal
            proceedWithSplash()
        }
    }

    private fun proceedWithSplash() {
        handler.postDelayed({
            moveToNextActivity()
        }, 3000)
    }

    override fun onInternetAvailable() {
        super.onInternetAvailable()
        // No need to call moveToNextActivity here anymore
    }

    override fun onInternetLost() {
        super.onInternetLost()
        handler.removeCallbacksAndMessages(null)
    }

    private fun moveToNextActivity() {
        if (isNavigate) return
        isNavigate = true

        val intent = if (Preference.getBooleanData(Preference.IsLogin)) {
            Intent(this@SplashScreen, MainActivity::class.java)
        } else {
            Intent(this@SplashScreen, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_UPDATE) {
            if (resultCode != RESULT_OK) {
                // User cancelled the update or update failed
                Toast.makeText(this, "Update canceled or failed", Toast.LENGTH_SHORT).show()
                proceedWithSplash()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    Toast.makeText(this, "An update has been downloaded", Toast.LENGTH_SHORT).show()
                }
            }

        appUpdateManager.registerListener(updateListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        appUpdateManager.unregisterListener(updateListener)
    }
}
