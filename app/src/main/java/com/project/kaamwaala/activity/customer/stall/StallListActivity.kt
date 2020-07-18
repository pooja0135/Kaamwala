package com.project.kaamwaala.activity.customer.stall

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixplicity.easyprefs.library.Prefs
import com.project.kaamwaala.BaseActivity
import com.project.kaamwaala.R
import com.project.kaamwaala.adapter.StallListAdapter
import com.project.kaamwaala.adapter.customer.CardPagerAdapter
import com.project.kaamwaala.adapter.customer.CardPagerAdapter1

import com.project.kaamwaala.model.customer.customer_store.CustomerStoreRequest
import com.project.kaamwaala.model.customer.customer_store.CustomerStoreResponse
import com.project.kaamwaala.network.PrefsConstants

import kotlinx.android.synthetic.main.activity_stall_list.*
import kotlinx.android.synthetic.main.activity_stall_list.viewpager
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*

class StallListActivity : BaseActivity() , KodeinAware {
    override val kodein by kodein()
    private val factory: CustomerStallListVMF by instance<CustomerStallListVMF>()
    private var viewModel: CustomerStallListVM? = null
    val DELAY_MS: Long = 1000
    val PERIOD_MS: Long = 3000
    var currentPage = 0
    var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stall_list)

        viewModel = ViewModelProviders.of(this, factory).get(CustomerStallListVM::class.java)

        GetStall(CustomerStoreRequest("Storelist",Prefs.getString(PrefsConstants.CityId,""),intent.getStringExtra("category_id")))

        tvCategoryName.setText(intent.getStringExtra("category_name"))

        ivBack.setOnClickListener { finish() }
    }

    private fun GetStall(customerStoreRequest: CustomerStoreRequest) {
        viewModel?.responseStall = MutableLiveData()
        viewModel?.responseStall?.observe(this, androidx.lifecycle.Observer {

            val response = (it as CustomerStoreResponse)


            if (response.Status) {

                if(response.BannerList.isEmpty())
                {

                }
                else
                {
                    viewpager.adapter= CardPagerAdapter1(this,response.BannerList)
                    val NUM_PAGES: Int = response.BannerList.size
                    val handler = Handler()
                    val Update = Runnable {
                        if (currentPage == NUM_PAGES) {
                            currentPage = 0
                        }
                        viewpager.setCurrentItem(currentPage++, true)
                    }

                    timer = Timer() // This will create a new Thread

                    timer!!.schedule(object : TimerTask() {
                        // task to be scheduled
                        override fun run() {
                            handler.post(Update)
                        }
                    }, DELAY_MS, PERIOD_MS)
                }

                rvStall.apply {
                    rvStall.layoutManager = LinearLayoutManager(context)
                    adapter = StallListAdapter(response.StoreList) }

                progressbar.visibility= View.GONE


            } else {
                progressbar.visibility= View.GONE
                //  activity?.toast("Something went wrong.")
            }
        })

        viewModel?.getStall(customerStoreRequest)
    }

}
