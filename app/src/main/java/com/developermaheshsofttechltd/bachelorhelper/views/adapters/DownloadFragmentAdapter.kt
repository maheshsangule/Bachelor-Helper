package com.developermaheshsofttechltd.bachelorhelper.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.developermaheshsofttechltd.bachelorhelper.databinding.ItemPdfBinding
import com.developermaheshsofttechltd.bachelorhelper.views.activities.PdfActivity
import java.util.Calendar
import java.util.Date


class DownloadFragmentAdapter(val list: ArrayList<DownloadModel>, val context: Context) :
    RecyclerView.Adapter<DownloadFragmentAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemPdfBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: DownloadModel, context: Context) {
            binding.apply {
                model.apply {
                    let {

                        val date = Date(dateAndTime.toLong())

                        val calendar = Calendar.getInstance()
                        calendar.time = date

                        val year = calendar.get(Calendar.YEAR)
                        val month = formatValue(calendar.get(Calendar.MONTH) + 1) // Note: Calendar.MONTH is zero-based
                        val day = formatValue(calendar.get(Calendar.DAY_OF_MONTH))
                        val hour = formatValue(calendar.get(Calendar.HOUR_OF_DAY))
                        val minute = formatValue(calendar.get(Calendar.MINUTE))
                        val second = formatValue(calendar.get(Calendar.SECOND))


                        tvPdfTitle.text = name
                        tvPdfDownloadDate.text = "$day/$month/$year"
                        tvPdfDownloadTime.text = "$hour:$minute:$second"

                        binding.root.setOnClickListener {
                            val intent = Intent(context, PdfActivity::class.java).apply {
                                putExtra("book_pdf", path)
                                putExtra("bookTitle", name)
                                putExtra("book_id", name)
                            }
                            context.startActivity(intent)

                        }
                    }
                }


            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPdfBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.bind(model, context)
    }

}

fun formatValue(value: Int): String {
    return if (value < 10) {
        "0$value"
    } else {
        value.toString()
    }
}
data class DownloadModel(
    val name: String,
    val path: String,
    val dateAndTime: String
)
