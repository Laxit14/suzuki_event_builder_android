package com.multitv.eventbuilder.ui.interaction.interactionchild.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.model.interactionmodel.model.InteractionDataItem

class InteractionAdapter(
    private val list: List<InteractionDataItem>,
    private val listener: OnInteractionItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnInteractionItemClickListener {
        fun onInteractionClick(tittle: String)
    }

    companion object {
        private const val TYPE_LEFT = 0
        private const val TYPE_RIGHT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) TYPE_LEFT else TYPE_RIGHT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_LEFT) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_travel_left, parent, false)
            LeftViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_travel, parent, false)
            RightViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        val context = holder.itemView.context
        if (holder is LeftViewHolder) {
            Glide.with(context)
                .load(item.file) // Replace with real base URL
                .into(holder.imageView)
            holder.titleText.text = item.title
            holder.itemView.setOnClickListener { listener.onInteractionClick(item.slug.lowercase()) }
        } else if (holder is RightViewHolder) {
            Glide.with(context)
                .load(item.file)
                .into((holder as RightViewHolder).imageView)

            holder.titleText.text = item.title
            holder.itemView.setOnClickListener { listener.onInteractionClick(item.slug.lowercase()) }
        }
    }

    override fun getItemCount(): Int = list.size

    class LeftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: AppCompatImageView = itemView.findViewById(R.id.leftImage)
        val titleText: AppCompatTextView = itemView.findViewById(R.id.leftTittle)
    }

    class RightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: AppCompatImageView = itemView.findViewById(R.id.rightImage)
        val titleText: AppCompatTextView = itemView.findViewById(R.id.rightTittle)
    }

    fun String.capitalizeWords(): String {
        return this.split(" ")
            .joinToString(" ") { word ->
                word.lowercase().replaceFirstChar { it.uppercase() }
            }
    }
}
