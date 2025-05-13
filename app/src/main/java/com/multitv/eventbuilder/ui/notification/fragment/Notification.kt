package com.multitv.eventbuilder.ui.notification.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.constant.AppConstant
import com.multitv.eventbuilder.constant.Preference
import com.multitv.eventbuilder.databinding.FragmentNotificationBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.model.NotificationDataItem
import com.multitv.eventbuilder.model.ResponseNotification
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.home.adaptor.NotificationAdapter
import com.multitv.eventbuilder.ui.home.adaptor.OnNotificationClickListener
import com.multitv.eventbuilder.ui.home.viewmodel.HomeViewModel
import com.multitv.eventbuilder.ui.home.viewmodelfactory.HomeViewModelFactory
import com.multitv.eventbuilder.utils.BottomMarginItemDecoration

class Notification : Fragment(), OnClickListener {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var viewModel: HomeViewModel
    private lateinit var notifyAdaptor: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]

        binding.backArrow.setOnClickListener(this)

        viewModel.notificationDataResponse.observe(viewLifecycleOwner) { response ->
            setDataFromApi(response)
        }

        getHomeDataFromApi()


    }

    fun getHomeDataFromApi() {
        viewModel.getHomeData(AppConstant.NOTIFICATIONAPI)
    }

    fun setDataFromApi(response: Generic<ResponseNotification>) {
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

                binding.notificationRecyclerHome.layoutManager =
                    LinearLayoutManager(requireContext())

                notifyAdaptor = NotificationAdapter(newData, object : OnNotificationClickListener {
                    override fun onNotificationClick(item: NotificationDataItem) {

                        when (item.pageName?.trim()?.lowercase()) {

                            "conference agenda".lowercase().trim() -> {
                                val bundle = Bundle().apply {
                                    putString("title", "Conference Agenda")
                                    putInt("imageRes", R.drawable.conference_agenda_img)
                                }
                                navController.navigate(
                                    R.id.action_notification_to_conferenceAgenda,
                                    bundle
                                )
                            }

                            "Conference".lowercase().trim() -> {
                                navController.navigate(R.id.action_notification_to_conference)
                            }

                            "speaker".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_speaker)

                            "seating plan".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_seatingPlan)

                            "dress code".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_dressCode)

                            "details agenda".lowercase().trim() -> {
                                val bundle = Bundle().apply {
                                    putString("title", "details agenda")
                                    putInt("imageRes", R.drawable.detailed_schedule_img)
                                }
                                navController.navigate(
                                    R.id.action_notification_to_detailsAgenda,
                                    bundle
                                )
                            }

                            "gallery".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_gallery)

                            "sc team".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_scTeam)

                            "awards".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_awards)

                            "digital exhibition".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_digitalExhibition)

                            "emergency contact".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_emergencyContact)

                            "msil contact".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_msilContact)

                            "about doha".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_aboutDoha)

                            "travel details".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_travelDetails)

                            "hotel & stay".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_hotelStay)

                            "shuttle bus details".lowercase().trim() -> {
                                val bundle = Bundle().apply {
                                    putString("title", "shuttle bus details")
                                    putInt("imageRes", R.drawable.shuttle_bus_details_img)
                                }
                                navController.navigate(
                                    R.id.action_notification_to_shuttleBusDetails,
                                    bundle
                                )
                            }

                            "event location".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_eventLocation)

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
                                    putString("title", "food details")
                                    putInt("imageRes", R.drawable.food_details)
                                }
                                navController.navigate(
                                    R.id.action_notification_to_foodDetails,
                                    bundle
                                )
                            }

                            "weather".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_weather)

                            "do's & dont".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_dosDont)

                            "feedback".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_feedback)

                            "entertainment".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_entertainment)

                            "currency converter".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_currencyConverter)

                            "welcome".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_welcome)

                            "msvc camera corner".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_msvcCameraCorner)

                            "ask your question".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_askYourQuestion)

                            "digital exhibition feedback".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_digitalExhibitionFeedback)

                            "notes".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_notes)

                            "lamp light".lowercase()
                                .trim() -> navController.navigate(R.id.action_notification_to_lamp)

                          /*  "notification".lowercase()
                                .trim() -> navController.navigate(R.id.homeFragment_to_notificationFragment_home)
*/
                            else -> {
                                Log.d("Notification", "Unknown page: ${item.pageName}")
                               // navController.navigate(R.id.homeFragment_to_notificationFragment_home)
                            }
                        }

                    }
                })
                binding.notificationRecyclerHome.adapter = notifyAdaptor
                binding.notificationRecyclerHome.addItemDecoration(BottomMarginItemDecoration(15))


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
            R.id.backArrow -> {
                navController.navigateUp()
            }


        }

    }
}