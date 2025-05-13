package com.multitv.eventbuilder.constant

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import com.multitv.eventbuilder.model.loginmodel.model.Data
import java.io.File
import java.io.FileOutputStream

object Preference {
    private const val PREF_NAME = "Event"
    private lateinit var sharedPreferences: SharedPreferences

    var IsLogin = "loginuser"
    private const val KEY_USER_Id = "USER_ID"
    private const val KEY_USER_NAME = "USER_NAME"
    private const val KEY_USER_EMAIL = "USER_EMAIL"
    private const val KEY_USER_MOBILE = "USER_MOBILE"
    private const val KEY_USER_IMAGE = "USER_IMAGE"
    private const val KEY_USER_CAT = "USER_CATEGORY"
    private const val IMAGE = "IMAGE"


    private const val KEY_FCM_TOKEN = "FCM_TOKEN"


    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveBooleanData(key :String,value : Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key,value)
        editor.apply()
    }

    fun getBooleanData(key:String) : Boolean{
     return sharedPreferences.getBoolean(key,false)
    }

    // Save all user data
    /*fun saveUserData(user: OtpData) {
        with(sharedPreferences.edit()) {
            putString(KEY_USER_NAME, user.name)
            putString(KEY_USER_EMAIL, user.email)
            putString(KEY_USER_MOBILE, user.mobile)
            putString(KEY_USER_IMAGE, user.image5)
            apply()
        }
    }*/

    fun saveUserData(user: Data) {
        with(sharedPreferences.edit()) {
            putLong(KEY_USER_Id, user.id)
            putString(KEY_USER_NAME, user.name)
            putString(KEY_USER_EMAIL, user.email)
            putString(KEY_USER_MOBILE, user.mobile)
            putString(KEY_USER_CAT, user.category)
            putString(IMAGE, user.uploadImage)
            /* putString(KEY_USER_IMAGE, user.image5)*/
            apply()
        }
    }
    fun getUserId(): Long? = sharedPreferences.getLong(KEY_USER_Id, 0)
    fun getUserName(): String? = sharedPreferences.getString(KEY_USER_NAME, null)
    fun getUserEmail(): String? = sharedPreferences.getString(KEY_USER_EMAIL, null)
    fun getUserMobile(): String? = sharedPreferences.getString(KEY_USER_MOBILE, null)
    fun getUserImage(): String? = sharedPreferences.getString(KEY_USER_IMAGE, null)
    fun getUserCategory(): String? = sharedPreferences.getString(KEY_USER_CAT, null)
    fun getImage(): String? = sharedPreferences.getString(IMAGE, null)

    fun clearUserData() {
        sharedPreferences.edit().clear().apply()
    }

    // Add to Preference object
    fun saveUserImagePath(path: String) {
        sharedPreferences.edit().putString(IMAGE, path).apply()
    }

    fun getUserImagePath(): String? {
        return sharedPreferences.getString(KEY_USER_IMAGE, null)
    }

    fun saveFCMToken(token: String) {
        sharedPreferences.edit().putString(KEY_FCM_TOKEN, token).apply()
    }

    fun getFCMToken(): String? {
        return sharedPreferences.getString(KEY_FCM_TOKEN, null)
    }


    fun saveBitmapToInternalStorage(context: Context, bitmap: Bitmap, fileName: String): String? {
        return try {
            val file = File(context.filesDir, fileName)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }



}