package com.project.kaamwaala.activity.customer.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixplicity.easyprefs.library.Prefs
import com.project.kaamwaala.R
import com.project.kaamwaala.adapter.MyOrderListAdapter
import com.project.kaamwaala.model.customer.customer_product.CustomerProductRequest
import com.project.kaamwaala.model.customer.customer_product.CustomerProductResponse
import com.project.kaamwaala.model.customer.order.CustomerOrderListRequest
import com.project.kaamwaala.model.customer.order.CustomerOrderListResponse
import com.project.kaamwaala.network.PrefsConstants
import kotlinx.android.synthetic.main.activity_order_list.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class OrderListActivity : AppCompatActivity() , KodeinAware {
    override val kodein by kodein()
    private val factory: CustomerProductListVMF by instance<CustomerProductListVMF>()
    private var viewModel: CustomerProductListVM? = null
    var list: MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        viewModel = ViewModelProviders.of(this, factory).get(CustomerProductListVM::class.java)


         GetOrder(CustomerOrderListRequest("OrderList",Prefs.getString(PrefsConstants.UserId,""),""))




        ivBack.setOnClickListener { finish() }
    }



    private fun GetOrder(customerListRequest: CustomerOrderListRequest) {
        viewModel?.responseOrderList = MutableLiveData()
        viewModel?.responseOrderList?.observe(this, androidx.lifecycle.Observer {

            val response = (it as CustomerOrderListResponse)


            if (response.Status) {

                rvOrderList.apply {
                    rvOrderList.layoutManager = LinearLayoutManager(context)
                    adapter = MyOrderListAdapter(response.OrderList) }

                progressbar.visibility= View.GONE

            } else {

                //  activity?.toast("Something went wrong.")

                progressbar.visibility= View.GONE
            }
        })

        viewModel?.orderlist(customerListRequest)
    }


    override fun onResume() {
        super.onResume()

        GetOrder(CustomerOrderListRequest("OrderList",Prefs.getString(PrefsConstants.UserId,""),""))


    }
}
