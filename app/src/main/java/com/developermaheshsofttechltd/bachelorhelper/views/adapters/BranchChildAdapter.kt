package com.developermaheshsofttechltd.bachelorhelper.views.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.developermaheshsofttechltd.bachelorhelper.databinding.ItemSearchBinding
import com.developermaheshsofttechltd.bachelorhelper.models.BookModel
import com.developermaheshsofttechltd.bachelorhelper.utils.loadOnline
import com.developermaheshsofttechltd.bachelorhelper.views.activities.BookDetailsActivity

class BranchChildAdapter(val list: ArrayList<BookModel>,  val context: Context,private val categoryTitle:String="") :
    RecyclerView.Adapter<BranchChildAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: BookModel, categoryTitle: String, context: Context) {
            binding.apply {
                ivItemBookImage.loadOnline(model.i)
                tvBooName.text = model.t
                tvBookAutherName.text = model.a
                tvBookDescription.text = model.d

                binding.root.setOnClickListener {
                    Intent().apply {
                        putExtra("book_model", model)
                        putExtra("categoryTitle", categoryTitle)
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ItemSearchBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val model = list[position]
        holder.bind(model, categoryTitle,context)

    }

}