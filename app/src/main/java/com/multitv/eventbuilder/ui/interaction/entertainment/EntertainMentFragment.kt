package com.multitv.eventbuilder.ui.interaction.entertainment

import EntertainmentResponse
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.multitv.eventbuilder.databinding.FragmentEntertainmentBinding
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.constant.AppConstant
import com.multitv.eventbuilder.constant.Preference
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.interaction.entertainment.viewmodel.EntertainmentViewModel
import com.multitv.eventbuilder.ui.interaction.entertainment.viewmodelfactory.EntertainmentViewModelFactory
import okio.ByteString.Companion.encodeUtf8

class EntertainMentFragment : Fragment() {

    private lateinit var navController: NavController
    private var _binding: FragmentEntertainmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EntertainmentViewModel

    private var isImage2Revealed = false
    private var image2Url: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntertainmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = EntertainmentViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[EntertainmentViewModel::class.java]

        binding.backArrow.setOnClickListener {
            navController.navigateUp()
        }

        viewModel.entertainmentLiveData.observe(viewLifecycleOwner) { response ->
            setData(response)
        }

        getDataFromApi()

        binding.btnSubmit.setOnClickListener {
            val name = binding.etName.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter celebrity name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            else{
                AlertDialog.Builder(requireContext())
                    .setTitle("Thank You!")
                    .setMessage("your guess name submitted")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }



            if (!isImage2Revealed) {
                image2Url?.let {
                    Glide.with(requireContext()).load(it).into(binding.imageFullBlur)
                    clearBlur()
                    binding.etName.visibility = View.GONE
                    binding.btnSubmit.visibility = View.GONE
                    isImage2Revealed = true
                    Toast.makeText(requireContext(), "Image revealed 🎉", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Image already revealed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getDataFromApi() {
        viewModel.getEntertainmentData(AppConstant.ENTERTAINMENTAPI)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun setData(response: Generic<EntertainmentResponse>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()
                val item = response.data.data.entertainment.firstOrNull()
                item?.let {

                    if(item.hide.equals("1")){
                        with (binding){
                            tittleText2.isVisible = true
                            etName.isVisible = true
                            btnSubmit.isVisible = true
                        }
                    }
                    else{
                        with (binding){
                            tittleText2.isVisible = false
                            etName.isVisible = false
                            btnSubmit.isVisible = false
                        }
                    }
                    Glide.with(requireContext())
                        .load(it.image1)
                        .into(binding.imageFullBlur)

                }
            }

            is Generic.Error -> {
                ProgressDialog.hideProgressDialog()
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            is Generic.Failure -> {
                ProgressDialog.hideProgressDialog()
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun applyBlur(radius: Float) {
        val blurEffect = RenderEffect.createBlurEffect(radius, radius, Shader.TileMode.CLAMP)
        binding.imageFullBlur.setRenderEffect(blurEffect)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun clearBlur() {
        binding.imageFullBlur.setRenderEffect(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
