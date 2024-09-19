package com.developermaheshsofttechltd.bachelorhelper.views.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.developermaheshsofttechltd.bachelorhelper.database.AppDatabase
import com.developermaheshsofttechltd.bachelorhelper.databinding.FragmentBookmarksBinding
import com.developermaheshsofttechltd.bachelorhelper.viewmodels.ToolsViewModel
import com.developermaheshsofttechltd.bachelorhelper.viewmodels.factories.ToolsViewModelFactory
import com.developermaheshsofttechltd.bachelorhelper.views.activities.PdfActivity
import com.developermaheshsofttechltd.bachelorhelper.views.adapters.BookmarksAdapter

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarksFragment : BottomSheetDialogFragment() {
    private val binding by lazy {
        FragmentBookmarksBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        val mActivity = requireActivity() as PdfActivity
        ViewModelProvider(mActivity, ToolsViewModelFactory(mActivity))[ToolsViewModel::class.java]
    }

    private val list: MutableList<Int> = mutableListOf()
    private val adapter by lazy {
        BookmarksAdapter(
            list = list,
            viewModel = viewModel,
            fragment = this,
            context = requireActivity()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            mBookmarksRv.adapter = adapter
            getData()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData() {
        val mActivity = requireActivity() as PdfActivity
        val database = AppDatabase.getDatabase(requireActivity())
        CoroutineScope(Dispatchers.IO).launch {
            database?.bookmarksDao()?.getBookmarks(mActivity.bookId)?.forEach {
                list.add(it.pageNo)
            }
            mActivity.runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root


}



















