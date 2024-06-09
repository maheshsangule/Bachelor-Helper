package com.developermaheshapps.bachelorhelper.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.developermaheshapps.bachelorhelper.R
import com.developermaheshapps.bachelorhelper.databinding.FragmentPdfToolsBinding
import com.developermaheshapps.bachelorhelper.enums.ToolsType
import com.developermaheshapps.bachelorhelper.models.ToolsModel
import com.developermaheshapps.bachelorhelper.viewmodels.ToolsViewModel
import com.developermaheshapps.bachelorhelper.viewmodels.factories.ToolsViewModelFactory
import com.developermaheshapps.bachelorhelper.views.activities.PdfActivity
import com.developermaheshapps.bachelorhelper.views.adapters.ToolsAdapter

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PdfToolsFragment : BottomSheetDialogFragment() {

    private val binding by lazy {
        FragmentPdfToolsBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        val mActivity = requireActivity() as PdfActivity
        ViewModelProvider(mActivity, ToolsViewModelFactory(mActivity))[ToolsViewModel::class.java]
    }
    private val list: MutableList<ToolsModel> = mutableListOf()
    private val adapter by lazy {
        ToolsAdapter(list = list,viewModel=viewModel,fragment=this, context = requireActivity())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rvinit()
    }

    private fun rvinit() {
        binding.mToolsRV.adapter = adapter
        // let's add tools

        list.apply {
            add(ToolsModel("Bookmark Me", R.drawable.ic_bookmark, ToolsType.ADD_TO_BOOKMARKS))
            add(ToolsModel("Bookmarks", R.drawable.ic_bookmarks, ToolsType.BOOKMARKS))
            add(ToolsModel("Take Notes", R.drawable.ic_mynotes, ToolsType.NOTES))
            add(ToolsModel("Night Mode", R.drawable.ic_nightmode, ToolsType.NIGHT_MODE))
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root


}



















