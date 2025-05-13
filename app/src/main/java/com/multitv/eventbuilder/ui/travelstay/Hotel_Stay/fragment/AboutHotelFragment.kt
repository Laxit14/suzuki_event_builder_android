package com.multitv.eventbuilder.ui.travelstay.Hotel_Stay.fragment


import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.databinding.FragmentAboutHotelBinding
import com.multitv.eventbuilder.ui.travelstay.Hotel_Stay.adaptor.PhoneNumberAdapterNew
import com.multitv.eventbuilder.model.hotelstaymodel.model.ContactInfo

class AboutHotelFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentAboutHotelBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PhoneNumberAdapterNew
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutHotelBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(),R.id.nav_host)
        binding.backArrow.setOnClickListener(this)

        val name = arguments?.getString("name") ?: ""
        val about = arguments?.getString("about") ?: ""
        val description = arguments?.getString("description") ?: ""
        val image = arguments?.getString("image") ?: ""
        val location = arguments?.getString("location") ?: ""
        val link = arguments?.getString("link") ?: ""
        val contactList = arguments?.getParcelableArrayList<ContactInfo>("contact_list")
        if (!contactList.isNullOrEmpty()) {
            setUi(contactList)
        }

        binding.mapAddress.paintFlags = binding.mapAddress.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        binding.hotelName.text = name
        binding.address.text = about
        binding.contentDescription.text = description

        Glide.with(requireContext())
            .load(image)
            .into(binding.backgroundImage)

        binding.mapAddress.setOnClickListener {
            if (link.isNotBlank()) {

                Log.e("link",""+link)
                try {
                    val uri = Uri.parse(link)

                    // Optional: Add this if you're expecting only maps links and want to ensure the scheme
                    if (uri.scheme == "http" || uri.scheme == "https" || uri.scheme == "geo") {
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Invalid link", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "No map link available", Toast.LENGTH_SHORT).show()
            }
        }

    }


    fun setUi(contactList: List<ContactInfo>){
       /* val phoneNumbers = listOf(
            PhoneNumberItem("Call Center", "4444"),
            PhoneNumberItem("Switchboard", "9"),
            PhoneNumberItem("Reception", "1000"),
            PhoneNumberItem("Doctor's Office", "6268"),
            PhoneNumberItem("Guest Relations", "2000"),
            PhoneNumberItem("Room Service", "6210")
        )*/

        binding.listOfNUmberRecycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = PhoneNumberAdapterNew(contactList)
        binding.listOfNUmberRecycler.adapter = adapter
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.backArrow -> {
                navController.navigateUp()
            }
        }
    }

}