package com.multitv.eventbuilder.ui.mynotes.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.annotations.SerializedName
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.constant.AppConstant
import com.multitv.eventbuilder.constant.Preference
import com.multitv.eventbuilder.databinding.FragmentAddNotesBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.model.mynotesmodel.model.MyNotesItem
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.dialog_fragment.postnotes.viewmodel.PostNotesViewmodel
import com.multitv.eventbuilder.ui.dialog_fragment.postnotes.viewmodelfactory.PostNotesViewModelFactory
import com.multitv.eventbuilder.utils.Utils

class AddNotesDialogFragment : DialogFragment() {


    interface OnNoteAddedListener {
        fun onNoteAdded()
    }

    companion object {
        private const val ARG_NOTE = "ARG_NOTE"

        fun newInstance(note: MyNotesItem): AddNotesDialogFragment {
            val fragment = AddNotesDialogFragment()
            val args = Bundle()
            args.putParcelable(ARG_NOTE, note)
            fragment.arguments = args
            return fragment
        }
    }

    private var note: MyNotesItem? = null
    private var listener: OnNoteAddedListener? = null

    fun setOnNoteAddedListener(listener: OnNoteAddedListener) {
        this.listener = listener
    }

    private var _binding: FragmentAddNotesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PostNotesViewmodel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Preference.init(requireContext())
        val userId = Preference.getUserId() ?: 0L

        note = arguments?.getParcelable(ARG_NOTE)

        val speakerId = note?.speakerId ?: generateRandomSpeakerId()

        note?.let {
            binding.titleInput.setText(it.title)
            binding.notesInput.setText(it.content)
        }

        binding.saveNotes.text = if (note == null) "SAVE" else "UPDATE"


        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = PostNotesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[PostNotesViewmodel::class.java]

        viewModel.postNotesLiveData.observe(viewLifecycleOwner) { result ->
            ProgressDialog.hideProgressDialog()
            when (result) {

                is Generic.Loading -> {
                    ProgressDialog.showProgresssDialog(requireContext())
                }

                is Generic.Success -> {
                    ProgressDialog.hideProgressDialog()
                    Toast.makeText(context, "Note saved successfully!", Toast.LENGTH_SHORT).show()
                    listener?.onNoteAdded()
                    dismiss()
                }
                is Generic.Error -> {
                    ProgressDialog.hideProgressDialog()
                    Toast.makeText(context, result.message ?: "Error occurred", Toast.LENGTH_SHORT).show()
                }
                is Generic.Failure -> {
                    ProgressDialog.hideProgressDialog()
                    Toast.makeText(context, result.exception.message ?: "Something went wrong", Toast.LENGTH_SHORT).show()
                }

            }
        }

        binding.saveNotes.setOnClickListener {
            val title = binding.titleInput.text?.toString()?.trim()
            val content = binding.notesInput.text?.toString()?.trim().orEmpty()

            if (title.isNullOrEmpty() || content.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter a title and content before saving.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            /*  ProgressDialog.showProgresssDialog(requireContext())*/
            viewModel.getPostNotesResponse(
                AppConstant.POSTNOTESAPI,
                speakerId,
                userId,
                content,
                title
            )
        }

        binding.cancelImage?.setOnClickListener {
            dismiss()
        }

        binding.downloadImage.setOnClickListener {
            val title = binding.titleInput.text.toString().trim()
            val note = binding.notesInput.text.toString().trim()

            if (title.isEmpty() || note.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill both title and content.", Toast.LENGTH_SHORT).show()
            } else {
                // Combine title and note for PDF content
                val pdfContent = "Title: $title\n\n$note"

                // Create PDF
                Utils.createPdfFromText(requireContext(), pdfContent,title)

                // Show Alert Dialog
                android.app.AlertDialog.Builder(requireContext())
                    .setTitle("PDF Saved")
                    .setMessage("Your note has been saved as a PDF in the Downloads folder.")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()

                        // Clear title and content
                        binding.titleInput.setText("")
                        binding.notesInput.setText("")
                    }
                    .setCancelable(false)
                    .show()
            }
        }

        binding.deleteImage.setOnClickListener {
            binding.titleInput.setText("")
            binding.notesInput.setText("")
            /*Toast.makeText(requireContext(), "Note cleared.", Toast.LENGTH_SHORT).show()*/
        }


    }

    private fun generateRandomSpeakerId(): Int {
        val randomId = (100_000_000..999_999_999).random()
        return randomId
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
