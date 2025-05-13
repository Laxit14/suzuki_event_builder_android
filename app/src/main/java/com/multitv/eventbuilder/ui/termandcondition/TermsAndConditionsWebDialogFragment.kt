package com.multitv.eventbuilder.ui.termandcondition

import androidx.fragment.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.multitv.eventbuilder.databinding.TermConditionPopupBinding

class TermsAndConditionsWebDialogFragment : DialogFragment() {

    private var _binding: TermConditionPopupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TermConditionPopupBinding.inflate(inflater, container, false)

        binding.btnClose.setOnClickListener {

        }
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl("https://vapi.multitvsolution.com/msvc/terms_conditions.html") // Replace with actual URL

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}

