package com.multitv.eventbuilder.ui.conference.conference_detail.Adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.model.TimelineItem

class TimelineAdapter(private val items: List<TimelineItem>,private val isDetail:Boolean = false ,private val isSocial :Boolean = false, private val isShuttle :Boolean = false,private val isConference : Boolean = false,private val isDetailAgenda : Boolean = false) : RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.activityTitle)
        val time: TextView = view.findViewById(R.id.activityTime)
        val venue: TextView = view.findViewById(R.id.activityVenue)
        val dot: ImageView = view.findViewById(R.id.dot)
        val line: View = view.findViewById(R.id.line)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_timeline, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.title.text =  HtmlCompat.fromHtml(item.title, HtmlCompat.FROM_HTML_MODE_LEGACY)





        if(isDetail){
            holder.title.isVisible = true
            holder.time.isVisible = true
            holder.venue.isVisible = true

            if (item.time.isNotEmpty()) {
                holder.time.text = item.time
            }else{
                holder.time.isVisible = false
            }

            if (item.venue.isNotEmpty()) {
                holder.venue.text = item.venue
            }else{
                holder.venue.isVisible = false
            }

        }


        if (isSocial){
            holder.title.isVisible = true
            holder.time.isVisible = true

            if (item.time.isNotEmpty()) {
                holder.time.text = item.time
            }else{
                holder.time.isVisible = false
            }
            holder.venue.isVisible = false
        }

        if (isConference || isDetailAgenda){
            holder.title.isVisible = true
            holder.venue.isVisible = true

            if (item.venue.isNotEmpty()) {
                holder.venue.text = item.venue
            }else{
                holder.venue.isVisible = false
            }
            holder.time.isVisible = false
        }

        if (isShuttle){
            holder.title.isVisible = true
            holder.venue.isVisible = true

            if (item.venue.isNotEmpty()) {
                holder.venue.text = item.venue
            }else{
                holder.venue.isVisible = false
            }
            holder.time.isVisible = false
        }





      /*  // Hide the line for the last item
        if (position == items.size - 1) {
            holder.line.visibility = View.GONE
        }*/
    }

    override fun getItemCount() = items.size
}
