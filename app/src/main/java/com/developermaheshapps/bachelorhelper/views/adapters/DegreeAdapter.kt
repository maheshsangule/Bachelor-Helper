package com.developermaheshapps.bachelorhelper.views.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.developermaheshapps.bachelorhelper.databinding.CourseItemBinding
import com.developermaheshapps.bachelorhelper.models.DegreeModel
import com.developermaheshapps.bachelorhelper.utils.SharedPrefKeys
import com.developermaheshapps.bachelorhelper.utils.SharedPrefUtils
import com.developermaheshapps.bachelorhelper.views.activities.CourseActivity

class DegreeAdapter(
    private var context: FragmentActivity,
    val courseList: ArrayList<DegreeModel> = ArrayList<DegreeModel>()
) :
    RecyclerView.Adapter<DegreeAdapter.ViewHolder>() {

    class ViewHolder(var binding: CourseItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: DegreeModel,context: Context)
        {
            SharedPrefUtils.init(context)
            binding.apply {
                model.apply {
                    ivCourseIcon.setImageResource(courseIcon)
                    binding.courseTitle.text=courseTitle
                    mCardBtnCourse.setOnClickListener{
                        val intent=Intent()
                        intent.putExtra("catTitle", courseTitle)
                        intent.setClass(context,CourseActivity::class.java)

                        if (courseTitle == "BE" || courseTitle == "be" || courseTitle == "bE" || courseTitle == "Be") {
                            SharedPrefUtils.putPrefString(SharedPrefKeys.COURSE_NAME, "BTECH")
                        } else {
                            SharedPrefUtils.putPrefString(
                                SharedPrefKeys.COURSE_NAME, courseTitle
                            )
                        }



                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            CourseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    }

    override fun getItemCount() = courseList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = courseList[position]
        holder.bind(model, context)



    }
}