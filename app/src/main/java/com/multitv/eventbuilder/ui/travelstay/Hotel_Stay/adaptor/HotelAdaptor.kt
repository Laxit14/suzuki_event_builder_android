// HotelAdapter.kt
package com.multitv.eventbuilder.ui.travelstay.Hotel_Stay.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.model.hotelstaymodel.model.HotelStayItem

class HotelAdapter(
    private val items: List<HotelStayItem>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<HotelAdapter.HotelViewHolder>() {

    interface OnItemClickListener {
        fun onDirectionClick(hotel: HotelStayItem)
    }

    inner class HotelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val banner: AppCompatImageView = view.findViewById(R.id.hotelImage)
        private val hotelName: TextView = view.findViewById(R.id.hotelName)
        private val hotelAddress: TextView = view.findViewById(R.id.hotelLocation)
        private val hotelDes: TextView = view.findViewById(R.id.contentDes)
        private val stay: TextView = view.findViewById(R.id.stay)
        private val parentCard: MaterialCardView = view.findViewById(R.id.parentCard)

        fun bind(item: HotelStayItem) {
            Glide.with(itemView.context)
                .load(item.image)
                .into(banner)

            hotelName.text = item.name
            hotelAddress.text = item.location
            hotelDes.text = item.description

            stay.text = when (position) {
                0 -> "Stay1"
                1 -> "Stay2"
                else -> ""
            }

            itemView.setOnClickListener {
                listener.onDirectionClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hotelandstay, parent, false)
        return HotelViewHolder(view)
    }

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
