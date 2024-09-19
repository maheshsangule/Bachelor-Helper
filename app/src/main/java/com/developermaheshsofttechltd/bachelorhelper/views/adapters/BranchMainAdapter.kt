package com.developermaheshsofttechltd.bachelorhelper.views.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.developermaheshsofttechltd.bachelorhelper.databinding.BookThumbnailItemBinding
import com.developermaheshsofttechltd.bachelorhelper.models.BookModel
import com.developermaheshsofttechltd.bachelorhelper.utils.loadOnline
import com.developermaheshsofttechltd.bachelorhelper.views.activities.BookDetailsActivity

class BranchMainAdapter(val list: ArrayList<BookModel>, private val categoryTitle:String, val context: Context) :
    RecyclerView.Adapter<BranchMainAdapter.ChildViewHolder>() {

    class ChildViewHolder(val binding: BookThumbnailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: BookModel,categoryTitle: String, context: Context) {

            binding.apply {
                model.apply {

                    ivItemBookImage.loadOnline(i)
                    cardView.setOnClickListener {
                        Intent().apply {
                            putExtra("book_model", model)

                            putExtra("categoryTitle",categoryTitle.toString())
                            setClass(context, BookDetailsActivity::class.java)
                            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                context as Activity,
                                cardView,cardView.transitionName
                            )
                            context.startActivity(this,options.toBundle())
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            BookThumbnailItemBinding.inflate(LayoutInflater.from(context), parent, false)
        )

    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {

        val model = list[position]
        holder.bind(model,categoryTitle, context)

    }
}