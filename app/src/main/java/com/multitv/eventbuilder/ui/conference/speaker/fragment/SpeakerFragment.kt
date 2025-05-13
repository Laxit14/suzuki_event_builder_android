package com.multitv.eventbuilder.ui.conference.speaker.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.databinding.FragmentOurSpeakersBinding
import com.multitv.eventbuilder.databinding.FragmentSpeakerBinding
import com.multitv.eventbuilder.model.OurSpeakerItem

class SpeakerFragment: Fragment(), View.OnClickListener {

    private var _binding: FragmentSpeakerBinding? = null
    private val binding get() = _binding!!

    private var speakerItem: OurSpeakerItem? = null

    private var navController : NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSpeakerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        binding.backArrow.setOnClickListener(this)
        setUi()
    }

    fun setUi(){

//        val speakerList = listOf(
//            OurSpeakerItem(R.drawable.man, "Pankaj Kumar", "Software Engineer", "Google"),
//            OurSpeakerItem(R.drawable.man, "John Doe", "Product Manager", "Microsoft"),
//            OurSpeakerItem(R.drawable.man, "Jane Smith", "Data Scientist", "Amazon"),
//            OurSpeakerItem(R.drawable.man, "Alice Johnson", "UX Designer", "Meta")
//        )

        speakerItem = arguments?.getParcelable("speakerItem")
        speakerItem.let {
            binding.itemSpeaker.speakerName.text  = speakerItem?.name
            binding.itemSpeaker.speakerCompany.text = speakerItem?.company
            binding.itemSpeaker.speakerPosition.text = speakerItem?.position
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