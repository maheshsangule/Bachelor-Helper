package com.developermaheshsofttechltd.bachelorhelper.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.developermaheshsofttechltd.bachelorhelper.R


class ViewPagerAdapter(var context: Context) : PagerAdapter() {
    var images = intArrayOf(
        R.drawable.ic_onboard1,
        R.drawable.ic_onboard2,
        R.drawable.ic_onboard3,
        R.drawable.ic_onboard4
    )
    var headings = intArrayOf(
        R.string.heading1,
        R.string.heading2,
        R.string.heading3,
        R.string.heading4
    )
    var description = intArrayOf(
        R.string.description1,
        R.string.description2,
        R.string.description3,
        R.string.description4
    )

    override fun getCount(): Int {
        return headings.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {



        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.slider_layout, container, false)
        val slidetitleimage = view.findViewById<View>(R.id.titleImage) as ImageView
        val slideHeading = view.findViewById<View>(R.id.texttitle) as TextView
        val slideDesciption = view.findViewById<View>(R.id.textdeccription) as TextView
        slidetitleimage.setImageResource(images[position])
        slideHeading.setText(headings[position])
        slideDesciption.setText(description[position])
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}