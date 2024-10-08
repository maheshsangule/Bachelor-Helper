package com.developermaheshsofttechltd.bachelorhelper.views.fragments

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.`1`.bachelorhelper.databinding.LayoutProgressBinding
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.developermaheshsofttechltd.bachelorhelper.R
import com.developermaheshsofttechltd.bachelorhelper.databinding.FragmentHomeBinding
import com.developermaheshsofttechltd.bachelorhelper.databinding.LayoutProgressBinding
import com.developermaheshsofttechltd.bachelorhelper.models.DegreeModel
import com.developermaheshsofttechltd.bachelorhelper.models.GroupModel
import com.developermaheshsofttechltd.bachelorhelper.models.HorizontalScrollViewModel
import com.developermaheshsofttechltd.bachelorhelper.utils.Constants
import com.developermaheshsofttechltd.bachelorhelper.utils.SharedPrefUtils
import com.developermaheshsofttechltd.bachelorhelper.utils.loadUserProfile
import com.developermaheshsofttechltd.bachelorhelper.views.activities.HomeActivity
import com.developermaheshsofttechltd.bachelorhelper.views.adapters.DegreeAdapter
import com.developermaheshsofttechltd.bachelorhelper.views.adapters.GroupAdapter
import com.developermaheshsofttechltd.bachelorhelper.views.adapters.HorizontalScrollViewAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class HomeFragment : Fragment() {

    var baseUrl: String? = null

    fun updateUserDetails() {
        binding.apply {
            val userProfile = SharedPrefUtils.getPrefString("profilePic", "").toString()
            if (userProfile.isNotEmpty())
                ivProfilePic.loadUserProfile(userProfile)


            val userName = SharedPrefUtils.getPrefString("name", "")
            tvName.text = userName.toString()
        }
    }

    private lateinit var dialog: Dialog

    private val courseList = ArrayList<DegreeModel>()
    private val groupList = ArrayList<GroupModel>()
    private val horizontalList = ArrayList<HorizontalScrollViewModel>()


    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseUrl = SharedPrefUtils.getPrefString("baseUrl", "").toString()

        val dialogBinding = LayoutProgressBinding.inflate(layoutInflater)
        dialog = Dialog(context as Activity).apply {
            setCancelable(false)
            setContentView(dialogBinding.root)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window!!.setLayout(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT
            )
        }

        if (!Constants.isConnected(requireActivity())) {
            mainLogic()
            Constants.checkInternet(requireActivity())
        } else {
            mainLogic()
        }
        binding.apply {

            if (SharedPrefUtils.getPrefBoolean("isAnyChange", false)) {
                updateUserDetails()
            }
            swipeRefreshLayout.setColorScheme(R.color.colorPrimary)
            swipeRefreshLayout.setOnRefreshListener {
                dialog.show()
                Handler(Looper.myLooper()!!).postDelayed({
                    if (!Constants.isConnected(requireActivity())) {
                        mainLogic()
                        Constants.checkInternet(requireActivity())
                    } else {
                        mainLogic()
                    }
                }, 1500)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = binding.root

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun mainLogic() {
        binding.apply {
            val calendar = Calendar.getInstance()
            val timeOfDay = calendar.get(Calendar.HOUR_OF_DAY)

            val greetingMessage = getGreetingMessage(timeOfDay)

            tvGreet.text = greetingMessage
            if (dialog.isShowing)
                dialog.dismiss()
            if (swipeRefreshLayout.isRefreshing)
                swipeRefreshLayout.isRefreshing = false
            SharedPrefUtils.init(activity as Context)

//            val image = SharedPrefUtils.getPrefString("profilePic", "")
//            if (image != null) {
//                Glide.with(requireActivity())
//                    .load(image.toUri()) // Pass the URI here
//                    .into(ivProfilePic)
//            }

            updateUserDetails()
            ivProfilePic.setOnClickListener {
                Constants.userProfile(context as Activity)
            }

//            tvName.text = SharedPrefUtils.getPrefString("name", "").toString()
//            swipeRefreshLayout.setColorScheme(R.color.colorPrimary)
//
//            swipeRefreshLayout.setOnRefreshListener {
//                Handler(Looper.myLooper()!!).postDelayed({
//                    swipeRefreshLayout.isRefreshing = false
//                }, 1500)
//            }

            val recycler = recyclerView
            val animation = AnimationUtils.loadAnimation(context, R.anim.blink_animation)
//            val fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)

            constraintLayout.setOnClickListener {
//                constraintLayout.animation=fadeIn

            }
            tvSearch.setOnClickListener {

                tvSearch.startAnimation(animation)
                Handler(Looper.myLooper()!!).postDelayed({
                    val fragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(
                        R.id.fragment_container, SearchFragment()
                    )
                    fragmentTransaction.commit()

                    val activity = requireActivity() as HomeActivity

                    activity.switchBottomNavigationItem(R.id.menu_search)
                }, 100)

            }


            val tempList = ArrayList<String>()

            val slideModels = java.util.ArrayList<SlideModel>()
            val databaseRef = FirebaseDatabase.getInstance().getReference("SliderHome")
            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    tempList.clear()
                    slideModels.clear()
                    for (i in snapshot.children) {
                        tempList.add(i.value.toString())
                        Log.i("VISHAY", "onDataChange: ${i.value} ")
                        Log.i("VISHAY", "$tempList")
                    }
                    for (url in tempList) {

                        slideModels.apply {

                            add(
                                SlideModel(
                                    baseUrl + url,
                                    ScaleTypes.FIT
                                )
                            )
                        }
                        Log.i("VISHAY", "Value :$url ")
                    }
                    imageSlider.setSlideAnimation(com.denzcoskun.imageslider.constants.AnimationTypes.DEPTH_SLIDE)
                    imageSlider.setImageList(slideModels, ScaleTypes.FIT)


                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


            val horizontalImageList = listOf<String>(
                "SliderHome/slider1.png",
                "SliderHome/slider6.2.png",
                "SliderHome/slider3.png",
                "SliderHome/slider4.png",
                "SliderHome/slider5.png",

                )



            horizontalList.clear()
            for (i in horizontalImageList.indices) {
                Log.i("VISHAY", "list :${horizontalImageList[i]} ")
                horizontalList.apply {
                    add(HorizontalScrollViewModel(horizontalImageList[i]))
                }
            }

            val horizontalAdapter = HorizontalScrollViewAdapter(requireActivity(), horizontalList)
            horScrollView.adapter = horizontalAdapter
            val titleList = listOf<String>(
                "BTECH",
                "BE",
                "BCA",
                "BBA",
                "BSC",
                "BED",
                "BPHARM",
                "BCOM",
                "BBACA",
                "BA"
            )
            val imageList = listOf<Int>(
                R.drawable.ic_btech,
                R.drawable.ic_be,
                R.drawable.ic_bca,
                R.drawable.ic_bba,
                R.drawable.ic_bsc,
                R.drawable.ic_bed,
                R.drawable.ic_bpharm,
                R.drawable.ic_bcom,
                R.drawable.ic_bbaca,
                R.drawable.ic_ba,
            )
            courseList.clear()
            for (i in imageList.indices) {
                courseList.apply {
                    add(DegreeModel(imageList[i], titleList[i]))
                }
            }


            val degreeAdapter = DegreeAdapter(requireActivity(), courseList)
            recycler.adapter = degreeAdapter



            groupList.clear()
            groupList.add(
                GroupModel(
                    R.drawable.join,
                    "Join"
                )
            )
            groupList.add(
                GroupModel(
                    R.drawable.help,
                    "Help"
                )
            )
            groupList.add(
                GroupModel(
                    R.drawable.about,
                    "About"
                )
            )
            val groupAdapter = GroupAdapter(requireActivity(), groupList)
            recyclerView2.adapter = groupAdapter


        }
    }

    private fun getGreetingMessage(hourOfDay: Int): String {
        return when (hourOfDay) {
            in 0..11 -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            in 16..20 -> "Good Evening"
            else -> "Good Night"
        }
    }
}


