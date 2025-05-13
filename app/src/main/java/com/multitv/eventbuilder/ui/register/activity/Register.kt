package com.multitv.eventbuilder.ui.register.activity

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.databinding.ActivityRegisterBinding
import com.multitv.eventbuilder.utils.ValidationClass

class Register : AppCompatActivity(), OnClickListener {
    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signup.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.signup -> {
                if (binding.email.text.isNullOrEmpty()) {
                    binding.email.error = "please enter email"
                } else if (!ValidationClass.emailValid(binding.email.text.toString().trim())) {
                    binding.email.error = "please enter valid email"
                } else if (binding.name.text.toString().trim().isEmpty()) {
                    binding.name.error = "please enter name"
                } else if (binding.password.text.toString().trim().isEmpty()) {
                    binding.password.error = "please enter password"
                } else if (binding.confirmPassword.text.toString().trim().isEmpty()) {
                    binding.confirmPassword.error = "please enter confirm password"
                } else {
                    val password = binding.password.text.toString().trim()
                    val confirmpassword = binding.confirmPassword.toString().trim()

                    if (password != confirmpassword) {
                        binding.confirmPassword.error = "confirm password and password not same"
                    } else {
                        Toast.makeText(this,"all field right",Toast.LENGTH_SHORT).show()
                        movetologinactivity()
                    }
                }
            }
        }
    }

    fun movetologinactivity(){

    }
}