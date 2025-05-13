package com.multitv.eventbuilder.ui.conference.ourspeakers.fragment

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.constant.AppConstant
import com.multitv.eventbuilder.databinding.FragmentOurSpeakersBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic

import com.multitv.eventbuilder.ui.conference.ourspeakers.adaptor.OurSpeakerAdaptor
import com.multitv.eventbuilder.model.ourspeaker.model.OurSpeakerResponse
import com.multitv.eventbuilder.model.ourspeaker.model.SpeakerItem
import com.multitv.eventbuilder.ui.conference.ourspeakers.viewmodel.OurSpeakerViewModel
import com.multitv.eventbuilder.ui.conference.ourspeakers.viewmodelfactory.OurSpeakerViewModelFactory
import com.multitv.eventbuilder.ui.dialog_fragment.NoteDialogFragment

class OurSpeakerFragment : Fragment(), OurSpeakerAdaptor.OnClickItemListener, View.OnClickListener {

    private var _binding: FragmentOurSpeakersBinding? = null
    private val binding get() = _binding!!

    private var navController : NavController? = null
    private lateinit var viewModel : OurSpeakerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOurSpeakersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(),R.id.nav_host)
        binding.backArrow.setOnClickListener(this)

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = OurSpeakerViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[OurSpeakerViewModel::class.java]

        viewModel.ourSpeakerResponse.observe(viewLifecycleOwner) { response ->
            setData(response)
        }

        getDataFromApi()

      /*  setUi()*/
    }

    private fun getDataFromApi() {
        viewModel.getSpeakerData(AppConstant.NEWSPEAKERAPI)
    }

    private fun setData(response: Generic<OurSpeakerResponse>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()
                val speakerList = response.data.data

                binding.tittle.text = response.data.pageTittle.trim()

                binding.speakerRecycler.layoutManager = LinearLayoutManager(requireContext())
                binding.speakerRecycler.adapter = OurSpeakerAdaptor(speakerList,this)
            }

            is Generic.Error -> {
                ProgressDialog.hideProgressDialog()
                Log.e("Conference Error", "Message: ${response.message}")
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            is Generic.Failure -> {
                ProgressDialog.hideProgressDialog()
                Log.e("Conference Failure", "Exception: ${response.exception.message}")
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*fun setUi(){
        val speakerList = listOf(
            OurSpeakerItem(R.drawable.speakerimg, "Pankaj Kumar", "Software Engineer", "Google"),
            OurSpeakerItem(R.drawable.speaker2, "John Doe", "Product Manager", "Microsoft"),
            OurSpeakerItem(R.drawable.speakerimg, "Jane Smith", "Data Scientist", "Amazon"),
            OurSpeakerItem(R.drawable.speaker2, "Alice Johnson", "UX Designer", "Meta")
        )

        binding.speakerRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.speakerRecycler.adapter = OurSpeakerAdaptor(speakerList,this)
    }*/

   /* override fun onClickItem(item: OurSpeakerItem) {
       Toast.makeText(requireContext(),item.name,Toast.LENGTH_SHORT).show()
        *//*when(item.name){
            "Pankaj Kumar" ->{
                val bundle = Bundle().apply {
                    putParcelable("speakerItem",item)
                }
                navController?.navigate(R.id.ourSpeakerFragment_to_speakerFragment,bundle)

            }
        }*//*
    }*/

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backArrow -> {
                navController?.navigateUp()
            }
        }
    }

    override fun onNoteClicked(item: SpeakerItem) {
        val dialog = NoteDialogFragment.newInstance(item.id,item.name)
        dialog.show(parentFragmentManager, "NoteDialog")
    }
}