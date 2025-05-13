package com.multitv.eventbuilder.ui.interaction.mvsc_camera.fragment

import android.Manifest
import android.content.ContentValues
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.databinding.FragmentCameraCornerMsvcBinding
import com.multitv.eventbuilder.databinding.FragmentOurSpeakersBinding
import java.io.File

class CameraCornerFragment : Fragment() {

    private var _binding: FragmentCameraCornerMsvcBinding? = null
    private val binding get() = _binding!!

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File

    private var capturedBitmap: Bitmap? = null
    private var lensFacing = CameraSelector.LENS_FACING_BACK


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraCornerMsvcBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        outputDirectory = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 10)
        }

        binding.saveNotes.setOnClickListener {
            takePhoto()
        }

        binding.deleteImage.setOnClickListener {
            capturedBitmap?.let {
                saveBitmapToDownloads(it)
            } ?: Toast.makeText(requireContext(), "No image to save", Toast.LENGTH_SHORT).show()
        }

        binding.retakeImage.setOnClickListener {
            binding.previewView.visibility = View.VISIBLE
            binding.previewFrameOverlay.visibility = View.VISIBLE
            binding.imagePreview.visibility = View.GONE
            binding.retakeImage.visibility = View.GONE
            binding.deleteImage.visibility = View.GONE
            binding.switchCamera.visibility = View.VISIBLE
            binding.saveNotes.isVisible = true
            startCamera()
        }
        binding.switchCamera.setOnClickListener {
            lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK)
                CameraSelector.LENS_FACING_FRONT
            else
                CameraSelector.LENS_FACING_BACK

            startCamera()
        }

    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().setTargetRotation(requireActivity().windowManager.defaultDisplay.rotation)
                .build()

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()


            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val file = File(
            outputDirectory,
            "CameraCorner_${System.currentTimeMillis()}.jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    /* val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                     val resultBitmap = overlayFrame(bitmap)*/
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    val correctedBitmap = correctImageRotation(file.absolutePath, bitmap)
                    val resultBitmap = overlayFrame(correctedBitmap)

                    capturedBitmap = resultBitmap
                    binding.imagePreview.setImageBitmap(resultBitmap)
                    binding.imagePreview.visibility = View.VISIBLE
                    binding.previewView.visibility = View.GONE
                    binding.switchCamera.visibility = View.GONE
                    binding.saveNotes.isVisible = false
                    binding.previewFrameOverlay.visibility = View.GONE
                    binding.deleteImage.visibility = View.VISIBLE
                    binding.retakeImage.visibility = View.VISIBLE
                    /* saveBitmapToDownloads(resultBitmap)*/
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(requireContext(), "Capture failed", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }



    private fun correctImageRotation(path: String, bitmap: Bitmap): Bitmap {
        val exif = androidx.exifinterface.media.ExifInterface(path)
        val orientation = exif.getAttributeInt(
            androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION,
            androidx.exifinterface.media.ExifInterface.ORIENTATION_NORMAL
        )

        val rotationDegrees = when (orientation) {
            androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }

        val matrix = android.graphics.Matrix()
        matrix.postRotate(rotationDegrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    private fun overlayFrame(original: Bitmap): Bitmap {
        val frame = if (original.width > original.height) {    BitmapFactory.decodeResource(resources, R.drawable.lanscape_frame)}else{    BitmapFactory.decodeResource(resources, R.drawable.image_frame)}

        val config = original.config ?: Bitmap.Config.ARGB_8888
        val result = Bitmap.createBitmap(original.width, original.height, config)

        val canvas = Canvas(result)
        canvas.drawBitmap(original, 0f, 0f, null)
        val scaledFrame = Bitmap.createScaledBitmap(frame, original.width, original.height, false)
        canvas.drawBitmap(scaledFrame, 0f, 0f, null)

        return result
    }

    private fun saveBitmapToDownloads(bitmap: Bitmap) {
        val filename = "framed_image_${System.currentTimeMillis()}.jpg"
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/CameraCorner")
        }

        val uri = requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        ) ?: return

        requireContext().contentResolver.openOutputStream(uri)?.use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            Toast.makeText(requireContext(), "Saved to Downloads", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    override fun onPause() {
        super.onPause()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // or whatever your default is
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            val rotation = requireActivity().windowManager.defaultDisplay.rotation
            imageCapture = ImageCapture.Builder()
                .setTargetRotation(rotation)
                .build()

            startCamera()

            val params = binding.previewFrameOverlay.layoutParams
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            binding.previewFrameOverlay.layoutParams = params

            Log.d("LOG Orientation", "Landscape mode")
            Glide.with(requireContext())
                .load("")
                .placeholder(R.drawable.lanscape_frame)
                .error(R.drawable.lanscape_frame)
                .into(binding.previewFrameOverlay)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            val rotation = requireActivity().windowManager.defaultDisplay.rotation
            imageCapture = ImageCapture.Builder()
                .setTargetRotation(rotation)
                .build()

            startCamera()

            Log.d("LOG Orientation", "Portrait mode")
            Glide.with(requireContext())
                .load("")
                .placeholder(R.drawable.image_frame)
                .error(R.drawable.image_frame)
                .into(binding.previewFrameOverlay)
        }
    }


}
