package com.developermaheshsofttechltd.bachelorhelper.repository

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.developermaheshsofttechltd.bachelorhelper.models.BookModel
import com.developermaheshsofttechltd.bachelorhelper.models.HomeModel
import com.developermaheshsofttechltd.bachelorhelper.utils.MyResponses
import com.developermaheshsofttechltd.bachelorhelper.utils.SharedPrefKeys
import com.developermaheshsofttechltd.bachelorhelper.utils.SharedPrefUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainRepo(context: Activity) {

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val courseName =
        SharedPrefUtils.getPrefString(SharedPrefKeys.COURSE_NAME, "").toString()

    private val databaseRef = firebaseDatabase.getReference("AppData").child(courseName)
    private val homeLD = MutableLiveData<MyResponses<ArrayList<HomeModel>>>()

    val homeLiveData get() = homeLD

    suspend fun getHomeData() {

        homeLiveData.postValue(MyResponses.Loading())
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshots: DataSnapshot) {
                if (!snapshots.exists()) {
                    homeLiveData.postValue(MyResponses.Error("Data snapshot not exists"))
                    return
                }
                val tempList = ArrayList<HomeModel>()
                for (snapshot in snapshots.children) {
                    val homeModel = snapshot.getValue(HomeModel::class.java)
                    homeModel?.let {
                        it.bl?.shuffle()
                    }
                    try {
                        homeModel?.let {
                            tempList.add(homeModel)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace().toString()
                        Log.i("VISHAY", "onDataChange:${e.localizedMessage?.toString()} ")
                    }

                }
                if (tempList.size > 0) {

                    if (courseName == SharedPrefKeys.BTECH || courseName == SharedPrefKeys.BE) {

                        tempList.add(tempList[tempList.size - 2])
                        val lastHomeModel = tempList[tempList.size - 1]
                        val list: ArrayList<BookModel> = lastHomeModel.bl!!
                        val list1: ArrayList<BookModel> = list.reversed() as ArrayList<BookModel>
                        list1.shuffle()
                        val modifiedHomeModel =
                            lastHomeModel.copy(c = "Mechanical Engineering", bl = list1)
                        tempList[tempList.size - 1] = modifiedHomeModel
                        homeLiveData.postValue(MyResponses.Success(tempList))
                        Log.i("VISHAY", "getHomeData:${tempList} ")
                    } else {
                        homeLiveData.postValue(MyResponses.Success(tempList))
                        Log.i("VISHAY", "getHomeData:${tempList} ")
                    }
                } else
                    homeLiveData.postValue(MyResponses.Error("Something Went wrong"))
            }

            override fun onCancelled(error: DatabaseError) {
                homeLiveData.postValue(MyResponses.Error("Something Went Wrong with Database"))
            }

        })

    }


    private val allLD = MutableLiveData<MyResponses<ArrayList<HomeModel>>>()

    val allLiveData get() = allLD



    private val courseList = mutableListOf<String>(
        "BTECH",
        "BCA",
        "BBA",
        "BSC",
        "BED",
        "BPHARM",
        "BCOM",
        "BBACA",
        "BA"
    )
    private lateinit var databaseRefAllData: DatabaseReference
    suspend fun getAppData() {


        allLiveData.postValue(MyResponses.Loading())
        val tempList = ArrayList<HomeModel>()
        for (course in courseList) {
            databaseRefAllData = firebaseDatabase.getReference("AppData").child(course.toString())
            databaseRefAllData.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshots: DataSnapshot) {
                    if (!snapshots.exists()) {
                        allLiveData.postValue(MyResponses.Error("Data snapshot not exists"))
                        return
                    }


                    for (snapshot in snapshots.children) {
                        val homeModel = snapshot.getValue(HomeModel::class.java)
                        try {
                            homeModel?.let {
                                tempList.add(homeModel)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace().toString()
                            Log.i("VISHAY", "onDataChange:${e.localizedMessage?.toString()} ")
                        }

                    }
                    if (tempList.isNotEmpty()) {


                        Log.i("VISHAY", "onDataChange:${tempList} ")
                        allLiveData.postValue(MyResponses.Success(tempList))
                    } else
                        allLiveData.postValue(MyResponses.Error("Something Went wrong"))
                }

                override fun onCancelled(error: DatabaseError) {
                    allLiveData.postValue(MyResponses.Error("Something Went Wrong with Database"))
                }

            })
        }


    }
}