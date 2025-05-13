package com.multitv.eventbuilder.ui.conference.conferenceGallery.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.URLUtil
import android.webkit.WebView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.multitv.eventbuilder.R
import java.io.File
import java.io.FileOutputStream

class SecondTabFragment : Fragment() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second_tab, container, false)
        val webView = view.findViewById<WebView>(R.id.webViewSecondTab)

        webView.settings.javaScriptEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.domStorageEnabled = true

        // Add JS interface
        webView.addJavascriptInterface(WebAppInterface(requireContext()), "Android")

        // Load the web page
        webView.loadUrl("https://vapi.multitvsolution.com/msvc/gallery.html")

        return view
    }


    class WebAppInterface(private val context: Context) {

        @JavascriptInterface
        fun downloadBase64Image(base64Data: String) {
            saveImageToGalleryFromBase64(context, base64Data)
        }


        fun saveImageToGalleryFromBase64(context: Context, base64Data: String) {
            val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }

            val missingPermissions = permissions.filter {
                ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
            }

            if (missingPermissions.isNotEmpty()) {
                ActivityCompat.requestPermissions(
                    (context as Activity),
                    missingPermissions.toTypedArray(),
                    101
                )
                Toast.makeText(context, "Please allow storage permission", Toast.LENGTH_SHORT).show()
                return
            }

            try {
                val base64Image = base64Data.substringAfter("base64,", "")
                if (base64Image.isNotEmpty()) {
                    val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)
                    val fileName = "image_${System.currentTimeMillis()}.jpg"

                    val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    if (!picturesDir.exists()) picturesDir.mkdirs()

                    val imageFile = File(picturesDir, fileName)

                    FileOutputStream(imageFile).use { it.write(imageBytes) }

                    MediaScannerConnection.scanFile(context, arrayOf(imageFile.absolutePath), null, null)

                    Toast.makeText(context, "Image saved to Gallery: ${imageFile.name}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to decode image", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
                // Optionally retry saving image here
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
