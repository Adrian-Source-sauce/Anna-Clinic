package com.example.annaclinic.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream


fun encodeBase64(imageUri: Uri, context: Context): String {
    val input = context.contentResolver.openInputStream(imageUri)
    val image = BitmapFactory.decodeStream(input , null, null)

    val baos = ByteArrayOutputStream()
    image?.compress(Bitmap.CompressFormat.JPEG, 50, baos)
    val imageBytes = baos.toByteArray()
    val imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT)
    return imageString
}

fun decodeBase64(imageBase64: String) : Bitmap {
    val imageBytes = Base64.decode(imageBase64, Base64.DEFAULT)
    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

    return decodedImage
}