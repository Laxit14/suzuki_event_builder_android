package com.multitv.eventbuilder.ui.welcome_msvc.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.constant.AppConstant
import com.multitv.eventbuilder.databinding.WelcomeMsvcBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.welcomemsvcmodel.model.WelcomeMessageResponse
import com.multitv.eventbuilder.ui.welcome_msvc.viewmodel.WelcomeViewModel
import com.multitv.eventbuilder.ui.welcome_msvc.viewmodelfactory.WelcomeViewModelFactory

class WelcomeMsvcFragment : Fragment(), View.OnClickListener {

    private var _binding: WelcomeMsvcBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var viewModel: WelcomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WelcomeMsvcBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        binding.backArrow.setOnClickListener(this)

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = WelcomeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[WelcomeViewModel::class.java]

        observeViewModel()

        val bannerId = 1
        viewModel.getWelcomeResponse(AppConstant.WELCOME_API)
    }

    private fun observeViewModel() {
        viewModel.welcomeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Generic.Loading -> {
                    ProgressDialog.showProgresssDialog(requireContext())
                }

                is Generic.Success -> {
                    ProgressDialog.hideProgressDialog()

                    /*  binding.textName.text = response.data.data.firstOrNull()?.name*/
                    val htmlText = response.data.data.firstOrNull()?.details.orEmpty()

                    binding.welcomeContentWebView.apply {
                        settings.javaScriptEnabled = true
                        setBackgroundColor(Color.TRANSPARENT)
                        settings.textZoom = 90

                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)

                                // Inject style after content loads
                                view?.evaluateJavascript(
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
                                )

                            }
                        }

                        loadDataWithBaseURL(null, htmlText, "text/html", "UTF-8", null)
                    }



                    Glide.with(requireContext())
                        .load(response.data.data.firstOrNull()?.img)
                        .into(binding.backgroundImage)


                    Log.e("WelcomeMsvc", "API full response: ${response.data}")

                    /* response.data?.let {
                         setData(it)
                     }*/
                }

                is Generic.Error -> {
                    ProgressDialog.hideProgressDialog()
                    Toast.makeText(
                        requireContext(),
                        "Error: ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("welcome Error", response.message ?: "Unknown error")
                }

                is Generic.Failure -> {
                    ProgressDialog.hideProgressDialog()
                    Toast.makeText(
                        requireContext(),
                        "Failure: ${response.exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("welcome Failure", "Exception: ${response.exception.message}")
                }
            }
        }
    }

    private fun setData(response: WelcomeMessageResponse) {
        // Example: assuming response has a 'message' field
        // binding.welcomeContent.text = response.message // Use actual field from your data model
        binding.welcomeContentWebView.apply {
            settings.javaScriptEnabled = true
            setBackgroundColor(Color.TRANSPARENT)

            // Optional: Set text zoom if needed
            settings.textZoom = 90

            loadData(
                """
        <html>
            <body style="color: white; text-align: center; font-family: 'Roboto', sans-serif; font-size: 14px; background-color: transparent;">
                Welcome to the conference!
            </body>
        </html>
        """.trimIndent(),
                "text/html",
                "UTF-8"
            )
        }

        Log.d("WelcomeMsvcFragment", "Message: ${response.message}")
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backArrow -> navController.navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}