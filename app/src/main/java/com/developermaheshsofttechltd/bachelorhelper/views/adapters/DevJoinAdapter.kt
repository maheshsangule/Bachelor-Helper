package com.developermaheshsofttechltd.bachelorhelper.views.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.developermaheshsofttechltd.bachelorhelper.databinding.DevJoinItemBinding
import com.developermaheshsofttechltd.bachelorhelper.models.DevJoinModel
import com.developermaheshsofttechltd.bachelorhelper.utils.Constants

class DevJoinAdapter(val list: ArrayList<DevJoinModel>, val context: Context) :
    RecyclerView.Adapter<DevJoinAdapter.ViewHolder>() {

    class ViewHolder(private val binding: DevJoinItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: DevJoinModel, context: Context) {
            binding.apply {
                ivbtnIcon.setImageResource(model.image)
                tvTitle.text = model.title
                tvSubTitle.text = model.subTitle


                root.setOnClickListener {
                    if (model.title.startsWith("I")) {
                        Constants.linkOpen(context as Activity, "https://www.instagram.com/mahesh_sangule_?igsh=NGUwZWllNGxrYnh4")
                    } else if (model.title.startsWith("L")) {
                        Constants.linkOpen(context as Activity, "https://www.linkedin.com/in/mahesh-sangule-8a7a0025b")
                    } else if (model.title.startsWith("Y")) {
                        Constants.linkOpen(context as Activity, "https://www.youtube.com/@developer-mahesh")
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
            DevJoinItemBinding.inflate(LayoutInflater.from(context), parent, false)
        )

    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.bind(model, context)
    }
}