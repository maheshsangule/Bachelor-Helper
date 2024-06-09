package com.developermaheshapps.bachelorhelper.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.developermaheshapps.bachelorhelper.R
import com.developermaheshapps.bachelorhelper.databinding.ItemNotesBinding
import com.developermaheshapps.bachelorhelper.entities.NotesEntity
import com.developermaheshapps.bachelorhelper.utils.SharedPrefUtils
import com.developermaheshapps.bachelorhelper.utils.loadUserProfile


class NotesAdpater(private var list: MutableList<NotesEntity>, var context: Context) :
    RecyclerView.Adapter<NotesAdpater.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemNotesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: NotesEntity, context: Context) {
            binding.apply {
                mNote.text = model.note
                val userProfile=SharedPrefUtils.getPrefString("profilePic", "").toString()
                if(userProfile.isNotEmpty())
                {
                    ivProfilePic.loadUserProfile(userProfile)
                }
                else{
                    ivProfilePic.setImageResource(R.drawable.ic_profile)
                }



            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemNotesBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(model = list[position], context = context)
    }
}
