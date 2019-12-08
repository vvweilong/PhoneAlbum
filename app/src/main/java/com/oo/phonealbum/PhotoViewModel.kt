package com.oo.phonealbum

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsProvider
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.provider.FontsContractCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class PhotoViewModel : ViewModel() {
    val photoLiveData = MutableLiveData<ArrayList<PhotoDataModel>>()
    val photoDataState = MutableLiveData<PhotoDataState>()

    fun getPhotoDatas(context:Context){
        GlobalScope.async(Dispatchers.IO) {
            val cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null
            )

            cursor?.let {
                photoDataState.postValue(PhotoDataState.LOADING)
                it.moveToFirst()
                it.moveToPrevious()

                val dataList = ArrayList<PhotoDataModel>()

                while (it.moveToNext()) {
                    val pathIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
                    val nameIndex = it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                    val photoDataModel =
                        PhotoDataModel(it.getString(pathIndex), it.getString(nameIndex))
                    dataList.add(photoDataModel)
                    Log.i("adapter", "------------")
                }

                photoLiveData.postValue(dataList)
                photoDataState.postValue(PhotoDataState.SUCCESS)

            }
            cursor?.close()
        }
    }

}