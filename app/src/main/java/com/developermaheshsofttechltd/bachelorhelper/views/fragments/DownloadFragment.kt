package com.developermaheshsofttechltd.bachelorhelper.views.fragments

import android.app.ActionBar
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.developermaheshsofttechltd.bachelorhelper.R
import com.developermaheshsofttechltd.bachelorhelper.databinding.FragmentDownloadBinding
import com.developermaheshsofttechltd.bachelorhelper.databinding.LayoutProgressBinding
import com.developermaheshsofttechltd.bachelorhelper.utils.Constants
import com.developermaheshsofttechltd.bachelorhelper.utils.FOLDER_PATH
import com.developermaheshsofttechltd.bachelorhelper.utils.SharedPreferencesHelper
import com.developermaheshsofttechltd.bachelorhelper.utils.SpringScrollHelper
import com.developermaheshsofttechltd.bachelorhelper.views.adapters.DownloadFragmentAdapter
import com.developermaheshsofttechltd.bachelorhelper.views.adapters.DownloadModel
import java.io.File

class DownloadFragment : Fragment() {

    private val list = ArrayList<DownloadModel>()
    val activity = this
    private lateinit var adapter: DownloadFragmentAdapter
    private lateinit var dialog: Dialog
    private val binding by lazy { FragmentDownloadBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {

            val dialogBinding = LayoutProgressBinding.inflate(layoutInflater)
            dialog = Dialog(requireActivity()).apply {
                setCancelable(false)
                setContentView(dialogBinding.root)
                window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window!!.setLayout(
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT
                )
            }
//            dialog.show()
//            Handler(Looper.myLooper()!!).postDelayed({
                mainLogic()
//            }, 1500)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<DownloadModel>()
            for (item in list) {
                if (item.name.contains(query, ignoreCase = true)) {
                    filteredList.add(item)
                }
            }

            if (filteredList.isNotEmpty()) {
                adapter = DownloadFragmentAdapter(filteredList, requireActivity())
                binding.rvDownload.adapter = adapter
                SpringScrollHelper().attachToRecyclerView(binding.rvDownload)
            }

        }

    }

    private fun mainLogic() {
        binding.apply {
            svDownloadFragment.searchView.queryHint="Search Downloads"
            emptyLayout.textView3.text="No downloads found"
            svDownloadFragment.searchView.apply {
                setOnQueryTextListener(object :
                    androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        // Handle query submission if needed
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText.isNullOrEmpty()) {
                            Constants.hideKeyboard(svDownloadFragment.searchView)
                            svDownloadFragment.searchView.clearFocus()
//                            mainLogic()
                        } else {
                            filterList(newText)
                        }
                        return true
                    }
                })
            }
        }

        binding.rvDownload.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Constants.hideKeyboard(binding.svDownloadFragment.searchView)
                binding.svDownloadFragment.searchView.clearFocus()
            }
        })

//        val downloadDir =
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val titleList = ArrayList<String>()
        list.clear()
        val retrievedList = SharedPreferencesHelper.getStringList(requireActivity()).toSet()
        for (list in retrievedList.reversed()) {
            titleList.add(list)
        }
        Log.i("VISHAY", "titleList:${titleList}")
        binding.apply {

            swipeRefreshLayout.setColorScheme(R.color.colorPrimary)
            swipeRefreshLayout.setOnRefreshListener {
                dialog.show()
                Handler(Looper.myLooper()!!).postDelayed({
                    mainLogic()
                }, 1500)
            }

            val iterator = titleList.iterator()

            if (titleList.isEmpty()) {

                emptyLayout.textView3.text="No downloads found"
                mainConstraintLayout.visibility = View.GONE
                emptyConstraintLayout.visibility = View.VISIBLE
                if (dialog.isShowing)
                    dialog.dismiss()
                swipeRefreshLayout.isRefreshing = false
            } else {



                while (iterator.hasNext()) {
                    val title = iterator.next()
                    Log.i("VISHAY", "title:${title}\n")
                    val file = File("${context?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)}/$FOLDER_PATH/", title)
                    if (!file.exists()) {
                        if (dialog.isShowing)
                            dialog.dismiss()
                        swipeRefreshLayout.isRefreshing = false
                        iterator.remove()  // Use iterator to remove elements
                        SharedPreferencesHelper.saveStringList(requireActivity(), titleList)
                        Log.i("VISHAY", "titleList:${titleList}")
                        if (titleList.isEmpty()) {
                            if (dialog.isShowing)
                                dialog.dismiss()
                            swipeRefreshLayout.isRefreshing = false
                            mainConstraintLayout.visibility = View.GONE
                            emptyConstraintLayout.visibility = View.VISIBLE
                        }
                    } else {
                        emptyLayout.textView3.text="No downloads found"
                        mainConstraintLayout.visibility = View.VISIBLE
                        emptyConstraintLayout.visibility = View.GONE
                        if (dialog.isShowing)
                            dialog.dismiss()
                        swipeRefreshLayout.isRefreshing = false
                        list.add(
                            DownloadModel(
                                file.nameWithoutExtension,
                                "${file.toURI()}",
                                "${file.lastModified()}"
                            )
                        )
                    }
                }
            }

            adapter = DownloadFragmentAdapter(list, requireActivity())
            rvDownload.layoutManager = LinearLayoutManager(requireActivity())
            rvDownload.adapter = adapter
        }

    }

}