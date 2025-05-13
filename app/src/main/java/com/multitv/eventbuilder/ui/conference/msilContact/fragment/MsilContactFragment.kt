package com.multitv.eventbuilder.ui.conference.msilContact.fragment

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
import com.multitv.eventbuilder.databinding.FragmentMsilContactBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.sealed.MsilContact
import com.multitv.eventbuilder.ui.conference.msilContact.ContactAdapter
import com.multitv.eventbuilder.model.msilcontact.model.MsilContactResponse
import com.multitv.eventbuilder.ui.conference.msilContact.viewmodel.MsilContactViewModel
import com.multitv.eventbuilder.ui.conference.msilContact.viewmodelfactory.MsilContactViewModelFactory

class MsilContactFragment : Fragment(), View.OnClickListener {

    private var _binding : FragmentMsilContactBinding? = null
    private val binding get() = _binding!!

    private lateinit var contactAdapter: ContactAdapter
    private var navController: NavController? = null

    private lateinit var viewModel : MsilContactViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMsilContactBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        binding.backArrow.setOnClickListener(this)

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = MsilContactViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MsilContactViewModel::class.java]

        viewModel.msilcontact.observe(viewLifecycleOwner) { response ->
            setData(response)
        }
        getDataFromApi()

       /* setUi()*/
    }

    private fun getDataFromApi() {
        viewModel.getMsilContactData(AppConstant.MSILCONTACTAPI)

    }

    private fun setData(response: Generic<MsilContactResponse>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()
                val data = response.data.data

                binding.title.text = response.data.pageTittle.trim()

                val contactList = mutableListOf<MsilContact>()

                   data.forEach { group ->
                    contactList.add(MsilContact.Header(group.title))
                    group.contacts.forEach { contact ->
                        contactList.add(MsilContact.Person(contact.name, contact.mobile))
                    }
                }

// Set adapter with API data
                contactAdapter = ContactAdapter(contactList)
                binding.contactRecycler.layoutManager = LinearLayoutManager(requireContext())
                binding.contactRecycler.adapter = contactAdapter


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

    fun setUi(){

        val contactList = listOf(
            MsilContact.Header("Overall Management"),
            MsilContact.Person("Mr. Prashant Verma", "9810569046"),
            MsilContact.Person("Mr. Prashant Verma", "9810569046"),
            MsilContact.Person("Mr. Prashant Verma", "9810569046"),

            MsilContact.Header("Event Management"),
            MsilContact.Person("Mr. Satya Pal", "9810569046"),
            MsilContact.Person("Mr. Prashant Verma", "9810569046")
        )

        contactAdapter = ContactAdapter(contactList)
        binding.contactRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.contactRecycler.adapter = contactAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backArrow -> {
                navController?.navigateUp()
            }
        }
    }
}