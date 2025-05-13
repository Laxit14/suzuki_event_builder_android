package com.multitv.eventbuilder.ui.travelstay.dodonot.fragment

import android.os.Bundle
import android.text.Html
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
import com.multitv.eventbuilder.databinding.FragmentDoDonotBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.dodonotmodel.model.DoDontResponse
import com.multitv.eventbuilder.ui.travelstay.dodonot.viewmodel.DoDoNotViewModel
import com.multitv.eventbuilder.ui.travelstay.dodonot.viewmodelfactory.DoDoNotViewModelFactory

class DoDoNotFragment : Fragment(), View.OnClickListener{

    private var _binding: FragmentDoDonotBinding? = null
    private val binding get() = _binding!!

    private var navController: NavController? = null
    private lateinit var viewModel : DoDoNotViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDoDonotBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        binding.backArrow.setOnClickListener(this)

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = DoDoNotViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[DoDoNotViewModel::class.java]

        viewModel.DoDoNotLiveData.observe(viewLifecycleOwner) { response ->
            setData(response)
        }
        getDataFromApi()

    }

    private fun getDataFromApi() {
        viewModel.getDoDoNotResponse(AppConstant.DODONOTAPI)
    }

    private fun setData(response: Generic<DoDontResponse>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()
                val data = response.data
                binding.tittle.text = response.data.pagetittle.trim()
                binding.doNotItContent.text = Html.fromHtml(data.data.get(1).description,Html.FROM_HTML_MODE_LEGACY)
                binding.doItContent.text = Html.fromHtml(data.data.get(0).description,Html.FROM_HTML_MODE_LEGACY)
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

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.backArrow -> {
                navController?.navigateUp()
            }
        }
    }
}