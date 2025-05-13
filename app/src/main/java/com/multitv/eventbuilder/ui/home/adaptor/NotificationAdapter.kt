package com.multitv.eventbuilder.ui.home.adaptor

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.model.NotificationDataItem
import com.multitv.eventbuilder.model.homemodel.model.NotificationData

class NotificationAdapter(
    private val notifications: List<NotificationDataItem>,
    private val listener: OnNotificationClickListener, private val isFromHome: Boolean = false
) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: AppCompatImageView = itemView.findViewById(R.id.notifyImage)
        val title: TextView = itemView.findViewById(R.id.tittle)
        val message: TextView = itemView.findViewById(R.id.message)
        val time: TextView = itemView.findViewById(R.id.time)
        val imageCard: MaterialCardView = itemView.findViewById(R.id.imageCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.title.text = notification.textField
        /*   holder.message.text = notification.*/
        holder.time.text = notification.createdAt

        if (isFromHome){
            holder.title.maxLines = 3
            holder.title.ellipsize = TextUtils.TruncateAt.END
        }

        if (!notification.image.isNullOrEmpty()) {
            holder.icon.visibility = View.VISIBLE
            holder.imageCard.visibility = View.VISIBLE

            Glide.with(holder.icon.context)
                .load(notification.image)
                .into(holder.icon)
        } else {
            holder.icon.visibility = View.GONE
            holder.imageCard.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            listener.onNotificationClick(notification)
        }
    }

    override fun getItemCount(): Int = notifications.size
}

interface OnNotificationClickListener {
    fun onNotificationClick(item: NotificationDataItem)
}

