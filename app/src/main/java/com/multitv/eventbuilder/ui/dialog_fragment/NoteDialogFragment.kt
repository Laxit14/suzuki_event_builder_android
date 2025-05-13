package com.multitv.eventbuilder.ui.dialog_fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.constant.AppConstant
import com.multitv.eventbuilder.constant.Preference
import com.multitv.eventbuilder.databinding.NotesPopupBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.dialog_fragment.postnotes.model.PostNotesResponse
import com.multitv.eventbuilder.ui.dialog_fragment.postnotes.viewmodel.PostNotesViewmodel
import com.multitv.eventbuilder.ui.dialog_fragment.postnotes.viewmodelfactory.PostNotesViewModelFactory
import com.multitv.eventbuilder.utils.Utils

class NoteDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_SPEAKER_ID = "speaker_id"
        private const val ARG_SPEAKER_NAME = "speaker_name"

        fun newInstance(speakerId: Int, speakerName: String): NoteDialogFragment {
            val fragment = NoteDialogFragment()
            val args = Bundle().apply {
                putInt(ARG_SPEAKER_ID, speakerId)
                putString(ARG_SPEAKER_NAME, speakerName)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private var speakerId: Int = -1
    private lateinit var speakerName: String
    private lateinit var viewModel: PostNotesViewmodel
    private var _binding: NotesPopupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Preference.init(requireContext())

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        _binding = NotesPopupBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        val userId = Preference.getUserId() ?: 0L
        speakerId = arguments?.getInt(ARG_SPEAKER_ID) ?: -1
        speakerName = arguments?.getString(ARG_SPEAKER_NAME).orEmpty()

        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = PostNotesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[PostNotesViewmodel::class.java]

        // Observe API response
        viewModel.postNotesLiveData.observe(this) { response ->
            setData(response)
        }

        binding.cancelImage.setOnClickListener {
            dialog.dismiss()
        }

        binding.saveNotes.setOnClickListener {
            val note = binding.notesInput.text.toString().trim()
            if (note.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a note before saving.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.getPostNotesResponse(
                AppConstant.POSTNOTESAPI,
                speakerId,
                userId,
                note,
                speakerName
            )
        }

        binding.downloadImage.setOnClickListener {
            Log.e("download",""+"click download")
            val note = binding.notesInput.text.toString().trim()
            if (note.isEmpty()) {
                Toast.makeText(requireContext(), "No content to download.", Toast.LENGTH_SHORT).show()
            } else {
                Utils.createPdfFromText(requireContext(), note,speakerName)
                android.app.AlertDialog.Builder(requireContext())
                    .setTitle("PDF Downloaded")
                    .setMessage("Your note has been saved as a PDF in the Downloads folder.")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()

                    }
                    .setCancelable(false)
                    .show()
            }
        }

        binding.deleteImage.setOnClickListener {
            binding.notesInput.setText("")
            Toast.makeText(requireContext(), "Note cleared.", Toast.LENGTH_SHORT).show()
        }



        return dialog
    }

    private fun setData(response: Generic<PostNotesResponse>) {
        when (response) {
            is Generic.Loading -> {
                ProgressDialog.showProgresssDialog(requireContext())
            }

            is Generic.Success -> {
                ProgressDialog.hideProgressDialog()
                binding.notesInput.setText("")
                Toast.makeText(requireContext(), "Note saved successfully!", Toast.LENGTH_SHORT).show()
                dismiss()
            }

            is Generic.Error -> {
                ProgressDialog.hideProgressDialog()
                Toast.makeText(requireContext(), "Error: ${response.message}", Toast.LENGTH_SHORT).show()
            }

            is Generic.Failure -> {
                ProgressDialog.hideProgressDialog()
                Toast.makeText(requireContext(), "Failure: ${response.exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
