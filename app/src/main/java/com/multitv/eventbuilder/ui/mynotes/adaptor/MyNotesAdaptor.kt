package com.multitv.eventbuilder.ui.mynotes.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.model.mynotesmodel.model.MyNotesItem

class MyNotesAdaptor(
    private val notes: List<MyNotesItem>,
    private val onNoteClicked: (MyNotesItem) -> Unit
) : RecyclerView.Adapter<MyNotesAdaptor.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: AppCompatTextView = itemView.findViewById(R.id.speakerName)
        val tvDate: AppCompatTextView = itemView.findViewById(R.id.noteDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notes, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.tvTitle.text = note.title
        holder.tvDate.text = note.created

        holder.itemView.setOnClickListener {
            onNoteClicked(note)
        }
    }

    override fun getItemCount() = notes.size
}
