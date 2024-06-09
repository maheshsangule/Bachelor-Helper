package com.developermaheshapps.bachelorhelper.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.developermaheshapps.bachelorhelper.repository.BookRepo
import com.developermaheshapps.bachelorhelper.repository.MainRepo
import com.developermaheshapps.bachelorhelper.viewmodels.BookViewModel
import com.developermaheshapps.bachelorhelper.viewmodels.MainViewModel

class MainViewModelFactory(private val repo:MainRepo):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repo) as T
    }
}