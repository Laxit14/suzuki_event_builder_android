package com.multitv.eventbuilder.ui.interaction.localization.fragment

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
import com.multitv.eventbuilder.databinding.FragmentLocalizationBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.interaction.localization.adaptor.LocalizationAdapter
import com.multitv.eventbuilder.model.localizationmodel.model.LocalizationResponse
import com.multitv.eventbuilder.ui.interaction.localization.viewmodel.LocalizationViewModel
import com.multitv.eventbuilder.ui.interaction.localization.viewmodelfactory.LocalizationViewModelFactory

class LocalizationFragment() : Fragment(), View.OnClickListener {

    private var _binding: FragmentLocalizationBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel : LocalizationViewModel

    private lateinit var adapter : LocalizationAdapter

    private var navController: NavController? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocalizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        binding.backArrow.setOnClickListener(this)

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = LocalizationViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[LocalizationViewModel::class.java]

        viewModel.localizationLivedata.observe(viewLifecycleOwner) { response ->
            setData(response)
        }

        getDataFromApi()

    }

    private fun getDataFromApi() {
        viewModel.getLocalData(AppConstant.LOCALIZATIONAPI)
    }


    private fun setData(response: Generic<LocalizationResponse>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()
                val dataList = response.data.data
                Log.e("Conference Success", "Data: $dataList")

                binding.title.text = response.data.page_tittle.trim()

                adapter = LocalizationAdapter(dataList)
                binding.localizationRecycler.layoutManager = LinearLayoutManager(requireContext())
                binding.localizationRecycler.adapter = adapter



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
        if (v?.id == R.id.backArrow) {
            navController?.navigateUp()
        }
    }
}