package com.multitv.eventbuilder.ui.mynotes.fragment

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
import com.multitv.eventbuilder.constant.Preference
import com.multitv.eventbuilder.databinding.FragmentMynoteBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.model.mynotesmodel.model.MYNotesResponse
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.mynotes.adaptor.MyNotesAdaptor
import com.multitv.eventbuilder.ui.mynotes.dialog.AddNotesDialogFragment
import com.multitv.eventbuilder.ui.mynotes.viewModel.MyNotesViewModel
import com.multitv.eventbuilder.ui.mynotes.viewmodelfactory.MyNotesViewModelFactory
import com.multitv.eventbuilder.utils.BottomMarginItemDecoration

class MyNotesFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentMynoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter : MyNotesAdaptor
    private var navController: NavController? = null

    private lateinit var viewModel : MyNotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMynoteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* binding.search.setOnClickListener(this)*/
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        binding.backArrow.setOnClickListener(this)
        binding.addNotes.setOnClickListener(this)

        Preference.init(requireContext())
        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = MyNotesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MyNotesViewModel::class.java]

        viewModel.myNotesLiveData.observe(viewLifecycleOwner) { response ->
            setData(response)
        }

        getDataFromApi()

        /*  setUi()*/

    }

    private fun getDataFromApi() {
        val userId = Preference.getUserId()!!
        Log.e("notes userId",""+userId)
        viewModel.getMyNotesData(AppConstant.GETSNOTESAPI,userId)
    }


    private fun setData(response: Generic<MYNotesResponse>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()

                binding.title.text = response.data.pageTittle.trim()

                val dataList = response.data.data
                binding.notesCount.text = "${dataList.size} Notes"
                Log.e("MyNotes Success", "Data: $dataList")
                binding.notesRecyclerview.layoutManager = LinearLayoutManager(requireContext())
                binding.notesRecyclerview.addItemDecoration(BottomMarginItemDecoration(15))
                adapter = MyNotesAdaptor(dataList) { note ->
                    val dialog = AddNotesDialogFragment.newInstance(note)

                    dialog.setOnNoteAddedListener(object : AddNotesDialogFragment.OnNoteAddedListener {
                        override fun onNoteAdded() {
                            getDataFromApi()
                        }
                    })

                    dialog.show(childFragmentManager, "EditNoteDialog")
                }
                binding.notesRecyclerview.adapter = adapter


            }

            is Generic.Error -> {
                ProgressDialog.hideProgressDialog()
                Log.e("MyNotes Error", "Message: ${response.message}")
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            is Generic.Failure -> {
                ProgressDialog.hideProgressDialog()
                Log.e("MyNotes Failure", "Exception: ${response.exception.message}")
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
            }
        }
    }




    override fun onClick(v: View?) {
        when(v?.id){

            R.id.backArrow -> {
                navController?.navigateUp()
            }

            R.id.addNotes -> {
                val dialog = AddNotesDialogFragment()

                dialog.setOnNoteAddedListener(object : AddNotesDialogFragment.OnNoteAddedListener{
                    override fun onNoteAdded() {
                        getDataFromApi()
                    }
                })

                dialog.show(childFragmentManager, "AddNotesDialog")
            }
        }
    }
}