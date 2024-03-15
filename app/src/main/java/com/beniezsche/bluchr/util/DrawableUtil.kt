package com.beniezsche.bluchr.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import java.io.ByteArrayOutputStream

object DrawableUtil {

    fun drawableToBase64(drawable: Drawable): String? {
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
        return null
    }

    fun base64ToDrawable(base64String: String?): Drawable? {
        if (base64String.isNullOrEmpty()) return null

        try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            return BitmapDrawable(null, bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}