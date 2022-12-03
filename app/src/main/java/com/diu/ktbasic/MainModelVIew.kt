package com.diu.ktbasic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainModelVIew : ViewModel()
{
    private val v : MutableLiveData<String> = MutableLiveData()

    fun saveVal(valu: String)
    {
        v.value= valu
    }

    fun getVal(): MutableLiveData<String>{ return v }

}