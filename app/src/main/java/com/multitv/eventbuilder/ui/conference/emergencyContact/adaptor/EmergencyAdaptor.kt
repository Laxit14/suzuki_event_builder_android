package com.multitv.eventbuilder.ui.conference.emergencyContact.adaptor

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.model.EmergencyContactItem
import com.multitv.eventbuilder.model.emergencymodel.EmergencyContact

class EmergencyAdaptor(private val contacts: List<EmergencyContact>) :
    RecyclerView.Adapter<EmergencyAdaptor.ContactViewHolder>() {
    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: AppCompatTextView = view.findViewById(R.id.contactName)
        val phoneTextView: AppCompatTextView = view.findViewById(R.id.contactPhone)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_emergency_number, parent, false)
        return ContactViewHolder(view)
    }
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.nameTextView.text = contact.name
        holder.phoneTextView.text = contact.phoneNumber
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${contact.phoneNumber}")
            }
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int = contacts.size
}