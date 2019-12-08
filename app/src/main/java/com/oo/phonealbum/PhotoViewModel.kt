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
import kotlinx.coroutines.launch

class PhotoViewModel : ViewModel() {
    val photoLiveData = MutableLiveData<ArrayList<PhotoDataModel>>()
    val photoDataState = MutableLiveData<PhotoDataState>()

    fun getPhotoDatas(context:Context){
        GlobalScope.launch  {
            photoDataState.postValue(PhotoDataState.LOADING)
            val photoAlbum = PhotoReponsitory.getPhotoAlbum(context)
            photoLiveData.postValue(photoAlbum)
            photoDataState.postValue(PhotoDataState.SUCCESS)
        }
    }

}