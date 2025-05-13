package com.multitv.eventbuilder.ui.login.dialog

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Outline
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.common.util.concurrent.ListenableFuture
import com.multitv.eventbuilder.constant.Preference
import com.multitv.eventbuilder.databinding.CaptureImagePopupBinding
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.min

class CameraDialogFragment(private val onPhotoTaken: (Bitmap) -> Unit) : DialogFragment() {

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService

    private var _binding: CaptureImagePopupBinding? = null
    private val binding get() = _binding!!

    private var capturedImage: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = CaptureImagePopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Preference.init(requireContext())
        cameraExecutor = Executors.newSingleThreadExecutor()
        view.post {
            startCamera()
        }
        binding.captureButton.setOnClickListener { takePhoto() }
        binding.refreshImage.setOnClickListener { startCamera() }
        binding.retakeButton.setOnClickListener {dismiss() }
        binding.confirmButton.setOnClickListener {
            capturedImage?.let {

                /*val path = saveImageToInternalStorage(it, requireContext())
                val prefs = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
                prefs.edit().putString("profile_image_path", path).apply()*/
/*
                val path = saveImageToInternalStorage(it, requireContext())
                Preference.saveUserImagePath(path)*/

                onPhotoTaken(it)
                Log.e("inside dialog bitmap","$it")
                dismiss()
            }
        }
        binding.previewView.post {
            binding.previewView.makeCircular()
        }
    }

    private fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            val rotation = activity?.windowManager?.defaultDisplay?.rotation ?: Surface.ROTATION_0
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetRotation(rotation)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Log.e("CameraDialog", "Camera binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))

        // Hide ImageView & Show Preview
        binding.imageView.visibility = View.GONE
        binding.previewView.visibility = View.VISIBLE
        binding.captureButton.visibility = View.VISIBLE
        binding.retakeButton.visibility = View.GONE
        binding.confirmButton.visibility = View.GONE
    }

    private fun takePhoto() {
        val outputFile = File(requireContext().cacheDir, "temp.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    try {
                        var bitmap = BitmapFactory.decodeFile(outputFile.absolutePath)

                        // 🔄 Read EXIF orientation and rotate accordingly
                        val exif = androidx.exifinterface.media.ExifInterface(outputFile.absolutePath)
                        val rotationDegrees = when (exif.getAttributeInt(
                            androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION,
                            androidx.exifinterface.media.ExifInterface.ORIENTATION_NORMAL
                        )) {
                            androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_90 -> 90
                            androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_180 -> 180
                            androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_270 -> 270
                            else -> 0
                        }

                        if (rotationDegrees != 0) {
                            val matrix = android.graphics.Matrix()
                            matrix.postRotate(rotationDegrees.toFloat())
                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                        }

                        // 🔄 Mirror if using front camera
                        val matrix = android.graphics.Matrix().apply { preScale(-1f, 1f) }
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

                        capturedImage = bitmap

                        binding.imageView.setImageBitmap(bitmap)
                        binding.imageView.visibility = View.VISIBLE
                        binding.previewView.visibility = View.GONE

                        binding.captureButton.visibility = View.GONE
                        binding.retakeButton.visibility = View.VISIBLE
                        binding.confirmButton.visibility = View.VISIBLE
                    } catch (e: Exception) {
                        Log.e("CameraDialog", "Failed to process captured image", e)
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraDialog", "Image capture failed", exception)
                }
            }
        )
    }


    /*fun Bitmap.mirrorHorizontally(): Bitmap {
        val matrix = Matrix().apply { preScale(-1f, 1f) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        cameraExecutor.shutdown()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.BLACK)) // Optional: Set black background
        }
    }

    fun View.makeCircular() {
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                val size = min(view.width, view.height)
                outline.setOval(0, 0, size, size)
            }
        }
        clipToOutline = true
    }


    fun saveImageToInternalStorage(bitmap: Bitmap, context: Context): String {
        val fileName = "profile_image.jpg"
        val file = File(context.filesDir, fileName)
        val outputStream = file.outputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return file.absolutePath
    }


}
