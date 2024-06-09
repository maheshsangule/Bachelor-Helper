package com.developermaheshapps.bachelorhelper.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.developermaheshapps.bachelorhelper.databinding.HorizantalScrollviewItemBinding
import com.developermaheshapps.bachelorhelper.models.HorizontalScrollViewModel
import com.developermaheshapps.bachelorhelper.utils.loadOnline


class HorizontalScrollViewAdapter(
    private var context: Context,
    private val horizontalScrollview: ArrayList<HorizontalScrollViewModel> = ArrayList<HorizontalScrollViewModel>()
) :
    RecyclerView.Adapter<HorizontalScrollViewAdapter.HomeItemViewHolder>() {


    class HomeItemViewHolder(var binding: HorizantalScrollviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemViewHolder {
        return HomeItemViewHolder(
            HorizantalScrollviewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = horizontalScrollview.size

    override fun onBindViewHolder(holder: HomeItemViewHolder, position: Int) {
        val model = horizontalScrollview[position]

        model.apply {

            holder.apply {
                binding.apply {
                    ivItemBookImage.loadOnline(model.url)
                }
            }
        }


    }

}