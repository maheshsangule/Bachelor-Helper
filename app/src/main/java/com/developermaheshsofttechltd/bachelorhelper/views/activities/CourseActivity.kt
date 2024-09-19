package com.developermaheshsofttechltd.bachelorhelper.views.activities

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.developermaheshsofttechltd.bachelorhelper.R
import com.developermaheshsofttechltd.bachelorhelper.databinding.ActivityCourseBinding
import com.developermaheshsofttechltd.bachelorhelper.databinding.LayoutProgressBinding
import com.developermaheshsofttechltd.bachelorhelper.models.HomeModel
import com.developermaheshsofttechltd.bachelorhelper.repository.MainRepo
import com.developermaheshsofttechltd.bachelorhelper.utils.Constants
import com.developermaheshsofttechltd.bachelorhelper.utils.MyResponses
import com.developermaheshsofttechltd.bachelorhelper.utils.SharedPrefUtils
import com.developermaheshsofttechltd.bachelorhelper.utils.SpringScrollHelper
import com.developermaheshsofttechltd.bachelorhelper.viewmodels.MainViewModel
import com.developermaheshsofttechltd.bachelorhelper.viewmodels.factories.MainViewModelFactory
import com.developermaheshsofttechltd.bachelorhelper.views.adapters.BookThumbnailAdapter

class CourseActivity : AppCompatActivity() {


    private val list = ArrayList<HomeModel>()
    val activity = this
    val adapter = BookThumbnailAdapter(activity, list)
    private val binding by lazy { ActivityCourseBinding.inflate(layoutInflater) }
    private val repo = MainRepo(activity)
    private lateinit var dialog: Dialog
    private val viewModel by lazy {
        ViewModelProvider(
            activity, MainViewModelFactory(repo)
        )[MainViewModel::class.java]
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        SharedPrefUtils.init(activity)
        if (Constants.isConnected(activity)) {
            mainLogic()
        } else {
            mainLogic()
            Constants.checkInternet(activity)
        }
    }

    private val TAG = "Home"


    private fun mainLogic() {
        try {
            binding.apply {

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
                handleHomeBackend()


                val title = intent.getStringExtra("catTitle").toString()
                toolBar.title = title
                toolBar.setNavigationOnClickListener {
                    finish()
                }

                swipeRefreshLayout.setColorScheme(R.color.colorPrimary)
                swipeRefreshLayout.setOnRefreshListener {
                    dialog.show()
                    Handler(Looper.myLooper()!!).postDelayed({
                        if (Constants.isConnected(activity)) {
                            handleHomeBackend()
                        } else {
                            handleHomeBackend()
                            Constants.checkInternet(activity)
                        }
                    }, 1500)
                }
                val baseUrl = SharedPrefUtils.getPrefString("baseUrl", "").toString()
                val slideModels = java.util.ArrayList<SlideModel>()
                slideModels.apply {
                    add(SlideModel("${baseUrl}SliderHome/slider1.png", ScaleTypes.FIT))
                    add(SlideModel("${baseUrl}SliderHome/slider6.2.png", ScaleTypes.FIT))
                    add(SlideModel("${baseUrl}SliderHome/slider3.png", ScaleTypes.FIT))
                    add(SlideModel("${baseUrl}SliderHome/slider4.png", ScaleTypes.FIT))
                    add(SlideModel("${baseUrl}SliderHome/slider5.png", ScaleTypes.FIT))
                }

                imageSlider.setSlideAnimation(com.denzcoskun.imageslider.constants.AnimationTypes.DEPTH_SLIDE)
                imageSlider.setImageList(slideModels, ScaleTypes.FIT)


                bookRecycler.adapter = adapter
                SpringScrollHelper().attachToRecyclerView(bookRecycler)
                viewModel.getHomeData()

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleHomeBackend() {
        viewModel.homeLiveData.observe(activity) {

            when (it) {
                is MyResponses.Error -> {
                    if (dialog.isShowing)
                        dialog.dismiss()
                    binding.swipeRefreshLayout.isRefreshing = false
                    Log.i(TAG, "handleHomeBackend: ${it.errorMessage}")
                }

                is MyResponses.Loading -> {
                    Log.i(TAG, "handleHomeBackend: Loading...")
                }

                is MyResponses.Success -> {
                    if (dialog.isShowing)
                        dialog.dismiss()
                    binding.swipeRefreshLayout.isRefreshing = false
                    val tempList = it.data
                    list.clear()
                    tempList?.forEach {
                        list.add(it)
                        adapter.notifyItemChanged(list.size)
                    }
                    Log.i(TAG, "handleHomeBackend: ${it.errorMessage}")
                }

                else -> {
                    if (dialog.isShowing)
                        dialog.dismiss()
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onRestart() {
        super.onRestart()

    }

}