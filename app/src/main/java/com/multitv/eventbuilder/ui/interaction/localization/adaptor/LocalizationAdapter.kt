package com.multitv.eventbuilder.ui.interaction.localization.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.multitv.eventbuilder.databinding.ItemLocalizationBinding
import com.multitv.eventbuilder.model.localizationmodel.model.LocalizationItem

class LocalizationAdapter(private var list: List<LocalizationItem>) :
    RecyclerView.Adapter<LocalizationAdapter.LocalizationViewHolder>() {

    inner class LocalizationViewHolder(val binding: ItemLocalizationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalizationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLocalizationBinding.inflate(inflater, parent, false)
        return LocalizationViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: LocalizationViewHolder, position: Int) {
        val item = list[position]

        holder.binding.title.text = item.name
        holder.binding.subtitle.text = item.description
        holder.binding.status.text = "INTERESTED"

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(item.image)
            .into(holder.binding.itemImage)
    }

    fun updateList(newList: List<LocalizationItem>) {
        list = newList
        notifyDataSetChanged()
    }
}
