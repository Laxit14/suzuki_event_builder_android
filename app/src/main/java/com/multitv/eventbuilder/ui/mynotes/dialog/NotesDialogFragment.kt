package com.multitv.eventbuilder.ui.mynotes.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.multitv.eventbuilder.databinding.DialogNotesBinding
import com.multitv.eventbuilder.databinding.NotesPopupBinding

class NotesDialogFragment : DialogFragment() {

    private var _binding: NotesPopupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NotesPopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Close popup on cancel click
        binding.cancelImage.setOnClickListener {
            dismiss()
        }

        // Handle Save Button Click
        binding.saveNotes.setOnClickListener {
            val noteText = binding.notesInput.text.toString()
            if (noteText.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a note!", Toast.LENGTH_SHORT).show()
            } else {
                dismiss()
                Toast.makeText(requireContext(), "Note Saved!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
