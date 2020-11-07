package com.example.piano_tiles_kw.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainVM: ViewModel() {
    //contoh
    private val contohLiveData = MutableLiveData<String>()
    fun getContoh (): LiveData<String> = contohLiveData
    fun setContoh (s:String){
        contohLiveData.value = s
    }
    //end of contoh

    init {
        contohLiveData.value = "Hello World"
    }

}