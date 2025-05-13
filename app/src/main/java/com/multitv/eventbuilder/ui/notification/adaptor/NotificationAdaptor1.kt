package com.multitv.eventbuilder.ui.notification.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.model.NotificationResponse

class NotificationAdaptor1(private val list: List<NotificationResponse>)
    : RecyclerView.Adapter<NotificationAdaptor1.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
     val view = LayoutInflater.from(parent.context).inflate(R.layout.notificationitem,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listitem = list[position]
        holder.notifytext.text = listitem.message

    }

    class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
      val notifytext : TextView = itemview.findViewById(R.id.notifytext)
    }
}