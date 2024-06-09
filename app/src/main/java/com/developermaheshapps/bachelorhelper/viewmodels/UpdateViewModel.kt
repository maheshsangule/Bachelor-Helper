package com.developermaheshapps.bachelorhelper.viewmodels

import androidx.lifecycle.ViewModel
import com.developermaheshapps.bachelorhelper.repository.UpdateRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateViewModel(val repo: UpdateRepo) : ViewModel() {
    val downloadLiveData get() = repo.updateLiveData

    fun updateApp() {
        CoroutineScope(Dispatchers.IO).launch {
            repo.updateApp()
        }
    }
}