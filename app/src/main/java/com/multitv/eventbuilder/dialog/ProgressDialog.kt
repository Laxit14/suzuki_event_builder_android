package com.multitv.eventbuilder.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import com.multitv.eventbuilder.R

object ProgressDialog {

    private var progressDialog : Dialog? = null

    fun showProgresssDialog(context: Context){
        if(progressDialog != null && progressDialog!!.isShowing) return
        progressDialog = Dialog(context)
        progressDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog?.setContentView(R.layout.progressdialog)
        progressDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        progressDialog?.window?.setGravity(Gravity.CENTER)
        progressDialog?.setCancelable(false) // Optional
        progressDialog?.show()

    }

    fun hideProgressDialog(){
        progressDialog?.dismiss()
    }
}