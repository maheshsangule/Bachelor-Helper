package com.developermaheshsofttechltd.bachelorhelper.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.developermaheshsofttechltd.bachelorhelper.repository.BookRepo
import com.developermaheshsofttechltd.bachelorhelper.viewmodels.BookViewModel

class BookViewModelFactory(private val repo:BookRepo):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookViewModel(repo) as T
    }
}