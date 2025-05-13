package com.multitv.eventbuilder.ui.conference.ourspeakers.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.model.OurSpeakerItem
import de.hdodenhof.circleimageview.CircleImageView

class SpeakerAdaptor(
    private val speakerList: List<OurSpeakerItem>,
    private val listener: OnClickItemListener
) : RecyclerView.Adapter<SpeakerAdaptor.SpeakerViewHolder>() {

    interface OnClickItemListener {
        fun onClickItem(item: OurSpeakerItem)
    }

    inner class SpeakerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: AppCompatImageView = itemView.findViewById(R.id.speakerImage)
        val nameText: AppCompatTextView = itemView.findViewById(R.id.speakerName)
        val positionText: AppCompatTextView = itemView.findViewById(R.id.speakerPosition)
        val companyText: AppCompatTextView = itemView.findViewById(R.id.speakerCompany)
        val pdfButton: MaterialButton = itemView.findViewById(R.id.speakerPdf)
        val noteButton: MaterialButton = itemView.findViewById(R.id.speakerNotes)

        fun bind(item: OurSpeakerItem) {
            imageView.setImageResource(item.imageResId)
            nameText.text = item.name
            positionText.text = item.position
            companyText.text = item.company

//            itemView.setOnClickListener {
//                listener.onClickItem(item)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeakerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_speaker, parent, false)
        return SpeakerViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpeakerViewHolder, position: Int) {
        val speaker = speakerList[position]
        holder.bind(speaker) // ✅ Correctly calling bind() method inside ViewHolder
    }

    override fun getItemCount(): Int = speakerList.size
}
