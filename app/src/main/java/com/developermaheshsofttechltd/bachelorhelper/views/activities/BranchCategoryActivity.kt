package com.developermaheshsofttechltd.bachelorhelper.views.activities

import android.app.ActionBar
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.developermaheshsofttechltd.bachelorhelper.R
import com.developermaheshsofttechltd.bachelorhelper.databinding.ActivityBranchCategoryBinding
import com.developermaheshsofttechltd.bachelorhelper.databinding.LayoutProgressBinding
import com.developermaheshsofttechltd.bachelorhelper.models.BookModel
import com.developermaheshsofttechltd.bachelorhelper.utils.Constants
import com.developermaheshsofttechltd.bachelorhelper.utils.SpringScrollHelper
import com.developermaheshsofttechltd.bachelorhelper.views.adapters.BranchChildAdapter

class BranchCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBranchCategoryBinding
    private lateinit var dialog: Dialog
    private val activity = this
    private val list = ArrayList<BookModel>()

    //    private val adapter = BranchChildAdapter(list, activity)
    private lateinit var adapter: BranchChildAdapter
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityBranchCategoryBinding.inflate(layoutInflater)
        adapter = BranchChildAdapter(list,activity,intent.getStringExtra("categoryTitle").toString())
        setContentView(binding.root)
        val dialogBinding = LayoutProgressBinding.inflate(layoutInflater)
        dialog = Dialog(activity).apply {
            setCancelable(false)
            setContentView(dialogBinding.root)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window!!.setLayout(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT
            )
        }


        dialog.show()
        if (Constants.isConnected(activity)) {
           setData(false)
        } else {
           setData(false)
            Constants.checkInternet(activity)
        }
        binding.apply {
            binding.mRvCategory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    Constants.hideKeyboard(svBranchCategory.searchView)
                    svBranchCategory.searchView.clearFocus()
                }
            })
            appBarLayout.visibility = View.VISIBLE
            linearLayout.visibility = View.GONE
            toolBar.setOnMenuItemClickListener { it ->

                when (it.itemId) {
                    R.id.action_search -> {
                        appBarLayout.visibility = View.GONE
                        linearLayout.visibility = View.VISIBLE
                    }
                }


                return@setOnMenuItemClickListener true
            }
            ibtnBackIcon.setOnClickListener {
                finish()
            }

            svBranchCategory.searchView.apply {
                setOnQueryTextListener(object :
                    androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        // Handle query submission if needed
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText.isNullOrEmpty()) {
                            Constants.hideKeyboard(svBranchCategory.searchView)
                            svBranchCategory.searchView.clearFocus()
//                            mainLogic()
                        } else {
                            filterList(newText)
                        }
                        return true
                    }
                })
            }

            swipeRefreshLayout.setColorScheme(R.color.colorPrimary)
            swipeRefreshLayout.setOnRefreshListener {
                dialog.show()
                Handler(Looper.myLooper()!!).postDelayed({
                    if (Constants.isConnected(activity)) {
                       setData(true)
                    } else {
                       setData(true)
                        Constants.checkInternet(activity)
                    }
                }, 1500)
            }
            toolBar.setNavigationOnClickListener {
                finish()
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
                adapter = BranchChildAdapter(filteredList, activity)
                binding.mRvCategory.adapter = adapter
                SpringScrollHelper().attachToRecyclerView(binding.mRvCategory)
            }

        }

    }

    private fun setData(isShuffle: Boolean) {
        binding.apply {

            svBranchCategory.searchView.queryHint = "Search"
            val title = intent.getStringExtra("categoryTitle").toString()
            toolBar.title = title
            mRvCategory.adapter = adapter
            SpringScrollHelper().attachToRecyclerView(mRvCategory)
            val bookList = intent.getSerializableExtra("book_list") as ArrayList<BookModel>
            binding.swipeRefreshLayout.isRefreshing = false
            list.clear()
            if (dialog.isShowing)
                dialog.dismiss()
            bookList.forEach {
                list.add(it)
                if (isShuffle) {
                    list.shuffle()
                }

            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        with(window)
        {
            sharedElementReenterTransition = null
            sharedElementReturnTransition = null
        }
        binding.mRvCategory.transitionName = null


    }
}