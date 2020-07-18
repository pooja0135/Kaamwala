package com.project.kaamwaala.activity.customer.product

import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pixplicity.easyprefs.library.Prefs
import com.project.kaamwaala.BaseActivity
import com.project.kaamwaala.OnItemSelectCity
import com.project.kaamwaala.R
import com.project.kaamwaala.adapter.OrderProductListAdapter
import com.project.kaamwaala.dialog.admin.SelectCancelOrderDialog
import com.project.kaamwaala.dialog.admin.SelectCityDialog
import com.project.kaamwaala.model.customer.order.*
import com.project.kaamwaala.network.PrefsConstants
import com.project.kaamwaala.utils.toast
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.inflate_order_product_list.view.*

import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class OrderDetailActivity : BaseActivity(), KodeinAware, OnItemSelectCity {
    override val kodein by kodein()
    private val factory: CustomerProductListVMF by instance<CustomerProductListVMF>()
    private var viewModel: CustomerProductListVM? = null
    var list: MutableList<OrderItem> = mutableListOf()
    lateinit var callbackCity: OnItemSelectCity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)

        viewModel = ViewModelProviders.of(this, factory).get(CustomerProductListVM::class.java)
        callbackCity = this

        ivClose.setOnClickListener { finish() }

        tvOrderID.setText("OrderID: "+intent.getStringExtra("order_id"))

        GetOrder(CustomerOrderDetailRequest("OrderDetails",Prefs.getString(PrefsConstants.UserId,""),intent.getStringExtra("order_id")))

        cardviewCancel.setOnClickListener { openMenuCity() }


    }


    private fun openMenuCity() {
        val ft = supportFragmentManager.beginTransaction()
        val newFragment = SelectCancelOrderDialog.newInstance()
        newFragment.show(ft, "update")
        newFragment.setCallBack(callbackCity)
    }




    override fun onClickCity(id: String, name: String) {
       progressbarCancel.visibility= View.VISIBLE
               cardviewCancel.visibility= View.GONE
        CancelOrder(CancelRequest(name,intent.getStringExtra("order_id")))
    }

    private fun GetOrder(customerListRequest: CustomerOrderDetailRequest) {
        viewModel?.responseOrderDetail = MutableLiveData()
        viewModel?.responseOrderDetail?.observe(this, androidx.lifecycle.Observer {

            val response = (it as CustomerOrderDetailResponse)

            progressbarCancel.visibility= View.GONE
            progressbar.visibility= View.GONE
            if (response.Status) {

                var total:Double=0.0
                var totalprice:Double=0.0
                var shipping:Double=0.0

                shipping=intent.getStringExtra("shipping_charge").toDouble()

                for (i in 0 until response.OrderItemList.size) {
                    list.add(response.OrderItemList[i])

                    total=total+response.OrderItemList[i].TotalPrice.toDouble()


                }
                totalprice=total+shipping

                tvPrice.setText("\u20B9"+total.toString())
                tvShipping.setText("\u20B9"+shipping)
                tvTotalPrice.setText("\u20B9"+totalprice)
                tvGrandTotal.setText("\u20B9"+totalprice)


                tvDeliveryStatus.setText(response.DeliveryStatus)
                tvDeliveryType.setText(response.DeliveryTime)
                tvTime.setText(response.OrderDate)
                tvPaymentMode.setText(response.PaymentMode)
                tvAddress.setText(response.CustomerAddress)
                tvStoreAddress.setText(response.StoreAddress)
                tvStoreName.setText(response.StoreName)


                if (response.DeliveryStatus.equals("New"))
                {
                    tvDeliveryStatus.setBackground(resources.getDrawable(R.drawable.rectangle_green))
                }
                else if (response.DeliveryStatus.equals("Shipped"))
                {
                    tvDeliveryStatus.setBackground(resources.getDrawable(R.drawable.rectangle_yellow))
                }
                else if (response.DeliveryStatus.equals("Cancelled"))
                {
                    tvDeliveryStatus.setBackground(resources.getDrawable(R.drawable.rectangle_red))
                    tvReason.setText("Reason:-"+response.CancelReason)
                    tvReason.visibility=View.VISIBLE
                    cardview.visibility=View.GONE
                }
                else if (response.DeliveryStatus.equals("Delivered"))
                {
                    tvDeliveryStatus.setBackground(resources.getDrawable(R.drawable.rectangle_green))
                    cardview.visibility=View.GONE
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
                    adapter = OrderProductListAdapter(response.OrderItemList) }

                // progressbar.visibility= View.GONE

            } else {

//                progressbar.visibility= View.GONE
            }
        })

        viewModel?.orderdetail(customerListRequest)
    }
    private fun CancelOrder(cancelRequest: CancelRequest) {
        viewModel?.responseCancelOrder = MutableLiveData()
        viewModel?.responseCancelOrder?.observe(this, androidx.lifecycle.Observer {

            val response = (it as CancelResponse)


            if (response.Status) {

                GetOrder(CustomerOrderDetailRequest("OrderDetails",Prefs.getString(PrefsConstants.UserId,""),intent.getStringExtra("order_id")))

                toast("Your Order has Cancelled.")
                // progressbar.visibility= View.GONE

            } else {

//                progressbar.visibility= View.GONE
            }
        })

        viewModel?.cancelorder(cancelRequest)
    }

}
