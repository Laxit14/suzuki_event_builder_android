package com.multitv.eventbuilder.ui.conference.conferencechild.fragment

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
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
import com.multitv.eventbuilder.constant.AppConstant.AWARDS
import com.multitv.eventbuilder.constant.AppConstant.DETAILED_AGENDA
import com.multitv.eventbuilder.constant.AppConstant.DIGITAL_EXHIBITION
import com.multitv.eventbuilder.constant.AppConstant.DRESS_CODE
import com.multitv.eventbuilder.constant.AppConstant.EMERGENCY_CONTACT
import com.multitv.eventbuilder.constant.AppConstant.GALLERY
import com.multitv.eventbuilder.constant.AppConstant.MAIL_CONTACT
import com.multitv.eventbuilder.constant.AppConstant.SC_TEAM
import com.multitv.eventbuilder.constant.AppConstant.SEATING_PLAN
import com.multitv.eventbuilder.constant.Preference
import com.multitv.eventbuilder.databinding.FragmentConferenceBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.model.conference.model.ConferenceDataResponse
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.conference.conferencechild.adaptor.ConferenceAdaptor
import com.multitv.eventbuilder.ui.conference.conferencechild.viewmodel.ConferenceViewModel
import com.multitv.eventbuilder.ui.conference.conferencechild.viewmodelfactory.ConferenceViewModelFactory

class Conference : Fragment(), ConferenceAdaptor.OnConferenceItemClickListener, OnClickListener {

    private var _binding: FragmentConferenceBinding? = null
    private val binding get() = _binding!!

    private var navController: NavController? = null
    private lateinit var conferenceAdapter: ConferenceAdaptor
    private lateinit var viewModel: ConferenceViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConferenceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        binding.backArrow.setOnClickListener(this)

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = ConferenceViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ConferenceViewModel::class.java]

        viewModel.conferenceResponse.observe(viewLifecycleOwner) { response ->
            setData(response)
        }

        getDataFromApi()
    }

    private fun getDataFromApi() {
        val url = AppConstant.CONFERENCEAPI + "&category=" + Preference.getUserCategory()?.trim()
        viewModel.getConferenceData(url)
    }

    private fun setData(response: Generic<ConferenceDataResponse>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()
                val dataList = response.data.data
                Log.e("Conference Success", "Data: $dataList")
                if (!(response.data.success)) {

                    binding.recycler.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                } else {
                    binding.tvNoData.visibility = View.GONE
                    binding.recycler.visibility = View.VISIBLE
                    binding.tittle.text = response.data.pageTittle.trim()
                    conferenceAdapter = ConferenceAdaptor(dataList, this)
                    binding.recycler.layoutManager = LinearLayoutManager(requireContext())
                    binding.recycler.adapter = conferenceAdapter
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

    override fun onConferenceClick(title: String) {
        when (val normalizedTitle = title.lowercase().trim()) {
            AppConstant.CONFERENCE_AGENDA.lowercase().trim() -> {
                val bundle = Bundle().apply {
                    putString("title", normalizedTitle.lowercase())
                    putInt("imageRes", R.drawable.conference_agenda_img)
                }
                navController?.navigate(R.id.conferenceFragment_to_conferenceDetailFragment, bundle)
            }

            AppConstant.SPEAKER.lowercase().trim() -> {
                navController?.navigate(R.id.conferenceFragment_to_ourSpeakerFragment)
            }

            SEATING_PLAN.lowercase().trim() -> {
                navController?.navigate(R.id.conferenceFragment_to_seatingPlanFragment)
            }

            MAIL_CONTACT.lowercase().trim() -> {
                navController?.navigate(R.id.conferenceFragment_to_msilcontactFragment)
            }

            DETAILED_AGENDA.lowercase().trim() -> {
                val bundle = Bundle().apply {
                    putString("title", normalizedTitle.lowercase())
                    putInt("imageRes", R.drawable.detailed_schedule_img)
                }
                navController?.navigate(R.id.conferenceFragment_to_conferenceDetailFragment, bundle)
            }

            DRESS_CODE.lowercase().trim() -> {
                navController?.navigate(R.id.conferenceFragment_to_dressCodeFragment)
            }

            GALLERY.lowercase().trim() -> {
                navController?.navigate(R.id.conferenceFragment_to_conferenceGalleryFragment)

                /*    if (!Preference.getImage().isNullOrEmpty()) {
                        navController?.navigate(R.id.conferenceFragment_to_conferenceGalleryFragment)
                    } else {
                        Toast.makeText(requireContext(), "Click your image first.", Toast.LENGTH_SHORT)
                            .show()

                    }*/

            }

            SC_TEAM.lowercase().trim() -> {
                navController?.navigate(R.id.conferenceFragment_to_scTeamFragment)
            }

            EMERGENCY_CONTACT.lowercase().trim() -> {
                navController?.navigate(R.id.conferenceFragment_to_emergencyContactFragment)
            }

            AWARDS.lowercase().trim() -> {
                navController?.navigate(R.id.conferenceFragment_to_awardsFragment)
            }

            DIGITAL_EXHIBITION.lowercase().trim() -> {
                navController?.navigate(R.id.conferenceFragment_to_digitalExhibition)
            }

            else -> {
                // Toast.makeText(requireContext(), title, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.backArrow) {
            navController?.navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
