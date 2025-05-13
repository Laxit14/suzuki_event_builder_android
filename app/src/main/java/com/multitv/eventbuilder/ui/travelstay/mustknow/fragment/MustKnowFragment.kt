package com.multitv.eventbuilder.ui.travelstay.mustknow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.databinding.FragmentAboutHotelBinding
import com.multitv.eventbuilder.databinding.FragmentMustknowBinding

class MustKnowFragment  : Fragment(), View.OnClickListener {

//    private var _binding =
     private var _binding: FragmentMustknowBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMustknowBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(),R.id.nav_host)
        binding.backArrow.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.backArrow -> {
                navController.navigateUp()
            }
        }
    }
}