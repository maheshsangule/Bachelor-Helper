package com.developermaheshsofttechltd.bachelorhelper.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.developermaheshsofttechltd.bachelorhelper.repository.MainRepo
import com.developermaheshsofttechltd.bachelorhelper.viewmodels.MainViewModel

class MainViewModelFactory(private val repo:MainRepo):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repo) as T
    }
}