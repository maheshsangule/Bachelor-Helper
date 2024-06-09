package com.developermaheshapps.bachelorhelper.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.developermaheshapps.bachelorhelper.repository.BookRepo
import com.developermaheshapps.bachelorhelper.viewmodels.BookViewModel

class BookViewModelFactory(private val repo:BookRepo):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookViewModel(repo) as T
    }
}