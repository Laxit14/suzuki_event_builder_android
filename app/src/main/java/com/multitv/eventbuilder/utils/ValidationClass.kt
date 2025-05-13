package com.multitv.eventbuilder.utils

object ValidationClass {

    fun emailValid(email:String) : Boolean {

        val emailpattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,6}\$"
        return email.matches(emailpattern.toRegex())

    }

    fun phoneValid(phone : String) : Boolean {
        val phonepattern =  "^[0-9]{10}$"
        return phone.matches(phonepattern.toRegex())
    }

}