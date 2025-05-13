package com.multitv.eventbuilder.ui.conference.conferenceGallery.subfragment.fragment

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.databinding.FragmentFullImageBinding
import com.multitv.eventbuilder.ui.mynotes.dialog.NotesDialogFragment

class FullImageFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentFullImageBinding
    private var imageResId: Int = 0

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageResId = arguments?.getInt("imageRes") ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFullImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.fullImageView.setImageResource(imageResId)

        binding.downloadButton.setOnClickListener {
            downloadImage(imageResId)
        }

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        binding.backArrow.setOnClickListener(this)
    }

    private fun downloadImage(imageResId: Int) {
        val drawable = ContextCompat.getDrawable(requireContext(), imageResId)
        val bitmap = (drawable as BitmapDrawable).bitmap

        val filename = "image_${System.currentTimeMillis()}.png"
        val path = MediaStore.Images.Media.insertImage(
            requireActivity().contentResolver,
            bitmap,
            filename,
            "Downloaded from EventBuilder"
        )

        if (path != null) {
            /*Toast.makeText(requireContext(), "Image downloaded", Toast.LENGTH_SHORT).show()*/
            showDownloadSuccessDialog()
        } else {
            Toast.makeText(requireContext(), "Download failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.backArrow -> {
                navController?.navigateUp()
            }
        }
    }

    private fun showDownloadSuccessDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Success")
            .setMessage("Image downloaded successfully.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

}
