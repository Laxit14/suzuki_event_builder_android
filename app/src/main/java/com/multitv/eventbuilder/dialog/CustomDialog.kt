package com.multitv.eventbuilder.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.Settings
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import com.multitv.eventbuilder.R

object CustomDialog {
    private var noInternetDialog: Dialog? = null

    fun showNoInternetDialog(context: Context) {
        val activity = context as? Activity

        if(activity == null || activity.isFinishing || activity.isDestroyed) return

        if (noInternetDialog != null && noInternetDialog!!.isShowing) return // Don't show multiple times

        noInternetDialog = Dialog(context)
        noInternetDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        noInternetDialog!!.setContentView(R.layout.internet_dialog)
        noInternetDialog!!.setCancelable(false) // Prevent manual dismissal
        noInternetDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        noInternetDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

        val btnGoToSettings = noInternetDialog!!.findViewById<Button>(R.id.btnGoToSettings)
        val btnClose = noInternetDialog!!.findViewById<ImageView>(R.id.cancelbtn)

        btnGoToSettings.setOnClickListener {
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            context.startActivity(intent)
        }

        btnClose.setOnClickListener {
            noInternetDialog!!.dismiss()
            if(context is Activity){
                context.finishAffinity()
            }
        }

        noInternetDialog!!.show()
    }

    fun dismissDialog() {
        noInternetDialog?.dismiss()
        noInternetDialog = null
    }

    fun isDialogShowing(): Boolean {
        return noInternetDialog?.isShowing == true
    }
}