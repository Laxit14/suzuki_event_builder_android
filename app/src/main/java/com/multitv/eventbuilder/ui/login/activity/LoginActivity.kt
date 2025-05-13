package com.multitv.eventbuilder.ui.login.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessaging
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.Repository.Repo
import com.multitv.eventbuilder.base.BaseActivityTwo
import com.multitv.eventbuilder.constant.AppConstant
import com.multitv.eventbuilder.constant.Preference
import com.multitv.eventbuilder.constant.Preference.saveBitmapToInternalStorage
import com.multitv.eventbuilder.databinding.ActivityLoginBinding
import com.multitv.eventbuilder.dialog.ProgressDialog
import com.multitv.eventbuilder.model.loginmodel.model.Data
import com.multitv.eventbuilder.retrofit.RetrofitInstance
import com.multitv.eventbuilder.sealed.Generic
import com.multitv.eventbuilder.ui.dialog_fragment.CameraPopupDialog
import com.multitv.eventbuilder.ui.login.dialog.CameraDialogFragment
import com.multitv.eventbuilder.ui.login.viewmodel.LoginViewModel
import com.multitv.eventbuilder.ui.login.viewmodelfactory.LoginViewModelFactory
import com.multitv.eventbuilder.ui.otp.activity.OtpActivity
import com.multitv.eventbuilder.ui.termandcondition.TermsAndConditionsWebDialogFragment
import com.multitv.eventbuilder.utils.ValidationClass
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class LoginActivity : BaseActivityTwo(), OnClickListener {
    lateinit var binding: ActivityLoginBinding

    private lateinit var viewModel: LoginViewModel
    private val CAMERA_REQUEST_CODE = 101
    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 201
    private var firebaseToken: String = ""
    private var capturedImage: Bitmap? = null
    private var loginData: Data? = null

    private var mobileNo: String? = null
    private var otp: String? = null

    private var userId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Preference.init(this)

        requestNotificationPermission() // 🔔 Ask for notification permission on launch

        binding.cameraIcon.setOnClickListener(this)
        binding.submit.setOnClickListener(this)

        binding.textViewTerms.setOnClickListener {
            // Show Terms and Conditions DialogFragment
            TermsAndConditionsWebDialogFragment().show(
                supportFragmentManager,
                "TermsDialog"
            )
        }


        val repository = Repo(RetrofitInstance.getRetrofit())
        val factory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]


        viewModel.uploadResult.observe(this) { response ->
            when (response) {
                is Generic.Loading -> {
                    ProgressDialog.showProgresssDialog(this@LoginActivity)
                }

                is Generic.Success -> {
                    ProgressDialog.hideProgressDialog()
                    if (response.data.status == 1) {
                        val intent = Intent(this, OtpActivity::class.java)
                        intent.putExtra("LOGIN_DATA", loginData)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this, OtpActivity::class.java)
                        intent.putExtra("LOGIN_DATA", loginData)
                        startActivity(intent)
                        finish()
                    }
                }

                is Generic.Error -> {

                    ProgressDialog.hideProgressDialog()
                }

                is Generic.Failure -> {
                    Toast.makeText(
                        this,
                        response.exception.message ?: "Something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()

                    ProgressDialog.hideProgressDialog()
                }
            }
        }

        viewModel.loginResponse.observe(this) { response ->
            when (response) {
                is Generic.Loading -> {
                    ProgressDialog.showProgresssDialog(this@LoginActivity)
                }

                is Generic.Success -> {
                    ProgressDialog.hideProgressDialog()
                    binding.submit.isEnabled = true
                    val loginResponse = response.data
                    if (loginResponse.success && loginResponse.data != null) {
                        loginData = loginResponse.data
                        userId = response.data.data.id.toString()
                        mobileNo = loginData?.mobile
                        otp = loginData?.otp.toString()


                        capturedImage?.let { bitmap ->
                            val imageFile =
                                bitmapToFile(this@LoginActivity, bitmap, "profile_upload.jpg")

                            viewModel.uploadImage(
                                AppConstant.IMAGECAPTURE,
                                response.data.data.id.toString(),
                                imageFile
                            )

                        } ?: run {
                            val intent = Intent(this, OtpActivity::class.java)
                            intent.putExtra("LOGIN_DATA", loginData)
                            startActivity(intent)
                            finish()
                        }


                    } else {
                        binding.errorText.visibility = View.VISIBLE
                        binding.errorText.text = loginResponse.message
                    }
                }

                is Generic.Error -> {
                    binding.submit.isEnabled = true
                    binding.errorText.visibility = View.VISIBLE
                    binding.errorText.text = response.message
                    ProgressDialog.hideProgressDialog()
                }

                is Generic.Failure -> {
                    Toast.makeText(
                        this,
                        response.exception.message ?: "Something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.errorText.visibility = View.VISIBLE
                    binding.errorText.text = response.exception.message
                    binding.submit.isEnabled = true
                    ProgressDialog.hideProgressDialog()
                }
            }
        }

        getFirebaseToken()
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

    // Convert File to MultipartBody.Part
    fun prepareImagePart(file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("upload_image", file.name, requestFile)
    }

    private fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseToken = task.result
                    Log.d("FirebaseToken", "Token: $firebaseToken")
                } else {
                    Log.e("FirebaseToken", "Fetching FCM token failed", task.exception)
                }
            }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    201
                )
            } else {
                Log.d("Permission", "Notification permission already granted")
            }
        } else {
            Log.d("Permission", "Notification permission not needed for this Android version")
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cameraIcon -> {
                checkPermissions()
            }

            R.id.submit -> {
                validateAndProceed()
            }
        }
    }

    private fun showConditionPopup() {
        val dialog = CameraPopupDialog { openCameraDialog() }
        dialog.show(supportFragmentManager, "CameraPopupDialog")
    }

    private fun openCameraDialog() {
        val cameraDialog = CameraDialogFragment { bitmap ->
            val imagePath = saveBitmapToInternalStorage(this, bitmap, "profile_image.png")
            imagePath?.let {
                Preference.saveUserImagePath(it)
            }
            binding.profileIcon.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.profileIcon.setImageBitmap(bitmap)
            capturedImage = bitmap


        }
        cameraDialog.show(supportFragmentManager, "CameraDialogFragment")
    }

    private fun checkPermissions() {
        val permissions =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val requestCode = 101

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, permissions, requestCode)
        } else {
            showConditionPopup()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            101 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showConditionPopup()
                } else {
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                }
            }

            NOTIFICATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d("Permission", "Notification permission granted")
                } else {
                    Log.w("Permission", "Notification permission denied")
                    Toast.makeText(
                        this,
                        "Notification permission is required for alerts",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun validateAndProceed() {
        val phoneNumber = binding.phoneNo.text.toString().trim()
        val isChecked = binding.checkboxTerms.isChecked
        binding.errorText.visibility = View.GONE
        binding.errorText.text = ""

        when {
            phoneNumber.isEmpty() -> {
                Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show()
            }

            !ValidationClass.phoneValid(phoneNumber) -> {
                Toast.makeText(this, "Enter a valid 10-digit phone number", Toast.LENGTH_SHORT)
                    .show()
            }

            !isChecked -> {
                Toast.makeText(this, "Please agree to the Terms and Conditions", Toast.LENGTH_SHORT).show()

                // Open Terms and Conditions Dialog
                /*TermsAndConditionsWebDialogFragment().show(
                    supportFragmentManager,
                    "TermsDialog"
                )*/
            }

            else -> {
                capturedImage?.let { bitmap ->
                    val imagePath = saveBitmapToInternalStorage(this, bitmap, "profile_image.png")
                    imagePath?.let {
                        Preference.saveUserImagePath(it)

                    }
                }

                binding.submit.isEnabled = false
                viewModel.loginUser(phoneNumber, firebaseToken)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE) {
            capturedImage = data?.extras?.get("data") as? Bitmap
            binding.profileIcon.setImageBitmap(capturedImage)
        }
    }



}
