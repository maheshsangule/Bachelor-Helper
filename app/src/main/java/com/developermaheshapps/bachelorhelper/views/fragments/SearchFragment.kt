package com.developermaheshapps.bachelorhelper.views.fragments

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.developermaheshapps.bachelorhelper.R
import com.developermaheshapps.bachelorhelper.databinding.FragmentSearchBinding
import com.developermaheshapps.bachelorhelper.databinding.LayoutProgressBinding
import com.developermaheshapps.bachelorhelper.models.BookModel
import com.developermaheshapps.bachelorhelper.repository.MainRepo
import com.developermaheshapps.bachelorhelper.utils.Constants
import com.developermaheshapps.bachelorhelper.utils.MyResponses
import com.developermaheshapps.bachelorhelper.utils.SharedPrefUtils
import com.developermaheshapps.bachelorhelper.utils.SpringScrollHelper
import com.developermaheshapps.bachelorhelper.viewmodels.MainViewModel
import com.developermaheshapps.bachelorhelper.viewmodels.factories.MainViewModelFactory
import com.developermaheshapps.bachelorhelper.views.activities.HomeActivity
import com.developermaheshapps.bachelorhelper.views.adapters.SearchAdapter

class SearchFragment : Fragment() {
    private val list = ArrayList<BookModel>()
    val activity = this

    //val context = requireContext()
    private val binding by lazy { FragmentSearchBinding.inflate(layoutInflater) }
    private lateinit var adapter: SearchAdapter

    private val repo = MainRepo(Activity())
    private lateinit var dialog: Dialog

    private val TAG = "SearchFragment"
    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), MainViewModelFactory(repo))[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {

            svSearchFragment.searchView.apply {
                setOnQueryTextListener(object :
                    androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        // Handle query submission if needed
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText.isNullOrEmpty()) {
                            Constants.hideKeyboard(svSearchFragment.searchView)
                            svSearchFragment.searchView.clearFocus()
//                            mainLogic()
                        } else {
                            filterList(newText)
                        }
                        return true
                    }
                })
            }

            rvSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    Constants.hideKeyboard(svSearchFragment.searchView)
                    svSearchFragment.searchView.clearFocus()
                }
            })


            SharedPrefUtils.init(requireActivity() as HomeActivity)
            val dialogBinding = LayoutProgressBinding.inflate(layoutInflater)
            dialog = Dialog(requireActivity()).apply {
                setCancelable(false)
                setContentView(dialogBinding.root)
                window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window!!.setLayout(
                    ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT
                )
            }
        }
        if (dialog.isShowing) dialog.dismiss()
        adapter = SearchAdapter(list, requireActivity())
        binding.svSearchFragment.searchView.queryHint="Search"
        if (Constants.isConnected(requireActivity())) {
            mainLogic()
        } else {
            mainLogic()
            Constants.checkInternet(requireActivity())
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = binding.root

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
                binding.rvSearch.adapter = adapter
                SpringScrollHelper().attachToRecyclerView(binding.rvSearch)
            } else {

            }

        }

    }

    private fun mainLogic() {
        binding.apply {
            try {

                binding.svSearchFragment.searchView.queryHint="Search"
                dialog.show()
                handleHomeBackend()

                swipeRefreshLayout.setColorScheme(R.color.colorPrimary)
                swipeRefreshLayout.setOnRefreshListener {
                    dialog.show()
                    Handler(Looper.myLooper()!!).postDelayed({
                        if (Constants.isConnected(requireActivity())) {
                            binding.svSearchFragment.searchView.queryHint="Search"
                            handleHomeBackend(true)
                        } else {
                            handleHomeBackend(true)
                            Constants.checkInternet(requireActivity())
                        }
                    }, 1500)
                }


                rvSearch.adapter = adapter
                SpringScrollHelper().attachToRecyclerView(rvSearch)
                viewModel.getAppData()

            } catch (e: Exception) {
                Log.i(TAG, "onDataChange:${e.localizedMessage?.toString()} ")
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleHomeBackend(isShuffle: Boolean = false) {

        try {
            viewModel.allLiveData.observe(requireActivity()) {

                when (it) {
                    is MyResponses.Error -> {
                        dialog.dismiss()
                        binding.swipeRefreshLayout.isRefreshing = false
                        Log.i(TAG, "handleHomeBackend: ${it.errorMessage}")
                    }

                    is MyResponses.Loading -> {
                        Log.i(TAG, "handleHomeBackend: Loading...")
                    }

                    is MyResponses.Success -> {
                        dialog.dismiss()

                        binding.swipeRefreshLayout.isRefreshing = false

                        val tempList = it.data
                        list.clear()

                        if (tempList!!.isNotEmpty()) {
                            binding.apply {
                                mainConstraintLayout.visibility = View.VISIBLE
                                emptyConstraintLayout.visibility = View.GONE
                            }
                            tempList.forEach { data ->
                                data.bl?.forEach { bookData ->

                                    val bookModel = BookModel(
                                        i = bookData.i,
                                        t = bookData.t,
                                        d = bookData.d,
                                        a = bookData.a,
                                        b = bookData.b
                                    )

                                    list.add(bookModel)
                                    if (isShuffle) {
                                        list.shuffle()
                                    }

                                }
                            }
                            adapter.notifyDataSetChanged()
                        } else {
                            binding.apply {
                                emptyLayout.textView3.text="No data found"
                                mainConstraintLayout.visibility = View.GONE
                                emptyConstraintLayout.visibility = View.VISIBLE
                            }
                        }

                    }

                    else -> {
                        dialog.dismiss()

                    }
                }
            }
        } catch (e: Exception) {
            dialog.dismiss()
            e.printStackTrace()
        }
    }
}
