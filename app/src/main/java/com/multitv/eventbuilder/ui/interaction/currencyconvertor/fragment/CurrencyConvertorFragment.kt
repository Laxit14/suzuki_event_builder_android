package com.multitv.eventbuilder.ui.interaction.currencyconvertor.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.multitv.eventbuilder.databinding.FragmentCurrencyConvertorBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.model.currencymodel.model.CurrencyConversionResponse
import com.multitv.eventbuilder.ui.interaction.currencyconvertor.viewmodel.CurrencyConvertorViewModel
import com.multitv.eventbuilder.ui.interaction.currencyconvertor.viewmodelfactory.CurrencyConvertorViewModelFactory
import androidx.navigation.findNavController

class CurrencyConvertorFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentCurrencyConvertorBinding? = null
    private val binding get() = _binding!!
    private var selectedCurrencyType: String = "INR" // default


    private var navController: NavController? = null

    private lateinit var viewModel: CurrencyConvertorViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrencyConvertorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = requireActivity().findNavController(R.id.nav_host)
        binding.backArrow.setOnClickListener(this)
        binding.inr.setOnClickListener(this)
        binding.euro.setOnClickListener(this)
        binding.usd.setOnClickListener(this)

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = CurrencyConvertorViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[CurrencyConvertorViewModel::class.java]


        viewModel.currencyLivedata.observe(viewLifecycleOwner) { result ->
            setData(result)
        }

        binding.inputAmount.setText("1") // Set default input
        updateSelectedButton(R.id.inr)   // Mark INR as selected

        val defaultType = "INR"
        val defaultValue = "1"
        val defaultUrl =
            "${AppConstant.CURRENCY_CHANGE}from_currency=${defaultType}&amount=${defaultValue}"
        viewModel.getCurrencyData(defaultUrl)  // Trigger API


        //text watcher
        binding.inputAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val amount = s.toString().trim()
                if (amount.isNotEmpty()) {
                    val url =
                        "${AppConstant.CURRENCY_CHANGE}from_currency=${selectedCurrencyType}&amount=$amount"
                    viewModel.getCurrencyData(url)
                }
            }
        })


    }

    private fun setData(response: Generic<CurrencyConversionResponse>) {
        when (response) {
            is Generic.Loading -> {
               // ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
             //   ProgressDialog.hideProgressDialog()
                val dataList = response.data.convertedAmount
                binding.convertInput.text = dataList.toString()

            }

            is Generic.Error -> {
               // ProgressDialog.hideProgressDialog()
                Log.e("Conference Error", "Message: ${response.message}")
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            is Generic.Failure -> {
               // ProgressDialog.hideProgressDialog()
                Log.e("Conference Failure", "Exception: ${response.exception.message}")
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(v: View?) {

        val value = binding.inputAmount.text.toString().trim()

        if (v?.id != R.id.backArrow) {
            if (value.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter amount", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // change for new logic
        when (v?.id) {
            R.id.backArrow -> {
                navController?.navigateUp()
            }

            R.id.inr -> {
                selectedCurrencyType = "INR"
                val url =
                    "${AppConstant.CURRENCY_CHANGE}from_currency=${selectedCurrencyType}&amount=${value}"
                updateSelectedButton(R.id.inr)
                viewModel.getCurrencyData(url)
            }

            R.id.euro -> {
                selectedCurrencyType = "EUR"
                val url =
                    "${AppConstant.CURRENCY_CHANGE}from_currency=${selectedCurrencyType}&amount=${value}"
                updateSelectedButton(R.id.euro)
                viewModel.getCurrencyData(url)
            }

            R.id.usd -> {
                selectedCurrencyType = "USD"
                val url =
                    "${AppConstant.CURRENCY_CHANGE}from_currency=${selectedCurrencyType}&amount=${value}"
                updateSelectedButton(R.id.usd)
                viewModel.getCurrencyData(url)
            }
        }


        /*  when (v?.id) {

              R.id.backArrow -> {
                  navController?.navigateUp()
              }

              R.id.inr -> {
                 *//* val value = binding.inputAmount.text*//*
                val type = "INR"
                val url = "${AppConstant.CURRENCY_CHANGE}from_currency=${type}&amount=${value}"
                updateSelectedButton(R.id.inr)
                viewModel.getCurrencyData(url)
            }

            R.id.euro -> {
                *//*val value = binding.inputAmount.text*//*
                val type = "EUR"
                val url = "${AppConstant.CURRENCY_CHANGE}from_currency=${type}&amount=${value}"
                updateSelectedButton(R.id.euro)
                viewModel.getCurrencyData(url)
            }
            R.id.usd -> {
               *//* val value = binding.inputAmount.text*//*
                val type = "USD"
                val url = "${AppConstant.CURRENCY_CHANGE}from_currency=${type}&amount=${value}"
                updateSelectedButton(R.id.usd)
                viewModel.getCurrencyData(url)
            }
        }*/
    }

    private fun updateSelectedButton(selectedId: Int) {
        val selectedTextColor = resources.getColor(R.color.black, null)
        val defaultTextColor = resources.getColor(R.color.white, null)

        val buttons = listOf(binding.inr, binding.usd, binding.euro)

        buttons.forEach {
            val isSelected = it.id == selectedId
            val backgroundRes = if (isSelected) R.drawable.background else R.drawable.border
            it.setBackgroundResource(backgroundRes)
            it.setTextColor(if (isSelected) selectedTextColor else defaultTextColor)
        }
    }


}