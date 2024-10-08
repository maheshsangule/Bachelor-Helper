package com.developermaheshsofttechltd.bachelorhelper.views.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.developermaheshsofttechltd.bachelorhelper.R
import com.developermaheshsofttechltd.bachelorhelper.databinding.ActivityBookDetailsBinding
import com.developermaheshsofttechltd.bachelorhelper.databinding.LayoutProgressBinding
import com.developermaheshsofttechltd.bachelorhelper.models.BookModel
import com.developermaheshsofttechltd.bachelorhelper.models.HomeModel
import com.developermaheshsofttechltd.bachelorhelper.repository.BookRepo
import com.developermaheshsofttechltd.bachelorhelper.repository.MainRepo
import com.developermaheshsofttechltd.bachelorhelper.utils.MyResponses
import com.developermaheshsofttechltd.bachelorhelper.utils.SharedPrefUtils
import com.developermaheshsofttechltd.bachelorhelper.utils.SharedPreferencesHelper
import com.developermaheshsofttechltd.bachelorhelper.utils.SpringScrollHelper
import com.developermaheshsofttechltd.bachelorhelper.utils.loadOnline
import com.developermaheshsofttechltd.bachelorhelper.viewmodels.BookViewModel
import com.developermaheshsofttechltd.bachelorhelper.viewmodels.MainViewModel
import com.developermaheshsofttechltd.bachelorhelper.viewmodels.factories.BookViewModelFactory
import com.developermaheshsofttechltd.bachelorhelper.viewmodels.factories.MainViewModelFactory
import com.developermaheshsofttechltd.bachelorhelper.views.adapters.BookThumbnailAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BookDetailsActivity : AppCompatActivity() {
    val activity = this
    private val STORAGE_PERMISSION_CODE_BOOK_DETAILS = 101

    private val binding by lazy { ActivityBookDetailsBinding.inflate(layoutInflater) }
    private val repo = BookRepo(activity)
    private val viewModel by lazy {
        ViewModelProvider(
            activity, BookViewModelFactory(repo)
        )[BookViewModel::class.java]
    }

    private lateinit var dialog: Dialog
    private lateinit var alertDialog: androidx.appcompat.app.AlertDialog
    private lateinit var dialogBinding: LayoutProgressBinding
    private lateinit var bookModel: BookModel
    private val viewModel1 by lazy {
        ViewModelProvider(
            activity, MainViewModelFactory(repo1)
        )[MainViewModel::class.java]
    }

    private lateinit var adapter: BookThumbnailAdapter
    private val list = ArrayList<HomeModel>()
    private val repo1 = MainRepo(this)

    //
    init {
        adapter = BookThumbnailAdapter(this, list)
    }

    private val TAG = "DetailsActivity"
    private var isPermissionDialogShown = false
    private lateinit var categoryTitle: String
    private var lastResponse: MyResponses<BookRepo.DownloadModel>? = null

    @SuppressLint("ObsoleteSdkInt", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        binding.tvShowMoreAndLess.setOnClickListener {
            if (binding.tvShowMoreAndLess.text == "Show More...") {
                binding.tvShowMoreAndLess.text = "Show Less..."
                binding.tvBookDescription.maxLines = 100
            }
            else
            {
                binding.tvShowMoreAndLess.text = "Show More..."
                binding.tvBookDescription.maxLines = 5
            }
        }
        handleHomeBackend()
        binding.bookRecycler.adapter = adapter
        SpringScrollHelper().attachToRecyclerView(binding.bookRecycler)
        viewModel1.getHomeData()

        viewModel.downloadLiveData.observe(activity) { response ->
            lastResponse = response
            if (response is MyResponses.Loading || response is MyResponses.Success || response is MyResponses.Error) {
                handleBackend(response)
            }
        }
        dialogBinding = LayoutProgressBinding.inflate(layoutInflater)
        dialog = Dialog(activity).apply {
            setContentView(dialogBinding.root)
            this.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            this.window!!.setLayout(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT
            )
        }
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
//            binding.mCardAudioBook.visibility = View.GONE
//        } else {
//            binding.mCardAudioBook.visibility = View.VISIBLE
//        }
        bookModel = intent.getSerializableExtra("book_model") as BookModel
        categoryTitle = intent.getStringExtra("categoryTitle").toString()

//        val bookModelSearch = intent.getSerializableExtra("bookDetails") as BookModel

        putHistory(bookModel.t, bookModel.a, bookModel.i, bookModel.b, bookModel.d)
        binding.apply {

            toolBar.setNavigationOnClickListener {
                finish()
            }
            bookModel.apply {
                tvBookTitle.text = t
                toolBar.title = t
                tvBookAutherName.text = a
                tvBookDescription.text = d
                ivBookImage.loadOnline(i)
            }


//            var bookUrl:String
            mbtnReadBook.setOnClickListener {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {

                    if (!isStoragePermissionGranted()) {
                        requestStoragePermission()
                    } else {
                        putData(bookModel.t)
                        viewModel.downloadFile(
                            SharedPrefUtils.getPrefString(
                                "baseUrl", ""
                            ) + bookModel.b, "${bookModel.t}.pdf"
                        )

                    }
                } else {
                    putData(bookModel.t)
                    viewModel.downloadFile(
                        SharedPrefUtils.getPrefString(
                            "baseUrl", ""
                        ) + bookModel.b, "${bookModel.t}.pdf"
                    )
                }
            }

        }
    }

    private fun putHistory(
        title: String, author: String, imageUrl: String, pdfUrl: String, description: String
    ) {
        val bookList = ArrayList<String>()

        bookList.clear()
        val retrievedBookList = SharedPreferencesHelper.getBookList(activity)
        for (i in retrievedBookList) {
            bookList.add(i)
            Log.i("VISHAY", "ForLoop:${i}\n")
        }
//        val newBookInfo = "$imageUrl|~|${title}|~|${author}|~|${pdfUrl}|~|${description}"
        bookList.add(
            listOf(
                "$imageUrl|~|", "$title|~|", "$author|~|", "$pdfUrl|~|", "$description|~|"
            ).toString()
        )
//        bookList.add(newBookInfo)
        Log.i("VISHAY", "RetrievedBookList:${bookList}")


        SharedPreferencesHelper.saveBookList(activity, bookList)
    }

    private fun putData(title: String) {


        val titleList = ArrayList<String>()
        titleList.clear()
        Log.i("VISHAY", "bookDetails:${titleList}")
        val retrievedList = SharedPreferencesHelper.getStringList(activity).toSet()
        for (i in retrievedList) {
            titleList.add(i.trim())
        }
        titleList.add("${title}.pdf".trim())
        Log.i("VISHAY", "bookDetails:${titleList}")
        SharedPreferencesHelper.saveStringList(activity, titleList)
    }

    private fun handleBackend(response: MyResponses<BookRepo.DownloadModel>) {

        when (response) {
            is MyResponses.Error -> {
                binding.circleProgressbar.visibility = View.GONE
                Log.e(TAG, "onCreate: ${response.errorMessage}")
            }

            is MyResponses.Loading -> {
                binding.circleProgressbar.progress
                binding.circleProgressbar.visibility = View.VISIBLE
                binding.circleProgressbar.progress = response.progress
                dialogBinding.pbDownload.setProgress(response.progress, true)
                Log.i(TAG, "onCreate: Progress ${response.progress}")
            }

            is MyResponses.Success -> {
                binding.circleProgressbar.visibility = View.GONE
                Log.i(TAG, "onCreate: Downloaded ${response.data?.filePath}")
                Intent().apply {
                    putExtra("book_pdf", response.data?.filePath)
                    putExtra("book_id", bookModel.t)
                    putExtra("bookTitle", bookModel.t.trim())
                    setClass(activity, PdfActivity::class.java)
                    startActivity(this)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE_BOOK_DETAILS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SharedPrefUtils.putPrefBoolean("isGranted", true)
                dismissPermissionDialog()
                putData(bookModel.t)
                viewModel.downloadFile(
                    SharedPrefUtils.getPrefString("baseUrl", "") + bookModel.b, "${bookModel.t}.pdf"
                )
            } else {
                showPermissionDialog()
            }
        }
    }

    private fun showPermissionDialog() {
        if (!SharedPrefUtils.getPrefBoolean("isGranted", false)) {
            initializePermissionDialog()
            alertDialog.show()
            isPermissionDialogShown = true
        }
    }

    private fun initializePermissionDialog() {
        val alertDialogObject =
            MaterialAlertDialogBuilder(activity, R.style.CustomAlertDialogStyle).apply {
                setTitle("Permission Required")
                setMessage("This app really need to use this permission. Do you want to allow it")
                setPositiveButton("OK") { _, _ ->
                    openAppSettings()
                }
                setCancelable(true)
            }.create()
        alertDialog = alertDialogObject


    }

    private fun dismissPermissionDialog() {
        alertDialog.dismiss()
        isPermissionDialogShown = false
    }

    private fun handleHomeBackend() {
        viewModel1.homeLiveData.observe(activity) { it ->

            when (it) {
                is MyResponses.Error -> {
//                    if (dialog.isShowing)
//                        dialog.dismiss()
//                    binding.swipeRefreshLayout.isRefreshing = false
                    Log.i(TAG, "handleHomeBackend: ${it.errorMessage}")
                }

                is MyResponses.Loading -> {
                    Log.i(TAG, "handleHomeBackend: Loading...")
                }

                is MyResponses.Success -> {
                    if (dialog.isShowing)
                        dialog.dismiss()
//                    binding.swipeRefreshLayout.isRefreshing = false
                    val tempList = it.data
                    list.clear()
                    tempList?.forEach {
                        if (it.c == categoryTitle) {

                            list.add(HomeModel("Computer Engineering", it.bl, isInside = true))
                            adapter.notifyItemChanged(list.size)
                        }
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

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, STORAGE_PERMISSION_CODE_BOOK_DETAILS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == STORAGE_PERMISSION_CODE_BOOK_DETAILS) {
            if (isStoragePermissionGranted()) {

                dismissPermissionDialog()

            } else {

                showPermissionDialog()
            }

        }
    }

    private fun isStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            activity, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            activity, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), STORAGE_PERMISSION_CODE_BOOK_DETAILS
        )
    }
}


//package com.developermaheshsofttechltd.bachelorhelper.views.activities
//
//import android.app.Dialog
//import android.content.Intent
//import android.graphics.drawable.ColorDrawable
//import android.net.Uri
//import android.os.Build
//import android.os.Bundle
//import android.view.WindowManager
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.app.ActivityCompat.startActivityForResult
//import androidx.core.content.ContextCompat
//import com.developermaheshsofttechltd.bachelorhelper.R
//import com.developermaheshsofttechltd.bachelorhelper.databinding.ActivityBookDetailsBinding
//import com.developermaheshsofttechltd.bachelorhelper.databinding.LayoutProgressBinding
//import com.developermaheshsofttechltd.bachelorhelper.models.BookModel
//import com.developermaheshsofttechltd.bachelorhelper.models.HomeModel
//import com.developermaheshsofttechltd.bachelorhelper.repository.BookRepo
//import com.developermaheshsofttechltd.bachelorhelper.repository.MainRepo
//import com.developermaheshsofttechltd.bachelorhelper.utils.SharedPrefUtils
//import com.developermaheshsofttechltd.bachelorhelper.utils.SharedPreferencesHelper
//import com.developermaheshsofttechltd.bachelorhelper.utils.SpringScrollHelper
//import com.developermaheshsofttechltd.bachelorhelper.viewmodels.BookViewModel
//import com.developermaheshsofttechltd.bachelorhelper.viewmodels.MainViewModel
//import com.developermaheshsofttechltd.bachelorhelper.views.adapters.BookThumbnailAdapter
//import com.google.android.material.dialog.MaterialAlertDialogBuilder
//import kotlin.properties.Delegates
//
//class BookDetailsActivity : AppCompatActivity() {
//
//    private val STORAGE_PERMISSION_CODE_BOOK_DETAILS = 101
//    private val TAG = "DetailsActivity"
//    private val activity = this
//    private val adapter: BookThumbnailAdapter
//    private var alertDialog: AlertDialog? = null
//    private lateinit var binding: ActivityBookDetailsBinding
//    private lateinit var dialog: Dialog
//    private lateinit var dialogBinding: LayoutProgressBinding
//    private var isPermissionDialogShown = false
//
//    private lateinit var bookModel: BookModel
//    var categoryTitle: String by Delegates.notNull()
//
//    private val list = ArrayList<HomeModel>()
//    private val repo = BookRepo(this)
//    private val repo1 = MainRepo(this)
//
//    private lateinit var viewModel: BookViewModel
//    private lateinit var viewModel1: MainViewModel
//
//    init {
//        adapter = BookThumbnailAdapter(this, list)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityBookDetailsBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        handleHomeBackend()
//
//        viewModel.downloadLiveData.observe(this) { response ->
//            // handle backend response
//        }
//
//        dialogBinding = LayoutProgressBinding.inflate(layoutInflater)
//        dialog = Dialog(this).apply {
//            setContentView(dialogBinding.root)
//            window?.setBackgroundDrawable(ColorDrawable(0))
//            window?.setLayout(
//                WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.WRAP_CONTENT
//            )
//        }
//
//        bookModel = intent.getSerializableExtra("book_model") as BookModel
//        categoryTitle = intent.getStringExtra("categoryTitle")
//            ?: throw IllegalArgumentException("categoryTitle not found")
//
//        putHistory(
//            title = bookModel.t,
//            author = bookModel.a,
//            imageUrl = bookModel.i,
//            pdfUrl = bookModel.b,
//            description = bookModel.d
//        )
//
//        binding.ivShareBook.setOnClickListener {
//            // handle share logic here
//        }
//
//        binding.bookRecycler.adapter = adapter
//        SpringScrollHelper().attachToRecyclerView(binding.bookRecycler)
//
//        binding.mbtnReadBook.setOnClickListener {
//            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
////
//                if (!isStoragePermissionGranted()) {
//                    requestStoragePermission()
//                } else {
//                    putData(bookModel.t)
//                    viewModel.downloadFile(
//                        SharedPrefUtils.getPrefString(
//                            "baseUrl",
//                            ""
//                        ) + bookModel.b, "${bookModel.t}.pdf"
//                    )
//
//                }
//            } else {
//                putData(bookModel.t)
//                viewModel.downloadFile(
//                    SharedPrefUtils.getPrefString(
//                        "baseUrl",
//                        ""
//                    ) + bookModel.b, "${bookModel.t}.pdf"
//                )
//            }
//        }
//
//    }
//
//}
//
//private fun putHistory(
//    title: String,
//    author: String,
//    imageUrl: String,
//    pdfUrl: String,
//    description: String
//) {
//    val bookList = SharedPreferencesHelper.getBookList(this).toList()
//    bookList.add("$imageUrl|~|$title|~|$author|~|$pdfUrl|~|$description|~|")
//    SharedPreferencesHelper.saveBookList(this, bookList)
//}
//
//private fun putData(title: String) {
//    val titleList = SharedPreferencesHelper.getStringList(this).toMutableList()
//    titleList.add("$title.pdf")
//    SharedPreferencesHelper.saveStringList(this, titleList)
//}
//
//private fun handleHomeBackend() {
//    viewModel1.homeLiveData.observe(this) { homeData ->
//        // handle home data
//    }
//}
//
//override fun onRequestPermissionsResult(
//    requestCode: Int,
//    permissions: Array<String>,
//    grantResults: IntArray
//) {
//    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    if (requestCode == STORAGE_PERMISSION_CODE_BOOK_DETAILS && grantResults.isNotEmpty() && grantResults[0] == 0) {
//        SharedPrefUtils.putPrefBoolean("isGranted", true)
//        dismissPermissionDialog()
//        putData(bookModel.t)
//        viewModel.downloadFile(
//            url = "${SharedPrefUtils.getPrefString("baseUrl", "")}${bookModel.b}",
//            filename = "${bookModel.t}.pdf"
//        )
//    } else {
//        showPermissionDialog()
//    }
//}
//
//private fun showPermissionDialog() {
//    if (!SharedPrefUtils.getPrefBoolean("isGranted", false)) {
//        initializePermissionDialog()
//        alertDialog?.show()
//        isPermissionDialogShown = true
//    }
//}
//
//private fun initializePermissionDialog() {
//    alertDialog = MaterialAlertDialogBuilder(this, R.style.CustomAlertDialogStyle)
//        .setTitle("Permission Required")
//        .setMessage("This app really needs this permission. Do you want to allow it?")
//        .setPositiveButton("OK") { _, _ ->
//            openAppSettings()
//        }
//        .setCancelable(true)
//        .create()
//}
//
//private fun dismissPermissionDialog() {
//    alertDialog?.dismiss()
//    isPermissionDialogShown = false
//}
//
//private fun openAppSettings() {
//    val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
//        data = Uri.fromParts("package", packageName, null)
//    }
//    startActivityForResult(intent, STORAGE_PERMISSION_CODE_BOOK_DETAILS)
//}
//
//override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//    super.onActivityResult(requestCode, resultCode, data)
//    if (requestCode == STORAGE_PERMISSION_CODE_BOOK_DETAILS && isStoragePermissionGranted()) {
//        dismissPermissionDialog()
//    } else {
//        showPermissionDialog()
//    }
//}
//
//private fun isStoragePermissionGranted(): Boolean {
//    return ContextCompat.checkSelfPermission(
//        this,
//        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//    ) == 0 || ContextCompat.checkSelfPermission(
//        this,
//        android.Manifest.permission.READ_EXTERNAL_STORAGE
//    ) == 0
//}
//
//private fun requestStoragePermission() {
//    ActivityCompat.requestPermissions(
//        this,
//        arrayOf(
//            android.Manifest.permission.READ_EXTERNAL_STORAGE,
//            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//        ),
//        STORAGE_PERMISSION_CODE_BOOK_DETAILS
//    )
//}
