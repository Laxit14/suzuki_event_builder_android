package com.multitv.eventbuilder.ui.travelstay.travelstayContent.fragment

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.constant.AppConstant.ABOUT_DOHAS
import com.multitv.eventbuilder.constant.AppConstant.DOS_AND_DONTS
import com.multitv.eventbuilder.constant.AppConstant.EVENT_LOCATION
import com.multitv.eventbuilder.constant.AppConstant.FOOD_DETAILS
import com.multitv.eventbuilder.constant.AppConstant.GUEST_GALLIVANT
import com.multitv.eventbuilder.constant.AppConstant.HOTEL_AND_STAY
import com.multitv.eventbuilder.constant.AppConstant.SHUTTLE_BUS_DETAILS
import com.multitv.eventbuilder.constant.AppConstant.TRAVEL_DETAILS
import com.multitv.eventbuilder.constant.AppConstant.WEATHER
import com.multitv.eventbuilder.constant.Preference
import com.multitv.eventbuilder.databinding.FragmentTravelstayContentBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.travelstay.travelstayContent.adaptor.TravelAdapter
import com.multitv.eventbuilder.model.travelstay.model.TravelInfoItem
import com.multitv.eventbuilder.ui.travelstay.travelstayContent.viewmodel.TravelAndStayViewModel
import com.multitv.eventbuilder.ui.travelstay.travelstayContent.viewmodelfactory.TravelAndStayViewModelFactory

class TravelStayFragment : Fragment(), OnClickListener, TravelAdapter.OnItemClickListener {

    private var _binding: FragmentTravelstayContentBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var adapter: TravelAdapter

    private lateinit var viewModel: TravelAndStayViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelstayContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = TravelAndStayViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TravelAndStayViewModel::class.java]

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        binding.backArrow.setOnClickListener(this)

        observeViewModel()

        val hotelStayId = 10 // example parent ID or from Preference.getHotelStayId()
        viewModel.getTravelAndStayData(hotelStayId, Preference.getUserCategory())
        Log.e("travel api", "checking call again and again")


        /*    setUi()*/

    }

    private fun observeViewModel() {
        viewModel.travelAndStayResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Generic.Loading -> {
                    ProgressDialog.showProgresssDialog(requireContext()) // show loader
                }

                is Generic.Success -> {
                    ProgressDialog.hideProgressDialog() // hide loader
                    val travelItems = response.data.data

                    binding.tittle.text = response.data.pageTittle.trim()

                    // ✅ Handle your data here (show in RecyclerView etc.)
                    Log.e("TravelStayFragment", "Fetched ${travelItems.size} items")



                    adapter = TravelAdapter(travelItems, this)
                    binding.travelRecycler.layoutManager = LinearLayoutManager(requireContext())
                    binding.travelRecycler.adapter = adapter
                }

                is Generic.Error -> {
                    ProgressDialog.hideProgressDialog()
                    Toast.makeText(
                        requireContext(),
                        "Error: ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Generic.Failure -> {
                    ProgressDialog.hideProgressDialog()
                    Toast.makeText(
                        requireContext(),
                        "Failure: ${response.exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()

                    Log.e("travelStay Failure", "Exception: ${response.exception.message}")
                }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // ✅ Prevent memory leaks
    }

    override fun onItemClick(item: TravelInfoItem) {
        val title = item.slug.lowercase()

        /* Toast.makeText(requireContext(), "Clicked: ${item.title}", Toast.LENGTH_SHORT).show()*/

        when (title.lowercase().trim()) {
            WEATHER.lowercase()
                .trim() -> navController.navigate(R.id.travelFragment_to_weatherFragment)

            HOTEL_AND_STAY.lowercase()
                .trim() -> navController.navigate(R.id.travelFragment_to_hotelstayFragment)

            "must know" -> navController.navigate(R.id.travelFragment_to_mustKnowFragment)
            ABOUT_DOHAS.lowercase()
                .trim() -> navController.navigate(R.id.travelFragment_to_aboutDohaFragment)

            EVENT_LOCATION.lowercase()
                .trim() -> navController.navigate(R.id.travelFragment_to_eventLocationFragment)

            TRAVEL_DETAILS.lowercase()
                .trim() -> navController.navigate(R.id.travelFragment_to_travelDetail)

            GUEST_GALLIVANT.lowercase().trim() -> {
                val bundle = Bundle().apply {
                    putString("title", item.title.lowercase())
                    putInt("imageRes", R.drawable.guest_gallivant)
                }
                navController.navigate(R.id.travelFragment_to_conferenceDetailFragment, bundle)
            }

            SHUTTLE_BUS_DETAILS.lowercase().trim() -> {
                val bundle = Bundle().apply {
                    putString("title", item.title.lowercase())
                    putInt("imageRes", R.drawable.shuttle_bus_details_img)
                }
                navController.navigate(R.id.travelFragment_to_conferenceDetailFragment, bundle)
            }

            FOOD_DETAILS.lowercase().trim() -> {
                val bundle = Bundle().apply {
                    putString("title", item.title.lowercase())
                    putInt("imageRes", R.drawable.food_details)
                }
                navController.navigate(R.id.travelFragment_to_conferenceDetailFragment, bundle)
            }

            DOS_AND_DONTS.lowercase().trim() -> {
                navController.navigate(R.id.travelFragment_to_do_donotFragmet)
            }

            else -> {
               /* Toast.makeText(
                    requireContext(),
                    "No navigation mapped for: ${item.title}",
                    Toast.LENGTH_SHORT
                ).show()*/
            }
        }
    }

}