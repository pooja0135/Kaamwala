package com.project.kaamwaala.activity.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.tabs.TabLayout
import com.project.kaamwaala.AppController
import com.project.kaamwaala.R
import com.project.kaamwaala.adapter.SectionsPagerAdapter
import com.project.kaamwaala.fragment.admin.SetUpFragment
import com.project.kaamwaala.fragment.admin.adminstall.AdminStallSettingFragment
import com.project.kaamwaala.fragment.admin.adminstall.AdminStallVM
import com.project.kaamwaala.fragment.admin.adminstall.AdminStallVMF
import com.project.kaamwaala.fragment.admin.inventory.InventoryFragment
import com.project.kaamwaala.model.admin_stall_detail.AdminStallDetailRequest
import com.project.kaamwaala.model.admin_stall_detail.AdminStallDetailResponse
import com.project.kaamwaala.model.adminstalllist.StoreItem
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_inventory.*
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.activity_product_list.tabLayout
import kotlinx.android.synthetic.main.activity_product_list.viewPager
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.ivStallImage
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*

class InventoryActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: AdminStallVMF by instance<AdminStallVMF>()
    private lateinit var VM: AdminStallVM
    var listOpening: MutableList<StoreItem> = mutableListOf()
    lateinit var response :AdminStallDetailResponse
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)



        VM = ViewModelProviders.of(this, factory).get(AdminStallVM::class.java)

        response=Paper.book().read("stalldetail")

        Glide.with(this) //1
            .load(response.StoreImageUrl)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .into(ivStallImage)


        tvStallName.setText(response.StoreName)

        val list = mutableListOf<String>()
        list.add("Setup")
        list.add("Inventory")
        list.add("Settings")
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




    }

    private fun setupSections(viewPager: ViewPager, list: MutableList<String>) {
        val mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        for (i in 0 until list.size) {
            if(i==0)
            {
                val fragment = SetUpFragment()
                mSectionsPagerAdapter.addFragment(
                    fragment,
                    list[i],
                    i
                )
            }
            else if(i==1){
                val fragment =
                    InventoryFragment()
                mSectionsPagerAdapter.addFragment(
                    fragment,
                    list[i],
                    i
                )
            }
            else if(i==2){
                val fragment =
                    AdminStallSettingFragment()
                mSectionsPagerAdapter.addFragment(
                    fragment,
                    list[i],
                    i
                )
            }


        }

        viewPager.offscreenPageLimit = list.size
        viewPager.adapter = mSectionsPagerAdapter
        tabLayout?.setupWithViewPager(viewPager)

    }


}