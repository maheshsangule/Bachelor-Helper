package com.developermaheshsofttechltd.bachelorhelper.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.developermaheshsofttechltd.bachelorhelper.databinding.HorizantalScrollviewItemBinding
import com.developermaheshsofttechltd.bachelorhelper.models.HorizontalScrollViewModel
import com.developermaheshsofttechltd.bachelorhelper.utils.loadOnline


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