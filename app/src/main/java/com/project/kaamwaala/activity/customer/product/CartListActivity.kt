package com.project.kaamwaala.activity.customer.product

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pixplicity.easyprefs.library.Prefs
import com.project.kaamwaala.OnItemClickSetting
import com.project.kaamwaala.OnItemMinus
import com.project.kaamwaala.OnSelectAddress
import com.project.kaamwaala.R
import com.project.kaamwaala.adapter.AddressListAdapter
import com.project.kaamwaala.adapter.CartListAdapter
import com.project.kaamwaala.dialog.admin.StallSettingDialog
import com.project.kaamwaala.model.customer.cart.*
import com.project.kaamwaala.model.customer.order.PlaceOrderRequest
import com.project.kaamwaala.model.customer.order.PlaceOrderResponse
import com.project.kaamwaala.network.PrefsConstants
import com.project.kaamwaala.utils.OpenDatePicker
import com.project.kaamwaala.utils.getMonthForInt
import com.project.kaamwaala.utils.openDateChooser
import com.project.kaamwaala.utils.toast
import kotlinx.android.synthetic.main.activity_cart_list.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*

class CartListActivity : AppCompatActivity()  , KodeinAware, OnItemMinus, OnItemClickSetting, OnSelectAddress
   {
    override val kodein by kodein()
    private val factory: CustomerProductListVMF by instance<CustomerProductListVMF>()
    private var viewModel: CustomerProductListVM? = null
    var list: MutableList<CartItem> = mutableListOf()
    var addresslist: MutableList<CustomerAddres> = mutableListOf()
    var addressid:String=""
    var delivery_time:String=""
    var gstrate:Double=0.0
    private lateinit var OnItemMinus: OnItemMinus
    lateinit var callback: OnItemClickSetting
    lateinit var callbackAddress: OnSelectAddress

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)

        viewModel = ViewModelProviders.of(this, factory).get(CustomerProductListVM::class.java)

        callback = this
        callbackAddress = this
        OnItemMinus=this


       /* rvAddress.apply {
            rvAddress.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
            adapter = AddressListAdapter(list) }
*/


        ivBack.setOnClickListener { finish() }



        tvCity.setText(Prefs.getString(PrefsConstants.CityName,""))

        GetCart(GetCartRequest("Select", Prefs.getString(PrefsConstants.CityId,""),"","",Prefs.getString(PrefsConstants.UserId,""),Prefs.getString(PrefsConstants.StoreId,"")))

        tvAddNew.setOnClickListener {   openMenu("2", "text", "", "ADDRESS")
        }

        ivEdit.setOnClickListener {     openMenu("1", "number", "", "PHONE_NUMBER")  }

        btnPlaceOrder.setOnClickListener {
            if(addresslist.isEmpty())
            {
                this.toast("Please add your delivery address")
            }
            else
            {
                if(tvPicktime.visibility==View.VISIBLE)
                {
                    delivery_time=tvPicktime.text.toString()
                }

                PlaceOrderApi(PlaceOrderRequest("Insert",addressid,Prefs.getString(PrefsConstants.UserId,""),delivery_time,"0.0",tvGst.text.toString().removePrefix("\u20B9"),tvTotalPrice.text.toString().removePrefix("\u20B9"),"0.0","COD",tvShipping.text.toString().removePrefix("\u20B9"),Prefs.getString(PrefsConstants.StoreId,""),tvPrice.text.toString().removePrefix("\u20B9"),"","pending",tvPhoneNumber.text.toString()))

            }

        }

        cardviewPick.setOnClickListener {
            tvPick.visibility=View.GONE
            tvPicktime.setText("")
            tvPicktime.visibility=View.VISIBLE
            tvPicktime.OpenDatePicker(System.currentTimeMillis())

            cardviewPick.setCardBackgroundColor(resources.getColor(R.color.yellow_700))
            cardviewDelivery.setCardBackgroundColor(resources.getColor(R.color.white))

        }

        cardviewDelivery.setOnClickListener {
            tvPick.visibility=View.VISIBLE
            tvPicktime.visibility=View.GONE
            cardviewDelivery.setCardBackgroundColor(resources.getColor(R.color.yellow_700))
            cardviewPick.setCardBackgroundColor(resources.getColor(R.color.white))
            delivery_time=tvDeliveryTime.text.toString()
        }

    }



    private fun openMenu(line: String, type: String, category: String, texttitle: String) {
        val ft = supportFragmentManager.beginTransaction()
        val newFragment = StallSettingDialog.newInstance(
            line, type, category, texttitle

        )
        newFragment.show(ft, "update")
        newFragment.setCallBack(callback)
    }


    override fun onClickMinus(id: String, price: String, value: String, product_id: String, quantity: String, action: String) {
         if (action.equals("Add"))
         {
             var total:Double=0.0
             var totalprice:Double=0.0
             total=tvTotalPrice.text.toString().removePrefix("₹").toDouble()+price.toDouble()
             tvTotalPrice.setText("\u20B9"+total.toString())
             tvPrice.setText("\u20B9"+total.toString())

             AddCartApi(AddCartRequest("Update",Prefs.getString(PrefsConstants.UserId,""),id,product_id,quantity,Prefs.getString(PrefsConstants.StoreId,"")),"Update")

         }
        else
         {
             var total:Double=0.0
             var totalprice:Double=0.0
             total=tvTotalPrice.text.toString().removePrefix("₹").toDouble()-price.toDouble()
             tvPrice.setText("\u20B9"+total.toString())
             tvTotalPrice.setText("\u20B9"+total.toString())

             if (quantity.equals("0"))
             {
                 AddCartApi(AddCartRequest("Delete",Prefs.getString(PrefsConstants.UserId,""),id,product_id,quantity,Prefs.getString(PrefsConstants.StoreId,"")),"Delete")

             }
             else
             {
                 AddCartApi(AddCartRequest("Update",Prefs.getString(PrefsConstants.UserId,""),id,product_id,quantity,Prefs.getString(PrefsConstants.StoreId,"")),"Update")

             }

         }
    }

    override fun onClick(settingtype: String, value: String) {
       if(settingtype.equals("PHONE_NUMBER"))
       {
           tvPhoneNumber.setText(value)

           AddPhone(UpdateProfileRequest("PhoneNo",Prefs.getString(PrefsConstants.UserId,""),value))
       }
        else
       {
           addresslist.add(CustomerAddres(value,""))
           rvAddress.apply {
               rvAddress.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
               adapter = AddressListAdapter(addresslist,callbackAddress) }

           AddAddress(AddAddressRequest("Insert",value,"",Prefs.getString(PrefsConstants.UserId,"")))
       }
    }


    private fun GetCart(getCartRequest: GetCartRequest) {
        viewModel?.responseGetCart = MutableLiveData()
        viewModel?.responseGetCart?.observe(this, androidx.lifecycle.Observer {

            val response = (it as GetCartResponse)
            list.clear()

            if (response.Status) {

                var total:Double=0.0
                var totalprice:Double=0.0

                for (i in 0 until response.CartItem.size) {
                    list.add(response.CartItem[i])

                    if(response.CartItem[i].SalePrice.isEmpty())
                    {
                        total=total+response.CartItem[i].Quantity.toInt()*response.CartItem[i].Price.toDouble()
                    }
                    else
                    {
                        total=total+response.CartItem[i].Quantity.toInt()*response.CartItem[i].SalePrice.toDouble()
                    }


                }
                totalprice=total

                tvPrice.setText("\u20B9"+total.toString())

                if (response.ShopingCharges.isEmpty())
                {
                    tvShipping.setText("\u20B9 0")

                    if (response.GSTRate.isEmpty())
                    {
                        tvGst.setText("\u20B9 0")
                        gstrate=0.0
                    }
                    else
                    {
                        gstrate=response.GSTRate.toDouble()
                        var gst:Double=0.0
                        gst=(total*response.GSTRate.toDouble())/100
                        totalprice=total+gst
                        tvGst.setText("\u20B9"+gst.toString())

                    }

                }
                else
                {
                    tvShipping.setText("\u20B9"+response.ShopingCharges)

                    totalprice=total+response.ShopingCharges.toDouble()

                    Log.v("fhhfhfhffhfh",totalprice.toString())
                    Log.v("fhhfhfhffhfh",response.ShopingCharges)
                    if (response.GSTRate.isEmpty())
                    {
                        tvGst.setText("\u20B9 0")

                    }
                    else
                    {
                        var gst:Double=0.0
                        gst=(total*response.GSTRate.toDouble())/100
                        totalprice=totalprice+gst
                        tvGst.setText("\u20B9"+gst.toString())

                    }
                }



                tvTotalPrice.setText("\u20B9"+totalprice.toString())


                rvCart.apply {
                    rvCart.layoutManager = LinearLayoutManager(context)
                    adapter = CartListAdapter(list,OnItemMinus) }


                tvPhoneNumber.setText(response.PhoneNumber)


            } else {

                //  activity?.toast("Something went wrong.")
            }


            GetAddress(GetAddressRequest("Select","","",Prefs.getString(PrefsConstants.UserId,"")))

        })

        viewModel?.getcart(getCartRequest)
    }
    private fun AddAddress(addAddressRequest: AddAddressRequest) {
        viewModel?.responseAddAddress = MutableLiveData()
        viewModel?.responseAddAddress?.observe(this, androidx.lifecycle.Observer {

            val response = (it as AddAddressResponse)
            if (response.Status) {
              //  addresslist[addresslist.size - 1].AddressId = response.Id
                GetAddress(GetAddressRequest("Select","","",Prefs.getString(PrefsConstants.UserId,"")))



            } else {


            }
        })

        viewModel?.addaddress(addAddressRequest)
    }
    private fun AddPhone(updateProfileRequest: UpdateProfileRequest) {
        viewModel?.responseUpdatePhone = MutableLiveData()
        viewModel?.responseUpdatePhone?.observe(this, androidx.lifecycle.Observer {

            val response = (it as UpdateProfileResponse)
            if (response.Status) {


            } else {


            }
        })

        viewModel?.addphone(updateProfileRequest)
    }
    private fun GetAddress(getAddressRequest: GetAddressRequest) {
        viewModel?.responseAddress = MutableLiveData()
        viewModel?.responseAddress?.observe(this, androidx.lifecycle.Observer {

            val response = (it as GetAddressResponse)
            if (response.Status) {

                Log.v("fjfhfhhfhhf","")

                for (i in 0 until response.CustomerAddresList.size) {
                    addresslist.add(response.CustomerAddresList[i])
                }
                addressid=addresslist.get(0).AddressId

                rvAddress.apply {
                    rvAddress.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
                    adapter = AddressListAdapter(addresslist,callbackAddress) }


            } else {


            }
        })

        progressbar.visibility= View.GONE
        viewModel?.getaddress(getAddressRequest)
    }
    private fun AddCartApi(addCartRequest: AddCartRequest,status:String) {
        viewModel?.responseAddCart= MutableLiveData()
        viewModel?.responseAddCart?.observe(this, androidx.lifecycle.Observer {

            val response = (it as AddCartResponse)
            if (response.Status) {
             //   tvCount.text=response.CartCount
              //  rlPrice.visibility=View.VISIBLE
               if (status.equals("Delete"))
               {
                   GetCart(GetCartRequest("Select", Prefs.getString(PrefsConstants.CityId,""),"","",Prefs.getString(PrefsConstants.UserId,""),Prefs.getString(PrefsConstants.StoreId,"")))

               }

            }
            else
            {
                progressbar.visibility= View.GONE
            }

        })
        viewModel?.addcart(addCartRequest)
    }
    private fun PlaceOrderApi(placeOrderRequest: PlaceOrderRequest) {
        viewModel?.responsePlaceOrder= MutableLiveData()
        viewModel?.responsePlaceOrder?.observe(this, androidx.lifecycle.Observer {

            val response = (it as PlaceOrderResponse)
            if (response.Status) {
                //   tvCount.text=response.CartCount
                //  rlPrice.visibility=View.VISIBLE
                progressbar.visibility= View.GONE
                this.toast("Your order placed successfully.")
                startActivity(Intent(this, OrderListActivity::class.java))

            }
            else
            {
                progressbar.visibility= View.GONE

                this.toast("Something went wrong.")
            }

        })
        viewModel?.placeorder(placeOrderRequest)
    }


    override fun onClickAddress(id: String) {
        addressid=id
    }


}
