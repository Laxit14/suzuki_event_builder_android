package com.multitv.eventbuilder.sealed

sealed class MsilContact {
    data class Header(val title: String) : MsilContact()
    data class Person(val name: String, val phone: String) : MsilContact()
}