package com.developermaheshsofttechltd.bachelorhelper.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.developermaheshsofttechltd.bachelorhelper.repository.UpdateRepo
import com.developermaheshsofttechltd.bachelorhelper.viewmodels.UpdateViewModel


class UpdateViewModelFactory(private val repo: UpdateRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UpdateViewModel(repo) as T
    }
}