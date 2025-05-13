package com.multitv.eventbuilder.ui.home.adaptor


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.model.homemodel.model.PagesData

class HomeAdapter(
    private var items: List<PagesData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: PagesData)
    }

    inner class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.itemImage)
        private val titleText: TextView = view.findViewById(R.id.itemTitle)

        fun bind(item: PagesData) {
            Glide.with(itemView.context)
                .load(item.file) // 🔹 Assuming file is a URL
                .placeholder(R.drawable.welcome) // Optional
                .into(imageView)

           /* titleText.text = item.title*/

            itemView.setOnClickListener {
                listener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<PagesData>) {
        items = newItems
        notifyDataSetChanged()
    }


}

