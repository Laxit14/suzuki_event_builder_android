package com.multitv.eventbuilder.ui.interaction.askyouquestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.constant.AppConstant
import com.multitv.eventbuilder.constant.Preference
import com.multitv.eventbuilder.databinding.FragmentQuestionSlipBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.interaction.askyouquestion.viewmodel.AskQuestionViewModel
import com.multitv.eventbuilder.ui.interaction.askyouquestion.viewmodelfactory.AskQuestionViewmodelFactory

class AskQuestionFragment : Fragment(), View.OnClickListener{

    private var _binding : FragmentQuestionSlipBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private lateinit var viewModel: AskQuestionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentQuestionSlipBinding.inflate(inflater,container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Preference.init(requireContext())
        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = AskQuestionViewmodelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[AskQuestionViewModel::class.java]

        binding.backArrow.setOnClickListener(this)
        binding.submit.setOnClickListener(this)
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host)

        viewModel.askQuestResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Generic.Loading -> {
                    // Show loading
                    ProgressDialog.showProgresssDialog(requireContext())
                }
                is Generic.Success -> {
                    ProgressDialog.hideProgressDialog()
                    response.data.let {
                        showSuccessDialog()
                        binding.etComment.setText("")
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

    }

    private fun showSuccessDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Success")
            .setMessage("Message sent successfully.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.backArrow -> {
                navController.navigateUp()
            }

            R.id.submit -> {
                val eventId = "309"
                val appId = "205224"
                val getQestion = binding.etComment.text.toString().trim()
                if(getQestion.isEmpty()){
                    binding.etComment.error = "Please enter your question"
                }
                else {
                    // Call API here
                    val userId = Preference.getUserId()
                    viewModel.postQuestionFeed(
                        AppConstant.QUESTION_POST,
                        userId!!,
                        getQestion,
                        "309",
                        "205224"
                    )
                }
            }
        }
    }
}