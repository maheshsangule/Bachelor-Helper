package com.developermaheshapps.bachelorhelper.views.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.developermaheshapps.bachelorhelper.databinding.ItemSearchBinding
import com.developermaheshapps.bachelorhelper.models.BookModel
import com.developermaheshapps.bachelorhelper.utils.loadOnline
import com.developermaheshapps.bachelorhelper.views.activities.BookDetailsActivity

class BranchChildAdapter(val list: ArrayList<BookModel>, val context: Context) :
    RecyclerView.Adapter<BranchChildAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: BookModel, context: Context) {
            binding.apply {
                ivItemBookImage.loadOnline(model.i)
                tvBooName.text = model.t
                tvBookAutherName.text = model.a
                tvBookDescription.text = model.d

                binding.root.setOnClickListener {
                    Intent().apply {
                        putExtra("book_model", model)
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
        holder.bind(model, context)

    }

}