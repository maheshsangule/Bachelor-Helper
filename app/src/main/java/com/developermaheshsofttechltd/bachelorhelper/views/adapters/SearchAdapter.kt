package com.developermaheshsofttechltd.bachelorhelper.views.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.developermaheshsofttechltd.bachelorhelper.databinding.ItemSearchBinding
import com.developermaheshsofttechltd.bachelorhelper.models.BookModel
import com.developermaheshsofttechltd.bachelorhelper.utils.loadOnline
import com.developermaheshsofttechltd.bachelorhelper.views.activities.BookDetailsActivity


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
                                    putExtra("categoryTitle", model.categoryTitle.toString())
//                                    Toast.makeText(context, model.categoryTitle, Toast.LENGTH_SHORT).show()
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
