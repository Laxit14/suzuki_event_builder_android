package com.multitv.eventbuilder.ui.interaction.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.multitv.eventbuilder.R

class PdfAdapter(private var pdfList: List<Pair<String, String>>, private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<PdfAdapter.PdfViewHolder>() {

    class PdfViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pdfName: TextView = itemView.findViewById(R.id.pdfName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pdf, parent, false)
        return PdfViewHolder(view)
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        val (fileName, filePath) = pdfList[position] // ✅ Get name & full path
        holder.pdfName.text = fileName

        holder.itemView.setOnClickListener {
            onItemClick(filePath)  // ✅ Pass full path to open
        }
    }

    override fun getItemCount(): Int = pdfList.size

    fun updateList(newList: List<Pair<String, String>>) {
        pdfList = newList
        notifyDataSetChanged()
    }
}


