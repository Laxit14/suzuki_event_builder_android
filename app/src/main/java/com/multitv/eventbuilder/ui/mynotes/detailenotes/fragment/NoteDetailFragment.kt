package com.multitv.eventbuilder.ui.mynotes.detailenotes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.multitv.eventbuilder.databinding.FragmentNoteDetailBinding

class NoteDetailFragment : Fragment() {

    private var _binding: FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteTitle: String
    private lateinit var noteContent: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the note data passed from the previous fragment
        arguments?.let {
            noteTitle = it.getString("NOTE_TITLE", "")
            noteContent = it.getString("NOTE_CONTENT", "")
        }

        // Display the note data
        binding.noteTitleText.text = noteTitle
        binding.noteContentText.text = noteContent
    }
}
