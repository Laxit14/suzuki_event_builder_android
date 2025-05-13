package com.multitv.eventbuilder.ui.conference.scTeam.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.databinding.FragmentEmergencyContactBinding
import com.multitv.eventbuilder.databinding.FragmentScTeamBinding

class ScTeamFragment : Fragment(), View.OnClickListener {

    private var _binding : FragmentScTeamBinding? = null
    private val binding get() = _binding!!

    private var navController : NavController? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScTeamBinding.inflate(inflater,container,false)
        return binding.root
    }
    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(),R.id.nav_host)
        binding.backArrow.setOnClickListener(this)

        binding.webview.apply {
            webViewClient = WebViewClient() // Ensures links open within the WebView
            settings.javaScriptEnabled = true // Enable JS if the page needs it
            loadUrl("https://vapi.multitvsolution.com/msvc/sc_team/index.html")
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backArrow -> {
                navController?.navigateUp()
            }
        }
    }
}