package com.multitv.eventbuilder.ui.home.fragment

import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.constant.AppConstant
import com.multitv.eventbuilder.constant.Preference
import com.multitv.eventbuilder.databinding.FragmentHomeBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.model.BannerDataItem
import com.multitv.eventbuilder.model.NotificationDataItem
import com.multitv.eventbuilder.model.ResponseNotification
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic

import com.multitv.eventbuilder.ui.home.adaptor.HomeAdapter
import com.multitv.eventbuilder.ui.home.adaptor.ImagePagerAdapter
import com.multitv.eventbuilder.ui.home.adaptor.NotificationAdapter
import com.multitv.eventbuilder.model.homemodel.model.BannerData
import com.multitv.eventbuilder.model.homemodel.model.HomeDataItem
import com.multitv.eventbuilder.model.homemodel.model.PagesData
import com.multitv.eventbuilder.ui.home.adaptor.OnNotificationClickListener
import com.multitv.eventbuilder.ui.home.viewmodel.HomeViewModel
import com.multitv.eventbuilder.ui.home.viewmodelfactory.HomeViewModelFactory
import com.multitv.eventbuilder.utils.BottomMarginItemDecoration


class Home : Fragment(), ImagePagerAdapter.ImageClickListener, HomeAdapter.OnItemClickListener,
    OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageAdapter: ImagePagerAdapter
    private var bannerList = listOf<BannerDataItem>()
    private lateinit var navController: NavController

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var notifyAdaptor: NotificationAdapter

    private lateinit var viewModel: HomeViewModel


    private val handler = Handler(Looper.getMainLooper())
    /*private val autoScrollRunnable = object : Runnable {
        override fun run() {
            val viewPager = binding.imagesTop
            val nextItem = (viewPager.currentItem + 1) % imageAdapter.itemCount
            viewPager.setCurrentItem(nextItem, true)
            handler.postDelayed(this, 3000) // Auto-scroll every 1 second
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val imagePath = Preference.getUserImagePath()
        val userName = Preference.getUserName()

        binding.profileName1.text = userName
        binding.notification1.setOnClickListener(this)
        binding.notificationName.isSelected = true

        Glide.with(requireContext())
            .load(Preference.getImage())
            .placeholder(R.drawable.login_placeholder)
            .error(R.drawable.login_placeholder)
            .into(binding.profilePic)

        /*if (!imagePath.isNullOrEmpty()) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            binding.profilePic.setImageBitmap(bitmap)
        }*/
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]


        // Observe once only if data isn't already available
        if (viewModel.homeDataResponse.value == null ||
            viewModel.homeDataResponse.value !is Generic.Success
        ) {
            getHomeDataFromApi()
        } else {
            // Reuse existing data
            (viewModel.homeDataResponse.value as? Generic.Success)?.let {
                setDataFromApi(it)
            }
        }

        /* startAutoScrollViewPager()*/
        /* startAutoScrollRecyclerView()*/

        viewModel.homeDataResponse.observe(viewLifecycleOwner) { response ->
            setDataFromApi(response)
        }

        viewModel.notificationDataResponse.observe(viewLifecycleOwner) { response ->
            setNotificationFromApi(response)
        }

        /*binding.notificationName.isSelected = true*/
        /* binding.notificationName.setMarqueeText("Your short text")*/

        /* val marqueeAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.marquee)
         binding.notificationName.startAnimation(marqueeAnim)*/

        binding.profilePic.setOnClickListener(this)

    }

    /*private fun startAutoScroll() {
        handler.postDelayed(autoScrollRunnable, 1000) // Start auto-scroll after 1 second
    }*/

    /*override fun getStatusBarView(): View? {
        return binding.actionLayout
    }
*/


    fun setDataFromApi(response: Generic<HomeDataItem>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()
                /*   val response = response.data*/
                Log.e("the home response", "" + response.data)

                val pagesDataList = response.data?.data?.pagesData ?: emptyList()
                Log.e("home data", "" + pagesDataList)
                if (!::homeAdapter.isInitialized) {
                    homeAdapter = HomeAdapter(pagesDataList, this)
                    binding.subImagesTRecycler.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    binding.subImagesTRecycler.adapter = homeAdapter
                } else {
                    Log.e("come back", "" + pagesDataList)
                    homeAdapter.updateItems(pagesDataList)
                    binding.subImagesTRecycler.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    binding.subImagesTRecycler.adapter = homeAdapter
                }

                val bannerDataList = response.data.data.bannerData
                imageAdapter = bannerDataList?.let { ImagePagerAdapter(it, this) }!!
                binding.imagesTop.adapter = imageAdapter
                binding.imagesTop.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                binding.wormDotsIndicator.attachTo(binding.imagesTop)

                binding.imagesTop.registerOnPageChangeCallback(object :
                    ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        imageAdapter.onPageSelected(
                            position,
                            binding.imagesTop.getChildAt(0) as RecyclerView
                        )
                    }
                })

                /*val notifyList = response.data.data.notificationData
                binding.notificationRecycler.layoutManager = LinearLayoutManager(requireContext())
                notifyAdaptor = NotificationAdapter(notifyList)
                binding.notificationRecycler.adapter = notifyAdaptor
                binding.notificationRecycler.addItemDecoration(BottomMarginItemDecoration(15))*/


                viewModel.getNotificationData(AppConstant.NOTIFICATIONAPI)


                binding.notificationName.text = response.data.data.marqueeText.toString().trim()

            }

            is Generic.Error -> {
                ProgressDialog.hideProgressDialog()
                Log.e("the home response", "" + response.message)
            }

            is Generic.Failure -> {
                ProgressDialog.hideProgressDialog()
                Log.e("the home response", "" + response.exception.message)
            }
        }

    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.profilePic -> {
                navController.navigate(R.id.homeFragment_to_profileFragment)
            }

            R.id.notification1 -> {
                navController.navigate(R.id.homeFragment_to_notificationFragment_home)
            }
        }

    }


    fun getHomeDataFromApi() {
        viewModel.getHomeData(AppConstant.HOMEAPI)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(autoScrollViewPager)  // ✅ Stop ViewPager auto-scroll
        handler.removeCallbacks(autoScrollRecyclerView)  // ✅ Stop RecyclerView auto-scroll
        _binding = null  // ✅ Prevent memory leaks
        if (this::imageAdapter.isInitialized) {
            imageAdapter.releaseAllPlayers()
        }

    }

    override fun onItemClick(imageItem: BannerData) {
        /*  Toast.makeText(requireContext(), imageItem.title, Toast.LENGTH_SHORT).show()*/

        val rawSlug = imageItem.slug
        if (rawSlug.isNullOrBlank()) return  // Check BEFORE calling lowercase()

        val slug = rawSlug.lowercase().trim()
        when (slug) {

            "welcome".lowercase().trim() -> {
                navController.navigate(R.id.homeFragment_to_welcomeFragment)
            }

            "hotel & stay".lowercase().trim() -> {
                navController.navigate(R.id.homeFragment_to_hotelstayFragment)
            }

            "Travel & stay".lowercase().trim() -> {
                navController.navigate(R.id.homeFragment_to_travelandstayFragment)
            }

            "about doha".lowercase().trim() -> {
                Log.d("BannerClick", "Clicked title: ${imageItem.title.lowercase().trim()}")
                navController.navigate(R.id.homeFragment_to_antalyaTour)
            }

            "conference details".lowercase().trim() -> {
                val bundle = Bundle().apply {
                    putString("title", "Conference Agenda")
                    putInt("imageRes", R.drawable.conference_agenda_img)
                }
                navController.navigate(R.id.homeFragment_to_conferenceDetailFragment, bundle)
            }

            "Conference Agenda".lowercase().trim() -> {
                val bundle = Bundle().apply {
                    putString("title", "Conference Agenda")
                    putInt("imageRes", R.drawable.conference_agenda_img)
                }
                navController.navigate(R.id.homeFragment_to_conferenceDetailFragment, bundle)
            }

            "Conference".lowercase().trim() -> {
                navController.navigate(R.id.homeFragment_to_conferenceFragment)
            }


            "shuttle bus".lowercase().trim() -> {
                val bundle = Bundle().apply {
                    putString("title", "SHUTTLE BUS")
                    putInt("imageRes", R.drawable.shuttle_bus_details_img)
                }
                navController.navigate(R.id.homeFragment_to_conferenceDetailFragment, bundle)
            }

            "food details".lowercase().trim() -> {
                val bundle = Bundle().apply {
                    putString("title", "food details")
                    putInt("imageRes", R.drawable.food_details)
                }
                navController.navigate(R.id.homeFragment_to_conferenceDetailFragment, bundle)
            }

            "guest gallivant".lowercase().trim() -> {
                val bundle = Bundle().apply {
                    putString("title", "guest gallivant")
                    putInt("imageRes", R.drawable.guest_gallivant)
                }
                navController.navigate(
                    R.id.action_home_notification_to_guestGallivant,
                    bundle
                )
            }

            "speaker".lowercase().trim() -> {
                navController?.navigate(R.id.action_home_notification_to_speaker)
            }

            "seating plan".lowercase().trim() -> {
                navController?.navigate(R.id.action_home_notification_to_seatingPlan)
            }

            "dress code".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_dressCode)

            "conference agenda".lowercase().trim() -> {
                val bundle = Bundle().apply {
                    putString("title", "Conference Agenda")
                    putInt("imageRes", R.drawable.conference_agenda_img)
                }
                navController.navigate(
                    R.id.action_home_notification_to_conferenceAgenda,
                    bundle
                )
            }

            "details agenda".lowercase().trim() -> {
                val bundle = Bundle().apply {
                    putString("title", "details agenda")
                    putInt("imageRes", R.drawable.detailed_schedule_img)
                }
                navController.navigate(
                    R.id.action_home_notification_to_detailsAgenda,
                    bundle
                )
            }

            "gallery".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_gallery)

            "sc team".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_scTeam)

            "awards".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_awards)

            "digital exhibition".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_digitalExhibition)

            "emergency contact".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_emergencyContact)

            "msil contact".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_msilContact)

            "travel details".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_travelDetails)

            "shuttle bus details".lowercase().trim() -> {
                val bundle = Bundle().apply {
                    putString("title", "Shuttle Bus Details")
                    putInt("imageRes", R.drawable.shuttle_bus_details_img)
                }
                navController.navigate(
                    R.id.action_home_notification_to_shuttleBusDetails,
                    bundle
                )
            }

            "event location".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_eventLocation)

            "weather".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_weather)

            "do's & dont".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_dosDont)

            "feedback".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_feedback)

            "entertainment".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_entertainment)

            "currency converter".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_currencyConverter)

            "msvc camera corner".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_msvcCameraCorner)

            "ask your question".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_askYourQuestion)

            "digital exhibition feedback".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_digitalExhibitionFeedback)

            "notes".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_notes)

            "lamp light".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_lamplight)

            "notification".lowercase()
                .trim() -> navController.navigate(R.id.homeFragment_to_notificationFragment_home)

            else -> {
                // Optional: Log if slug is not handled
                Log.d("BannerClick", "Video slug: $slug")
            }
        }
    }

    override fun onItemClick(homeItem: PagesData) {
        /*Toast.makeText(requireContext(), "Clicked: ${homeItem.title}", Toast.LENGTH_SHORT).show()*/

        val rawSlug = homeItem.slug
        if (rawSlug.isNullOrBlank()) return
        val slug = rawSlug.lowercase().trim()
        when (slug) {

            "hotel data".lowercase().trim() -> {
                navController.navigate(R.id.homeFragment_to_hotelstayFragment)
            }

            "Conference".lowercase().trim() -> {
                navController.navigate(R.id.homeFragment_to_conferenceFragment)
            }

            "lamplight".lowercase().trim() -> {
                navController.navigate(R.id.homeFragment_to_lamplightingFragment)
            }

            "weather".lowercase().trim() -> {
                navController.navigate(R.id.homeFragment_to_weatherFragment)
            }

            "conference".lowercase().trim() -> {
                navController.navigate(R.id.homeFragment_to_conferenceFragment)
            }

            "food".lowercase().trim() -> {
                val bundle = Bundle().apply {
                    putString("title", "food details")
                    putInt("imageRes", R.drawable.food_details)
                }
                navController.navigate(R.id.homeFragment_to_conferenceDetailFragment, bundle)
            }

            "camera".lowercase().trim() -> {
                navController.navigate(R.id.homeFragment_to_cameraFragment)
            }

            "stays".lowercase().trim() -> {
                navController.navigate(R.id.homeFragment_to_hotelstayFragment)
            }

            "localization".lowercase().trim() -> {
                navController.navigate(R.id.homeFragment_to_LocalizationFragment)
            }

            "notes".lowercase().trim() -> {
                navController.navigate(R.id.homeFragment_to_notesFragment)
            }

            "shuttle bus details".lowercase().trim() -> {
                val bundle = Bundle().apply {
                    putString("title", "SHUTTLE BUS")
                    putInt("imageRes", R.drawable.shuttle_bus_details_img)
                }
                navController.navigate(R.id.homeFragment_to_conferenceDetailFragment, bundle)
            }

            "details agenda".lowercase().trim() -> {
                val bundle = Bundle().apply {
                    putString("title", "details agenda")
                    putInt("imageRes", R.drawable.detailed_schedule_img)
                }
                navController.navigate(
                    R.id.action_home_notification_to_detailsAgenda,
                    bundle
                )
            }

            "guest gallivant".lowercase().trim() -> {
                val bundle = Bundle().apply {
                    putString("title", "guest gallivant")
                    putInt("imageRes", R.drawable.guest_gallivant)
                }
                navController.navigate(
                    R.id.action_home_notification_to_guestGallivant,
                    bundle
                )
            }


            "welcome".lowercase().trim() -> {
                navController.navigate(R.id.homeFragment_to_welcomeFragment)
            }

            "hotel & stay".lowercase().trim() -> {
                navController.navigate(R.id.homeFragment_to_hotelstayFragment)
            }

            "Travel & stay".lowercase().trim() -> {
                navController.navigate(R.id.homeFragment_to_travelandstayFragment)
            }

            "about doha".lowercase().trim() -> {
                Log.d("BannerClick", "Clicked title: ${slug}")
                navController.navigate(R.id.homeFragment_to_antalyaTour)
            }

            "conference details".lowercase().trim() -> {
                val bundle = Bundle().apply {
                    putString("title", "Conference Agenda")
                    putInt("imageRes", R.drawable.conference_agenda_img)
                }
                navController.navigate(R.id.homeFragment_to_conferenceDetailFragment, bundle)
            }
            /*  "shuttle bus" -> {
                  val bundle = Bundle().apply {
                      putString("title", "SHUTTLE BUS")
                      putInt("imageRes", R.drawable.shuttle_bus_details_img)
                  }
                  navController.navigate(R.id.homeFragment_to_conferenceDetailFragment,bundle)
              }*/
            "food details".lowercase().trim() -> {
                val bundle = Bundle().apply {
                    putString("title", "food details")
                    putInt("imageRes", R.drawable.food_details)
                }
                navController.navigate(R.id.homeFragment_to_conferenceDetailFragment, bundle)
            }

            /*"guest gallivant" -> {
                val bundle = Bundle().apply {
                    putString("title", "guest gallivant")
                    putInt("Details", R.drawable.guest_gallivant)
                }
                navController.navigate(
                    R.id.action_home_notification_to_guestGallivant,
                    bundle
                )
            }*/

            "speaker".lowercase().trim() -> {
                navController?.navigate(R.id.action_home_notification_to_speaker)
            }

            "seating plan".lowercase().trim() -> {
                navController?.navigate(R.id.action_home_notification_to_seatingPlan)
            }

            "dress code".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_dressCode)

            "conference agenda".lowercase().trim() -> {
                val bundle = Bundle().apply {
                    putString("title", "Conference Agenda")
                    putInt("imageRes", R.drawable.conference_agenda_img)
                }
                navController.navigate(
                    R.id.action_home_notification_to_conferenceAgenda,
                    bundle
                )
            }

            "gallery".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_gallery)

            "sc team".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_scTeam)

            "awards".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_awards)

            "digital exhibition".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_digitalExhibition)

            "emergency contact".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_emergencyContact)

            "msil contact".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_msilContact)

            "travel details".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_travelDetails)

            "shuttle bus details".lowercase().trim() -> {
                val bundle = Bundle().apply {
                    putString("title", "Shuttle Bus Details")
                    putInt("imageRes", R.drawable.shuttle_bus_details_img)
                }
                navController.navigate(
                    R.id.action_home_notification_to_shuttleBusDetails,
                    bundle
                )
            }

            "event location".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_eventLocation)
            /*    "weather" -> navController.navigate(R.id.action_home_notification_to_weather)*/
            "do's & dont".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_dosDont)

            "feedback".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_feedback)

            "entertainment".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_entertainment)

            "currency converter".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_currencyConverter)

            "msvc camera corner".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_msvcCameraCorner)

            "ask your question".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_askYourQuestion)

            "digital exhibition feedback".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_digitalExhibitionFeedback)
            /* "notes" -> navController.navigate(R.id.action_home_notification_to_notes)*/

            "lamp light".lowercase()
                .trim() -> navController.navigate(R.id.action_home_notification_to_lamplight)

            "notification".lowercase()
                .trim() -> navController.navigate(R.id.homeFragment_to_notificationFragment_home)

            else -> {
                // Optional: Log if slug is not handled
                Log.d("BannerClick", "Video slug: $slug")
            }


        }

    }

    private val autoScrollViewPager = object : Runnable {
        override fun run() {
            binding.imagesTop.let { viewPager ->
                val nextItem = (viewPager.currentItem + 1) % (imageAdapter.itemCount)
                viewPager.setCurrentItem(nextItem, true)
                handler.postDelayed(this, 3000) // Auto-scroll every 3 seconds
            }
        }
    }

    private fun startAutoScrollViewPager() {
        handler.postDelayed(autoScrollViewPager, 5000)
    }

    private val autoScrollRecyclerView = object : Runnable {
        override fun run() {
            val layoutManager = binding.subImagesTRecycler.layoutManager
            if (layoutManager is LinearLayoutManager) {
                val lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
                if (lastVisibleItem < homeAdapter.itemCount - 1) {
                    binding.subImagesTRecycler.smoothScrollToPosition(lastVisibleItem + 1)
                } else {
                    binding.subImagesTRecycler.smoothScrollToPosition(0)
                }
            }
            handler.postDelayed(this, 3000) // Scroll every 3 sec
        }
    }

    private fun startAutoScrollRecyclerView() {
        if (::homeAdapter.isInitialized && homeAdapter.itemCount > 1) {
            handler.postDelayed(autoScrollRecyclerView, 3000)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.notificationName.clearAnimation()
    }

    override fun onStop() {
        super.onStop()
        if (::imageAdapter.isInitialized) {
            imageAdapter.releaseAllPlayers()
        }
    }

    override fun onResume() {
        super.onResume()
        if (::imageAdapter.isInitialized) {
            imageAdapter.playPlayer()
        }
    }


    fun setNotificationFromApi(response: Generic<ResponseNotification>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()
                val newData = response.data.data
                    ?.filter {
                        it?.category.equals(
                            Preference.getUserCategory(),
                            ignoreCase = true
                        ) || it?.category.equals(
                            "All Categories",
                            ignoreCase = true
                        )
                    }
                    ?.mapNotNull { it } ?: emptyList()
                binding.notificationRecycler.layoutManager = LinearLayoutManager(requireContext())
                notifyAdaptor = NotificationAdapter(newData, object : OnNotificationClickListener {
                    override fun onNotificationClick(item: NotificationDataItem) {
                        when (item.pageName?.trim()?.lowercase()) {
                            "conference agenda".lowercase().trim() -> {
                                val bundle = Bundle().apply {
                                    putString("title", "Conference Agenda")
                                    putInt("imageRes", R.drawable.conference_agenda_img)
                                }
                                navController.navigate(
                                    R.id.action_home_notification_to_conferenceAgenda,
                                    bundle
                                )
                            }

                            "Conference".lowercase().trim() -> {
                                navController.navigate(R.id.homeFragment_to_conferenceFragment)
                            }

                            "speaker".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_speaker)

                            "seating plan".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_seatingPlan)

                            "dress code".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_dressCode)

                            "details agenda".lowercase().trim() -> {
                                val bundle = Bundle().apply {
                                    putString("title", "details agenda")
                                    putInt("imageRes", R.drawable.detailed_schedule_img)
                                }
                                navController.navigate(
                                    R.id.action_home_notification_to_detailsAgenda,
                                    bundle
                                )
                            }

                            "gallery".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_gallery)

                            "sc team".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_scTeam)

                            "awards".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_awards)

                            "digital exhibition".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_digitalExhibition)

                            "emergency contact".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_emergencyContact)

                            "msil contact".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_msilContact)

                            "about doha".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_aboutDoha)

                            "travel details".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_travelDetails)

                            "hotel & stay".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_hotelStay)

                            "shuttle bus details".lowercase().trim() -> {
                                val bundle = Bundle().apply {
                                    putString("title", "Shuttle Bus Details")
                                    putInt("imageRes", R.drawable.shuttle_bus_details_img)
                                }
                                navController.navigate(
                                    R.id.action_home_notification_to_shuttleBusDetails,
                                    bundle
                                )
                            }

                            "event location".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_eventLocation)

                            "guest gallivant".lowercase().trim() -> {
                                val bundle = Bundle().apply {
                                    putString("title", "guest gallivant")
                                    putInt("imageRes", R.drawable.guest_gallivant)
                                }
                                navController.navigate(
                                    R.id.action_home_notification_to_guestGallivant,
                                    bundle
                                )
                            }

                            "food details".lowercase().trim() -> {
                                val bundle = Bundle().apply {
                                    putString("title", "Food Details")
                                    putInt("imageRes", R.drawable.food_details)
                                }
                                navController.navigate(
                                    R.id.action_home_notification_to_foodDetails,
                                    bundle
                                )
                            }

                            "weather".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_weather)

                            "do's & dont".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_dosDont)

                            "feedback".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_feedback)

                            "entertainment".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_entertainment)

                            "currency converter".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_currencyConverter)

                            "welcome".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_welcome)

                            "msvc camera corner".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_msvcCameraCorner)

                            "ask your question".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_askYourQuestion)

                            "digital exhibition feedback".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_digitalExhibitionFeedback)

                            "notes".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_notes)

                            "lamp light".lowercase()
                                .trim() -> navController.navigate(R.id.action_home_notification_to_lamplight)

                            "notification".lowercase()
                                .trim() -> navController.navigate(R.id.homeFragment_to_notificationFragment_home)

                            else -> {
                                Log.d("Notification", "Unknown page: ${item.pageName}")
                                navController.navigate(R.id.homeFragment_to_notificationFragment_home)
                            }
                        }

                    }
                }, isFromHome = true)
                binding.notificationRecycler.adapter = notifyAdaptor
                binding.notificationRecycler.addItemDecoration(BottomMarginItemDecoration(15))

            }

            is Generic.Error -> {
                ProgressDialog.hideProgressDialog()
                Log.e("the home response", "" + response.message)
            }

            is Generic.Failure -> {
                ProgressDialog.hideProgressDialog()
                Log.e("the home response", "" + response.exception.message)
            }
        }
    }

}