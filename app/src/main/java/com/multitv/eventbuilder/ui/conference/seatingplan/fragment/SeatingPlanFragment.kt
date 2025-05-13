package com.multitv.eventbuilder.ui.conference.seatingplan.fragment

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
import com.multitv.eventbuilder.constant.Preference
import com.multitv.eventbuilder.databinding.FragmentSeatingPlanBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.seatingplanmodel.model.SeatResponse
import com.multitv.eventbuilder.ui.conference.seatingplan.viewmodel.SeatPlanViewModel
import com.multitv.eventbuilder.ui.conference.seatingplan.viewmodelfactory.SeatingPlanViewModelFactory

class SeatingPlanFragment : Fragment(), View.OnClickListener {


    private var _binding: FragmentSeatingPlanBinding? = null
    private val binding get() = _binding!!

    private var navController: NavController? = null

    private lateinit var viewModel: SeatPlanViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSeatingPlanBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Preference.init(requireContext())

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        binding.backArrow.setOnClickListener(this)

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = SeatingPlanViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[SeatPlanViewModel::class.java]

        viewModel.seatPlanResponse.observe(viewLifecycleOwner) { response ->
            setData(response)
        }
        getDataFromApi()
    }

    private fun getDataFromApi() {

        val userId = Preference.getUserId().toString()

        if (!userId.isNullOrEmpty()) {
            Log.e("userId", userId)
            viewModel.getSeatNo(AppConstant.SEATINPLAN, userId.toString())
        } else {
            Log.e("userId", "User ID is null or empty")
            Toast.makeText(requireContext(), "User ID not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setData(response: Generic<SeatResponse>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()
                val data = response.data

                if (data.success) {
                    if (!data.data.firstOrNull()?.seat_no.isNullOrEmpty()) {
                        val seatNumber = data.data.firstOrNull()?.seat_no ?: "N/A"
                        binding.seatNumber.text = getString(R.string.your_seat_number, seatNumber)

                        binding.seatNumber.visibility = View.VISIBLE
                        Glide.with(requireContext())
                            .load(data.data.firstOrNull()?.image6) // 🔹 Assuming file is a URL
                            .into(binding.seatImage)

                        binding.title.text = response.data.pageTittle.trim()
                        binding.noDataFound.visibility = View.GONE
                    } else {
                        binding.noDataFound.visibility = View.VISIBLE
                        binding.seatNumber.visibility = View.GONE
                    }
                } else {
                    binding.noDataFound.visibility = View.VISIBLE
                    binding.seatNumber.visibility = View.GONE
                }
            }

            is Generic.Error -> {
                binding.noDataFound.visibility = View.VISIBLE
                binding.seatNumber.visibility = View.GONE
                ProgressDialog.hideProgressDialog()
                Toast.makeText(requireContext(), "Error: ${response.message}", Toast.LENGTH_SHORT)
                    .show()
            }

            is Generic.Failure -> {
                binding.noDataFound.visibility = View.VISIBLE
                binding.seatNumber.visibility = View.GONE
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
                navController?.navigateUp()
            }
        }
    }
}