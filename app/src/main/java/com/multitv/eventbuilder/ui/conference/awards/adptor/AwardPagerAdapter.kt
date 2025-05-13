package com.multitv.eventbuilder.ui.conference.awards.adptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.multitv.eventbuilder.R

class AwardPagerAdapter(private val imageList: List<String>) :
    RecyclerView.Adapter<AwardPagerAdapter.AwardViewHolder>() {

    inner class AwardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.awardImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AwardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_award, parent, false)
        return AwardViewHolder(view)
    }

    override fun onBindViewHolder(holder: AwardViewHolder, position: Int) {
        val item = imageList[position]

        Glide.with(holder.itemView.context)
            .load(item)
            .into(holder.image)
    }

    override fun getItemCount(): Int = imageList.size

    fun getImageUrlAt(position: Int): String = imageList[position]
}

/*
class AwardPagerAdapter(private val imageList: List<String>) :
    RecyclerView.Adapter<AwardPagerAdapter.AwardViewHolder>() {

    inner class AwardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.awardImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AwardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_award, parent, false)
        return AwardViewHolder(view)
    }

    override fun onBindViewHolder(holder: AwardViewHolder, position: Int) {
       val item = imageList[position]

        Glide.with(holder.itemView.context)
            .load(item) // Optional error image
            .into(holder.image)

    }

    override fun getItemCount(): Int = imageList.size
}
*/


