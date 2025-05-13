package com.multitv.eventbuilder.ui.conference.conferenceGallery.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.constant.Preference
import com.multitv.eventbuilder.databinding.FragmentConferenceGalleryBinding
import com.multitv.eventbuilder.databinding.FragmentScTeamBinding
import com.multitv.eventbuilder.ui.conference.conferenceGallery.adaptor.PhotoAdapter
import com.multitv.eventbuilder.ui.conference.conferenceGallery.adaptor.TabPagerAdapter

class ConferenceGalleryFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentConferenceGalleryBinding? = null
    private val binding get() = _binding!!
    private var navController: NavController? = null
    /*private lateinit var adapter: PhotoAdapter*/
    private lateinit var myPhotoAdapter: PhotoAdapter
    private lateinit var conferencePhotoAdapter: PhotoAdapter
    private var isExpanded = false // Track if expanded or not
    private var isExpandedConference = false // Track if expanded or not

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConferenceGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        binding.backArrow.setOnClickListener(this)
        setUiForMyPhoto()
        val name = Preference.getUserName()?.replace(" ", "_") ?: ""
        val id = Preference.getUserId()
        val url = "https://msvc-cf.videostech.cloud/outputs/images/${name}_${id}/"
        val adapter = TabPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "My Images"
                1 -> "Conference Images"
                else -> null
            }
        }.attach()
    }

    fun setUiForMyPhoto() {
        val myPhotos = listOf(
            R.drawable.feedback,
            R.drawable.feedback,
            R.drawable.feedback,
            R.drawable.feedback,
            R.drawable.feedback,
            R.drawable.feedback
        ) // Add real images

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backArrow -> {
                navController?.navigateUp()
            }
        }
    }
}