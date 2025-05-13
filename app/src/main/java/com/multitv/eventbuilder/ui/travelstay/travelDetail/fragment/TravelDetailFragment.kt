package com.multitv.eventbuilder.ui.travelstay.travelDetail.fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.constant.AppConstant
import com.multitv.eventbuilder.databinding.FragmentTravelDetailBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.traveldetailmodel.model.TravelDetailResponse
import com.multitv.eventbuilder.ui.travelstay.travelDetail.viewmodel.TravelDetailViewModel
import com.multitv.eventbuilder.ui.travelstay.travelDetail.viewmodelfactory.TravelDetailViewModelFactory

class TravelDetailFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentTravelDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private lateinit var viewModel: TravelDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTravelDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        binding.backArrow.setOnClickListener(this)

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = TravelDetailViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TravelDetailViewModel::class.java]

        viewModel.travelDetailResponse.observe(viewLifecycleOwner) { response ->
            setData(response)
        }

        getDataFromApi()

    }

    private fun getDataFromApi() {
        viewModel.getTravelDetailResponse(AppConstant.TRAVELDETAILAPI)
    }

    private fun setData(response: Generic<TravelDetailResponse>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()

                Log.e("Conference Success", "Data: ${response.data}")

                binding.title.text = response.data.pageTittle.trim()

                val travelDetailItem = response.data.data.firstOrNull()
                val url = "https://vapi.multitvsolution.com/msvc/sc_team/travel_detail.html"
                binding.webView.apply {
                    settings.javaScriptEnabled = true

                    setBackgroundColor(Color.TRANSPARENT)

                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                            if (url != null && url.startsWith("tel:")) {
                                val intent = Intent(Intent.ACTION_DIAL)
                                intent.data = Uri.parse(url)
                                startActivity(intent)
                                return true
                            }
                            return false
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)

                            // Inject custom style
                         /*   view?.evaluateJavascript(
                                """
                (function() {
                    document.body.style.backgroundColor = 'transparent';
                    document.body.style.color = 'white';
                    var tags = document.querySelectorAll('td, th, p, li, span, a');
                    tags.forEach(function(tag) {
                        tag.style.color = 'white';
                    });
                })();
                """.trimIndent(),
                                null
                            )*/
                        }
                    }

                    loadUrl(url)
                }


            }

            is Generic.Error -> {
                ProgressDialog.hideProgressDialog()
                Log.e("Conference Error", "Message: ${response.message}")
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            is Generic.Failure -> {
                ProgressDialog.hideProgressDialog()
                Log.e("Conference Failure", "Exception: ${response.exception.message}")
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backArrow -> {
                navController.navigateUp()
            }
        }
    }

}