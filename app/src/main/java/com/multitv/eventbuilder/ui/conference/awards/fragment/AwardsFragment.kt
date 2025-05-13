package com.multitv.eventbuilder.ui.conference.awards.fragment

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import com.multitv.eventbuilder.databinding.FragmentAwordsBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.conference.awards.adptor.AwardPagerAdapter
import com.multitv.eventbuilder.model.award.model.AwardResponse
import com.multitv.eventbuilder.ui.conference.awards.viewmodel.AwardsViewModel
import com.multitv.eventbuilder.ui.conference.awards.viewmodelfactory.AwardsViewModelFactory

class AwardsFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentAwordsBinding? = null
    private val binding get() = _binding!!
    private var navController: NavController? = null
    private lateinit var viewModel : AwardsViewModel
    private lateinit var adapter: AwardPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAwordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Preference.init(requireContext())

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = AwardsViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[AwardsViewModel::class.java]

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        binding.backArrow.setOnClickListener(this)

        viewModel.awardsLiveData.observe(viewLifecycleOwner) { response ->
            setData(response)
        }

        binding.textDownload.setOnClickListener {
            val currentPosition = binding.viewPagerAward.currentItem
            val imageUrl = adapter.getImageUrlAt(currentPosition)
            downloadImage(requireContext(), imageUrl)
        }

        getDataFromApi()


    }

    private fun downloadImage(context: Context, imageUrl: String) {
        val request = DownloadManager.Request(Uri.parse(imageUrl))
            .setTitle("Downloading Image")
            .setDescription("Award image is being downloaded...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "award_image_${System.currentTimeMillis()}.jpg")

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)

        Toast.makeText(context, "Download started...", Toast.LENGTH_SHORT).show()
    }

    private fun getDataFromApi() {
        val userId = Preference.getUserId()
        Log.e("Awards userId",""+userId)
        viewModel.getAwardImages(AppConstant.AWARDSAPI,userId!!)
    }

    private fun setData(response: Generic<AwardResponse>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()
                val dataList = response.data.data.firstOrNull()?.image5!!
                adapter = AwardPagerAdapter(dataList)
                binding.viewPagerAward.adapter = adapter
                binding.wormDotsIndicator.attachTo(binding.viewPagerAward)
                binding.title.text = response.data.pageTittle.toString().trim()

                binding.viewPagerAward.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)

                    }
                })
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.backArrow) {
            navController?.navigateUp()
        }
    }
}
