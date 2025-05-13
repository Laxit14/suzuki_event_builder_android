package com.multitv.eventbuilder.ui.travelstay.eventlocation.fragment

import android.content.Intent
import android.net.Uri
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
import com.multitv.eventbuilder.databinding.FragmentEventlocationBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.model.eventlocation.model.EventLocationResponse
import com.multitv.eventbuilder.model.eventlocation.model.Segment
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.travelstay.eventlocation.adaptor.TripAdapter
import com.multitv.eventbuilder.ui.travelstay.eventlocation.viewmodel.EventLocationViewModel
import com.multitv.eventbuilder.ui.travelstay.eventlocation.viewmodelfactory.EventLocationViewModelFactory
import com.multitv.eventbuilder.utils.BottomMarginItemDecoration

class EventLocationFragment : Fragment(), View.OnClickListener, TripAdapter.OnclickImageInterface {

    private var _binding: FragmentEventlocationBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var adapter: TripAdapter

    private lateinit var viewModel: EventLocationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventlocationBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        binding.backArrow.setOnClickListener(this)

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = EventLocationViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[EventLocationViewModel::class.java]

        viewModel.eventLocationLiveData.observe(viewLifecycleOwner) { response ->
            setData(response)
        }
        getDataFromApi()

        /* setUi()*/
    }


    private fun getDataFromApi() {
        viewModel.getEventResponseData(AppConstant.EVENTlOCATIONAPI)

    }


    private fun setData(response: Generic<EventLocationResponse>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()
                val segmentList = response.data.data.eventLocation.segments

                binding.tittle.text = response.data.pageTittle.trim()

                binding.eventLocationRecycler.layoutManager = LinearLayoutManager(requireContext())
                adapter = TripAdapter(segmentList, this@EventLocationFragment)
                binding.eventLocationRecycler.adapter = adapter
                binding.eventLocationRecycler.addItemDecoration(BottomMarginItemDecoration(30))


            }

            is Generic.Error -> {
                ProgressDialog.hideProgressDialog()
                Toast.makeText(requireContext(), "Error: ${response.message}", Toast.LENGTH_SHORT)
                    .show()
            }

            is Generic.Failure -> {
                ProgressDialog.hideProgressDialog()
                Toast.makeText(
                    requireContext(),
                    "Failure: ${response.exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
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

    override fun OnclickImage(item : Segment) {
        val link = item.link
        if (!link.isNullOrEmpty() && (link.startsWith("http") && link.contains("maps"))) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            }
    }
}