package com.developermaheshsofttechltd.bachelorhelper.views.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.developermaheshsofttechltd.bachelorhelper.databinding.ItemDevDetailsBinding
import com.developermaheshsofttechltd.bachelorhelper.models.GroupModel
import com.developermaheshsofttechltd.bachelorhelper.utils.Constants

class GroupAdapter(
    private var context: FragmentActivity,
    val groupList: ArrayList<GroupModel> = ArrayList<GroupModel>()
) :
    RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemDevDetailsBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemDevDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    }

    override fun getItemCount() = groupList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.ivDevDetails.setImageResource(groupList[position].image)
        holder.binding.tvDevDetails.text = groupList[position].title
        holder.binding.root.setOnClickListener {
            if(groupList[position].title.toString()=="Join")
            {
                Constants.devData(context as Activity,"Join")
            }
            else if(groupList[position].title.toString()=="Help")
            {
                Constants.devData(context as Activity,"Help")
            }
            else{
                Constants.devData(context as Activity,"About")
            }

        }


    }
}