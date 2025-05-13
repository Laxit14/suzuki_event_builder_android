package com.multitv.eventbuilder.ui.lamplight.fragment

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
import com.bumptech.glide.Glide
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.constant.AppConstant
import com.multitv.eventbuilder.databinding.FragmentLamplightingBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.model.lamplightingmodel.PartsResponse
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.lamplight.viewmodel.LampLightinfViewModel
import com.multitv.eventbuilder.ui.lamplight.viewmodelfactory.LampLightingViewModelFactory

class LampLightFragment : Fragment() {

    private var _binding: FragmentLamplightingBinding? = null
    private val binding get() = _binding!!
    private var glowLevel = 0  //
    private var isGlowing = false

    private lateinit var navController: NavController
    private lateinit var viewModel: LampLightinfViewModel

    private var defaultImageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLamplightingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = LampLightingViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[LampLightinfViewModel::class.java]

        /*binding.parentFl.setOnClickListener {
            toggleGlowEffect()
        }*/
        binding.backArrow.setOnClickListener {
            navController.navigateUp()
        }

        viewModel.lampLiveData.observe(viewLifecycleOwner) { response ->
            setData(response)
        }

        getDataFromApi()

    }

    private fun getDataFromApi() {
        viewModel.getLampLighting(AppConstant.LAMPLIGHTING)
    }

    private fun setData(response: Generic<PartsResponse>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()
                val dataList = response.data.data

              /*  binding.title.text = response.data.pageTittle.trim()*/

                if (dataList.isNotEmpty()) {
                    val firstItem = dataList[0]
                    val secondItem = dataList[1]

                    Glide.with(requireContext())
                        .load(firstItem.image)
                        .into(binding.glowOverlay)

                    binding.parentFl.setOnClickListener {
                        if (!isGlowing) {
                            Glide.with(requireContext())
                                .load(secondItem.image)
                                .into(binding.glowOverlay)
                            binding.tapToLightText.visibility = View.GONE
                        } else {
                            Glide.with(requireContext())
                                .load(firstItem.image)
                                .into(binding.glowOverlay)
                            /*binding.tapToLightText.visibility = View.VISIBLE*/
                        }
                        isGlowing = !isGlowing
                    }

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

private fun toggleGlowEffect() {

    if (!isGlowing) {
        binding.glowOverlay.setImageResource(R.drawable.lamplighting)
        binding.tapToLightText.visibility = View.GONE
    } else {
        binding.glowOverlay.setImageResource(R.drawable.growlamp)
        binding.tapToLightText.visibility = View.VISIBLE
    }
    isGlowing = !isGlowing

}


override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}
}