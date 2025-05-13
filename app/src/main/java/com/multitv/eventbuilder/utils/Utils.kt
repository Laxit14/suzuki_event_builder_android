package com.multitv.eventbuilder.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.graphics.Paint
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date
import java.util.Locale

object Utils {

    fun createPdfFromText(context: Context, text: String,title : String) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
        val page = pdfDocument.startPage(pageInfo)

        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 16f
            isAntiAlias = true
        }

        val x = 20f
        var y = 40f
        text.split("\n").forEach { line ->
            page.canvas.drawText(line, x, y, paint)
            y += 20f
        }

        pdfDocument.finishPage(page)

        // 📌 **Generate Timestamp for Unique Filename**
        val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault())
        val formattedDate = dateFormat.format(Date())  // Example: "2024-03-27_14-30-15"
        var fileName = "${title}_$formattedDate.pdf"

        // ✅ **Check if file already exists and rename if needed**
        var file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
        var counter = 1

        while (file.exists()) {
            fileName = "pankaj_${formattedDate}_$counter.pdf"
            file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
            counter++
        }

        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis() / 1000)
            put(MediaStore.MediaColumns.DATE_MODIFIED, System.currentTimeMillis() / 1000)
        }

        val pdfUri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

        try {
            pdfUri?.let { uri ->
                resolver.openOutputStream(uri)?.use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
            }
            Log.d("LOG PDF", "PDF saved successfully: $fileName")
//            Toast.makeText(context, "PDF Saved Successfully!", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("LOG PDF", "Error saving PDF: ${e.message}")
            Toast.makeText(context, "Failed to save PDF", Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()
        }
    }
    fun getAllPdfFiles(context: Context): List<Pair<String, String>> {
        val pdfList = mutableListOf<Pair<String, String>>() // (FileName, FilePath)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val projection = arrayOf(
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATA // ✅ Get full file path
            )

            val selection = "${MediaStore.Files.FileColumns.MIME_TYPE} = ?"
            val selectionArgs = arrayOf("application/pdf")
            val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"

            val queryUri = MediaStore.Files.getContentUri("external")

            context.contentResolver.query(queryUri, projection, selection, selectionArgs, sortOrder)?.use { cursor ->
                val nameColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val pathColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)

                while (cursor.moveToNext()) {
                    val fileName = cursor.getString(nameColumn)
                    val filePath = cursor.getString(pathColumn)

                    val file = File(filePath)
                    if (file.exists()) {  // ✅ Only add real files
                        pdfList.add(Pair(fileName, filePath))
                    } else {
                        refreshMediaStore(context, filePath) // ✅ Remove ghost entry
                    }
                }
            }
        } else {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (downloadsDir.exists() && downloadsDir.isDirectory) {
                downloadsDir.listFiles()?.forEach { file ->
                    if (file.isFile && file.extension.equals("pdf", ignoreCase = true)) {
                        pdfList.add(Pair(file.name, file.absolutePath))
                    }
                }
            }
        }

        return pdfList
    }
    fun refreshMediaStore(context: Context, filePath: String) {
        val resolver = context.contentResolver
        val uri = MediaStore.Files.getContentUri("external")

        val selection = "${MediaStore.Files.FileColumns.DATA} = ?"
        val selectionArgs = arrayOf(filePath)

        resolver.delete(uri, selection, selectionArgs)  // ✅ Remove entry from MediaStore
    }


}