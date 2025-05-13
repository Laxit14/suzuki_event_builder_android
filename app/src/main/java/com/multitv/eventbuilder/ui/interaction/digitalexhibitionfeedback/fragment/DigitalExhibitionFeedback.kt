package com.multitv.eventbuilder.ui.interaction.digitalexhibitionfeedback.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.constant.AppConstant
import com.multitv.eventbuilder.constant.Preference
import com.multitv.eventbuilder.databinding.FragmentDigitalExhibitionFeedbackBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.digitalexhibitionfeedbackmodel.model.DigitalExhibitionFeedbackResponse
import com.multitv.eventbuilder.ui.interaction.digitalexhibitionfeedback.viewmodel.DigitalExhibitionFeedbackViewModel
import com.multitv.eventbuilder.ui.interaction.digitalexhibitionfeedback.viewmodelfactory.DigitalExhibitionFeedbackViewModelFactory

class DigitalExhibitionFeedback : Fragment(), View.OnClickListener {

    private var _binding: FragmentDigitalExhibitionFeedbackBinding? = null
    private val binding get() = _binding!!

    private var navController: NavController? = null

    private lateinit var viewModel : DigitalExhibitionFeedbackViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDigitalExhibitionFeedbackBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Preference.init(requireContext())
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        binding.backArrow.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)

        binding.rgBenefit.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                val selectedBenefitText = group.findViewById<RadioButton>(checkedId).text.toString()
                if (selectedBenefitText.equals("No", ignoreCase = true)) {
                    binding.tittleText.visibility = View.VISIBLE
                    binding.etReason.visibility = View.VISIBLE
                } else {
                    binding.tittleText.visibility = View.GONE
                    binding.etReason.visibility = View.GONE
                }
            }
        }


        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = DigitalExhibitionFeedbackViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[DigitalExhibitionFeedbackViewModel::class.java]

        viewModel.digitalExhibitionfeedbackLiveData.observe(viewLifecycleOwner) { response ->
            setData(response)
        }
    }


    private fun setData(response: Generic<DigitalExhibitionFeedbackResponse>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()
                val response = response.data
                if(response != null){
                    Toast.makeText(requireContext(),response.message, Toast.LENGTH_SHORT).show()
                }

                binding.rgContent.clearCheck()
                binding.rgBenefit.clearCheck()
                binding.rgExperience.clearCheck()
                binding.studyRadio.clearCheck()
                binding.etReason.setText("")
                binding.etExpectations.setText("")
                binding.etLast.setText("")

            }

            is Generic.Error -> {
                ProgressDialog.hideProgressDialog()
                Log.e("digitalfeedback Error", "Message: ${response.message}")
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            is Generic.Failure -> {
                ProgressDialog.hideProgressDialog()
                Log.e("digitalfeedback Failure", "Exception: ${response.exception.message}")
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backArrow -> {
                navController?.navigateUp()
            }

            R.id.btnSubmit -> {
               validateAllFields()
            }
        }
    }


    fun validateAllFields() {
        val selectedContentId = binding.rgContent.checkedRadioButtonId
        val selectedBenefitId = binding.rgBenefit.checkedRadioButtonId
        val selectedExperienceId = binding.rgExperience.checkedRadioButtonId
        val selectedStudyId = binding.studyRadio.checkedRadioButtonId

        val reason = binding.etReason.text.toString().trim()
        val expectation = binding.etExpectations.text.toString().trim()
        val digilibComment = binding.etLast.text.toString().trim()


        when {
            selectedContentId == -1 -> {
                showToast("Please rate the Content")
            }
            selectedBenefitId == -1 -> {
                showToast("Please answer the Benefit question")
            }
            selectedExperienceId == -1 -> {
                showToast("Please answer the Experience question")
            }
            selectedStudyId == -1 -> {
                showToast("Please answer the Organization Study question")
            }
            expectation.isEmpty() -> {
                showToast("Please fill the Expectation")
            }
            digilibComment.isEmpty() -> {
                showToast("Please fill the Digilib Comment")
            }
            selectedBenefitId != -1 -> {
                val selectedBenefitText = binding.rgBenefit.findViewById<RadioButton>(selectedBenefitId).text.toString()
                if (selectedBenefitText.equals("No", ignoreCase = true) && reason.isEmpty()) {
                    binding.tittleText.visibility = View.VISIBLE
                    binding.etReason.visibility = View.VISIBLE
                    showToast("Please enter a reason for selecting 'No'")
                } else {
                    callSubmitApi(
                        content = binding.rgContent.findViewById<RadioButton>(selectedContentId).text.toString(),
                        benefit = selectedBenefitText,
                        experience = binding.rgExperience.findViewById<RadioButton>(selectedExperienceId).text.toString(),
                        study = binding.studyRadio.findViewById<RadioButton>(selectedStudyId).text.toString(),
                        expectation = expectation,
                        digilibComment = digilibComment,
                        reason = reason
                    )
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    private fun callSubmitApi(
        content: String,
        benefit: String,
        experience: String,
        study: String,
        expectation: String,
        digilibComment: String,
        reason: String
    ) {
        // Replace this with your actual API call logic
        Log.d("API_CALL", "Content: $content")
        Log.d("API_CALL", "Benefit: $benefit")
        Log.d("API_CALL", "Experience: $experience")
        Log.d("API_CALL", "Study: $study")
        Log.d("API_CALL", "Expectation: $expectation")
        Log.d("API_CALL", "DigilibComment: $digilibComment")
        Log.d("API_CALL", "Reason: $reason")

        val userId = Preference.getUserId()!!

        viewModel.getDigitalExhibitionFeedBackData(
            url = AppConstant.DIGITALEXHIBITIONFEEDBACK,
            userId = userId,
            overallFeedback = experience,
            content_feedback = content,
            organization_reason = reason,
            organization_benefit = benefit,
            expectations = expectation,
            study_area = study,
            digilib_comment = digilibComment
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}