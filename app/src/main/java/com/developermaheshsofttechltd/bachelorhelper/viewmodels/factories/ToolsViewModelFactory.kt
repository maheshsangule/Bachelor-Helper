package com.developermaheshsofttechltd.bachelorhelper.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.developermaheshsofttechltd.bachelorhelper.views.activities.PdfActivity
import com.developermaheshsofttechltd.bachelorhelper.viewmodels.ToolsViewModel

class ToolsViewModelFactory(
    private val activity: PdfActivity
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ToolsViewModel(activity) as T
    }
}