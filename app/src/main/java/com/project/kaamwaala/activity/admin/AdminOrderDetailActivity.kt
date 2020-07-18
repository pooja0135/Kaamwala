package com.project.kaamwaala.activity.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pixplicity.easyprefs.library.Prefs
import com.project.kaamwaala.R
import com.project.kaamwaala.adapter.admin.customer.AdminOrderListAdapter
import com.project.kaamwaala.adapter.admin.customer.AdminOrderProductListAdapter
import com.project.kaamwaala.fragment.admin.adminorder.AdminOrderVM
import com.project.kaamwaala.fragment.admin.adminorder.AdminOrderVMF
import com.project.kaamwaala.model.admin_order.*
import com.project.kaamwaala.model.customer.order.OrderItem
import com.project.kaamwaala.network.PrefsConstants
import kotlinx.android.synthetic.main.activity_admin_order_detail.*
import kotlinx.android.synthetic.main.activity_admin_order_detail.rvOrderProductList
import kotlinx.android.synthetic.main.activity_admin_order_detail.tvGrandTotal
import kotlinx.android.synthetic.main.activity_admin_order_detail.tvPrice
import kotlinx.android.synthetic.main.activity_admin_order_detail.tvShipping
import kotlinx.android.synthetic.main.activity_admin_order_detail.tvTotalPrice

import kotlinx.android.synthetic.main.fragment_admin_tablayout.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class AdminOrderDetailActivity : AppCompatActivity() , KodeinAware {
    override val kodein by kodein()
    private val factory: AdminOrderVMF by instance<AdminOrderVMF>()
    private lateinit var viewmodel: AdminOrderVM
    var list: MutableList<StoreOrderDetail> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_order_detail)
        viewmodel = ViewModelProviders.of(this, factory).get(AdminOrderVM::class.java)


        ivClose.setOnClickListener { finish() }

        tvOrderID.setText("ORDER ID: "+intent.getStringExtra("order_id"))

        AdminOrderDetailApi(AdminOrderDetailRequest("OrderDetails",Prefs.getString(PrefsConstants.UserId,""),"",intent.getStringExtra("order_id")))

    }

//======================================API============================================================//
    private fun AdminOrderDetailApi(adminStallListRequest: AdminOrderDetailRequest) {
        viewmodel?.responseOrderDetail = MutableLiveData()
        viewmodel?.responseOrderDetail?.observe(this, Observer {

            val response = (it as AdminOrderDetailResponse)
            if (response.Status) {

                var total:Double=0.0
                var totalprice:Double=0.0
                var shipping:Double=0.0

                shipping=intent.getStringExtra("shipping_charge").toDouble()

                for (i in 0 until response.StoreOrderDetailList.size) {
                    list.add(response.StoreOrderDetailList[i])

                    total=total+response.StoreOrderDetailList[i].TotalPrice.toDouble()


                }
                totalprice=total+shipping

                tvPrice.setText("\u20B9"+total.toString())
                tvShipping.setText("\u20B9"+shipping)
                tvTotalPrice.setText("\u20B9"+totalprice)
                tvGrandTotal.setText("\u20B9"+totalprice)



                tvOrderStatus.setText(response.DeliveryStatus)

                tvDetiveryTime.setText(response.DeliveryTime)
                tvTime.setText(response.OrderDate)
                tvPaymentMode.setText(response.PaymentMode)
                tvCustomerAddress.setText(response.CustomerAddress)
                tvStoreAddress.setText(response.StoreAddress)
                tvStoreName.setText(response.StoreName)
                tvCustomerMobile.setText(response.CustomerMobileNo)


                if (response.DeliveryStatus.equals("New"))
                {
                    tvOrderStatus.setBackground(resources.getDrawable(R.drawable.rectangle_green))
                }
                else if (response.DeliveryStatus.equals("Shipped"))
                {
                    tvOrderStatus.setBackground(resources.getDrawable(R.drawable.rectangle_yellow))
                }
                else if (response.DeliveryStatus.equals("Cancelled"))
                {
                    tvOrderStatus.setBackground(resources.getDrawable(R.drawable.rectangle_red))
                    tvReason.setText("Reason:-"+response.CancelReason)
                    tvReason.visibility= View.VISIBLE
                    cardview.visibility= View.GONE
                }
                else if (response.DeliveryStatus.equals("Delivered"))
                {
                    tvOrderStatus.setBackground(resources.getDrawable(R.drawable.rectangle_green))
                    cardview.visibility= View.GONE
                }


                Glide.with(this) //1
                    .load(response.StoreImageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .skipMemoryCache(true) //2
                    .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                    .into(ivStallImage)

                rvOrderProductList.apply {
                    rvOrderProductList.layoutManager = LinearLayoutManager(context)
                    adapter = AdminOrderProductListAdapter(response.StoreOrderDetailList)
                }
            }
            else {


            }

        })
        viewmodel.adminorderdetail(adminStallListRequest)
    }

}