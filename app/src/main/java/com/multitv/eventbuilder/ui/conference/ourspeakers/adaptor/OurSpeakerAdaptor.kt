package com.multitv.eventbuilder.ui.conference.ourspeakers.adaptor

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.model.ourspeaker.model.SpeakerItem

class OurSpeakerAdaptor(
    private val speakerList: List<SpeakerItem>,
    private val listener: OnClickItemListener
) : RecyclerView.Adapter<OurSpeakerAdaptor.SpeakerViewHolder>() {

    interface OnClickItemListener {
        fun onNoteClicked(item: SpeakerItem)
    }

    inner class SpeakerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: AppCompatImageView = itemView.findViewById(R.id.speakerImage)
        val nameText: AppCompatTextView = itemView.findViewById(R.id.speakerName)
        val positionText: AppCompatTextView = itemView.findViewById(R.id.speakerPosition)
        val companyText: AppCompatTextView = itemView.findViewById(R.id.speakerCompany)
        val notesText: AppCompatTextView = itemView.findViewById(R.id.notes)
        val pdfText: AppCompatTextView = itemView.findViewById(R.id.pdf)

        fun bind(item: SpeakerItem) {
           /* imageView.setImageResource(item.imageResId)*/
            nameText.text = item.name
            positionText.text = item.designation
            companyText.text = item.company

            Glide.with(itemView.context)
                .load(item.image)
                .into(imageView)

           /* itemView.setOnClickListener {
                listener.onClickItem(item)
            }*/

            notesText.setOnClickListener {
                listener.onNoteClicked(item)
            }

            // Handle PDF visibility and click
            if (!item.pdf.isNullOrEmpty() && item.pdf.endsWith(".pdf", ignoreCase = true)) {
                pdfText.visibility = View.VISIBLE
                pdfText.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(Uri.parse(item.pdf), "application/pdf")
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

                    try {
                        itemView.context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(
                            itemView.context,
                            "No PDF viewer found.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                pdfText.visibility = View.GONE
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeakerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_our_speaker, parent, false)
        return SpeakerViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpeakerViewHolder, position: Int) {
        val speaker = speakerList[position]
        holder.bind(speaker) // ✅ Correctly calling bind() method inside ViewHolder
    }

    override fun getItemCount(): Int = speakerList.size
}
