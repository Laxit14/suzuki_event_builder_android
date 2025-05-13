package com.multitv.eventbuilder.ui.conference.emergencyContact.fragment

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
import com.multitv.eventbuilder.databinding.FragmentEmergencyContactBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.model.EmergencyContactItem
import com.multitv.eventbuilder.model.conference.model.ConferenceDataResponse
import com.multitv.eventbuilder.model.emergencymodel.EmergencyResponse
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.conference.conferencechild.adaptor.ConferenceAdaptor
import com.multitv.eventbuilder.ui.conference.conferencechild.viewmodel.ConferenceViewModel
import com.multitv.eventbuilder.ui.conference.conferencechild.viewmodelfactory.ConferenceViewModelFactory
import com.multitv.eventbuilder.ui.conference.emergencyContact.adaptor.EmergencyAdaptor
import com.multitv.eventbuilder.ui.conference.emergencyContact.viewmodel.EmergencyContactViewmodel
import com.multitv.eventbuilder.ui.conference.emergencyContact.viewmodelfactory.EmergencyContactViewmodelFactory
import com.multitv.eventbuilder.ui.conference.msilContact.ContactAdapter

class EmergencyContactFragment : Fragment(), View.OnClickListener {


    private var _binding : FragmentEmergencyContactBinding? = null
    private val binding get() = _binding!!

    private lateinit var emergencyAdaptor: EmergencyAdaptor
    private var navController: NavController? = null
    private lateinit var viewModel : EmergencyContactViewmodel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmergencyContactBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        binding.backArrow.setOnClickListener(this)

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = EmergencyContactViewmodelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[EmergencyContactViewmodel::class.java]

        viewModel.emergencyContact.observe(viewLifecycleOwner) { response ->
            setData(response)
        }

        getDataFromApi()

      /*  setUi()*/

    }

    private fun getDataFromApi() {
        viewModel.emergencyContactData(AppConstant.Emegency_Contact)
    }


    private fun setData(response: Generic<EmergencyResponse>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()
                val dataList = response.data.data
                Log.e("emergencyData Success", "Data: $dataList")

                if(response.data.success){
                    val list = response.data.data.emerCont

                    binding.title.text = response.data.pageTittle.trim()
                    emergencyAdaptor = EmergencyAdaptor(list)
                    binding.emergencyContactRecycler.layoutManager = LinearLayoutManager(requireContext())
                    binding.emergencyContactRecycler.adapter = emergencyAdaptor
                }
                else{
                    binding.noText.visibility = View.VISIBLE
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

  /*  fun setUi(){

        val contacts = listOf(
            EmergencyContactItem("Police Emergency number", "155"),
            EmergencyContactItem("Ambulance Emergency number", "112"),
            EmergencyContactItem("Fire Emergency number", "110"),
            EmergencyContactItem("Telephone Directory Enquiries", "11880"),
            EmergencyContactItem("Water", "185"),
            EmergencyContactItem("Electricity", "186"),
            EmergencyContactItem("Gas", "187")

        )

        emergencyAdaptor = EmergencyAdaptor(contacts)
        binding.emergencyContactRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.emergencyContactRecycler.adapter = emergencyAdaptor

    }*/

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backArrow -> {
                navController?.navigateUp()
            }
        }
    }
}