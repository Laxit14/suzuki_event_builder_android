package com.multitv.eventbuilder.ui.conference.msilContact

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.sealed.MsilContact

class ContactAdapter(private val itemList: List<MsilContact>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.categoryTitle)
    }
    class PersonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.contactName)
        val phone: TextView = view.findViewById(R.id.contactPhone)
    }
    override fun getItemViewType(position: Int): Int {
        return when (itemList[position]) {
            is MsilContact.Header -> VIEW_TYPE_HEADER
            is MsilContact.Person -> VIEW_TYPE_ITEM
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_emergency_number, parent, false)
            PersonViewHolder(view)
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = itemList[position]) {
            is MsilContact.Header -> {
                (holder as HeaderViewHolder).title.text = item.title
            }
            is MsilContact.Person -> {
                val personHolder = holder as PersonViewHolder
                personHolder.name.text = item.name
                personHolder.phone.text = item.phone
// Add dialer intent on click
                personHolder.itemView.setOnClickListener {
                    val context = personHolder.itemView.context
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:${item.phone}")
                    }
                    context.startActivity(intent)
                }
            }
        }
    }
    override fun getItemCount(): Int = itemList.size
}

