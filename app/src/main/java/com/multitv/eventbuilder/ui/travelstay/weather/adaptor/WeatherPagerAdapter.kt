package com.multitv.eventbuilder.ui.travelstay.weather.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.multitv.eventbuilder.databinding.ItemWeatherForecastBinding
import com.multitv.eventbuilder.model.weathermodel.model.ForecastItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WeatherPagerAdapter(
    private val items: List<ForecastItem>,
    private val onItemClick: (ForecastItem) -> Unit
) : RecyclerView.Adapter<WeatherPagerAdapter.ViewHolder>() {

    private var selectedPosition = 0

    inner class ViewHolder(val binding: ItemWeatherForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ForecastItem, isSelected: Boolean) = binding.apply {
            val context = root.context


            root.scaleX = if (isSelected) 1.1f else 1.0f
            root.scaleY = if (isSelected) 1.1f else 1.0f

            /*val layoutParams = root.layoutParams
            layoutParams.height = if (isSelected) dpToPx(context, 200) else dpToPx(context, 190)
            root.layoutParams = layoutParams*/

            // Load image and set data
            Glide.with(root.context).load(item.icon).into(weatherIcon)
            dateText.text = formatToShortDayWithSuffix(item.date)
            temp1.text = "${item.minTemp}°C"
            temp2.text = "${item.maxTemp}°C"

            // Click listener
            root.setOnClickListener {
                val previous = selectedPosition
                val current = adapterPosition
                if (current != RecyclerView.NO_POSITION) {
                    selectedPosition = current
                    notifyItemChanged(previous)
                    notifyItemChanged(current)
                    onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWeatherForecastBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position == selectedPosition)
    }

    fun getSelectedItem(): ForecastItem = items[selectedPosition]

    fun setSelectedPosition(position: Int) {
        val previous = selectedPosition
        selectedPosition = position
        notifyItemChanged(previous)
        notifyItemChanged(position)
    }

    fun dpToPx(context: android.content.Context, dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}


fun formatToShortDayWithSuffix(dateStr: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val date = inputFormat.parse(dateStr)

        val calendar = Calendar.getInstance()
        calendar.time = date!!

        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val suffix = when {
            dayOfMonth in 11..13 -> "th"
            dayOfMonth % 10 == 1 -> "st"
            dayOfMonth % 10 == 2 -> "nd"
            dayOfMonth % 10 == 3 -> "rd"
            else -> "th"
        }

        val dayFormat = SimpleDateFormat("EEE", Locale.ENGLISH) // e.g., Wed
        val day = dayFormat.format(date)

        "$day ${dayOfMonth}$suffix"
    } catch (e: Exception) {
        dateStr
    }

}
