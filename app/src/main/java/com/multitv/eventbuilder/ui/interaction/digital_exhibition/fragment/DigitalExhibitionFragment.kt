package com.multitv.eventbuilder.ui.interaction.digital_exhibition.fragment

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
import androidx.viewpager2.widget.ViewPager2
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.constant.AppConstant
import com.multitv.eventbuilder.constant.Preference
import com.multitv.eventbuilder.databinding.FragmentDigitalExhibitionBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.interaction.digital_exhibition.adapter.DigitalExhibitionPagerAdapter
import com.multitv.eventbuilder.model.digitalexhibitionmodel.model.DigitalExhibitionResponse
import com.multitv.eventbuilder.ui.interaction.digital_exhibition.viewmodel.DigitalExhibitionViewModel
import com.multitv.eventbuilder.ui.interaction.digital_exhibition.viewmodelfactory.DigitalviewModelFactory

class DigitalExhibitionFragment() : Fragment(), View.OnClickListener {

    private var _binding: FragmentDigitalExhibitionBinding? = null
    private val binding get() = _binding!!

    private var navController: NavController? = null
    private lateinit var viewModel : DigitalExhibitionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentDigitalExhibitionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        binding.backArrow.setOnClickListener(this)
        binding.moveToDigiFeed.setOnClickListener(this)

        Preference.init(requireContext())
        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = DigitalviewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[DigitalExhibitionViewModel::class.java]

        viewModel.digitalExhibitionLiveData.observe(viewLifecycleOwner) { response ->
            setData(response)
        }

        getDataFromApi()

    }

    private fun getDataFromApi() {
        val userId = Preference.getUserId()!!
        Log.e("userId",""+userId)
        viewModel.getDigitalExhibitionData(AppConstant.DIGITALEXHIBITIONAPI,userId)
    }


    private fun setData(response: Generic<DigitalExhibitionResponse>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()

                val items = response.data.data
                if (!items.isNullOrEmpty()) {
                    binding.tittle.text = response.data.pageTittle
                    val allImages: List<String> = response.data.data.flatMap { it.image }
                    val adapter = DigitalExhibitionPagerAdapter(allImages)

                    binding.batchCount.text = items.firstOrNull()?.batchNo
                    binding.dateValue.text = items.firstOrNull()?.date
                    binding.timeValue.text =items.firstOrNull()?.time
                    binding.content.text = items.firstOrNull()?.description

                    binding.viewpager2.adapter = adapter

                    // Optional: ViewPager2 page change listener to update text dynamically
                    binding.viewpager2.registerOnPageChangeCallback(object :
                        ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                      /*      val item = items[position]
                            binding.batchCount.text = item.batchNo
                            binding.dateValue.text = item.date
                            binding.timeValue.text = item.time
                            binding.content.text = item.description*/
                        }
                    })


                    binding.backImage.setOnClickListener {
                        val currentItem = binding.viewpager2.currentItem
                        if (currentItem > 0) binding.viewpager2.currentItem = currentItem - 1
                    }
                    binding.forwardImage.setOnClickListener {
                        val currentItem = binding.viewpager2.currentItem
                        val totalItems = binding.viewpager2.adapter?.itemCount ?: 0
                        if (currentItem < totalItems - 1) binding.viewpager2.currentItem = currentItem + 1
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

    override fun onClick(v: View?) {
        if (v?.id == R.id.backArrow) {
            navController?.navigateUp()
        }

        if(v?.id == R.id.moveToDigiFeed){
           navController?.navigate(R.id.digitalExhibition_digitalExhibitionFeedback)
        }
    }
}