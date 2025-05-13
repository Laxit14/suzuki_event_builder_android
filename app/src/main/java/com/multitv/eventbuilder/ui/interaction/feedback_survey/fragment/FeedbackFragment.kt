package com.multitv.eventbuilder.ui.interaction.feedback_survey.fragment

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
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.constant.AppConstant
import com.multitv.eventbuilder.constant.Preference
import com.multitv.eventbuilder.databinding.FragmentFeedbackBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.feedbackmodel.model.FeedbackResponse
import com.multitv.eventbuilder.ui.interaction.feedback_survey.viewmodel.FeedbackViewModel
import com.multitv.eventbuilder.ui.interaction.feedback_survey.viewmodelfactory.FeedbackViewModelFactory

class FeedbackFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentFeedbackBinding? = null
    private val binding get() = _binding!!
    private var navController: NavController? = null
    private lateinit var viewModel : FeedbackViewModel
    private var hotelAccoRating: Float = 0f
    private var entertainmentRating: Float = 0f
    private var engagementRating: Float = 0f
    private var foodBeveragesRating: Float = 0f
    private var overallExperienceRating: Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedbackBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host)
        binding.backArrow.setOnClickListener(this)
        binding.ratingBarHotelAcco.rating = 0f
        binding.ratingBarEntertainment.rating = 0f
        binding.ratingBarEngagement.rating = 0f
        binding.ratingBarFoodBeverges.rating = 0f
        binding.ratingBarOverallExperience.rating = 0f
        binding.ratingBarHotelAcco.onRatingListener = {
            Log.d("rating hotel", "nice rating view -> $it")
        }
        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = FeedbackViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[FeedbackViewModel::class.java]
        viewModel.feedbackLiveData.observe(viewLifecycleOwner) { response ->
            setData(response)
        }
        binding.ratingBarEntertainment.setOnClickListener(this)
        binding.ratingBarEngagement.setOnClickListener(this)
        binding.ratingBarFoodBeverges.setOnClickListener(this)
        binding.ratingBarOverallExperience.setOnClickListener(this)
        binding.submit.setOnClickListener(this)
    }
    private fun setData(response: Generic<FeedbackResponse>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }
            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()

                val message = response.data.message
                if(!message.isEmpty()){
                    Toast.makeText(requireContext(),"Thank You for giving Valuable Feedback", Toast.LENGTH_SHORT).show()
                }
// Clear EditTexts
                binding.hotelInput.text?.clear()
                binding.entertainmentlInput.text?.clear()
                binding.engagementlInput.text?.clear()
                binding.foodInput.text?.clear()
                binding.overalllInput.text?.clear()
                binding.ratingBarHotelAcco.rating =0f
                binding.ratingBarEntertainment.rating =0f
                binding.ratingBarEngagement.rating =0f
                binding.ratingBarFoodBeverges.rating =0f
                binding.ratingBarOverallExperience.rating = 0f

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
        when(v?.id){
            R.id.backArrow -> {
                navController?.navigateUp()
            }
            R.id.ratingBarEngagement -> engagementRating = binding.ratingBarEngagement.rating
            R.id.ratingBarEntertainment -> entertainmentRating = binding.ratingBarEntertainment.rating
            R.id.ratingBarFoodBeverges -> foodBeveragesRating = binding.ratingBarFoodBeverges.rating
            R.id.ratingBarOverallExperience -> overallExperienceRating = binding.ratingBarOverallExperience.rating
            R.id.ratingBarHotelAcco -> hotelAccoRating = binding.ratingBarHotelAcco.rating
            R.id.submit ->{ validateAndSubmit()}
        }
    }
    private fun validateAndSubmit() {
// Check if all ratings are 0
        val hotelAccoRating = binding.ratingBarHotelAcco.rating
        val entertainmentRating = binding.ratingBarEntertainment.rating
        val engagementRating = binding.ratingBarEngagement.rating
        val foodBeveragesRating = binding.ratingBarFoodBeverges.rating
        val overallExperienceRating = binding.ratingBarOverallExperience.rating
        if (hotelAccoRating == 0f &&
            entertainmentRating == 0f &&
            engagementRating == 0f &&
            foodBeveragesRating == 0f &&
            overallExperienceRating == 0f
        ) {
            Toast.makeText(requireContext(), "Please rate your experience.", Toast.LENGTH_SHORT).show()
            return
        }
        val htl_comment = binding.hotelInput.text.toString().trim()
        val entrtainment_cmt = binding.entertainmentlInput.text.toString().trim()
        val engamnt_cmt = binding.engagementlInput.text.toString().trim()
        val food_cmt = binding.foodInput.text.toString().trim()
        val overall_cmt = binding.overalllInput.text.toString().trim()
        viewModel.getFeedBackData(
            AppConstant.FEEDBACKAPI,
            hotelAccoRating, htl_comment,
            entertainmentRating, entrtainment_cmt,
            engagementRating, engamnt_cmt,
            foodBeveragesRating, food_cmt,
            overallExperienceRating, overall_cmt,
            Preference.getUserId().toString()
        )
        Log.d(
            "Ratings", "Hotel: $hotelAccoRating, Entertainment: $entertainmentRating, " +
                    "Engagement: $engagementRating, Food: $foodBeveragesRating, " +
                    "Overall: $overallExperienceRating"
        )
    }

}