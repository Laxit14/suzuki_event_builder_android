package com.multitv.eventbuilder.ui.interaction.digital_exhibition.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.multitv.eventbuilder.databinding.ItemAwardBinding

class DigitalExhibitionPagerAdapter(
    private val imageUrls: List<String>
) : RecyclerView.Adapter<DigitalExhibitionPagerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemAwardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAwardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = imageUrls.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = imageUrls[position]

        // Load the image from the URL
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.binding.awardImage)


    }
}
