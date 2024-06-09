package com.developermaheshapps.bachelorhelper.views.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.developermaheshapps.bachelorhelper.databinding.ItemDevAboutBinding
import com.developermaheshapps.bachelorhelper.models.DevAboutModel
import com.developermaheshapps.bachelorhelper.utils.Constants

class DevAboutAdapter(val list: ArrayList<DevAboutModel>, val context: Context) :
    RecyclerView.Adapter<DevAboutAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemDevAboutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: DevAboutModel, context: Context) {
            binding.apply {
                ibtnIcon.setImageResource(model.image)
                mtvTitle.text = model.title



                root.setOnClickListener {
                    if (model.title.startsWith("I")) {
                        Constants.linkOpen(
                            context as Activity,
                            "https://www.instagram.com/mahesh_sangule_?igsh=NGUwZWllNGxrYnh4"
                        )
                    } else if (model.title.startsWith("L")) {
                        Constants.linkOpen(
                            context as Activity,
                            "https://www.linkedin.com/in/mahesh-sangule-8a7a0025b"
                        )
                    } else if (model.title.startsWith("Y")) {
                        Constants.linkOpen(
                            context as Activity,
                            "https://www.youtube.com/@developer-mahesh"
                        )
                    } else if (model.title.startsWith("E")) {
                        Constants.linkOpen(context as Activity, "developermaheshhelp@gmail.com")
                    } else if (model.title.startsWith("W")) {
                        Constants.linkOpen(
                            context as Activity,
                            "https://whatsapp.com/channel/0029Va6b8sf4NViiLFpoe80l"
                        )
                    } else {
                        Constants.linkOpen(
                            context as Activity,
                            "https://www.facebook.com/profile.php?id=100094035831979&mibextid=ZbWKwL"
                        )
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDevAboutBinding.inflate(LayoutInflater.from(context), parent, false)
        )

    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.bind(model, context)
    }
}