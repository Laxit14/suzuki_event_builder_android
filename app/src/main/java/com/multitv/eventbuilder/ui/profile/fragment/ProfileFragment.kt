package com.multitv.eventbuilder.ui.profile.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.constant.AppConstant
import com.multitv.eventbuilder.constant.Preference
import com.multitv.eventbuilder.constant.Preference.saveBitmapToInternalStorage
import com.multitv.eventbuilder.databinding.FragmentProfileBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.login.activity.LoginActivity
import com.multitv.eventbuilder.ui.login.dialog.CameraDialogFragment
import com.multitv.eventbuilder.ui.login.viewmodel.LoginViewModel
import com.multitv.eventbuilder.ui.login.viewmodelfactory.LoginViewModelFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ProfileFragment : Fragment(), View.OnClickListener {
    private lateinit var viewModel: LoginViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Preference.init(requireContext())
        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        val imagePath = Preference.getUserImagePath()
        Log.d("LoadImagePath", "Trying to load image from path: $imagePath")

        /* if (!imagePath.isNullOrEmpty()) {
             val file = File(imagePath)
             if (file.exists()) {
                 val bitmap = BitmapFactory.decodeFile(imagePath)
                 binding.lacation1.setImageBitmap(bitmap)
             } else {
                 Log.e("ImageLoad", "Image file not found at path: $imagePath")
                 Glide.with(requireContext()).load("").placeholder(R.drawable.login_placeholder).into(binding.lacation1)

             }
         } else {
             Log.e("ImageLoad", "Image path is null or empty")
             Glide.with(requireContext()).load("").placeholder(R.drawable.login_placeholder).into(binding.lacation1)
         }*/

        val imageUrl = Preference.getImage()
        val defaultImageUrl = "https://vapi.multitvsolution.com/msvc/user_profile.png"

        Glide.with(requireContext())
            .load(if (imageUrl.isNullOrEmpty()) defaultImageUrl else imageUrl)
            .centerCrop()  // Ensures the image is center-cropped
            .placeholder(R.drawable.camera_24)  // Placeholder as a local image
            .error(R.drawable.camera_24)       // Error as a local image
            .into(binding.lacation1)




        binding.backArrow.setOnClickListener(this)
        binding.logoutll.setOnClickListener(this)
        binding.lacation1.setOnClickListener(this)
        setProfileData()

        viewModel.uploadResult.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Generic.Loading -> {
                    ProgressDialog.showProgresssDialog(requireContext())
                }

                is Generic.Success -> {
                    ProgressDialog.hideProgressDialog()
                    if (response.data.status == 1) {
                        Toast.makeText(
                            requireContext(),
                            "Image uploaded successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        Preference.saveUserImagePath(response.data.uploadImage)

                    }
                }

                is Generic.Error -> {

                    ProgressDialog.hideProgressDialog()
                }

                is Generic.Failure -> {
                    Toast.makeText(
                        requireContext(),
                        response.exception.message ?: "Something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()

                    ProgressDialog.hideProgressDialog()
                }
            }
        }
    }

    /*override fun getStatusBarView(): View? {
        return  return binding.frameLayout
    }*/

    private fun setProfileData() {
        // Get data from SharedPreferences
        val name = Preference.getUserName()
        val email = Preference.getUserEmail()
        val mobile = Preference.getUserMobile()
        val category = Preference.getUserCategory()

        // Set to UI
        binding.profileName.text = name
        binding.emailContent.text = email
        binding.userMobileNoContent.text = mobile
        binding.msilText.text = category


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backArrow -> {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            R.id.logoutll -> {
                showLogoutDialog()
            }

            R.id.lacation1 -> {

                if (Preference.getImage().isNullOrEmpty()){
                    checkPermissions()
                }

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                showConditionPopup()
            } else {
                Toast.makeText(requireContext(), "Camera and Storage permissions are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLogoutDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { dialog, _ ->
                performLogout()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


        private fun checkPermissions() {
            val requestCode = 101
            val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            } else {
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }

            val allPermissionsGranted = permissions.all {
                ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
            }

            if (!allPermissionsGranted) {
                val showRationale = permissions.any {
                    shouldShowRequestPermissionRationale(it)
                }

                if (showRationale) {
                    // Show normal permission dialog
                    requestPermissions(permissions, requestCode)
                } else {
                    // User might have clicked "Don't ask again"
                    AlertDialog.Builder(requireContext())
                        .setTitle("Permission Needed")
                        .setMessage("Camera and Storage permissions are required. Please allow them from App Settings.")
                        .setPositiveButton("Go to Settings") { _, _ ->
                            openAppSettings()
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
            } else {
                showConditionPopup()
            }
        }

        private fun openAppSettings() {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", requireContext().packageName, null)
            intent.data = uri
            startActivity(intent)
        }

    private fun showConditionPopup() {
        openCameraDialog()
    }

    private var capturedImage: Bitmap? = null

    private fun openCameraDialog() {
        val cameraDialog = CameraDialogFragment { bitmap ->
            val imagePath =
                saveBitmapToInternalStorage(requireContext(), bitmap, "profile_image.png")
            imagePath?.let {
              //  Preference.saveUserImagePath(it)
            }
            binding.lacation1.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.lacation1.setImageBitmap(bitmap)
            capturedImage = bitmap

            capturedImage?.let { bitmap ->
                val imageFile = bitmapToFile(requireContext(), bitmap, "profile_upload.jpg")

                viewModel.uploadImage(
                    AppConstant.IMAGECAPTURE,
                    Preference.getUserId().toString(),
                    imageFile
                )

            }


        }
        cameraDialog.show(parentFragmentManager, "CameraDialogFragment")
    }

    private fun bitmapToFile(context: Context, bitmap: Bitmap, fileName: String): File {
        var scaledBitmap = bitmap
        var quality = 100
        val maxFileSize = 5 * 1024 * 1024 // 5MB
        val file = File(context.cacheDir, fileName)

        do {
            val outputStream = ByteArrayOutputStream()
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            val byteArray = outputStream.toByteArray()

            if (byteArray.size <= maxFileSize || quality <= 10) {
                // If size is okay or quality too low, write to file
                file.writeBytes(byteArray)
                return file
            }

            // Reduce quality
            quality -= 5

            // If quality is too low but still over 5MB, resize the bitmap
            if (quality <= 10 && byteArray.size > maxFileSize) {
                val newWidth = (scaledBitmap.width * 0.9).toInt()
                val newHeight = (scaledBitmap.height * 0.9).toInt()
                scaledBitmap = Bitmap.createScaledBitmap(scaledBitmap, newWidth, newHeight, true)
                quality = 100 // Reset quality to try again with smaller bitmap
            }

        } while (true)
    }


    private fun performLogout() {
        // Clear stored data
        Preference.clearUserData()
        Preference.saveBooleanData(Preference.IsLogin, false)

        // Navigate to LoginActivity and clear backstack
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }


    override fun onDestroy() {
        ProgressDialog.hideProgressDialog()
        super.onDestroy()
    }


    override fun onResume() {
        ProgressDialog.hideProgressDialog()
        super.onResume()
    }



}