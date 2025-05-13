package com.multitv.eventbuilder.ui.conference.conferenceGallery.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.multitv.eventbuilder.R

class PhotoAdapter(private var photos: List<Int>,
                   private val onImageClick: (Int) -> Unit
) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: AppCompatImageView = view.findViewById(R.id.images)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_conferece_gallery, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageResource(photos[position])

        holder.imageView.setOnClickListener {
            onImageClick(photos[position])
        }
    }

    override fun getItemCount(): Int = photos.size

    fun updatePhotos(newPhotos: List<Int>) {
        photos = newPhotos
        notifyDataSetChanged()
    }
}
