package com.developermaheshsofttechltd.bachelorhelper.viewmodels

import androidx.lifecycle.ViewModel
import com.developermaheshsofttechltd.bachelorhelper.repository.MainRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(val repo: MainRepo) : ViewModel() {
    val homeLiveData get() = repo.homeLiveData
    val allLiveData get() = repo.allLiveData

    fun getHomeData() {
        CoroutineScope(Dispatchers.IO).launch {
            repo.getHomeData()
        }
    }

    fun getAppData() {
        CoroutineScope(Dispatchers.IO).launch {
            repo.getAppData()
        }
    }

}