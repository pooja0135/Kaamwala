package com.project.kaamwaala.activity.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.tabs.TabLayout
import com.pixplicity.easyprefs.library.Prefs
import com.project.kaamwaala.R
import com.project.kaamwaala.adapter.admin.AdminSectionsPagerAdapter
import com.project.kaamwaala.dialog.admin.AdminModeDialog
import com.project.kaamwaala.dialog.admin.LogoutDialog
import com.project.kaamwaala.network.PrefsConstants
import kotlinx.android.synthetic.main.activity_admin_dashboard.*
import kotlinx.android.synthetic.main.activity_admin_dashboard.ivLogo
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_dashboard.ivUser
import kotlinx.android.synthetic.main.activity_product_list.tabLayout
import kotlinx.android.synthetic.main.activity_product_list.viewPager


class AdminDashboardActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)


        Glide.with(this) //1
            .load(Prefs.getString(PrefsConstants.UserImage,""))
            .placeholder(R.drawable.ic_user)
            .error(R.drawable.ic_user)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .into(ivUser)


        tvUser.setText(Prefs.getString(PrefsConstants.UserName,""))

       ivLogo.setOnClickListener {  }


        val list = mutableListOf<String>()
        list.add("Orders")
        list.add("Stalls")

        setupSections(viewPager!!, list)


        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })


        ivLogo.setOnClickListener { AdminMode() }
        ivUser.setOnClickListener { Logout() }


    }

    private fun setupSections(viewPager: ViewPager, list: MutableList<String>) {
        val mSectionsPagerAdapter = AdminSectionsPagerAdapter(supportFragmentManager)
        for (i in 0 until list.size) {
            mSectionsPagerAdapter.addFragment(
                list[i],
                i
            )
        }

        viewPager.offscreenPageLimit = list.size
        viewPager.adapter = mSectionsPagerAdapter
        tabLayout?.setupWithViewPager(viewPager)

    }


    private fun Logout() {
        val ft = this.supportFragmentManager.beginTransaction()
        val newFragment = LogoutDialog()
        newFragment.show(ft, "dialog")
    }

    private fun AdminMode() {
        val ft = this.supportFragmentManager.beginTransaction()
        val newFragment = AdminModeDialog()
        newFragment.show(ft, "dialog")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
