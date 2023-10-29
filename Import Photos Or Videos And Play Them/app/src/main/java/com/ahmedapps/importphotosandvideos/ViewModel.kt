package com.ahmedapps.importphotosandvideos

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ViewModel: ViewModel(){

    var imageUrl = mutableStateOf("")

    var videoUri: Uri? = null
    var isVideo = false
    var isNewVideoPicked = mutableStateOf(false)
}