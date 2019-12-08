package com.oo.phonealbum

import android.content.ContentResolver
import android.content.Context
import android.provider.DocumentsProvider
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider

object PhotoReponsitory {

    private val photoMemoryCache = ArrayList<PhotoDataModel>()

    fun getPhotoAlbum(context: Context):ArrayList<PhotoDataModel>{

        val dataList = ArrayList<PhotoDataModel>()
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.let {
            it.moveToFirst()
            it.moveToPrevious()
            while (it.moveToNext()) {
                val pathIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
                val nameIndex = it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                val photoDataModel =
                    PhotoDataModel(it.getString(pathIndex), it.getString(nameIndex))
                dataList.add(photoDataModel)
                Log.i("adapter", "------------")
            }
        }
        cursor?.close()
        return dataList
    }



}