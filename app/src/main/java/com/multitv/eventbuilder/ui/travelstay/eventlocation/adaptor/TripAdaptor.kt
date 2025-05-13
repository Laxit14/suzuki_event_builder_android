package com.multitv.eventbuilder.ui.travelstay.eventlocation.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.model.eventlocation.model.Segment

class TripAdapter(private val tripList: List<Segment>,
                  private val listener: OnclickImageInterface
    ) : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    interface OnclickImageInterface{
        fun OnclickImage(item: Segment)
    }

    class TripViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: AppCompatTextView = view.findViewById(R.id.flyName)
        val duration: AppCompatTextView = view.findViewById(R.id.time)
        val distance: AppCompatTextView = view.findViewById(R.id.tripDistance)
        val source: AppCompatTextView = view.findViewById(R.id.departureCity)
        val destination: AppCompatTextView = view.findViewById(R.id.arrivalCity)
        val departureText: TextView = view.findViewById(R.id.departureLabel)
        val arrivableevel: AppCompatTextView = view.findViewById(R.id.arrivalLabel)
        val icon: AppCompatImageView = view.findViewById(R.id.tripImage)
        val imageEvent: ShapeableImageView = view.findViewById(R.id.imageEvent)
        val parent: ConstraintLayout = view.findViewById(R.id.parent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_eventlocation, parent, false)
        return TripViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = tripList[position]
        holder.title.text = trip.title
       /* holder.duration.text = trip.duration*/
        holder.source.text = trip.from.name
        holder.destination.text = trip.to.name
        holder.departureText.text = trip.from.placeType
        holder.arrivableevel.text = trip.to.placeType

        when (trip.icon) {
            "flight_icon" -> holder.icon.setImageResource(R.drawable.flight)
            "car_icon" -> holder.icon.setImageResource(R.drawable.car_svg)
        }

        Glide.with(holder.imageEvent.context)
            .load(trip.image)
            .into(holder.imageEvent)

        holder.parent.setOnClickListener {
            listener.OnclickImage(trip)
        }

    }

    override fun getItemCount() = tripList.size
}
