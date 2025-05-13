package com.multitv.eventbuilder.ui.travelstay.travelstayContent.adaptor


import com.multitv.eventbuilder.model.travelstay.model.TravelInfoItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.constant.Helper.capitalizeWords

class TravelAdapter(
    private val items: List<TravelInfoItem>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_LEFT = 0
        private const val TYPE_RIGHT = 1
    }

    interface OnItemClickListener {
        fun onItemClick(item: TravelInfoItem)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) TYPE_LEFT else TYPE_RIGHT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = if (viewType == TYPE_LEFT) R.layout.item_travel_left else R.layout.item_travel
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return if (viewType == TYPE_LEFT) LeftViewHolder(view, listener)
        else RightViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is LeftViewHolder) holder.bind(item)
        else if (holder is RightViewHolder) holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    class LeftViewHolder(view: View, private val listener: OnItemClickListener) :
        RecyclerView.ViewHolder(view) {
        private val imageView: AppCompatImageView = view.findViewById(R.id.leftImage)
        private val titleText: AppCompatTextView = view.findViewById(R.id.leftTittle)

        fun bind(item: TravelInfoItem) {
            titleText.text = item.title
            Glide.with(itemView.context)
                .load(item.file)
                .into(imageView)

            itemView.setOnClickListener { listener.onItemClick(item) }
        }
    }

    class RightViewHolder(view: View, private val listener: OnItemClickListener) :
        RecyclerView.ViewHolder(view) {
        private val imageView: AppCompatImageView = view.findViewById(R.id.rightImage)
        private val titleText: AppCompatTextView = view.findViewById(R.id.rightTittle)

        fun bind(item: TravelInfoItem) {
            titleText.text = item.title
            Glide.with(itemView.context)
                .load(item.file)
                .into(imageView)

            itemView.setOnClickListener { listener.onItemClick(item) }
        }
    }
}

