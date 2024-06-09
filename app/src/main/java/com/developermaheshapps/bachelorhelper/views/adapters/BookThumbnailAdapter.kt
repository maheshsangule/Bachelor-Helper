package com.developermaheshapps.bachelorhelper.views.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.developermaheshapps.bachelorhelper.databinding.BookCourseItemBinding
import com.developermaheshapps.bachelorhelper.models.BookModel
import com.developermaheshapps.bachelorhelper.models.HomeModel
import com.developermaheshapps.bachelorhelper.utils.SpringScrollHelper
import com.developermaheshapps.bachelorhelper.views.activities.BranchCategoryActivity

const val LAYOUT_HOME = 0
const val LAYOUT_TWO = 1


//val list:ArrayList<BookModel> =ArrayList<BookModel>()
class BookThumbnailAdapter(
    private var context: Context,
    val bookThumbnail: ArrayList<HomeModel> = ArrayList<HomeModel>()
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class HomeItemViewHolder(var binding: BookCourseItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val mViewPool = RecyclerView.RecycledViewPool()

        fun bind(model: HomeModel, context: Context) {

            binding.apply {

                model.apply {

                    tvBranchTitle.text = c
                    mbtnSeeAll.setOnClickListener {
                        val intent = Intent()
                        intent.putExtra("book_list", bl)
                        intent.putExtra("catTitle", c)
                        intent.setClass(context, BranchCategoryActivity::class.java)
                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context as Activity,
                            rvRecyclerViewHorizantal,rvRecyclerViewHorizantal.transitionName
                        )
                        context.startActivity(intent,options.toBundle())

                    }
                    if (bl != null) {
                        rvRecyclerViewHorizantal.setupChildRv(bl, context)
                    }

                }

            }
        }

        private fun RecyclerView.setupChildRv(list: ArrayList<BookModel>, context: Context) {
            val adapter = BranchMainAdapter(list, context)
            this.adapter = adapter
            setRecycledViewPool(mViewPool)
            SpringScrollHelper().attachToRecyclerView(this)
        }

    }

    override fun getItemViewType(position: Int): Int {
        val model = bookThumbnail[position]
        return when (model.LAYOUT_TYPE) {
            LAYOUT_HOME -> LAYOUT_HOME
            else -> LAYOUT_TWO

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            LAYOUT_HOME -> {
                HomeItemViewHolder(
                    BookCourseItemBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                HomeItemViewHolder(
                    BookCourseItemBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                    )
                )
            }


        }

    }

    override fun getItemCount() = bookThumbnail.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = bookThumbnail[position]

        when (model.LAYOUT_TYPE) {
            LAYOUT_HOME -> {
                (holder as HomeItemViewHolder).bind(model, context)
            }

            else -> {
                (holder as HomeItemViewHolder).bind(model, context)
            }
        }

    }
}