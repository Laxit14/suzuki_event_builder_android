package com.multitv.eventbuilder.ui.travelstay.Hotel_Stay.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.model.hotelstaymodel.model.ContactInfo

class PhoneNumberAdapterNew(private val phoneList: List<ContactInfo>) :
    RecyclerView.Adapter<PhoneNumberAdapterNew.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.name)
        val number: TextView = itemView.findViewById(R.id.number)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_phone_number, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val phoneNumber = phoneList[position]
        holder.title.text = phoneNumber.title
        holder.number.text = phoneNumber.value
    }

    override fun getItemCount(): Int {
        return phoneList.size
    }
}
