package com.developermaheshapps.bachelorhelper.views.fragments

import android.app.ActionBar
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.developermaheshapps.bachelorhelper.R
import com.developermaheshapps.bachelorhelper.databinding.FragmentBookmarkBinding
import com.developermaheshapps.bachelorhelper.databinding.LayoutProgressBinding
import com.developermaheshapps.bachelorhelper.models.BookModel
import com.developermaheshapps.bachelorhelper.utils.Constants
import com.developermaheshapps.bachelorhelper.utils.SharedPreferencesHelper
import com.developermaheshapps.bachelorhelper.utils.SpringScrollHelper
import com.developermaheshapps.bachelorhelper.views.adapters.SearchAdapter

class BookmarkFragment : Fragment() {
    //    private lateinit var itemViewModel: ItemViewModel
    private val list = ArrayList<BookModel>()
    private lateinit var dialog: Dialog

    private lateinit var adapter: SearchAdapter
    private val binding by lazy { FragmentBookmarkBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dialogBinding = LayoutProgressBinding.inflate(layoutInflater)
        dialog = Dialog(requireActivity()).apply {
            setCancelable(false)
            setContentView(dialogBinding.root)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window!!.setLayout(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT
            )
        }
        if (Constants.isConnected(requireActivity())) {
            mainLogic()
        } else {
            Constants.checkInternet(requireActivity())
        }
        binding.apply {
            svSearchHistory.searchView.apply {

                setOnQueryTextListener(object :
                    androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        // Handle query submission if needed
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText.isNullOrEmpty()) {
                            Constants.hideKeyboard(svSearchHistory.searchView)
                            svSearchHistory.searchView.clearFocus()
//                            mainLogic()
                        } else {
                            filterList(newText)
                        }
                        return true
                    }
                })
            }

            rvHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    Constants.hideKeyboard(svSearchHistory.searchView)
                    svSearchHistory.searchView.clearFocus()
                }
            })
            swipeRefreshLayout.setColorScheme(R.color.colorPrimary)
            swipeRefreshLayout.setOnRefreshListener {
                dialog.show()
                Handler(Looper.myLooper()!!).postDelayed({
                    if (Constants.isConnected(requireActivity())) {
                        binding.svSearchHistory.searchView.queryHint = "Search history"
                        mainLogic()
                    } else {
                        mainLogic()
                        Constants.checkInternet(requireActivity())
                    }
                }, 1500)
            }
        }
    }

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<BookModel>()
            for (item in list) {
                if (item.t.contains(query, ignoreCase = true)) {
                    filteredList.add(item)
                }
            }

            if (filteredList.isNotEmpty()) {
                adapter = SearchAdapter(filteredList, requireActivity())
                binding.rvHistory.adapter = adapter
                SpringScrollHelper().attachToRecyclerView(binding.rvHistory)
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    private fun mainLogic() {
        binding.apply {
            if (dialog.isShowing)
                dialog.dismiss()
            binding.swipeRefreshLayout.isRefreshing = false
            list.clear()
            val retrievedBookList = SharedPreferencesHelper.getBookList(requireActivity())
            val bookList = ArrayList(retrievedBookList)

            if (bookList.isNotEmpty()) {
                emptyConstraintLayout.visibility = View.GONE
                mainConstraintLayout.visibility = View.VISIBLE
                for (i in bookList.toSet()) {
                    val bookInfo = i.split("|~|, ")
                    val imageUrl = bookInfo[0].trim().removePrefix("[")
                    val title = bookInfo[1]
                    val author = bookInfo[2]
                    val pdfUrl = bookInfo[3]
                    val description = bookInfo[4].trim().removeSuffix("|~|]")

                    val bookModel = BookModel(imageUrl, title, description, author, pdfUrl)
                    list.add(bookModel)
                    Log.i("VISHAY", "ForLoop:${bookModel}\n")
                }
                Log.i("VISHAY", "EndElement:${list.last()}")
                val list1: ArrayList<BookModel> = ArrayList(list.reversed())

                adapter = SearchAdapter(list1, requireActivity())
                rvHistory.adapter = adapter
                SpringScrollHelper().attachToRecyclerView(rvHistory)
            } else {
                emptyLayout.textView3.text = "You haven't explore\nanything"
                emptyConstraintLayout.visibility = View.VISIBLE
                mainConstraintLayout.visibility = View.GONE
            }

        }
    }

}