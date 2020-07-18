package com.project.kaamwaala.activity.customer.product

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.pixplicity.easyprefs.library.Prefs
import com.project.kaamwaala.R
import com.project.kaamwaala.activity.customer.category.ProductCategoryActivity
import com.project.kaamwaala.activity.customer.category.ProductCategoryActivity.Companion.categorylist
import com.project.kaamwaala.adapter.SectionsPagerCategoryAdapter
import com.project.kaamwaala.fragment.customer.ProductListFragment
import com.project.kaamwaala.model.customer.cart.GetCartRequest
import com.project.kaamwaala.model.customer.cart.GetCartResponse
import com.project.kaamwaala.model.customer.customer_category.CustomerStallCategoryRequest
import com.project.kaamwaala.model.customer.customer_category.StoreCategoryItem
import com.project.kaamwaala.model.customer.customer_product.CustomerProductRequest
import com.project.kaamwaala.model.customer.customer_product.CustomerProductResponse
import com.project.kaamwaala.network.PrefsConstants
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_product_category.*
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.tvCount
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ProductListActivity : AppCompatActivity() , KodeinAware {
    override val kodein by kodein()
    private val factory: CustomerProductListVMF by instance<CustomerProductListVMF>()
    private var viewModel: CustomerProductListVM? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        viewModel = ViewModelProviders.of(this, factory).get(CustomerProductListVM::class.java)

        tvName.text= ProductCategoryActivity.StoreName

        setupSections(viewPager!!, categorylist)

        selectTabIndex(intent.getStringExtra("position").toInt())

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })


        ivBack.setOnClickListener { finish() }




        GetCategory(CustomerProductRequest("ProductList", Prefs.getString(PrefsConstants.CityId,""),"",intent.getStringExtra("category_id"),Prefs.getString(PrefsConstants.StoreId,"")))

    }

    private fun selectTabIndex(index: Int) {
        Handler().postDelayed(Runnable {
            tabLayout.setScrollPosition(index, 0F, true)
            viewPager.currentItem = index
            // or
            // tabLayout.getTabAt(index).select();
        }, 100)
    }


    private fun setupSections(viewPager: ViewPager, list: MutableList<StoreCategoryItem>) {
        val mSectionsPagerAdapter = SectionsPagerCategoryAdapter(supportFragmentManager)
        for (i in 0 until list.size) {
            val fragment = ProductListFragment()
                .newInstance(list.get(i).StoreCategoryId)
            mSectionsPagerAdapter.addFragment(
                fragment,
                list[i],
                i
            )
        }

        viewPager.offscreenPageLimit = list.size
        viewPager.adapter = mSectionsPagerAdapter
        tabLayout?.setupWithViewPager(viewPager)

    }


    private fun GetCategory(customercategoryRequest: CustomerProductRequest) {
        viewModel?.responseProduct = MutableLiveData()
        viewModel?.responseProduct?.observe(this, androidx.lifecycle.Observer {

            val response = (it as CustomerProductResponse)


            if (response.Status) {



            } else {

                //  activity?.toast("Something went wrong.")
            }
        })

        viewModel?.getProduct(customercategoryRequest)
    }



    override fun onResume() {
        super.onResume()
    }

}
