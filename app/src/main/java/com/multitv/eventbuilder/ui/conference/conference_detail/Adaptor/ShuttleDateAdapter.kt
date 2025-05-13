package com.multitv.eventbuilder.ui.conference.conference_detail.Adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.constant.Helper.toFormattedDate
import com.multitv.eventbuilder.databinding.DateItemBinding

class ShuttleDateAdapter(
    private val dates: List<String>,
    private val onDateSelected: (Int) -> Unit
) : RecyclerView.Adapter<ShuttleDateAdapter.DateViewHolder>() {

    private var selectedPosition = 0

    inner class DateViewHolder(val binding: DateItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(date: String, isSelected: Boolean) {
            binding.dateFirst.text = date.toFormattedDate()
            binding.dateFirst.setBackgroundResource(
                if (isSelected) R.drawable.filled else R.drawable.border
            )
            binding.dateFirst.setTextColor(
                ContextCompat.getColor(binding.root.context, if (isSelected) R.color.bottomBar_color else R.color.white)
            )

            binding.dateFirst.setOnClickListener {
                selectedPosition = adapterPosition
                onDateSelected(adapterPosition)
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val binding = DateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.bind(dates[position], position == selectedPosition)
    }

    override fun getItemCount() = dates.size
}

