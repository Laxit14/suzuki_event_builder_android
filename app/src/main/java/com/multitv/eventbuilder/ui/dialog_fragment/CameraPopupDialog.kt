package com.multitv.eventbuilder.ui.dialog_fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.multitv.eventbuilder.R

class CameraPopupDialog(private val onDismissAction: () -> Unit) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.camera_popup, container, false)
        isCancelable = false

        val closeButton = view.findViewById<MaterialButton>(R.id.cancel)
        closeButton.setOnClickListener {
            dismiss() // Close the dialog
        }

        val forwardBtn = view.findViewById<MaterialButton>(R.id.forward)
        forwardBtn.setOnClickListener {
            dismiss()
            onDismissAction()
        }

        /*// Automatically dismiss after 5 seconds and open camera
        Handler(Looper.getMainLooper()).postDelayed({
            if (isAdded) {
                dismiss()
                onDismissAction()
            }
        }, 5000) // 5 seconds delay*/

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}
