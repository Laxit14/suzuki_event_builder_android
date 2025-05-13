package com.multitv.eventbuilder.ui.interaction.interactionchild.fragment

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.constant.AppConstant
import com.multitv.eventbuilder.constant.AppConstant.ASK_YOUR_QUESTION
import com.multitv.eventbuilder.constant.AppConstant.CURRENCY_CONVERTER
import com.multitv.eventbuilder.constant.AppConstant.DIGITAL_EXHIBITION
import com.multitv.eventbuilder.constant.AppConstant.DIGITAL_EXHIBITION_FEEDBACK
import com.multitv.eventbuilder.constant.AppConstant.ENTERTAINMENT
import com.multitv.eventbuilder.constant.AppConstant.FEEDBACK
import com.multitv.eventbuilder.constant.AppConstant.MSVC_CAMERA_CORNER
import com.multitv.eventbuilder.constant.Preference
import com.multitv.eventbuilder.databinding.FragmentInteractionBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.interaction.adapter.PdfAdapter
import com.multitv.eventbuilder.ui.interaction.interactionchild.adaptor.InteractionAdapter
import com.multitv.eventbuilder.model.interactionmodel.model.InteractionResponse
import com.multitv.eventbuilder.ui.interaction.interactionchild.viewmodel.InteractionViewModel
import com.multitv.eventbuilder.ui.interaction.interactionchild.viewmodelfactory.InteractionViewModelFactory

class Interactionchild : Fragment(), InteractionAdapter.OnInteractionItemClickListener,
    View.OnClickListener {

    private var _binding: FragmentInteractionBinding? = null
    private val binding get() = _binding!!

    private var navController: NavController? = null
    private lateinit var interactionAdapter: InteractionAdapter

    private lateinit var adapter: PdfAdapter
    private var pdfList: List<Pair<String, String>> = listOf()
    // List to store PDF file names

    private lateinit var viewModel: InteractionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInteractionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        binding.backArrow.setOnClickListener(this)

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = InteractionViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[InteractionViewModel::class.java]


        viewModel.interactionLiveData.observe(viewLifecycleOwner) { response ->
            setData(response)
        }

        getDataFromApi()

    }

    private fun getDataFromApi() {
        val url = AppConstant.INTERACTIONAPI + "&category=" + Preference.getUserCategory()?.trim()
        viewModel.getInteractionData(url)
    }

    private fun setData(response: Generic<InteractionResponse>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()
                val dataList = response.data.data
                Log.e("Interaction Success", "Data: $dataList")
                if (!(response.data.success)) {
                    binding.interactionRecycler.visibility = View.GONE
                    binding.noData.visibility = View.VISIBLE
                } else {
                    binding.noData.visibility = View.GONE
                    binding.interactionRecycler.visibility = View.VISIBLE
                    binding.tittle.text = response.data.pageTittle.trim()
                    interactionAdapter = InteractionAdapter(dataList, this)
                    binding.interactionRecycler.layoutManager =
                        LinearLayoutManager(requireContext())
                    binding.interactionRecycler.adapter = interactionAdapter
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

    override fun onInteractionClick(tittle: String) {
        /*  Toast.makeText(requireContext(), "Clicked: ${tittle}", Toast.LENGTH_SHORT).show()*/

        when (tittle.lowercase().trim()) {
            FEEDBACK.lowercase().trim() -> {
                navController?.navigate(R.id.interactionFragment_to_feedbackFragment)
            }

            CURRENCY_CONVERTER.lowercase().trim() -> {
                navController?.navigate(R.id.interactionFragment_to_currencyConverter)
            }

            ENTERTAINMENT.lowercase().trim() -> {
                navController?.navigate(R.id.interactionFragment_to_entertainmentFragment)
            }

            ASK_YOUR_QUESTION.lowercase().trim() -> {
                navController?.navigate(R.id.interactionFragment_to_askQuestionFragment)
            }

            MSVC_CAMERA_CORNER.lowercase().trim() -> {
                navController?.navigate(R.id.interactionFragment_to_msvcCameraCornerFragment)
            }

            DIGITAL_EXHIBITION.lowercase().trim() -> {
                navController?.navigate(R.id.interactionFragment_to_digitalExhibition)
            }

            "expression of intrest for localistation" -> {
                navController?.navigate(R.id.interactionFragment_to_localizationFragment)
            }

            DIGITAL_EXHIBITION_FEEDBACK.lowercase().trim() -> {
                navController?.navigate(R.id.interactionFragment_to_digitalExhibitionFeedback)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backArrow -> {
                navController?.navigateUp()
            }
        }
    }
}

