package com.multitv.eventbuilder.ui.conference.conference_detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.constant.AppConstant
import com.multitv.eventbuilder.databinding.FragmentConferenceDetailBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.model.TimelineItem
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.conference.conference_detail.Adaptor.ShuttleDateAdapter
import com.multitv.eventbuilder.ui.conference.conference_detail.Adaptor.TimelineAdapter
import com.multitv.eventbuilder.ui.conference.conference_detail.viewmodel.ConferenceAgendaViewModel
import com.multitv.eventbuilder.ui.conference.conference_detail.viewmodelfactory.ConferenceAgendaViewModelFactory

class ConfrenceFragmentDetail : Fragment(), View.OnClickListener {

    private var _binding: FragmentConferenceDetailBinding? = null
    private val binding get() = _binding!!

    private var navController: NavController? = null
    private lateinit var adapter: TimelineAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConferenceDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("title") ?: "Title"
        val imageRes = arguments?.getInt("imageRes") ?: R.drawable.conference_agenda_img

        if (title.equals("Conference Agenda", ignoreCase = true)) {

            binding.backgroundImage.setImageResource(imageRes)

            val repository = Repo(RetrofitInstance.getRetrofit())
            val factory = ConferenceAgendaViewModelFactory(repository)
            val viewModel = ViewModelProvider(this, factory)[ConferenceAgendaViewModel::class.java]

            binding.dateRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.timelineRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            viewModel.getConferenceAgenda(AppConstant.CONFERENCEAGENDAAPI)

            viewModel.conferenceAgendaResponse.observe(viewLifecycleOwner) { result ->
                when (result) {

                    is Generic.Loading -> {
                        ProgressDialog.showProgresssDialog(requireContext())
                    }

                    is Generic.Success -> {

                        ProgressDialog.hideProgressDialog()

                        val response = result.data
                        if (response?.success == true && response.data.isNotEmpty()) {

                            binding.tittleText.text = response.pageTittle.toString().trim()

                            val dates = response.data.map { it.date }
                            val agendaMap = response.data.map { it.data }

                            val dateAdapter = ShuttleDateAdapter(dates) { selectedIndex ->
                                val items = agendaMap[selectedIndex].map {
                                    TimelineItem(it.title, it.startDate, it.description)
                                }
                                binding.timelineRecyclerView.adapter = TimelineAdapter(items, isConference = true)
                            }

                            binding.dateRecyclerView.adapter = dateAdapter

                            // Load first date by default
                            val firstItems = agendaMap.first().map {
                                TimelineItem(it.title, it.startDate, it.description)
                            }
                            binding.timelineRecyclerView.adapter = TimelineAdapter(firstItems, isConference = true)
                        }
                    }

                    is Generic.Error -> {
                        ProgressDialog.hideProgressDialog()
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    }

                    is Generic.Failure -> {
                        ProgressDialog.hideProgressDialog()
                        Toast.makeText(requireContext(), result.exception.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }

        if (title.equals("food details", ignoreCase = true)) {

            binding.backgroundImage.setImageResource(imageRes)

            val repository = Repo(RetrofitInstance.getRetrofit())
            val factory = ConferenceAgendaViewModelFactory(repository)
            val viewModel = ViewModelProvider(this, factory)[ConferenceAgendaViewModel::class.java]

            binding.dateRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.timelineRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            viewModel.getFoodPlanDetail(AppConstant.FOODPLANAPI)

            viewModel.foodPlanLiveData.observe(viewLifecycleOwner) { result ->
                when (result) {

                    is Generic.Loading -> {
                        ProgressDialog.showProgresssDialog(requireContext())
                    }

                    is Generic.Success -> {

                        ProgressDialog.hideProgressDialog()

                        val response = result.data
                        if (response?.success == true && response.data.isNotEmpty()) {

                            binding.tittleText.text = response.pageTittle.trim()

                            val dates = response.data.map { it.date }
                            val agendaMap = response.data.map { it.data }

                            val dateAdapter = ShuttleDateAdapter(dates) { selectedIndex ->
                                val items = agendaMap[selectedIndex].map {
                                    TimelineItem(it.title, it.time, it.about)
                                }
                                binding.timelineRecyclerView.adapter = TimelineAdapter(items , isDetail= true)
                            }

                            binding.dateRecyclerView.adapter = dateAdapter

                            // Load first date by default
                            val firstItems = agendaMap.first().map {
                                TimelineItem(it.title, it.time, it.about)
                            }
                            binding.timelineRecyclerView.adapter = TimelineAdapter(firstItems , isDetail =  true)
                        }
                    }

                    is Generic.Error -> {
                        ProgressDialog.hideProgressDialog()
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    }

                    is Generic.Failure -> {
                        ProgressDialog.hideProgressDialog()
                        Toast.makeText(requireContext(), result.exception.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }

        if (title.equals("details agenda", ignoreCase = true) || title.equals("detailed agenda", ignoreCase = true)) {

            binding.backgroundImage.setImageResource(imageRes)

            val repository = Repo(RetrofitInstance.getRetrofit())
            val factory = ConferenceAgendaViewModelFactory(repository)
            val viewModel = ViewModelProvider(this, factory)[ConferenceAgendaViewModel::class.java]

            binding.dateRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.timelineRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            viewModel.getDetailAgendaData(AppConstant.DETAILAGENDAAPI)

            viewModel.detailAgendaResponse.observe(viewLifecycleOwner) { result ->
                when (result) {

                    is Generic.Loading -> {
                        ProgressDialog.showProgresssDialog(requireContext())
                    }
                    is Generic.Success -> {

                        ProgressDialog.hideProgressDialog()

                        val response = result.data
                        if (response?.success == true && response.data.isNotEmpty()) {

                            binding.tittleText.text = response.pageTittle.toString().trim()

                            val dates = response.data.map { it.date }
                            val agendaMap = response.data.map { it.data }

                            val dateAdapter = ShuttleDateAdapter(dates) { selectedIndex ->
                                val items = agendaMap[selectedIndex].map {
                                    TimelineItem(it.title, it.scheduleTime, it.place)
                                }
                                binding.timelineRecyclerView.adapter = TimelineAdapter(items, isDetailAgenda = true)
                            }

                            binding.dateRecyclerView.adapter = dateAdapter

                            // Load first agenda by default
                            val firstItems = agendaMap.first().map {
                                TimelineItem(it.title, it.scheduleTime, it.place)
                            }
                            binding.timelineRecyclerView.adapter = TimelineAdapter(firstItems,isDetailAgenda = true)
                        }
                    }

                    is Generic.Error -> {
                        ProgressDialog.hideProgressDialog()
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    }

                    is Generic.Failure -> {
                        ProgressDialog.hideProgressDialog()
                        Toast.makeText(requireContext(), result.exception.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }

        if (title.equals("shuttle bus details", ignoreCase = true) || title.equals("SHUTTLE BUS", ignoreCase = true)) {
            binding.backgroundImage.setImageResource(imageRes)

            val repository = Repo(RetrofitInstance.getRetrofit())
            val factory = ConferenceAgendaViewModelFactory(repository)
            val viewModel = ViewModelProvider(this, factory)[ConferenceAgendaViewModel::class.java]

            binding.dateRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.timelineRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            viewModel.getShuttleBusDetail(AppConstant.SHUTTLEBUSDETAILAPI)

            viewModel.shuttleBusDetail.observe(viewLifecycleOwner) { result ->

                when (result) {
                    is Generic.Loading -> {
                        ProgressDialog.showProgresssDialog(requireContext())
                    }

                    is Generic.Success -> {

                        ProgressDialog.hideProgressDialog()

                        val response = result.data
                        if (response?.success == true && response.data.isNotEmpty()) {

                            binding.tittleText.text = response.pageTittle.toString().trim()

                            val dates = response.data.map { it.date }
                            val agendaMap = response.data.map { it.data }

                            val dateAdapter = ShuttleDateAdapter(dates) { selectedIndex ->
                                val items = agendaMap[selectedIndex].map { it ->
                                    TimelineItem(it.title, it.time, it.about)
                                }
                                binding.timelineRecyclerView.adapter = TimelineAdapter(items, isShuttle = true)
                            }

                            binding.dateRecyclerView.adapter = dateAdapter

                            // Load first date by default
                            val firstItems = agendaMap.first().map { it ->
                                TimelineItem(it.title, it.time, it.about)
                            }
                            binding.timelineRecyclerView.adapter = TimelineAdapter(firstItems, isShuttle = true)
                        }
                    }

                    is Generic.Error -> {
                        ProgressDialog.hideProgressDialog()
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    }

                    is Generic.Failure -> {

                        ProgressDialog.hideProgressDialog()

                        Toast.makeText(
                            requireContext(),
                            result.exception.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        if (title.equals("Socio-cultural Experience", ignoreCase = true)) {

            binding.backgroundImage.setImageResource(imageRes)
            val repository = Repo(RetrofitInstance.getRetrofit())
            val factory = ConferenceAgendaViewModelFactory(repository)
            val viewModel = ViewModelProvider(this, factory)[ConferenceAgendaViewModel::class.java]

            binding.dateRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.timelineRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            viewModel.getSpouseTourResponse(AppConstant.SPOUSETOURAPI)

            viewModel.spouseTourResponse.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Generic.Loading -> {
                        ProgressDialog.showProgresssDialog(requireContext())
                    }

                    is Generic.Success -> {
                        ProgressDialog.hideProgressDialog()
                        val response = result.data
                        if (response?.success == true && response.data.isNotEmpty()) {

                            binding.tittleText.text = response.pageTittle.toString().trim()
                            val dates = response.data.map { it.date }
                            val agendaMap = response.data.map { it.data }

                            val dateAdapter = ShuttleDateAdapter(dates) { selectedIndex ->
                                val items = agendaMap[selectedIndex].map {
                                    TimelineItem(it.title, it.time, it.about)
                                }
                                binding.timelineRecyclerView.adapter = TimelineAdapter(items, isSocial =  true)
                            }

                            binding.dateRecyclerView.adapter = dateAdapter

                            // Load first date by default
                            val firstItems = agendaMap.first().map {
                                TimelineItem(it.title, it.time, it.about)
                            }
                            binding.timelineRecyclerView.adapter = TimelineAdapter(firstItems, isSocial = true)
                        }
                    }

                    is Generic.Error -> {
                        ProgressDialog.hideProgressDialog()
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    }

                    is Generic.Failure -> {
                        ProgressDialog.hideProgressDialog()
                        Toast.makeText(requireContext(), result.exception.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        binding.backArrow.setOnClickListener(this)
        binding.timelineRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        /*val items = listOf(
            TimelineItem("Lorem lepsum", "10:00 AM", "Hall A"),
            TimelineItem("Lorem lepsum", "11:00 AM", "Hall B"),
            TimelineItem("Lorem lepsum", "12:00 PM", "Hall C"),
            TimelineItem("Lorem lepsum", "01:00 PM", "Hall D"),
            TimelineItem("Lorem lepsum", "01:00 PM", "Hall D"),
            TimelineItem("Lorem lepsum", "01:00 PM", "Hall D"),
            TimelineItem("Lorem lepsum", "01:00 PM", "Hall D"),
            TimelineItem("Lorem lepsum", "01:00 PM", "Hall D"),
            TimelineItem("Lorem lepsum", "01:00 PM", "Hall D")
        )*/

        /*adapter = TimelineAdapter(items)
        binding.timelineRecyclerView.adapter = adapter
*/
//        binding.backButton.setOnClickListener {
//            requireActivity().onBackPressedDispatcher.onBackPressed()
//        }
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