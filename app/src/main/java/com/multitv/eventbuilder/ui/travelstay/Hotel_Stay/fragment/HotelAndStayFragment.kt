package com.multitv.eventbuilder.ui.travelstay.Hotel_Stay.fragment

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
import com.multitv.eventbuilder.databinding.FragmentHotelstayBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.travelstay.Hotel_Stay.adaptor.HotelAdapter
import com.multitv.eventbuilder.model.hotelstaymodel.model.HotelStayItem
import com.multitv.eventbuilder.model.hotelstaymodel.model.HotelStayResponse
import com.multitv.eventbuilder.ui.travelstay.Hotel_Stay.viewmodel.HotelStayViewModel
import com.multitv.eventbuilder.ui.travelstay.Hotel_Stay.viewmodelfactory.HotelStayViewModelFactory

class HotelAndStayFragment : Fragment() , HotelAdapter.OnItemClickListener, View.OnClickListener{

    private var _binding: FragmentHotelstayBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HotelAdapter
    private lateinit var navController: NavController
    private lateinit var viewModel: HotelStayViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHotelstayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = HotelStayViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[HotelStayViewModel::class.java]

        navController = Navigation.findNavController(requireActivity(),R.id.nav_host)
        binding.backArrow.setOnClickListener(this)

        viewModel.hotelStayResponse.observe(viewLifecycleOwner) { response ->
            setData(response)
        }
        getDataFromApi()
      /*  setUi()*/
    }

    private fun getDataFromApi() {
        viewModel.getHotelStayData(AppConstant.HOTELSTAY)
    }

    private fun setData(response: Generic<HotelStayResponse>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()

                val dataList = response.data.data  // Extract the list from the response

                if (dataList.isNullOrEmpty()) {
                    binding.hotelRecycler.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                } else {
                    binding.tvNoData.visibility = View.GONE
                    binding.hotelRecycler.visibility = View.VISIBLE
                    binding.title.text = response.data.pageTittle.trim()
                    adapter = HotelAdapter(dataList, this)
                    binding.hotelRecycler.layoutManager = LinearLayoutManager(requireContext())
                    binding.hotelRecycler.adapter = adapter
                }
            }

            is Generic.Error -> {
                ProgressDialog.hideProgressDialog()
                Toast.makeText(requireContext(), "Error: ${response.message}", Toast.LENGTH_SHORT).show()
            }

            is Generic.Failure -> {
                ProgressDialog.hideProgressDialog()
                Toast.makeText(requireContext(), "Failure: ${response.exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }



    /* fun setUi(){
         hotelList = listOf(
             HotelItem(
                 R.drawable.sheraton_grand_img,
                 "SHERATON GRAND",
                 "DOHA",
                 "Since 1982, Sheraton Grand Doha has been a pioneer.",
                 "Stay1"
             ),
             HotelItem(
                 R.drawable.hotel2_img,
                 "SHERATON GRAND NEW",
                 "DOHA",
                 "Since 1982, Sheraton Grand Doha has been a pioneer.",
                 "Stay2"
             )
         )

         adapter = HotelAdapter(hotelList, this)
         binding.hotelRecycler.layoutManager = LinearLayoutManager(requireContext())
         binding.hotelRecycler.adapter = adapter
     }*/

    override fun onDirectionClick(item: HotelStayItem) {
        /*Toast.makeText(requireContext(), item.name, Toast.LENGTH_SHORT).show()*/

        if (item.name.equals("Hotel W", ignoreCase = true)) {
            val bundle = Bundle().apply {
                putString("name", item.name)
                putString("about", item.about)
                putString("description", item.description)
                putString("image", item.image) // Assuming it's a URL
                putString("location", item.location)
                putString("link", item.link)
                putParcelableArrayList("contact_list", ArrayList(item.contactInfo))
            }
            navController.navigate(R.id.hotelstayFragment_to_aboutHotelFragment,bundle)
        }

        if (item.name.equals("Sheraton Grand", ignoreCase = true)) {
            val bundle = Bundle().apply {
                putString("name", item.name)
                putString("about", item.about)
                putString("description", item.description)
                putString("image", item.image) // Assuming it's a URL
                putString("location", item.location)
                putString("link", item.link)
                putParcelableArrayList("contact_list", ArrayList(item.contactInfo))
            }
            navController.navigate(R.id.hotelstayFragment_to_aboutHotelFragment,bundle)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.backArrow -> {
                navController.navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}