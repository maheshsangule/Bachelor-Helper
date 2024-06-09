package com.developermaheshapps.bachelorhelper.views.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.developermaheshapps.bachelorhelper.databinding.ItemSearchBinding
import com.developermaheshapps.bachelorhelper.models.BookModel
import com.developermaheshapps.bachelorhelper.utils.loadOnline
import com.developermaheshapps.bachelorhelper.views.activities.BookDetailsActivity


class SearchAdapter(var list: ArrayList<BookModel>, val context: Context) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: BookModel, context: Context) {
            binding.apply {


                model.apply {

                        let {
                            ivItemBookImage.loadOnline(i)
                            tvBooName.text = t
                            tvBookAutherName.text = a
                            tvBookDescription.text = d

                            binding.root.setOnClickListener {
                                val intent = Intent(context, BookDetailsActivity::class.java).apply {
                                    putExtra("book_model", model)
                                }
                                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    context as Activity,
                                    cardView,
                                    cardView.transitionName
                                )
                                context.startActivity(intent, options.toBundle())

                        }
                    }
                }



            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSearchBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.bind(model, context)

    }

}
