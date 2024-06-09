package com.developermaheshapps.bachelorhelper.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.developermaheshapps.bachelorhelper.R
import com.developermaheshapps.bachelorhelper.databinding.ActivityOnboardingBinding
import com.developermaheshapps.bachelorhelper.views.adapters.ViewPagerAdapter

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var dots: Array<TextView?>
    private lateinit var mSLideViewPager: ViewPager
    private lateinit var mDotLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ellipse1.visibility = View.INVISIBLE
        binding.backbtn.visibility = View.INVISIBLE
        binding.apply {
            backbtn.setOnClickListener {
                if (getItem(0) > 0) {
                    mSLideViewPager.setCurrentItem(getItem(-1), true)
                }
            }

            nextbtn.setOnClickListener {
                if (getItem(0) < 3) {
                    mSLideViewPager.setCurrentItem(getItem(1), true)
                } else {
                    val i = Intent(this@OnboardingActivity, HomeActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }

            skipButton.setOnClickListener {
                val i = Intent(this@OnboardingActivity, HomeActivity::class.java)
                startActivity(i)
                finish()
            }

            mSLideViewPager = slideViewPager
            mDotLayout = indicatorLayout

            viewPagerAdapter = ViewPagerAdapter(this@OnboardingActivity)
            mSLideViewPager.adapter = viewPagerAdapter
            setUpIndicator(0)
            mSLideViewPager.addOnPageChangeListener(viewListener)
        }
    }

    private fun setUpIndicator(position: Int) {
        dots = arrayOfNulls(4)
        mDotLayout.removeAllViews()

        for (i in dots.indices) {
            dots[i] = TextView(this)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 10, 10, 10)

            dots[i]!!.layoutParams = params
            dots[i]!!.setBackgroundResource(R.drawable.ic_unselected_dot)
            mDotLayout.addView(dots[i])
        }


        dots[position]!!.setBackgroundResource(R.drawable.ic_selected_dot)
    }

    private val viewListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            setUpIndicator(position)

            if (position > 0) {
                binding.ellipse1.visibility = View.VISIBLE
                binding.backbtn.visibility = View.VISIBLE
            } else {
                binding.ellipse1.visibility = View.INVISIBLE
                binding.backbtn.visibility = View.INVISIBLE
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
        }
    }

    private fun getItem(i: Int): Int {
        return mSLideViewPager.currentItem + i
    }
}