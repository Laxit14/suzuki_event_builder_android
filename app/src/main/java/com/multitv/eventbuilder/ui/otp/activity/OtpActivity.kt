package com.multitv.eventbuilder.ui.otp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.constant.Preference
import com.multitv.eventbuilder.databinding.ActivityOtpBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.model.loginmodel.model.Data
import com.multitv.eventbuilder.model.otpmodel.model.OtpResponse
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.main.activity.MainActivity
import com.multitv.eventbuilder.ui.otp.viewmodel.OtpViewModel
import com.multitv.eventbuilder.ui.otp.viewmodelfactory.OtpViewModelFactory

class OtpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityOtpBinding
    private lateinit var viewModel: OtpViewModel
    private var loginData: Data? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginData = intent.getParcelableExtra<Data>("LOGIN_DATA")
        Preference.init(this)
        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = OtpViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[OtpViewModel::class.java]

        binding.nextbutton.setOnClickListener(this)


        viewModel.otpResponse.observe(this) { response ->
            verifyAndMoveMainActivity(response)
        }

    }

    fun verifyAndMoveMainActivity(response: Generic<OtpResponse>) {

        when (response) {
            is Generic.Loading -> {
                /* Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show()*/
                ProgressDialog.showProgresssDialog(this@OtpActivity)
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()
                binding.nextbutton.isEnabled = true

                val otpResponse = response.data
                if (otpResponse.success && otpResponse.data != null) {
                    /*val otpData = otpResponse.data
                    Preference.saveUserData(otpData)
                    Preference.saveBooleanData(Preference.IsLogin, true)*/

                    loginData?.let { Preference.saveUserData(it) }
                    Preference.saveBooleanData(Preference.IsLogin, true)

                    val intent = Intent(this@OtpActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    binding.errorText.visibility = View.VISIBLE
                    binding.errorText.text = otpResponse.message
                }
            }

            is Generic.Error -> {
                /*Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()*/
                binding.nextbutton.isEnabled = true
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = response.message
                ProgressDialog.hideProgressDialog()
            }

            is Generic.Failure -> {
                Toast.makeText(
                    this,
                    response.exception.message ?: "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
                binding.nextbutton.isEnabled = true
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = response.exception.message
                ProgressDialog.hideProgressDialog()
            }
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.nextbutton -> {
                handleOtp()
            }
        }
    }

    fun handleOtp() {
        val enteredOtp = binding.otpPinView.text.toString().trim()
        binding.errorText.visibility = View.GONE
        binding.errorText.text = ""

        if (enteredOtp.length == 4) {  // Ensure OTP is 6 digits
            verifyOtp(enteredOtp)
        } else {
            // If OTP is not 6 digits
            Toast.makeText(this, "Please enter a 4-digit OTP", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyOtp(otp: String) {
        binding.nextbutton.isEnabled = false
        loginData?.mobile?.let { viewModel.verifyOty(it, otp) }

    }
}