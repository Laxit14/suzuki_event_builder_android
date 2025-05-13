package com.multitv.eventbuilder.ui.conference.dresscode.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.constant.AppConstant
import com.multitv.eventbuilder.constant.Helper.capitalizeWords
import com.multitv.eventbuilder.databinding.FragmentDresscodeBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.dresscode.model.DressCodeResponse
import com.multitv.eventbuilder.ui.conference.dresscode.viewmodel.DressViewModel
import com.multitv.eventbuilder.ui.conference.dresscode.viewmodelfactory.DressViewModelFactory

class Dresscode : Fragment(), View.OnClickListener {

    private var _binding : FragmentDresscodeBinding? = null
    private val binding get() = _binding!!

    private var navController: NavController? = null

    private lateinit var viewModel : DressViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDresscodeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        binding.backArrow.setOnClickListener(this)

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = DressViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[DressViewModel::class.java]

        viewModel.dressCodeResponse.observe(viewLifecycleOwner) { response ->
            setData(response)
        }
        getDataFromApi()
    }

    private fun getDataFromApi() {
        viewModel.getDressCode(AppConstant.DRESSCODEAPI)

    }

    private fun setData(response: Generic<DressCodeResponse>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()
                val data = response.data.data

                binding.title.text = response.data.pageTittle.trim()

                val position_1 = data[0]
                binding.materialButton.text = position_1.forDress.capitalizeWords()
                binding.businessFormal.text = position_1.dressType.capitalizeWords()

                Glide.with(requireContext())
                    .load(position_1.image)
                    .into(binding.boysImage)

                Glide.with(requireContext())
                    .load( data[1].image)
                    .into(binding.girlImage)

                val position_2 = data[2]
                binding.materialButton2.text = position_2.forDress.capitalizeWords()
                binding.businessFormal2.text = position_2.dressType.capitalizeWords()

                Glide.with(requireContext())
                    .load(position_2.image)
                    .into(binding.boysImage2)

                Glide.with(requireContext())
                    .load(data[3].image)
                    .into(binding.girlImage2)

            }

            is Generic.Error -> {
                ProgressDialog.hideProgressDialog()
                Toast.makeText(requireContext(), "Error: ${response.message}", Toast.LENGTH_SHORT).show()
            }

            is Generic.Failure -> {
                ProgressDialog.hideProgressDialog()
                Toast.makeText(requireContext(), "Failure: ${response.exception.message}", Toast.LENGTH_SHORT).show()
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